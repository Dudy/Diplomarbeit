/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_integrationVCS_client.implementation;

import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import p2p_access.P2PAccess;
import p2p_integrationVCS_client.DocumentType;
import p2p_integrationVCS_client.Link;
import p2p_integrationVCS_client.P2P_IntegrationVCS_Client;
import p2p_integrationVCS_client.message.CommandMessage;
import p2p_integrationVCS_client.message.CommitMessage;
import p2p_integrationVCS_client.message.CommitSuccessfulMessage;
import p2p_integrationVCS_client.message.MessageType;
import p2p_integrationVCS_client.message.ReleaseLockRequestMessage;
import p2p_integrationVCS_client.message.ReleaseLockResponseMessage;
import p2p_integrationVCS_client.message.TupelUpdateValidRequestMessage;
import p2p_integrationVCS_client.message.TupelUpdateValidResponseMessage;
import p2p_vcs_client.implementation.P2P_VCS_ClientImplementation;
import p2p_vcs_client.Document;
import p2p_vcs_client.implementation.storage.StorageManager;
import p2p_vcs_client.interfaces.ClientController;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;

/**
 *
 * @author podolak
 */
public class P2P_IntegrationVCS_ClientImplementation extends P2P_VCS_ClientImplementation implements P2P_IntegrationVCS_Client {

    private final static Logger LOGGER = Logger.getLogger(P2P_IntegrationVCS_ClientImplementation.class);
    
    private int releaseIndex;

    // <editor-fold defaultstate="collapsed" desc=" constructors ">
    public P2P_IntegrationVCS_ClientImplementation(ClientController clientController, Properties properties)
            throws UnknownHostException, IOException {
        super(clientController, properties);
    }

    public P2P_IntegrationVCS_ClientImplementation(ClientController clientController, P2PAccess p2pAccess)
            throws UnknownHostException, IOException {
        super(clientController, p2pAccess);
    }

    public P2P_IntegrationVCS_ClientImplementation(ClientController clientController, P2PAccess p2pAccess,
            PrintStream outputStream) throws UnknownHostException, IOException {
        super(clientController, p2pAccess, outputStream);
    }

    public P2P_IntegrationVCS_ClientImplementation(ClientController clientController, P2PAccess p2pAccess,
            PrintStream outputStream, String name) throws UnknownHostException, IOException {
        super(clientController, p2pAccess, outputStream, name);
    }
    // </editor-fold> // constructors
    
//[-]   getConsistentTriple(singleDocument)
//[ ]   getLink(source, target)
//[ ]   getLatestTriple()
//[ ]   getLastConsistentTriple()
//[x]   commitSingleDocument(document, documentType)
//[ ]   commitTriple(triple)
//[ ]   getHistory(document, branch)
//[ ]   getCompleteDocumentHistory (all branches, all document versions, GUI?)
    
    public ArrayList<Document> getLastConsistentTriple() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList<Document> getConsistentTriple(Id documentId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Link getLink() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList<Document> getLatestTriple() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void commitTripel(Id vcsId, ArrayList<Document> documentList) {
        commitTripel(vcsId, documentList, "HEAD");
    }
    
    public void commitTripel(Id vcsId, ArrayList<Document> documentList, String branch) {
        commitDocumentList = documentList;
        commitIndex = -2;

        if (documentList != null && documentList.size() > 0) {
            commitIndex = -1;
            sendNext(vcsId, branch);
        } else {
            //TODO ???
        }
    }

    public ArrayList<String> getDocumentHistory(String branch, Id documentId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getNodeStatus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    // <editor-fold defaultstate="collapsed" desc=" message handling ">
    private void handleError(CommandMessage message) {
        LOGGER.info(name + ": handleError mit message: " + message);
        
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleIsUpdateValidMessage(TupelUpdateValidRequestMessage message) {
        LOGGER.info(name + ": handleIsUpdateValidMessage mit message: " + message);
        
        CommandMessage responseMessage = null;
        StorageManager storageManager = storage.get(message.getVcsId());

        if (storageManager == null) {
            LOGGER.info(name + ": handleIsUpdateValidMessage: no storage manager found, assume this is will be a new replica soon");
            
            // the document that should be updated is not hosted on this node
            //message.setType(MessageType.WRONG_NODE_ERROR);

            //TODO rausfinden, ob das wirklich der falsche Knoten ist oder ob neu initial eingecheckt wird
            
            // assume the document should be stored initially (= this node is a new replica) and return true
            responseMessage = new TupelUpdateValidResponseMessage(
                    node.getLocalNodeHandle(),
                    message.getVcsId(),
                    message.getDocumentId(),
                    message.getBranch());
        } else {
            if (!storageManager.isActualVersion_WithLockingCheck(message.getVcsId())) {
                // version number of the document is outdated
                responseMessage.setIntegrationSpecificType(MessageType.DOCUMENT_VERSION_OUTDATED_ERROR);
            } else {
                responseMessage = new TupelUpdateValidResponseMessage(
                        node.getLocalNodeHandle(),
                        message.getVcsId(),
                        message.getDocumentId(),
                        message.getBranch());
            }
        }

        // send answer
        send(responseMessage, message.getSender());
    }

    private void handleUpdateValid(TupelUpdateValidResponseMessage message) {
        LOGGER.info(name + ": handleUpdateValid mit message: " + message);
        primaryNodes.put(message.getVcsId(), message.getSender());
        send(
                new CommitMessage(
                    node.getLocalNodeHandle(),
                    message.getVcsId(),
                    commitDocumentList.get(commitIndex),
                    message.getBranch()
                ),
                message.getSender());
    }
    
    private void handleCommitMessage(CommitMessage message) {
        if (message != null) {
            LOGGER.info(name + ": handleCommitMessage: " + message.toString());

            Document document = message.getDocument();

            if (document.getId() != null) {
                LOGGER.info(name + ": handleCommitMessage: id of document to commit = " + document.getId().toStringFull());
            } else {
                LOGGER.info(name + ": handleCommitMessage: id of document to commit is NULL");
            }

            LOGGER.info(name + ": handleCommitMessage: search StorageManager for " + message.getVcsId());

            StorageManager storageManager = storage.get(message.getVcsId());
            CommandMessage responseMessage = null;

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
            //   // the document that should be updated is not hosted on this node
            //   // send message with type MessageType.WRONG_NODE_ERROR
            // }
            //
            // anstatt iteration vielleicht auch replicaNodes.contain(node.getNodeHandle()); ??
            
            if (storageManager == null) {
                LOGGER.info(name + ": handleCommitMessage: storage manager is null");

                // store document for the first time
                storageManager = new StorageManager(name);
                storageManager.addNewVersion_WithLocking(message.getVcsId(), document);

                // --- new (replication) ---
                //TODO Is this necessary? Does the framework replicate by itself?
                // which one of the following two?
                //NodeHandleSet replicaNodes = endpoint.replicaSet(commitMessage.getVcsId(), REPLICATION_FACTOR);
                //NodeHandleSet replicaNodes = getReplicas();

                responseMessage = new CommitSuccessfulMessage(node.getLocalNodeHandle(), message.getVcsId(), document.getId(), message.getBranch());
            } else if (!storageManager.isActualVersion_WithLockingCheck(document.getId())) {
                LOGGER.info(name + ": handleCommitMessage: found StorageManager: " + storageManager);
                LOGGER.info(name + ": handleCommitMessage: outdated document");

                // version number of the document is outdated
                responseMessage = new CommandMessage(node.getLocalNodeHandle(), message.getVcsId(),
                        MessageType.DOCUMENT_VERSION_OUTDATED_ERROR);
            } else {
                LOGGER.info(name + ": handleCommitMessage: found StorageManager: " + storageManager);
                LOGGER.info(name + ": handleCommitMessage: store new version of " + document);

                // store new version
                storageManager.addNewVersion_WithLocking(message.getVcsId(), document);

                // update replicas
                //TODO, necassary? Yes, if we don't want to wait till next maintainance cycle.

                responseMessage = new CommitSuccessfulMessage(node.getLocalNodeHandle(), message.getVcsId(), document.getId(), message.getBranch());
            }

            // inform user
            send(responseMessage, message.getSender());
        } else {
            LOGGER.error(name + ": handleCommitMessage called with NULL message");
        }
    }
    
    protected void handleCommitSuccessfulMessage(CommitSuccessfulMessage message) {
        if (message != null) {
            LOGGER.info(name + ": handleCommitSuccessfulMessage: " + message);
            primaryNodes.put(message.getVcsId(), message.getSender());
            sendNext(message.getVcsId(), message.getBranch());
        } else {
            LOGGER.error(name + ": handleCommitSuccessfulMessage called with NULL message");
        }
    }
    
    protected void handleReleaseLockRequestMessage(ReleaseLockRequestMessage message) {
        if (message != null) {
            LOGGER.info(name + ": handleReleaseLockRequestMessage: " + message.toString());

            if (message.getDocumentId() != null) {
                LOGGER.info(name + ": handleReleaseLockRequestMessage: id of document to release = " + message.getDocumentId().toStringFull());
            } else {
                LOGGER.info(name + ": handleReleaseLockRequestMessage: id of document to release is NULL");
            }

            LOGGER.info(name + ": handleReleaseLockRequestMessage: search StorageManager for " + message.getVcsId());

            StorageManager storageManager = storage.get(message.getVcsId());
            boolean success = false;

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
            //   // the document that should be updated is not hosted on this node
            //   // send message with type MessageType.WRONG_NODE_ERROR
            // }
            //
            // anstatt iteration vielleicht auch replicaNodes.contain(node.getNodeHandle()); ??
            
            if (storageManager == null) {
                LOGGER.info(name + ": handleReleaseLockRequestMessage: storage manager is null");
                
                // nothing to release
                success = false;
            } else {
                LOGGER.info(name + ": handleReleaseLockRequestMessage: found StorageManager: " + storageManager);
                LOGGER.info(name + ": handleReleaseLockRequestMessage: unlock document " + message.getDocumentId());
                storageManager.unlock(message.getBranch());
                success = true;
            }
            
            // answer caller
            send(new ReleaseLockResponseMessage(node.getLocalNodeHandle(), message.getVcsId(), message.getDocumentId(),
                    success, message.getBranch()), message.getSender());
        } else {
            LOGGER.error(name + ": handleReleaseLockRequestMessage called with NULL message");
        }
    }
    
    protected void handleReleaseLockResponseMessage(ReleaseLockResponseMessage message) {
        if (message != null) {
            LOGGER.info(name + ": handleReleaseLockResponseMessage: " + message);
            primaryNodes.put(message.getVcsId(), message.getSender());
            
            if (message.getSuccess()) {
                releaseNext(message.getVcsId(), message.getBranch());
            } else {
                //TODO file couldn't get unlocked, error ? For the moment, just continue.
                releaseNext(message.getVcsId(), message.getBranch());
            }
        } else {
            LOGGER.error(name + ": handleReleaseLockResponseMessage called with NULL message");
        }
    }
    // </editor-fold> // message handling

    // <editor-fold defaultstate="collapsed" desc=" helper ">
    private void sendNext(Id vcsId) {
        sendNext(vcsId, "HEAD");
    }
    
    private void sendNext(Id vcsId, String branch) {
        commitIndex++;

        if (commitIndex == commitDocumentList.size()) {
        // stop!
//            // return to user
//            System.out.println("----------------> commit done");
//            
//            clientController.commitResponse(vcsId, true);

        // all documents are checked in, but locked (set to invalid)
        // now set a release message to make them valid
            
            releaseIndex = -1;
            releaseNext(vcsId);
        } else {
            System.out.println("send next message");

            if (commitDocumentList.get(commitIndex) == null) {
                //TODO is this valid? May you checkin only parts of a tupel? Metainfoupdate nonetheless?
                sendNext(vcsId, branch);
            } else {
                sendTransactionMessage(vcsId, new TupelUpdateValidRequestMessage(
                        node.getLocalNodeHandle(), vcsId, commitDocumentList.get(commitIndex).getId(), branch));
            }
        }
    }
    
    private void releaseNext(Id vcsId) {
        releaseNext(vcsId, "HEAD");
    }
    
    private void releaseNext(Id vcsId, String branch) {
        releaseIndex++;
        
        if (releaseIndex == commitDocumentList.size()) {
            // stop!
            // return to user
            clientController.commitResponse(vcsId, true, DocumentType.TRIPLE.toString());
        } else {
            if (commitDocumentList.get(releaseIndex) == null) {
                //TODO is this valid? May you checkin only parts of a tupel? Metainfoupdate nonetheless?
                releaseNext(vcsId);
            } else {
                sendTransactionMessage(vcsId, new ReleaseLockRequestMessage(
                        node.getLocalNodeHandle(), vcsId, commitDocumentList.get(releaseIndex).getId(), branch));
            }
        }
    }
    // </editor-fold> // helper
    
    @Override
    public void deliver(Id id, Message message) {
        if (message == null) {
            LOGGER.error("deliver wants to deliver a NULL message, id: " + id.toStringFull());
        } else {
            LOGGER.info("deliver: message class = " + message.getClass());

            if (message instanceof CommandMessage) {
                CommandMessage commandMessage = (CommandMessage) message;

                LOGGER.info("deliver: sender = " + commandMessage.getSender().getId().toStringFull());

                if (commandMessage.getSender() != node.getId()) {

                    switch (commandMessage.getIntegrationSpecificType()) {
                        case NONE:
                            break;

                        case IS_UPDATE_VALID:
                            LOGGER.info("deliver: UpdateValidRequestMessage von " +
                                    commandMessage.getSender());
                            handleIsUpdateValidMessage((TupelUpdateValidRequestMessage) commandMessage);
                            break;
                        case UPDATE_IS_VALID:
                            LOGGER.info("deliver: UpdateValidResponseMessage von " +
                                    commandMessage.getSender());
                            handleUpdateValid((TupelUpdateValidResponseMessage) commandMessage);
                            break;
                            
                        case COMMIT:
                            LOGGER.info("deliver: CommitMessage von " + commandMessage.getSender());
                            handleCommitMessage((CommitMessage) commandMessage);
                            break;
                        case COMMIT_SUCCESSFUL:
                            LOGGER.info("deliver: CommitSuccessfulMessage von " + commandMessage.getSender());
                            handleCommitSuccessfulMessage((CommitSuccessfulMessage) commandMessage);
                            break;
                        case RELEASE_LOCK:
                            LOGGER.info("deliver: ReleaseLockRequestMessage von " + commandMessage.getSender());
                            handleReleaseLockRequestMessage((ReleaseLockRequestMessage) commandMessage);
                            break;
                        case RELEASE_LOCK_RESPONSE:
                            LOGGER.info("deliver: ReleaseLockResponseMessage von " + commandMessage.getSender());
                            handleReleaseLockResponseMessage((ReleaseLockResponseMessage) commandMessage);
                            break;
                            
                        case DOCUMENT_VERSION_OUTDATED_ERROR:
                        case WRONG_NODE_ERROR:
                            LOGGER.info("deliver: CommandMessage [" +
                                    commandMessage.getMessageType() + "] von " +
                                    commandMessage.getSender());
                            handleError((CommandMessage) commandMessage);
                            break;
                    }
                } else {
                    LOGGER.info("deliver: I don't ask myself :-)");
                }
            } else {
                super.deliver(id, message);
            }
        }
    }
}
