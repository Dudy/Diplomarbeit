package p2p_vcs_client.implementation;

import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import p2p_access.P2PAccess;
import p2p_vcs_client.Document;
import p2p_vcs_client.P2P_VCS_Client;
import p2p_vcs_client.implementation.storage.StorageManager;
import p2p_vcs_client.interfaces.ClientController;
import p2p_vcs_client.message.CheckoutRequestMessage;
import p2p_vcs_client.message.CheckoutResponseMessage;
import p2p_vcs_client.message.CommandMessage;
import p2p_vcs_client.message.CommitMessage;
import p2p_vcs_client.message.CommitSuccessfulMessage;
import p2p_vcs_client.message.DocumentStorageAbstractMessage;
import p2p_vcs_client.message.FindNodeResponseMessage;
import p2p_vcs_client.message.MessageType;
import p2p_vcs_client.message.StorageManagerRequestMessage;
import p2p_vcs_client.message.StorageManagerResponseMessage;
import p2p_vcs_client.message.TagMessage;
import p2p_vcs_client.message.TagsRequestMessage;
import p2p_vcs_client.message.TagsResponseMessage;
import p2p_vcs_client.message.UpdateValidRequestMessage;
import p2p_vcs_client.message.UpdateValidResponseMessage;
import rice.Continuation;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.IdFactory;
import rice.p2p.commonapi.IdRange;
import rice.p2p.commonapi.IdSet;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.p2p.replication.Replication;
import rice.p2p.replication.ReplicationImpl;

/**
 * This is the P2P client.
 *
 * @author podolak
 */
public class P2P_VCS_ClientImplementation implements P2P_VCS_Client {

    private static final Logger LOGGER = Logger.getLogger(P2P_VCS_ClientImplementation.class);
    private static final int REPLICATION_FACTOR = 3;
    protected Properties properties;
    protected Node node;
    protected P2PAccess p2pAccess;
    protected Endpoint endpoint;
    protected DocumentStorageMessageDeserializer deserializer;
    protected HashMap<Id, NodeHandle> primaryNodes;
    protected ClientController clientController;
    // local storage
    protected HashMap<Id, StorageManager> storage;
    protected ArrayList<Document> commitDocumentList;
    protected int commitIndex;
    private Document commitDocument;
    protected PrintStream output;
    protected String name;
    private Continuation storageManagerContinuation;
    private IdRange myRange;
    
    private Replication replication;

    // <editor-fold defaultstate="collapsed" desc=" constructors ">
    public P2P_VCS_ClientImplementation(ClientController clientController, Properties properties)
            throws UnknownHostException, IOException {
        this.clientController = clientController;
        init(properties);
    }

    public P2P_VCS_ClientImplementation(ClientController clientController, P2PAccess p2pAccess)
            throws UnknownHostException, IOException {
        this.clientController = clientController;
        init(p2pAccess);
    }

    public P2P_VCS_ClientImplementation(ClientController clientController, P2PAccess p2pAccess,
            PrintStream output) throws UnknownHostException, IOException {
        this.clientController = clientController;
        this.output = output;
        init(p2pAccess);
    }

    public P2P_VCS_ClientImplementation(ClientController clientController, P2PAccess p2pAccess,
            PrintStream output, String name) throws UnknownHostException, IOException {
        this.clientController = clientController;
        this.output = output;
        this.name = name;
        init(p2pAccess);

        LOGGER.info(name + " hat die ID " + node.getId().toStringFull());
    }
    // </editor-fold> // constructors
    //<editor-fold defaultstate="collapsed" desc=" init ">
    private void init(Properties properties) throws IOException {
        this.properties = properties;
        init(new P2PAccess(properties));
    }

    private void init(P2PAccess p2pAccess) throws IOException {
        BasicConfigurator.configure();
        NDC.push(name);
        LOGGER.setLevel(Level.WARN);

        primaryNodes = new HashMap<Id, NodeHandle>();
        storage = new HashMap<Id, StorageManager>();

        // init P2P stuff
        this.p2pAccess = p2pAccess;
        node = p2pAccess.getNode();

        // create endpoint
        endpoint = node.buildEndpoint(this, "P2P_VCS_Client");

        // serialization
        deserializer = new DocumentStorageMessageDeserializer(this.endpoint);
        endpoint.setDeserializer(deserializer);
        
        // replication
        replication = new ReplicationImpl(node, this, REPLICATION_FACTOR, name);

        // init done, register endpoint
        endpoint.register();

    }
    //</editor-fold> // init
    // <editor-fold defaultstate="collapsed" desc=" getter and setter ">
    public String getBootAddress() {
        return properties.getProperty("bootaddress.ip") +
                ":" +
                properties.getProperty("bootaddress.port");
    }

    public String getLocalBindPort() {
        return properties.getProperty("bindport");
    }

//    public PastryIdFactory getIdFactory() {
//        return P2PAccess.getIdFactory();
//
////        if (idFactory == null) {
////            idFactory = new PastryIdFactory(p2pAccess.getEnvironment());
////        }
////
////        return idFactory;
//    }
    public IdFactory getIdFactory() {
        return P2PAccess.getIdFactory();
    }

    // </editor-fold> // getter and setter
    // <editor-fold defaultstate="collapsed" desc=" connect/disconnect ">
    public void connect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void disconnect() {
        //TODO some more? Kill the environment? Is the cast ok? I dunno how to destroy ...
        ((rice.pastry.PastryNode)node).destroy();
        //MyEnvironment.getInstance().destroy();
        node = null;
    }
    // </editor-fold> // connect/disconnect
    // <editor-fold defaultstate="collapsed" desc=" interface for ClientController ">
    public void checkout(Id vcsId, int version) {
        LOGGER.info("checkout: fordere Dokument an von Id " + vcsId);
        sendTransactionMessage(vcsId, new CheckoutRequestMessage(node.getLocalNodeHandle(), vcsId, version));
    }

    public void commit(Id vcsId, Document document) {
        // You do not have to route commit messages!
        // You do not have to send the new data to the primary node. It is enough to send it to any
        // replica. Updates to one replica are propageted to all others. In FreePastry, there is no
        // "primary node". There *is* one that has the least distance to the vcsId, but all replicas
        // are functionally identically.
        // If you have found the primary node and use it for a while, other nodes may join the ring
        // with a lesser distance to vcsId than the previously primary node. If more nodes with a
        // lesser distance have joined than the replication factor then the node handle you have
        // cached will return a WRONG_NODE_ERROR message.
        //TODO verify this!

        LOGGER.info("commit: document : " + document);
        commitDocument = document;

        LOGGER.info("node.getLocalNodeHandle(): " + node.getLocalNodeHandle());
        LOGGER.info("vcsId: " + vcsId);
        LOGGER.info("document.getId(): " + document.getId());

        LOGGER.info("try to do: new UpdateValidRequestMessage(node.getLocalNodeHandle(), vcsId, document.getId());");

        try {
            UpdateValidRequestMessage mes = new UpdateValidRequestMessage(node.getLocalNodeHandle(), vcsId, document.getId());
            LOGGER.info("message: " + mes);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        sendTransactionMessage(vcsId, new UpdateValidRequestMessage(node.getLocalNodeHandle(), vcsId, document.getId()));
    }

    public void tagDocument(Id vcsId, String tagText, Id documentId, String branch) {
        sendTransactionMessage(vcsId, new TagMessage(node.getLocalNodeHandle(), vcsId, tagText, documentId, branch));
    }

    /**
     * Tags a list of documents.
     * There must be given a list of IDs (vcsIds) that address the nodes in the P2P network that are responsible
     * for the documents that should be tagged, which are identified by their IDs (documentIds). The length of
     * the list of documents and the list of node addresses must be equal. Their entries must match in order, i.e.
     * the n-th node address of vcsIds will indicate the node to hold the n-th document in documentIds.
     * 
     * 
     * @param vcsIds node addresses
     * @param tagText text to tag the documents with
     * @param documentIds documents to tag
     * @param branch name of the branch to tag documents in
     */
    public void tagDocuments(ArrayList<Id> vcsIds, String tagText, ArrayList<Id> documentIds, String branch) {
        if (vcsIds.size() > 0) {
            if (documentIds.size() == vcsIds.size()) {
                for (int i = 0; i < vcsIds.size(); i++) {
                    tagDocument(vcsIds.get(i), tagText, documentIds.get(i), branch);
                }
            } else {
            //TODO number of ids and number of nodes don't match, send error to client
            }
        } else {
        // error to user
        }
    }

    public void getTags(Id vcsId, String branch, Id documentId) {
        if (vcsId != null) {
            sendTransactionMessage(vcsId, new TagsRequestMessage(node.getLocalNodeHandle(), vcsId, documentId, branch));
        } else {
        // error to user
        }
    }
    // </editor-fold> // interface for ClientController
    // <editor-fold defaultstate="collapsed" desc=" send ">
    // Use this send method if you don't know if the corresponding node has already
    // been determined, i.e. at the beginning of a transaction. Don't use this if you
    // already know that you can send directy.
    protected void sendTransactionMessage(Id vcsId, DocumentStorageAbstractMessage message) {
        LOGGER.info("sendTransactionMessage: message: " + message);

        NodeHandle primaryNode = primaryNodes.get(vcsId.toStringFull());

        LOGGER.info("sendTransactionMessage: primaryNode: " + primaryNode);

        // if you don't know where to send the message to, route it
        // if you do know, send directly
        if (primaryNode == null) {
            LOGGER.info("sendTransactionMessage: vcsId = " + vcsId.toStringFull());
            LOGGER.info("sendTransactionMessage: message = " + message);

//            LOGGER.info(this+" sending to "+id);
//            Message msg = new MyMsg(endpoint.getId(), id);
//            endpoint.route(id, msg, null);

            // id: route to the node responsible for that id
            // message: the message to route
            // null: nodeHandle of the first hop
            endpoint.route(vcsId, message, null);
        } else {
            // null: don't route the message
            // message: the message to send
            // primaryNode: nodeHandle of the first hop: send directly to responsible node
            endpoint.route(null, message, primaryNode);
        }
    }

    protected void send(DocumentStorageAbstractMessage message, NodeHandle nodeHandle) {

        LOGGER.info("send: message: " + message);

        // null: don't route the message
        // message: the message to send
        // primaryNode: nodeHandle of the first hop: send directly to responsible node
        endpoint.route(null, message, nodeHandle);
    }
    // </editor-fold> // send
    // <editor-fold defaultstate="collapsed" desc=" message handling ">
    public void update(Document document) {
        LOGGER.info("update: " + document);
    }

    public boolean forward(RouteMessage message) {
        //LOGGER.info("forward: Id = " + message.getDestinationId().toStringFull() + ": message class = " + message.getClass());
        LOGGER.info("forward: myId = " + node.getId().toStringFull() + ", destinationId = " + message.getDestinationId().toStringFull() + ": message class = " + message.getClass());

        //TODO irgendwie funktioniert das nicht mehr, wenn ich die nachfolgenden drei Zeilen auskommentiere,
        // keine Ahnung wieso. Muß ich mir einmal das localNodeHandle geben lassen? Irgendein lazy init, das ich
        // verplant hab???
//        LOGGER.info("=======================================================================================================");
//        LOGGER.info(endpoint.getLocalNodeHandle().toString());
//        LOGGER.info("=======================================================================================================");

        return true;
    }

    public void deliver(Id id, Message message) {
        if (message == null) {
            LOGGER.error("deliver wants to deliver a NULL message, id: " + id.toStringFull());
        } else {
            LOGGER.info(name + ": deliver: message class = " + message.getClass());
            
            System.out.println(message);

            if (message instanceof DocumentStorageAbstractMessage) {
                DocumentStorageAbstractMessage documentStorageAbstractMessage = (DocumentStorageAbstractMessage) message;

                LOGGER.info("deliver: sender = " + documentStorageAbstractMessage.getSender().getId().toStringFull());

                if (documentStorageAbstractMessage.getSender() != node.getId()) {

                    switch (documentStorageAbstractMessage.getMessageType()) {
                        case NONE:
                            break;

                        case CHECKOUT_REQUEST:
                            LOGGER.info("deliver: CheckoutRequestMessage von " +
                                    documentStorageAbstractMessage.getSender());
                            handleCheckoutRequestMessage((CheckoutRequestMessage) documentStorageAbstractMessage);
                            break;
                        case CHECKOUT_RESPONSE:
                            LOGGER.info("deliver: CheckoutResponseMessage von " +
                                    documentStorageAbstractMessage.getSender());
                            handleCheckoutResponseMessage((CheckoutResponseMessage) documentStorageAbstractMessage);
                            break;
                        case STORAGE_MANAGER_REQUEST:
                            LOGGER.info("deliver: CommandMessage [STORAGE_MANAGER_REQUEST] von " +
                                    documentStorageAbstractMessage.getSender());
                            handleStorageManagerRequestMessage((StorageManagerRequestMessage) documentStorageAbstractMessage);
                            break;
                        case STORAGE_MANAGER_RESPONSE:
                            LOGGER.info("deliver: CommandMessage [STORAGE_MANAGER_RESPONSE] von " +
                                    documentStorageAbstractMessage.getSender());
                            handleStorageManagerResponseMessage((StorageManagerResponseMessage) documentStorageAbstractMessage);
                            break;

                        case IS_UPDATE_VALID:
                            LOGGER.info("deliver: UpdateValidRequestMessage von " +
                                    documentStorageAbstractMessage.getSender());
                            handleIsUpdateValidMessage((UpdateValidRequestMessage) documentStorageAbstractMessage);
                            break;
                        case UPDATE_IS_VALID:
                            LOGGER.info("deliver: UpdateValidResponseMessage von " +
                                    documentStorageAbstractMessage.getSender());
                            handleUpdateValid((UpdateValidResponseMessage) documentStorageAbstractMessage);
                            break;
                        case DOCUMENT_VERSION_OUTDATED_ERROR:
                        case WRONG_NODE_ERROR:
                            LOGGER.info("deliver: CommandMessage [" +
                                    documentStorageAbstractMessage.getMessageType() + "] von " +
                                    documentStorageAbstractMessage.getSender());
                            handleError((CommandMessage) documentStorageAbstractMessage);
                            break;

                        case COMMIT:
                            LOGGER.info("deliver: CommitMessage von " +
                                    documentStorageAbstractMessage.getSender());
                            handleCommitMessage((CommitMessage) documentStorageAbstractMessage);
                            break;
                        case COMMIT_SUCCESSFUL:
                            LOGGER.info("deliver: CommitSuccessfulMessage von " +
                                    documentStorageAbstractMessage.getSender());
                            handleCommitSuccessfulMessage((CommitSuccessfulMessage) documentStorageAbstractMessage);
                            break;
                        case TAG:
                            LOGGER.info("deliver: TagMessage von " + documentStorageAbstractMessage.getSender());
                            handleTagMessage((TagMessage) documentStorageAbstractMessage);
                            break;
                        case TAGS_REQUEST:
                            LOGGER.info("deliver: TagsRequestMessage von " + documentStorageAbstractMessage.getSender());
                            handleTagsRequestMessage((TagsRequestMessage) documentStorageAbstractMessage);
                            break;
                        case TAGS_RESPONSE:
                            LOGGER.info("deliver: TagsResponseMessage von " + documentStorageAbstractMessage.getSender());
                            handleTagsResponseMessage((TagsResponseMessage) documentStorageAbstractMessage);
                            break;
                        // testing
                        case FIND_NODE_REQUEST:
                            LOGGER.info("deliver: CommandMessage [FIND_NODE_REQUEST] von " + documentStorageAbstractMessage.getSender());
                            handleFindNodeRequestMessage((CommandMessage) documentStorageAbstractMessage);
                            break;
                        case FIND_NODE_RESPONSE:
                            LOGGER.info("deliver: CommandMessage [FIND_NODE_RESPONSE] von " + documentStorageAbstractMessage.getSender());
                            handleFindNodeResponseMessage((FindNodeResponseMessage) documentStorageAbstractMessage);
                            break;
                    }
                } else {
                    LOGGER.info("deliver: I don't ask myself :-)");
                }
            }
        }
    }

    public void update(rice.p2p.commonapi.NodeHandle handle, boolean joined) {
    }

    private void handleCheckoutRequestMessage(CheckoutRequestMessage message) {
        Document document = null;

        LOGGER.info("handleCheckoutRequestMessage: message = " + message);

        if (message != null) {
            if (message.getVersionNumber() < 0) {
                //TODO error?
                document = null;
            } else {
                StorageManager storageManager = storage.get(message.getVcsId());
                LOGGER.info("handleCheckoutRequestMessage: found storageManager: " + storageManager);

                if (storageManager == null) {
                    //TODO error?
                    document = null;
                } else {
                    document = storageManager.getDocument(message.getVersionNumber());
                }
            }

            LOGGER.info("handleCheckoutRequestMessage: found document: " + document);

            // send document
            send(new CheckoutResponseMessage(node.getLocalNodeHandle(), message.getVcsId(), document), message.getSender());
        }
    }

    private void handleCheckoutResponseMessage(CheckoutResponseMessage message) {
        if (message != null) {
            Document document = message.getDocument();

            LOGGER.info("handleCheckoutResponseMessage: zurückerhaltenes Dokument:\n" + document);

            primaryNodes.put(message.getVcsId(), message.getSender());
            clientController.checkoutResponse(message.getVcsId(), document);
        } else {
            LOGGER.error("handleCheckoutResponseMessage called with NULL message");
        }
    }

    private void handleStorageManagerRequestMessage(StorageManagerRequestMessage message) {
        if (message != null) {
            StorageManager storageManager = storage.get(message.getVcsId());

            if (storageManager != null) {
                send(
                        new StorageManagerResponseMessage(
                        node.getLocalNodeHandle(),
                        message.getVcsId(),
                        storageManager),
                        message.getSender());
            } else {
            //TODO no-document-stored-here-error or something similar
            }
        } else {
            LOGGER.error("handleStorageManagerRequestMessage called with NULL message");
        }
    }

    private void handleStorageManagerResponseMessage(StorageManagerResponseMessage message) {
        if (message != null) {
            primaryNodes.put(message.getVcsId(), message.getSender());
            storageManagerContinuation.receiveResult(message.getStorageManager());
        } else {
            LOGGER.error("handleStorageManagerResponseMessage called with NULL message");
        }
    }

    private void handleIsUpdateValidMessage(UpdateValidRequestMessage updateValidRequestMessage) {
        if (updateValidRequestMessage != null) {
            CommandMessage message = null;
            LOGGER.info("updateValidRequestMessage.getVcsId() = " + updateValidRequestMessage.getVcsId());
            StorageManager storageManager = storage.get(updateValidRequestMessage.getVcsId());
            LOGGER.info("handleIsUpdateValidMessage: storageManager = " + storageManager);

            if (storageManager == null) {
                // the document that should be updated is not hosted on this node
                //message.setType(MessageType.WRONG_NODE_ERROR);

                //TODO rausfinden, ob das wirklich der falsche Knoten ist oder ob neu initial eingecheckt wird

                storageManager = new StorageManager(name);
                storage.put(updateValidRequestMessage.getVcsId(), storageManager);

                LOGGER.info("handleIsUpdateValidMessage: new StorageManager for " +
                        updateValidRequestMessage.getVcsId());

                message = new UpdateValidResponseMessage(
                        node.getLocalNodeHandle(),
                        updateValidRequestMessage.getVcsId(),
                        updateValidRequestMessage.getDocumentId());
            } else {
                if (!storageManager.isActualVersion(updateValidRequestMessage.getDocumentId())) {
                    // version number of the document is outdated
                    message = new CommandMessage(node.getLocalNodeHandle(), updateValidRequestMessage.getVcsId(),
                            MessageType.DOCUMENT_VERSION_OUTDATED_ERROR);
                } else {
                    message = new UpdateValidResponseMessage(
                            node.getLocalNodeHandle(),
                            updateValidRequestMessage.getVcsId(),
                            updateValidRequestMessage.getDocumentId());
                }
            }

            // send answer
            send(message, updateValidRequestMessage.getSender());
        } else {
            LOGGER.error("handleIsUpdateValidMessage called with NULL message");
        }
    }

    private void handleUpdateValid(CommandMessage commandMessage) {
        if (commandMessage != null) {
            LOGGER.info("handleUpdateValid");

            primaryNodes.put(commandMessage.getVcsId(), commandMessage.getSender());
            send(new CommitMessage(node.getLocalNodeHandle(), commandMessage.getVcsId(), commitDocument),
                    commandMessage.getSender());
        } else {
            LOGGER.error("handleUpdateValid called with NULL message");
        }
    }

    private void handleCommitMessage(CommitMessage commitMessage) {
        if (commitMessage != null) {
            LOGGER.info(name + ": handleCommitMessage: " + commitMessage.toString());

            Document document = commitMessage.getDocument();

            LOGGER.info(name + ": handleCommitMessage: document to commit = " + document);

            if (document.getId() != null) {
                LOGGER.info(name + ": handleCommitMessage: id of document to commit = " + document.getId().toStringFull());
            } else {
                LOGGER.info(name + ": handleCommitMessage: id of document to commit is NULL");
            }

            LOGGER.info(name + ": handleCommitMessage: search StorageManager for " + commitMessage.getVcsId());

            StorageManager storageManager = storage.get(commitMessage.getVcsId());

            LOGGER.info(name + ": handleCommitMessage: found StorageManager: " + storageManager);

            MessageType responseMessageType = MessageType.NONE;
            DocumentStorageAbstractMessage message = null;

            //TODO rausfinden, ob das wirklich der falsche Knoten ist, vielleicht ungefähr so:
            // boolean isValid = false;
            // NodeHandleSet replicaNodes = getReplicas();
            // Iterator i = replicaNodes.getIterator();
            // while (i.hasNext()) {
            //   NodeHandle replica = i.getNext();
            //   if (node.getId().equals(replica.getId()) {
            //     isValid = true;
            //   }
            // }
            // if (isValid) {
            //   // go on
            // } else {
            //   // send message with type MessageType.WRONG_NODE_ERROR
            // }
            //
            // anstatt iteration vielleicht auch replicaNodes.contain(node.getNodeHandle()); ??
        /*
            if (!document.getId().equals(commitMessage.getVcsId())) {
            // the document that should be updated is not hosted on this node
            responseMessageType = MessageType.WRONG_NODE_ERROR;
            } else */ if (storageManager == null) {

                LOGGER.info(name + ": handleCommitMessage: storage manager is null");

                // store document for the first time
                storageManager = new StorageManager(name);
                storageManager.addNewVersion(commitMessage.getVcsId(), document);

                // --- new (replication) ---
                //TODO Is this necessary? Does the framework replicate by itself?
                // which one of the following two?
                //NodeHandleSet replicaNodes = endpoint.replicaSet(commitMessage.getVcsId(), REPLICATION_FACTOR);
                //NodeHandleSet replicaNodes = getReplicas();


                //responseMessageType = MessageType.COMMIT_SUCCESSFUL;
                message = new CommitSuccessfulMessage(node.getLocalNodeHandle(), commitMessage.getVcsId(), document.getId());
            } else if (!storageManager.isActualVersion(document.getId())) {

                LOGGER.info(name + ": handleCommitMessage: outdated document");

                // version number of the document is outdated
                //responseMessageType = MessageType.DOCUMENT_VERSION_OUTDATED_ERROR;
                message = new CommandMessage(node.getLocalNodeHandle(), commitMessage.getVcsId(),
                        MessageType.DOCUMENT_VERSION_OUTDATED_ERROR);
            } else {
                LOGGER.info(name + ": handleCommitMessage: store new version of " + document);

                // store new version
                storageManager.addNewVersion(commitMessage.getVcsId(), document);

                // update replicas
                //TODO, necassary? Yes, if we don't want to wait till next maintainance cycle.

                //responseMessageType = MessageType.COMMIT_SUCCESSFUL;
                message = new CommitSuccessfulMessage(node.getLocalNodeHandle(), commitMessage.getVcsId(), document.getId());
            }

            // inform user
            send(message, commitMessage.getSender());
//                    new CommandMessage(node.getLocalNodeHandle(), commitMessage.getVcsId(), responseMessageType),
//                    commitMessage.getSender());
        } else {
            LOGGER.error(name + ": handleCommitMessage called with NULL message");
        }
    }

//    protected void handleCommitSuccessfulMessage(CommandMessage commandMessage) {
//        if (commandMessage != null) {
//            LOGGER.info("handleCommitSuccessfulMessage: " + commandMessage);
//            primaryNodes.put(commandMessage.getVcsId(), commandMessage.getSender());
//            //clientController.commitResponse(commandMessage.getVcsId(), true, commitDocument.getMetadata().getType());
//            clientController.commitResponse(commandMessage.getVcsId(), commandMessage.getSender().getId(), commitDocument.getId(), true, commitDocument.getMetadata().getType());
//            commitDocument = null;
//        } else {
//            LOGGER.error("handleCommitSuccessfulMessage called with NULL message");
//        }
//    }
    protected void handleCommitSuccessfulMessage(CommitSuccessfulMessage message) {
        if (message != null) {
            LOGGER.info("handleCommitSuccessfulMessage: " + message);
            primaryNodes.put(message.getVcsId(), message.getSender());
            //clientController.commitResponse(commandMessage.getVcsId(), true, commitDocument.getMetadata().getType());
            clientController.commitResponse(message.getVcsId(), message.getSender().getId(), message.getDocumentId(), true, commitDocument.getMetadata().getType());
            commitDocument = null;
        } else {
            LOGGER.error("handleCommitSuccessfulMessage called with NULL message");
        }
    }

    private void handleTagMessage(TagMessage tagMessage) {
        if (tagMessage != null) {
            StorageManager storageManager = storage.get(tagMessage.getVcsId());

            if (storageManager == null) {
            // WRONG NODE?
            } else {
                storageManager.tag(
                        tagMessage.getTagText(),
                        tagMessage.getDocumentId(),
                        tagMessage.getBranch());
            }
        } else {
            LOGGER.error("handleTagMessage called with NULL message");
        }
    }

    private void handleTagsRequestMessage(TagsRequestMessage tagsRequestMessage) {
        if (tagsRequestMessage != null) {
            StorageManager storageManager = storage.get(tagsRequestMessage.getVcsId());

            if (storageManager == null) {
            // WRONG NODE?
            } else {
                send(
                        new TagsResponseMessage(node.getLocalNodeHandle(), tagsRequestMessage.getVcsId(),
                        tagsRequestMessage.getDocumentId(), tagsRequestMessage.getBranch(),
                        storageManager.getTags(tagsRequestMessage.getBranch(), tagsRequestMessage.getDocumentId())),
                        tagsRequestMessage.getSender());
            }
        } else {
            LOGGER.error("handleTagsRequestMessage called with NULL message");
        }
    }

    private void handleTagsResponseMessage(TagsResponseMessage tagsResponseMessage) {
        if (tagsResponseMessage != null) {
            primaryNodes.put(tagsResponseMessage.getVcsId(), tagsResponseMessage.getSender());
            clientController.getTagsResponse(
                    tagsResponseMessage.getVcsId(),
                    tagsResponseMessage.getBranch(),
                    tagsResponseMessage.getDocumentId(),
                    tagsResponseMessage.getTags());
        } else {
            LOGGER.error("handleTagsResponseMessage called with NULL message");
        }
    }

    // errors
    private void handleError(CommandMessage commandMessage) {
        LOGGER.info("handleError: " + commandMessage);
        LOGGER.info("ERROR HANDLING NOT YET IMPLEMENTED");
    }

    // </editor-fold> // message handling

    // <editor-fold defaultstate="collapsed" desc=" test ">
    // ---------------------------------------------
    // ----- Test
    // ---------------------------------------------
    private class IdNamePair extends ArrayList<Object> {

        private Id id;
        private String name;

        public IdNamePair(Id id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }
    }
    private Continuation findNodeContinuation;
    private IdNamePair owner;

    public String getOwnerName(Id vcsId) {
        final Id id = vcsId;

        findNode(vcsId, new Continuation() {

            public void receiveResult(Object result) {
                setOwner((IdNamePair) result);
            }

            public void receiveException(Exception result) {
                result.printStackTrace();
                System.exit(0);
            }
        });

        return getOwner().getName();
    }

    public Id getOwnerId(Id vcsId) {
        final Id id = vcsId;

        findNode(vcsId, new Continuation() {

            public void receiveResult(Object result) {
                setOwner((IdNamePair) result);
            }

            public void receiveException(Exception result) {
                result.printStackTrace();
                System.exit(0);
            }
        });

        return getOwner().getId();
    }

    public void doFind(Id vcsId) {
        final Id id = vcsId;

        findNode(vcsId, new Continuation() {

            public void receiveResult(Object result) {
                setOwner((IdNamePair) result);

                LOGGER.info("primary node of vcsId " + id.toStringFull() + " is " + getOwner().getId().toStringFull());
            }

            public void receiveException(Exception result) {
                result.printStackTrace();
                System.exit(0);
            }
        });
    }

    public void findNode(Id vcsId, Continuation c) {
        LOGGER.info("findNode");

        findNodeContinuation = c;

        sendTransactionMessage(vcsId, new CommandMessage(node.getLocalNodeHandle(), vcsId, MessageType.FIND_NODE_REQUEST));
    }

    private void handleFindNodeRequestMessage(CommandMessage commandMessage) {
        if (commandMessage != null) {
            send(
                    new FindNodeResponseMessage(
                    node.getLocalNodeHandle(),
                    commandMessage.getVcsId(),
                    node.getId(),
                    name),
                    commandMessage.getSender());
        } else {
            LOGGER.error("handleFindNodeRequestMessage called with NULL message");
        }
    }

    private void handleFindNodeResponseMessage(FindNodeResponseMessage message) {
        LOGGER.info("handleFindNodeResponseMessage: message: " + message);

        if (message != null) {
            findNodeContinuation.receiveResult(message.getNodeAddress());
        } else {
            findNodeContinuation.receiveException(new NullPointerException("FindNodeRequestMEssage is NULL"));
        }
    }

    private void setOwner(IdNamePair owner) {
        this.owner = owner;
    }

    private IdNamePair getOwner() {
        return owner;
    }
    // ---------------------------------------------
    // </editor-fold> // test
    // <editor-fold defaultstate="collapsed" desc=" replication ">
    public void getStorageManager(Id vcsId, Continuation c) {
        LOGGER.info("getStorageManager: hole alle Daten für vcsId " + vcsId);

        storageManagerContinuation = c;
        sendTransactionMessage(vcsId, new CommandMessage(node.getLocalNodeHandle(), vcsId, MessageType.STORAGE_MANAGER_REQUEST));
    }

    // <editor-fold defaultstate="collapsed" desc=" implements ReplicationManagerClient ">
//    // ----- REPLICATION MANAGER METHODS -----
//    /**
//     * This method is invoked when the node becomes a replica for another node.
//     * The data stored on the other node is fetched and backuped here.
//     *
//     * @param vcsId the address of the node to fetch the data from
//     */
//    public void fetch(final Id vcsId, NodeHandle hint, Continuation command) {
//        LOGGER.info("Sending out replication fetch request for the id " + vcsId);
//
//        Boolean b = new Boolean(true);
//        StorageManager storageManager = storage.get(vcsId);
//
//        if (storageManager != null) {
//        // this is ALREADY a replica, can this ever occur?
//        } else {
//            // get storage manager
//            getStorageManager(vcsId, new Continuation.StandardContinuation(command) {
//
//                public void receiveResult(Object result) {
//                    storage.put(vcsId, (StorageManager) result);
//                }
//            });
//        }
//
//        command.receiveResult(b);
//    }
//
//    /**
//     * This method is invoked when this P2P_VCS_Client respectively its node does no longer
//     * hold a replica of the data that are stored on the node addressed by vcsId.
//     *
//     * @param vcsId the id to remove
//     */
//    public void remove(final Id vcsId, Continuation command) {
//        // you may backup the data, cache it for a while aso ...
//        storage.remove(vcsId);
//        command.receiveResult(Boolean.TRUE);
//    }
//
//    /**
//     * This upcall should return the set of keys that the application
//     * currently stores in this range. Should return a empty IdSet (not null),
//     * in the case that no keys belong to this range.
//     *
//     * @param range the requested range
//     */
//    public IdSet scan(IdRange range) {
//        //LOGGER.info(name + "scan");
//        
//        IdSet set = getIdFactory().buildIdSet();
//
////        if (storage.keySet().size() == 0) {
////            System.out.println(name + " stores nothing");
////        } else {
////            for (Id id : storage.keySet()) {
////                System.out.println(name + " stores " + id.toStringFull());
////            }
////        }
//
//        for (Id id : storage.keySet()) {
//            if (range.containsId(id)) {
//                set.addId(id);
////                System.out.println("ID " + id + " is in range " + range);
//            } else {
////                System.out.println("ID " + id + " is not in range " + range);
//            }
//        }
//
//        return set;
//    }
//
//    /**
//     * This upcall should return the set of keys that the application
//     * currently stores.  Should return a empty IdSet (not null),
//     * in the case that no keys belong to this range.
//     *
//     * @param range the requested range
//     */
//    public IdSet scan() {
//        IdSet set = getIdFactory().buildIdSet();
//
//        for (Id id : storage.keySet()) {
//            set.addId(id);
//        }
//
//        return set;
//    }
//
//    /**
//     * This upcall should return whether or not the given id is currently stored
//     * by the client.
//     *
//     * @param id The id in question
//     * @return Whether or not the id exists
//     */
//    public boolean exists(Id id) {
//        return storage.keySet().contains(id);
//    }
//
//    public void existsInOverlay(Id id, Continuation command) {
//        lookupHandles(id, replicationFactor + 1, new StandardContinuation(command) {
//
//            public void receiveResult(Object result) {
//                Object results[] = (Object[]) result;
//                for (int i = 0; i < results.length; i++) {
//                    if (results[i] instanceof PastContentHandle) {
//                        parent.receiveResult(Boolean.TRUE);
//                        return;
//                    }
//                }
//                parent.receiveResult(Boolean.FALSE);
//            }
//        });
//        command.receiveResult(Boolean.TRUE);
//    }
//
//    public void reInsert(Id id, Continuation command) {
//        storage.getObject(id, new StandardContinuation(command) {
//
//            public void receiveResult(final Object o) {
//                insert((PastContent) o, new StandardContinuation(parent) {
//
//                    public void receiveResult(Object result) {
//                        Boolean results[] = (Boolean[]) result;
//                        for (int i = 0; i < results.length; i++) {
//                            if (results[i].booleanValue()) {
//                                parent.receiveResult(Boolean.TRUE);
//                                return;
//                            }
//                        }
//                        parent.receiveResult(Boolean.FALSE);
//                    }
//                });
//            }
//        });
//        command.receiveResult(Boolean.TRUE);
//    }

    //public void replicate() {
    //System.out.println("replication: " + replicationManager);
    //System.out.println("replication.getReplication(): " + replicationManager.getReplication());

    //replicationManager.getReplication().replicate();
    //}
    // </editor-fold> // implements ReplicationManagerClient

    // <editor-fold defaultstate="collapsed" desc=" implements ReplicationClient ">
    public void fetch(IdSet keySet, NodeHandle hint) {
        LOGGER.info(name + "fetch");

        Iterator i = keySet.getIterator();

        while (i.hasNext()) {
            final Id next = (Id) i.next();
            StorageManager storageManager = storage.get(next);
            
            if (storageManager != null) {
                // this is ALREADY a replica, can this ever occur?
            } else {
                // get storage manager, use this sick Continuation trick
                getStorageManager(next, new Continuation() {

                    public void receiveResult(Object result) {
                        storage.put(next, (StorageManager) result);
                    }

                    public void receiveException(Exception result) {
                        LOGGER.warn(node.getId() + ": error while fetching data for building a new replication from " + next.toString());
                    }
                });
            }
        }
    }
    
    /**
     * This upcall should return the set of keys that the application
     * currently stores in this range. Should return a empty IdSet (not null),
     * in the case that no keys belong to this range.
     * 
     * This is called often!
     *
     * @param range the requested range
     */
    public IdSet scan(IdRange range) {
        LOGGER.info(name + "scan");
        
        IdSet set = getIdFactory().buildIdSet();

//        if (storage.keySet().size() == 0) {
//            System.out.println(name + " stores nothing");
//        } else {
//            for (Id id : storage.keySet()) {
//                System.out.println(name + " stores " + id.toStringFull());
//            }
//        }

        for (Id id : storage.keySet()) {
            if (range.containsId(id)) {
                set.addId(id);
//                System.out.println("ID " + id + " is in range " + range);
            } else {
//                System.out.println("ID " + id + " is not in range " + range);
            }
        }

        return set;
    }

    /**
     * Tells the ReplicationClient which id range it is responsible for.
     * This ReplicationClient is the primary node of all ids within that range.
     * 
     * @param range
     */
    public void setRange(IdRange range) {
        LOGGER.info(name + ": setRange to " + range);

//        if (storage.keySet().size() > 0) {
//            System.out.println("Neighborhood Set");
//            NodeHandleSet neighbors = endpoint.neighborSet(2);
//            for (int i = 0; i < neighbors.size(); i++) {
//                NodeHandle neighbor = neighbors.getHandle(i);
//                System.out.println("neighbor " + i + ": " + neighbor);
//            }
//            
//            for (Id id : storage.keySet()) {
//                NodeHandleSet replicas = endpoint.replicaSet(id, 1);
//            
//                for (int i = 0; i < replicas.size(); i++) {
//                    NodeHandle replica = replicas.getHandle(i);
//                    System.out.println("replica " + i + " for id " + id + ": " + replica);
//                }
//            }
//
//            Id id = (Id) storage.keySet().toArray()[0];
//            NodeHandleSet replicas = endpoint.replicaSet(id, 1);
//            
//            for (int i = 0; i < replicas.size(); i++) {
//                NodeHandle replica = replicas.getHandle(i);
//                System.out.println("replica " + i + ": " + replica);
//            }
//        }



//System.out.println("===============================================>>>>>>>>>>>>>>>>>>" + node.getLeafSet());

        if (myRange == null || !myRange.equals(range)) {
            myRange = range;

            IdRange deprecatedRange = range.getComplementRange();
            IdSet deprecatedIds = scan(deprecatedRange);
            Iterator i = deprecatedIds.getIterator();

            while (i.hasNext()) {
                Id id = (Id) i.next();
                storage.remove(id);
            }

            clientController.setNewRange(range);
        }

//        if (storage.keySet().size() == 0) {
//            System.out.println(name + " stores nothing");
//        } else {
//            for (Id id : storage.keySet()) {
//                System.out.println(name + " stores " + id.toStringFull());
//            }
//        }
    }
    // </editor-fold> // replication
    
    
//    int counter = 0;
//
//    /**
//     * Internal method which returns the handles to an object.  It first checks to see if
//     * the handles can be determined locally, and if so, returns.  Otherwise, it sends a
//     * LookupHandles messsage out to find out the nodes.
//     *
//     * @param id The id to fetch the handles for
//     * @param max The maximum number of handles to return
//     */
//    protected NodeHandleSet getReplicas(Id vcsId, final int max) {
//        // try to find it locally
//        NodeHandleSet set = endpoint.replicaSet(vcsId, max);
//
//        //TODO < or >, I don't know!
//        if (set.size() != max) {
////            // get it from primary node
////            
////            //TODO!!!
////            
////            //Id primaryNode = getOwnerId(vcsId);
////            
////            sendRequest(vcsId, new LookupHandlesMessage(getUID(), vcsId, max, getLocalNodeHandle(), vcsId), new StandardContinuation(command) {
////
////                public void receiveResult(Object o) {
////                    NodeHandleSet replicas = (NodeHandleSet) o;
////
////                    // check to make sure we've fetched the correct number of replicas
////                    // the deal with this is for the small ring.  If you are requesting
////                    // 4 nodes, but the ring is only 3, you are only going to get 3
////                    // Note: this is still kind of funky, because the replicationFactor+1
////                    // argument is kind of weird, but I don't know how to get it right
////                    // -Jeff 1/24/07
////                    if (Math.min(max, endpoint.replicaSet(endpoint.getLocalNodeHandle().getId(), replicationFactor + 1).size()) > replicas.size()) {
////                        parent.receiveException(new PastException("Only received " + replicas.size() + " replicas - cannot insert as we know about more nodes."));
////                    } else {
////                        parent.receiveResult(replicas);
////                    }
////                }
////            });
//        }
//
//        return set;
//    }
}
