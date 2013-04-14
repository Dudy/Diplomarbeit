package documentstorage;

import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import p2p_access.P2PAccess;
import p2p_integrationVCS_client.DocumentType;
import p2p_integrationVCS_client.P2P_IntegrationVCS_Client;
import p2p_integrationVCS_client.implementation.P2P_IntegrationVCS_ClientImplementation;
import p2p_integrationVCS_client.implementation.document.DocumentFactory;
import p2p_vcs_client.Document;
import p2p_vcs_client.implementation.P2P_VCS_ClientImplementation;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.IdRange;
import rice.p2p.replication.ReplicationImpl;

/**
 *
 * @author podolak
 */
public class ClientController extends Thread implements p2p_vcs_client.interfaces.ClientController {

    private static final Logger LOGGER = Logger.getLogger(ClientController.class);
    private static final String PROPERTIES_FILE_NAME = "client.properties";
    
    private ClientFrame view;
    private P2P_IntegrationVCS_Client client;
    private Id nodeAddress;
    private Document source;
    private Document link;
    private Document target;
    private P2PAccess p2pAccess;
    private String name;
    private PrintStream output;
    private boolean commitSemaphore = false;
    private boolean checkoutSemaphore = false;
    private int dummyInitCounter = 0;
    private ArrayList<Document> triple = new ArrayList<Document>(3);
    
    private ReplicationImpl replication;
    private static final int REPLICATION_FACTOR = 1;

    public ClientController(P2PAccess p2pAccess) {
        this(p2pAccess, "", null);
    }

    public ClientController(P2PAccess p2pAccess, String name, PrintStream outputStream) {
        BasicConfigurator.configure();
        NDC.push(name);

        LOGGER.setLevel(Level.ALL);

        // <editor-fold defaultstate="collapsed" desc=" Logger Test ">
//        System.out.println("All: " + LOGGER.getLevel().toInt());
//        
//        LOGGER.debug("Debug");
//        LOGGER.error("Error");
//        LOGGER.fatal("Fatal");
//        LOGGER.info("Info");
//        LOGGER.trace("Trace");
//        LOGGER.warn("Warn");
//        
//        LOGGER.setLevel(Level.DEBUG);
//        System.out.println("Debug: " + LOGGER.getLevel().toInt());
//        
//        LOGGER.debug("Debug");
//        LOGGER.error("Error");
//        LOGGER.fatal("Fatal");
//        LOGGER.info("Info");
//        LOGGER.trace("Trace");
//        LOGGER.warn("Warn");
//        
//        LOGGER.setLevel(Level.ERROR);
//        System.out.println("Error: " + LOGGER.getLevel().toInt());
//        
//        LOGGER.debug("Debug");
//        LOGGER.error("Error");
//        LOGGER.fatal("Fatal");
//        LOGGER.info("Info");
//        LOGGER.trace("Trace");
//        LOGGER.warn("Warn");
//        
//        LOGGER.setLevel(Level.FATAL);
//        System.out.println("Fatal: " + LOGGER.getLevel().toInt());
//        
//        LOGGER.debug("Debug");
//        LOGGER.error("Error");
//        LOGGER.fatal("Fatal");
//        LOGGER.info("Info");
//        LOGGER.trace("Trace");
//        LOGGER.warn("Warn");
//        
//        LOGGER.setLevel(Level.INFO);
//        System.out.println("Info: " + LOGGER.getLevel().toInt());
//        
//        LOGGER.debug("Debug");
//        LOGGER.error("Error");
//        LOGGER.fatal("Fatal");
//        LOGGER.info("Info");
//        LOGGER.trace("Trace");
//        LOGGER.warn("Warn");
//        
//        LOGGER.setLevel(Level.OFF);
//        System.out.println("Off: " + LOGGER.getLevel().toInt());
//        
//        LOGGER.debug("Debug");
//        LOGGER.error("Error");
//        LOGGER.fatal("Fatal");
//        LOGGER.info("Info");
//        LOGGER.trace("Trace");
//        LOGGER.warn("Warn");
//        
//        LOGGER.setLevel(Level.TRACE);
//        System.out.println("Trace: " + LOGGER.getLevel().toInt());
//        
//        LOGGER.debug("Debug");
//        LOGGER.error("Error");
//        LOGGER.fatal("Fatal");
//        LOGGER.info("Info");
//        LOGGER.trace("Trace");
//        LOGGER.warn("Warn");
//        
//        LOGGER.setLevel(Level.WARN);
//        System.out.println("Warn: " + LOGGER.getLevel().toInt());
//        
//        LOGGER.debug("Debug");
//        LOGGER.error("Error");
//        LOGGER.fatal("Fatal");
//        LOGGER.info("Info");
//        LOGGER.trace("Trace");
//        LOGGER.warn("Warn");
//        
//        LOGGER.setLevel(Level.toLevel(0));
//        System.out.println("0: " + LOGGER.getLevel().toInt());
//        
//        LOGGER.debug("Debug");
//        LOGGER.error("Error");
//        LOGGER.fatal("Fatal");
//        LOGGER.info("Info");
//        LOGGER.trace("Trace");
//        LOGGER.warn("Warn");
//        
//        LOGGER.setLevel(Level.toLevel(1));
//        System.out.println("1: " + LOGGER.getLevel().toInt());
//        
//        LOGGER.debug("Debug");
//        LOGGER.error("Error");
//        LOGGER.fatal("Fatal");
//        LOGGER.info("Info");
//        LOGGER.trace("Trace");
//        LOGGER.warn("Warn");
//        
//        LOGGER.setLevel(Level.toLevel(2));
//        System.out.println("2: " + LOGGER.getLevel().toInt());
//        
//        LOGGER.debug("Debug");
//        LOGGER.error("Error");
//        LOGGER.fatal("Fatal");
//        LOGGER.info("Info");
//        LOGGER.trace("Trace");
//        LOGGER.warn("Warn");
//        
//        LOGGER.setLevel(Level.toLevel(3));
//        System.out.println("3: " + LOGGER.getLevel().toInt());
//        
//        LOGGER.debug("Debug");
//        LOGGER.error("Error");
//        LOGGER.fatal("Fatal");
//        LOGGER.info("Info");
//        LOGGER.trace("Trace");
//        LOGGER.warn("Warn");
        // </editor-fold> // Logger Test

        this.p2pAccess = p2pAccess;
        this.name = name;
        this.output = outputStream;

//        System.setOut(outputStream);
//        System.setErr(outputStream);

        view = new ClientFrame(this, name);
        view.setBootAddress(p2pAccess.getBootaddress() + ":" + p2pAccess.getBootport());
        view.setLocalBindPort(Integer.toString(p2pAccess.getBindport()));
        view.setP2PAccessFieldEnabled(false);
        view.setVisible(true);
    }

//    private static void loadProperties() {
//        try {
//            properties = new Properties();
//            properties.load(new FileInputStream(new File(PROPERTIES_FILE_NAME)));
//        } catch (IOException ex) {
//            LOGGER.error("error while reading properties file", ex);
//        }
//    }

    public void connect() throws UnknownHostException, IOException {
        if (p2pAccess != null) {
            client = new P2P_IntegrationVCS_ClientImplementation(this, p2pAccess, output, name);
//            replication = new ReplicationImpl(p2pAccess.getNode(), client, REPLICATION_FACTOR, name);
//            replication = new ReplicationImpl(p2pAccess.getNode(),
//                    (P2P_VCS_ClientImplementation)client,
//                    REPLICATION_FACTOR, name);
            
            view.setTitle(name + " [" + p2pAccess.getNodeId().toStringFull() + "]");
        } else {
            String s = view.getBootAddress();
            
            Properties properties = new Properties();
            
            properties.setProperty("bootaddress.ip", s.substring(0, s.indexOf(":")));
            properties.setProperty("bootaddress.port", s.substring(s.indexOf(":") + 1));
            properties.setProperty("bindport", view.getLocalBindPort());

            client = new P2P_IntegrationVCS_ClientImplementation(this, properties);
        }
    }

    public void disconnect() {
        client.disconnect();
        client = null;
    }

    //    // <editor-fold defaultstate="collapsed" desc=" main ">
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            public void run() {
//                new ClientController();
//            }
//        });
//    }
//    // </editor-fold> // main
    
    public Id getNodeAddress() {
        if (nodeAddress == null) {
            nodeAddress = client.getIdFactory().buildId("");
        }

        return nodeAddress;
    }

    public String getNodeAddressText() {
        if (nodeAddress == null) {
            nodeAddress = client.getIdFactory().buildId("");
        }

        return nodeAddress.toStringFull();
    }

    public void checkoutRequest(DocumentType documentType) {
        checkoutSemaphore = true;

        LOGGER.info("checkoutRequest: getDocumentTypeString(" + documentType + ") = " + getDocumentTypeString(documentType));
        LOGGER.info("checkoutRequest: vcsId = " + client.getIdFactory().buildId(getDocumentTypeString(documentType)));

        // this is an easy way to get three different node addresses
        //TODO use a user given ID here [bootstrap problem]
        client.checkout(client.getIdFactory().buildId(getDocumentTypeString(documentType)), 0);

        // wait for checkout operation to succeed or to fail, but don't go on without result
        // use a timeout to not get stuck
        long starttime = System.currentTimeMillis();

        LOGGER.info("checkoutRequest startet bei " + starttime);

        while (checkoutSemaphore) {
            try {
                //wait(100);
                sleep(100);

                long delay = System.currentTimeMillis() - starttime;

                LOGGER.info("checkoutRequest wartet seit " + delay + " Millisekunden");

                if (delay > 10000) {
                    checkoutSemaphore = false;
                }
            } catch (InterruptedException ex) {
                checkoutSemaphore = false;
            }
        }

        LOGGER.info("checkoutRequest fertig bei " + System.currentTimeMillis() + " (= " +
                (System.currentTimeMillis() - starttime) + " ms)");
    }

    public void checkoutResponse(Id id, Object result) {
        if (result != null) {
            LOGGER.info("checkoutResponse: Successfully checked out " + result + " for id " + id + ".");

            if (result instanceof Document) {
                LOGGER.info("checkoutResponse: The result is a document.");

                Document document = (Document) result;

                DocumentType documentType = DocumentType.NONE;

                if (!"".equals(document.getMetadata().getType())) {
                    documentType = DocumentType.valueOf(document.getMetadata().getType());
                    LOGGER.info("checkoutResponse: It's type is " + documentType);
                } else {
                    LOGGER.info("checkoutResponse: It has no type");
                }

                LOGGER.info("checkoutResponse: It's version number is " + document.getVersionNumber());

                view.setDocumentText(getDocumentTypeString(documentType), documentType);
                view.setToolTipText(document.getMetamodel().getContent(), documentType);

                switch (documentType) {
                    case SOURCE:
                        source = document;
                        break;
                    case LINK:
                        link = document;
                        break;
                    case TARGET:
                        target = document;
                        break;
                }
            }
        } else {
            LOGGER.info("checkoutResponse: Checkout for id " + id + " failed.");
        }

        triple = new ArrayList<Document>(3);
        triple.add(0, source);
        triple.add(1, link);
        triple.add(2, target);

        LOGGER.info("checkoutResponse fertig");

        checkoutSemaphore = false;
    }

    public void getTagsResponse(Id vcsId, String branch, Id documentId, ArrayList<String> tags) {
        for (String tag : tags) {
            view.addOutputText(tag);
        }
    }

    public void createDocument(DocumentType documentType) {
        Document document = createNewDocument(documentType);

        switch (documentType) {
            case SOURCE:
                source = document;
                break;
            case LINK:
                link = document;
                break;
            case TARGET:
                target = document;
                break;
        }

        view.setDocumentText(getDocumentTypeString(documentType), documentType);
    }

    

    private Document createNewDocument(DocumentType documentType) {
        Document document = DocumentFactory.getInstance().newDocument();
        document.getMetamodel().setContent("vom Benutzer erzeugtes " + getDocumentTypeString(documentType) + ", Version 1");
        document.getMetadata().setType(documentType.toString());

        switch (documentType) {
            case SOURCE:
            case TARGET:
                document.getMetadata().setProperty("link", new byte[0]);
                break;
            case LINK:
                document.getMetadata().setProperty("source", new byte[0]);
                document.getMetadata().setProperty("target", new byte[0]);
                break;
        }

        return document;
    }

    public void editDocument(DocumentType documentType) {
        Document document = getDocumentByType(documentType);

        if (document != null) {
            editDocument(document);

            view.setToolTipText(document.getMetamodel().getContent(), documentType);
        }
    }

    public void editDocument(Document document) {
        if (document != null) {
            String content = document.getMetamodel().getContent();
            String start = content.substring(0, content.lastIndexOf(' ') + 1);
            String end = content.substring(content.lastIndexOf(' ') + 1);
            Integer i = null;

            try {
                i = Integer.parseInt(end) + 1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (i != null) {
                document.getMetamodel().setContent(start + i.toString());
                document.getMetadata().setProperty("baseVersionNumber", document.getVersionNumber());

                // indicates an edited document
                document.setVersionNumber(-1);
            }
        }
    }

    public void commit(DocumentType documentType) {
        long starttime = System.currentTimeMillis();

        commitSemaphore = true;

        Document document = getDocumentByType(documentType);

        if (document != null) {
            LOGGER.info("commit(" + documentType + "): " + document);
            client.commit(client.getIdFactory().buildId(getDocumentTypeString(documentType)), document);

            // wait for commit operation to succeed or to fail, but don't go on without result
            // use a timeout to not get stuck

            LOGGER.info("commit startet bei " + starttime);

            while (commitSemaphore) {
                try {
                    //wait(100);
                    sleep(100);

                    long delay = System.currentTimeMillis() - starttime;

                    LOGGER.info("commit wartet seit " + delay + " Millisekunden");

                    if (delay > 10000) {
                        commitSemaphore = false;
                    }
                } catch (InterruptedException ex) {
                    commitSemaphore = false;
                }
            }
        }

        LOGGER.info("commit fertig bei " + System.currentTimeMillis() + " (= " +
                (System.currentTimeMillis() - starttime) + " ms)");
    }

    public void commitResponse(Id id, boolean success, String documentTypeString) {
        if (success) {
            LOGGER.info("commitResponse: commit successful");
            DocumentType documentType = DocumentType.valueOf(documentTypeString);
            view.setDocumentTextSimple(documentType);
        } else {
            LOGGER.info("commitResponse: commit not successful");
        }

        commitSemaphore = false;
    }
    
    public void commitResponse(Id vcsId, Id nodeAddress, Id documentId, boolean success, String documentTypeString) {
        if (success) {
            LOGGER.info("commitResponse: commit successful");
            LOGGER.info("document with id " + documentId + " stored on node " + nodeAddress + " with VCS ID " + vcsId);
            DocumentType documentType = DocumentType.valueOf(documentTypeString);
            view.setDocumentTextSimple(documentType);
        } else {
            LOGGER.info("commitResponse: commit not successful");
        }

        commitSemaphore = false;
    }

    private Document getDocumentByType(DocumentType documentType) {
        Document document = null;

        switch (documentType) {
            case SOURCE:
                document = source;
                break;
            case LINK:
                document = link;
                break;
            case TARGET:
                document = target;
                break;
        }

        return document;
    }

    private String getDocumentTypeString(DocumentType documentType) {
        String typestring = "unbekanntes Dokument";

        switch (documentType) {
            case SOURCE:
                typestring = "Quelldokument";
                break;
            case LINK:
                typestring = "Linkdokument";
                break;
            case TARGET:
                typestring = "Zieldokument";
                break;
        }

        return typestring;
    }

//    private void log(String text) {
//    //output.println(name + "[" + LOG_PREFIX + "] " + text);
//      System.out.println(name + "[" + LOG_PREFIX + "] " + text);
//    }

    // <editor-fold defaultstate="collapsed" desc=" init dummy repository ">
//dummy repository:
//
//Step   Source   Link   Target    action
//  1       -       -       -      new source (create)
//  2       1       -       -      forward(source) => new link, new target
//  3       1       1       1      new source (edit)
//  4       2       1       1      new source (edit)
//  5       3       1       1      forward(source) => old link, new target
//  6       3       1       2      new source (edit)
//  7       4       1       2      forward(source) => old link, old target
//  8       4       1       2      new target (edit)
//  9       4       1       3      backward(target) => old source, old link
// 10       4       1       3      new target (edit)
// 11       4       1       4      new target (edit)
// 12       4       1       5      new link (edit)
// 13       4       2       5      new target (edit)
// 14       4       2       6      backward(target) => new source, new link
// 15       5       3       6      new target (edit)
// 16       5       3       7      backward(target) => new source, old link
// 17       6       3       7
//
//possible actions before commit:
//- new source (create or edit)
//- new link (create or edit)
//- new target (create or edit)
//- forward(source) => old link, old target
//- forward(source) => old link, new target
//- forward(source) => new link, new target
//- backward(target) => old source, old link
//- backward(target) => new source, old link
//- backward(target) => new source, new link
//
//consistent tripels are:
//(1,1,1)
//(3,1,2)
//(4,1,2)
//(4,1,3)
//(5,3,6)
//(6,3,7)
    public void dummyInitRepository() {
        dummyInitCounter++;

        source = null;
        link = null;
        target = null;

        triple = new ArrayList<Document>(3);
        triple.add(source);
        triple.add(link);
        triple.add(target);

        // step 1
        if (dummyInitCounter == 1) {
            LOGGER.debug("step 1");
            source = createNewDocument(DocumentType.SOURCE);
            LOGGER.debug("1a");
            commit(DocumentType.SOURCE);
            LOGGER.debug("1b");
        }

        // step 2
        if (dummyInitCounter == 2) {

            LOGGER.debug("step 2");
            checkoutRequest(DocumentType.SOURCE);
            LOGGER.debug("2a");
            triple = forwardTransformation_newLink_newTarget(triple);
            LOGGER.debug("2b");
            commit(DocumentType.SOURCE); // die Source nicht einchecken? Ist ja die alte ... TODO
            LOGGER.debug("2c");

            commit(DocumentType.LINK);
            LOGGER.debug("2d");
            commit(DocumentType.TARGET);
            LOGGER.debug("2e");
        }

        // step 3
        if (dummyInitCounter == 3) {
            LOGGER.debug("step 3");
            checkoutRequest(DocumentType.SOURCE);
            LOGGER.debug("3a");
            checkoutRequest(DocumentType.LINK);
            LOGGER.debug("3b");
            checkoutRequest(DocumentType.TARGET);
            LOGGER.debug("3c");
            editDocument(source);
            LOGGER.debug("3d");
            commit(DocumentType.SOURCE);
            LOGGER.debug("3e");
        }

        // step 4
        if (dummyInitCounter == 4) {
            LOGGER.debug("step 4");
            // kein source checkout! es wird lokal weitergearbeitet ...
            editDocument(source);
            commit(DocumentType.SOURCE);
        }

        // step 5
        if (dummyInitCounter == 5) {
            LOGGER.debug("step 5");
            checkoutRequest(DocumentType.SOURCE);
            triple = forwardTransformation_oldLink_newTarget(triple);
            commit(DocumentType.SOURCE);  // die Source nicht einchecken? Ist ja die alte ... TODO
            commit(DocumentType.LINK);  // den Link nicht einchecken? Ist ja der alte ... TODO
            commit(DocumentType.TARGET);
        }

        // step 6
        if (dummyInitCounter == 6) {
            checkoutRequest(DocumentType.SOURCE);
            checkoutRequest(DocumentType.LINK);
            checkoutRequest(DocumentType.TARGET);
            LOGGER.debug("step 6");
            editDocument(source);
            commit(DocumentType.SOURCE);
        }

        // step 7
        if (dummyInitCounter == 7) {
            LOGGER.debug("step 7");
            checkoutRequest(DocumentType.SOURCE);
            triple = forwardTransformation_oldLink_oldTarget(triple);
            commit(DocumentType.SOURCE);  // die Source nicht einchecken? Ist ja die alte ... TODO
            commit(DocumentType.LINK);  // den Link nicht einchecken? Ist ja der alte ... TODO
            commit(DocumentType.TARGET);  // das Target nicht einchecken? Ist ja das alte ... TODO
        }

        // step 8
        if (dummyInitCounter == 8) {
            LOGGER.debug("step 8");
            checkoutRequest(DocumentType.SOURCE);
            checkoutRequest(DocumentType.LINK);
            checkoutRequest(DocumentType.TARGET);
            editDocument(target);
            commit(DocumentType.TARGET);
        }

        // step 9
        if (dummyInitCounter == 9) {
            LOGGER.debug("step 9");
            // kein checkout, es werden nur die Targetänderungen aus step 8 transformiert
            triple = backwardTransformation_oldSource_oldLink(triple);
            commit(DocumentType.SOURCE);  // die Source nicht einchecken? Ist ja die alte ... TODO
            commit(DocumentType.LINK);  // den Link nicht einchecken? Ist ja der alte ... TODO
            commit(DocumentType.TARGET);  // das Target nicht einchecken? Ist ja das alte ... TODO
        }

        // step 10
        if (dummyInitCounter == 10) {
            // kein checkout, Weiterarbeit an Target
            LOGGER.debug("step 10");
            editDocument(target);
            commit(DocumentType.TARGET);
        }

        // step 11
        if (dummyInitCounter == 11) {
            LOGGER.debug("step 11");
            // jemand anderes arbeitet an Target weiter
            checkoutRequest(DocumentType.TARGET);
            editDocument(target);
            commit(DocumentType.TARGET);
        }

        // step 12
        if (dummyInitCounter == 12) {
            LOGGER.debug("step 12");
            checkoutRequest(DocumentType.SOURCE);
            checkoutRequest(DocumentType.LINK);
            checkoutRequest(DocumentType.TARGET);
            editDocument(link);
            commit(DocumentType.LINK);
        }

        // step 13
        if (dummyInitCounter == 13) {
            LOGGER.debug("step 13");
            // kein checkout
            editDocument(target);
            commit(DocumentType.TARGET);
        }

        // step 14
        if (dummyInitCounter == 14) {
            LOGGER.debug("step 14");
            checkoutRequest(DocumentType.SOURCE);
            checkoutRequest(DocumentType.LINK);
            checkoutRequest(DocumentType.TARGET);
            triple = backwardTransformation_newSource_newLink(triple);
            commit(DocumentType.SOURCE);
            commit(DocumentType.LINK);
            commit(DocumentType.TARGET);  // das Target nicht einchecken? Ist ja das alte ... TODO
        }

        // step 15
        if (dummyInitCounter == 15) {
            LOGGER.debug("step 15");
            checkoutRequest(DocumentType.SOURCE);
            checkoutRequest(DocumentType.LINK);
            checkoutRequest(DocumentType.TARGET);
            editDocument(target);
            commit(DocumentType.TARGET);
        }

        // step 16
        if (dummyInitCounter == 16) {
            LOGGER.debug("step 16");
            checkoutRequest(DocumentType.SOURCE);
            checkoutRequest(DocumentType.LINK);
            checkoutRequest(DocumentType.TARGET);
            triple = backwardTransformation_newSource_oldLink(triple);
            commit(DocumentType.SOURCE);
            commit(DocumentType.LINK);  // den Link nicht einchecken? Ist ja der alte ... TODO
            commit(DocumentType.TARGET);  // das Target nicht einchecken? Ist ja das alte ... TODO
        }
    }

    private ArrayList<Document> forwardTransformation_newLink_newTarget(ArrayList<Document> documents) {
        LOGGER.debug("forwardTransformation_newLink_newTarget");

        ArrayList<Document> newDocuments = new ArrayList<Document>(3);

        Document newSource = documents.get(0);
        Document newLink = documents.get(1);
        Document newTarget = documents.get(2);

        LOGGER.debug("source: " + newSource);
        LOGGER.debug("link: " + newLink);
        LOGGER.debug("target: " + newTarget);

        // target
        if (newTarget == null) {
            LOGGER.debug("create new target");
            newTarget = createNewDocument(DocumentType.TARGET);
        }

        newTarget.getMetamodel().setContent("erzeugt durch forward(" + newSource.getId().toStringFull() + ")");

        // link
        if (newLink == null) {
            LOGGER.debug("create new link");
            newLink = createNewDocument(DocumentType.LINK);
        }

        newLink.getMetamodel().setContent("erzeugt durch forward(" + newSource.getId().toStringFull() + ")");
        newLink.getMetadata().setProperty("source", newSource.getId());
        newLink.getMetadata().setProperty("target", newTarget.getId());

        return newDocuments;
    }

    private ArrayList<Document> forwardTransformation_oldLink_newTarget(ArrayList<Document> documents) {
        ArrayList<Document> newDocuments = new ArrayList<Document>(3);

        Document newSource = documents.get(0);
        Document newLink = documents.get(1);
        Document newTarget = documents.get(2);

        // target
        if (newTarget == null) {
            newTarget = createNewDocument(DocumentType.TARGET);
        }

        newTarget.getMetamodel().setContent("erzeugt durch forward(" + newSource.getId().toStringFull() + ")");

        // link
        newLink.getMetadata().setProperty("source", newSource.getId());
        newLink.getMetadata().setProperty("target", newTarget.getId());

        return newDocuments;
    }

    private ArrayList<Document> forwardTransformation_oldLink_oldTarget(ArrayList<Document> documents) {
        ArrayList<Document> newDocuments = new ArrayList<Document>(3);

        Document newSource = documents.get(0);
        Document newLink = documents.get(1);
        Document newTarget = documents.get(2);

        // target

        // link
        newLink.getMetadata().setProperty("source", newSource.getId());
        newLink.getMetadata().setProperty("target", newTarget.getId());

        return newDocuments;
    }

    private ArrayList<Document> backwardTransformation_newSource_newLink(ArrayList<Document> documents) {
        ArrayList<Document> newDocuments = new ArrayList<Document>(3);

        Document newSource = documents.get(0);
        Document newLink = documents.get(1);
        Document newTarget = documents.get(2);

        // source
        if (newSource == null) {
            newSource = createNewDocument(DocumentType.SOURCE);
        }

        newSource.getMetamodel().setContent("erzeugt durch backward(" + newTarget.getId().toStringFull() + ")");

        // link
        if (newLink == null) {
            newLink = createNewDocument(DocumentType.LINK);
        }

        newLink.getMetamodel().setContent("erzeugt durch backward(" + newTarget.getId().toStringFull() + ")");
        newLink.getMetadata().setProperty("source", newSource.getId());
        newLink.getMetadata().setProperty("target", newTarget.getId());

        return newDocuments;
    }

    private ArrayList<Document> backwardTransformation_newSource_oldLink(ArrayList<Document> documents) {
        ArrayList<Document> newDocuments = new ArrayList<Document>(3);

        Document newSource = documents.get(0);
        Document newLink = documents.get(1);
        Document newTarget = documents.get(2);

        // source
        if (newSource == null) {
            newSource = createNewDocument(DocumentType.SOURCE);
        }

        newSource.getMetamodel().setContent("erzeugt durch backward(" + newTarget.getId().toStringFull() + ")");

        // link
        newLink.getMetadata().setProperty("source", newSource.getId());
        newLink.getMetadata().setProperty("target", newTarget.getId());

        return newDocuments;
    }

    private ArrayList<Document> backwardTransformation_oldSource_oldLink(ArrayList<Document> documents) {
        ArrayList<Document> newDocuments = new ArrayList<Document>(3);

        Document newSource = documents.get(0);
        Document newLink = documents.get(1);
        Document newTarget = documents.get(2);

        // source

        // link
        newLink.getMetadata().setProperty("source", newSource.getId());
        newLink.getMetadata().setProperty("target", newTarget.getId());

        return newDocuments;
    }

    public int getDummyInitCounter() {
        return dummyInitCounter;
    }
    // </editor-fold> // init dummy repository
    
    public void replicate() {
        LOGGER.info(name + ": replicate");
        //client.replicate();
        replication.replicate();
    }
    
    public void test() {
        LOGGER.info("test");
        
        Id id = client.getIdFactory().buildId(getDocumentTypeString(DocumentType.SOURCE));
        IdRange all = client.getIdFactory().buildIdRange(client.getIdFactory().buildId(new byte[20]), client.getIdFactory().buildId(new byte[20]));
        
        if (client.scan(all).isMemberId(id)) {
            view.appendOutput(id.toStringFull() + " belongs to me.");
        } else {
            view.appendOutput(id.toStringFull() + " does not belong to me.");
        }
        
        //view.appendOutput(id.toStringFull() + " belongs to " + client.getOwnerName(id) + " with id " + client.getOwnerId(id));
    }

    public void setNewRange(IdRange range) {
        view.appendOutput("new range: " + range);
    }
    
    public void output(String text) {
        view.appendOutput(text);
    }
}
