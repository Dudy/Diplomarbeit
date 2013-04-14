package p2p_vcs_client.implementation.storage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Timer;
import org.apache.log4j.Logger;
import p2p_access.P2PAccess;
import p2p_vcs_client.Document;
import p2p_vcs_client.implementation.SerializationUtilities;
import rice.Continuation;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.IdFactory;


//TODO There is a storage mechanism in PAST (via interface StorageManager)
import rice.pastry.IdRange;
import rice.pastry.IdSet;
// Can this be used?
/**
 *
 * @author podolak
 */
public class StorageManager {
    
    // Versionierung von Metadaten geschieht durch Versionierung von Document, bei dem sich zum
    // Vorgänger nur die Metadaten geändert haben.
    
    // logger
    private final static Logger LOGGER = Logger.getLogger(StorageManager.class);
    // find version number by id
    private BranchIdVersionNumberHashMap versionNumbersByIdInBranch;
    // find id by version number
    private BranchIdList idsByVersionNumber;
    // actual versions
    private BranchActualVersionNumberHashMap actualVersions;
    // actual documents
    private BranchActualDocuments actualDocuments;
    // document version history
    private BranchDocumentList branches;
    // tags (branch -> id -> tags
    private BranchIdTagListHashMap tagListOfDocumentInBranch;
    // vcsId (node address) for the document ids
    private IdIdHashMap vcsIdByDocumentId;
    // factory to build IDs
    private IdFactory idFactory;
    // name of the application, maily for debugging
    private String name;
    private HashMap<String, Boolean> mostActualVersionLockedHashMap;
    private Timer lockReleaseTimer;
    private static final int LOCK_RELEASE_TIMEOUT = 10000; // milliseconds

    public StorageManager() {
        this("");
    }

    public StorageManager(String name) {
        this(P2PAccess.getIdFactory(), name);
    }

    public StorageManager(IdFactory idFactory, String name) {
        //BasicConfigurator.configure();
        //NDC.push("[" + name + "]"); 

        this.idFactory = idFactory;
        this.name = name;

        versionNumbersByIdInBranch = new BranchIdVersionNumberHashMap();
        versionNumbersByIdInBranch.put("HEAD", new IdVersionNumberHashMap());

        idsByVersionNumber = new BranchIdList();
        idsByVersionNumber.put("HEAD", new IdList());

        actualVersions = new BranchActualVersionNumberHashMap();
        actualDocuments = new BranchActualDocuments();

        branches = new BranchDocumentList();
        branches.put("HEAD", new DocumentList());

        tagListOfDocumentInBranch = new BranchIdTagListHashMap();
        tagListOfDocumentInBranch.put("HEAD", new IdTagListHashMap(0));

        vcsIdByDocumentId = new IdIdHashMap(0);

        // beta!
        mostActualVersionLockedHashMap = new HashMap<String, Boolean>();
        mostActualVersionLockedHashMap.put("HEAD", Boolean.FALSE);
    }

    public Document getActualDocument() {
        return getActualDocument("HEAD");
    }

    public Document getActualDocument(String branch) {
        return actualDocuments.get(branch);
    }

    public Integer getActualVersion() {
        return getActualVersion("HEAD");
    }

    public Integer getActualVersion(String branch) {
        return actualVersions.get(branch);
    }

    public Document getDocument(int version) {
        return getDocument(version, "HEAD");
    }

    public Document getDocument(int version, String branch) {
        Document document = null;

        if (version < 0) {
            //TODO, error?
        } else if (version == 0) {
            document = actualDocuments.get(branch);
        } else {
            document = branches.get(branch).get(version);
        }

        if (document != null) {
            // document is possibly about to get changed, store base version number
            document.getMetadata().setProperty("baseVersionNumber", document.getVersionNumber());
        }

        return document;
    }

    public int getVersionNumber(Id documentId) {
        return getVersionNumber(documentId, "HEAD");
    }

    public int getVersionNumber(Id documentId, String branch) {
        int versionNumber = -1;

        if (documentId != null && branch != null && !branch.equals("")) {
            HashMap<Id, Integer> versionNumberById = versionNumbersByIdInBranch.get(branch);

            if (versionNumberById != null) {
                versionNumber = versionNumberById.get(documentId);
            }
        }

        return versionNumber;
    }

    public boolean isActualVersion(Id documentId) {


        return isActualVersion(documentId, "HEAD");
    }

    public boolean isActualVersion(Id documentId, String branch) {
        return
                documentId != null &&
                (
                    getActualDocument(branch) == null ||
                    documentId.equals(getActualDocument(branch).getId()
                ));
    }

    public void addNewVersion(Id vcsId, Document document) {
        addNewVersion(vcsId, document, "HEAD");
    }

    public void addNewVersion(Id vcsId, Document document, String branch) {
        int newVersionNumber = 1;

        if (getActualVersion(branch) != null) {
            newVersionNumber = getActualVersion(branch).intValue() + 1;
        }

        document.setVersionNumber(newVersionNumber);
        document.setId(idFactory.buildId(new String(document.toByteArray())));
        
        versionNumbersByIdInBranch.get(branch).put(document.getId(), newVersionNumber);
        idsByVersionNumber.get(branch).add(document.getId());
        branches.get(branch).add(document);
        actualVersions.put(branch, newVersionNumber);
        actualDocuments.put(branch, document);
        vcsIdByDocumentId.put(document.getId(), vcsId);
    }

    public void createBranch(String newBranchName) throws BranchAlreadyExistsException {
        createBranch("HEAD", newBranchName);
    }

    public void createBranch(String sourceBranch, String newBranchName) throws BranchAlreadyExistsException {
        if (branches.containsKey(newBranchName)) {
            throw new BranchAlreadyExistsException();
        } else {
            branches.put(newBranchName, (DocumentList) branches.get(sourceBranch).clone());
        }
    }

    public void tag(String tagText, Id documentId) {
        this.tag(tagText, documentId, "HEAD");
    }

    public void tag(String tagText, Id documentId, String branch) {
        if (branch == null || branch.equals("")) {
            branch = "HEAD";
        }

        if (documentId != null) {
            if (tagText != null && !tagText.equals("")) {
                IdTagListHashMap branchTags = tagListOfDocumentInBranch.get(branch);

                if (branchTags == null) {
                    branchTags = new IdTagListHashMap(1);
                    tagListOfDocumentInBranch.put(branch, branchTags);
                }

                TagList tags = branchTags.get(documentId);
                if (tags == null) {
                    tags = new TagList(1);
                    branchTags.put(documentId, tags);
                }

                tags.add(tagText);
            }
        }
    }

    public ArrayList<String> getTags(Id documentId) {
        return getTags("HEAD", documentId);
    }

    public ArrayList<String> getTags(String branch, Id documentId) {
        ArrayList<String> tags = null;

        if (branch == null || branch.equals("")) {
            branch = "HEAD";
        }

        if (documentId != null) {
            IdTagListHashMap branchTags = tagListOfDocumentInBranch.get(branch);

            if (branchTags != null) {
                tags = branchTags.get(documentId);
            }
        }

        return tags;
    }

    // <editor-fold defaultstate="collapsed" desc=" de-/serialization ">
    /**
     * Creates a byte array from this StorageManager object (serialization).
     * 
     * @return
     */
    public byte[] toByteArray() {
        int size = 0;
        
        byte[] versionNumbersByIdInBranchByteArray = versionNumbersByIdInBranch.toByteArray();
        byte[] idsByVersionNumberByteArray = idsByVersionNumber.toByteArray();
        byte[] actualVersionsByteArray = actualVersions.toByteArray();
        byte[] actualDocumentsByteArray = actualDocuments.toByteArray();
        byte[] branchesByteArray = branches.toByteArray();
        byte[] tagListOfDocumentInBranchByteArray = tagListOfDocumentInBranch.toByteArray();
        byte[] vcsIdByDocumentIdByteArray = vcsIdByDocumentId.toByteArray();
        
        size += SerializationUtilities.sizeByteArray(versionNumbersByIdInBranchByteArray);
        size += SerializationUtilities.sizeByteArray(idsByVersionNumberByteArray);
        size += SerializationUtilities.sizeByteArray(actualVersionsByteArray);
        size += SerializationUtilities.sizeByteArray(actualDocumentsByteArray);
        size += SerializationUtilities.sizeByteArray(branchesByteArray);
        size += SerializationUtilities.sizeByteArray(tagListOfDocumentInBranchByteArray);
        size += SerializationUtilities.sizeByteArray(vcsIdByDocumentIdByteArray);
        size += SerializationUtilities.sizeString(name);

        ByteBuffer bb = ByteBuffer.allocate(size);
        
        System.out.println("versionNumbersByIdInBranch: " + versionNumbersByIdInBranchByteArray);
        System.out.println("idsByVersionNumber: " + idsByVersionNumberByteArray);
        System.out.println("actualVersions: " + actualVersionsByteArray);
        System.out.println("actualDocuments: " + actualDocumentsByteArray);
        System.out.println("branches: " + branchesByteArray);
        System.out.println("tagListOfDocumentInBranch: " + tagListOfDocumentInBranchByteArray);
        System.out.println("vcsIdByDocumentId: " + vcsIdByDocumentIdByteArray);

        SerializationUtilities.putByteArray(bb, versionNumbersByIdInBranchByteArray);
        SerializationUtilities.putByteArray(bb, idsByVersionNumberByteArray);
        SerializationUtilities.putByteArray(bb, actualVersionsByteArray);
        SerializationUtilities.putByteArray(bb, actualDocumentsByteArray);
        SerializationUtilities.putByteArray(bb, branchesByteArray);
        SerializationUtilities.putByteArray(bb, tagListOfDocumentInBranchByteArray);
        SerializationUtilities.putByteArray(bb, vcsIdByDocumentIdByteArray);

        return bb.array();
    }

    /**
     * Creates a StorageManager object from a byte array (deserialization).
     * 
     * @param byteArray
     * @return
     */
    public static StorageManager fromByteArray(byte[] byteArray) {
        StorageManager storageManager = new StorageManager();
        ByteBuffer bb = ByteBuffer.wrap(byteArray);

        storageManager.versionNumbersByIdInBranch = new BranchIdVersionNumberHashMap(SerializationUtilities.getByteArray(bb));
        storageManager.idsByVersionNumber = new BranchIdList(SerializationUtilities.getByteArray(bb));
        storageManager.actualVersions = new BranchActualVersionNumberHashMap(SerializationUtilities.getByteArray(bb));
        storageManager.actualDocuments = new BranchActualDocuments(SerializationUtilities.getByteArray(bb));
        storageManager.branches = new BranchDocumentList(SerializationUtilities.getByteArray(bb));
        storageManager.tagListOfDocumentInBranch = new BranchIdTagListHashMap(SerializationUtilities.getByteArray(bb));
        storageManager.vcsIdByDocumentId = new IdIdHashMap(SerializationUtilities.getByteArray(bb));
        
        return storageManager;
    }
    // </editor-fold> // de-/serialiszation

    // <editor-fold defaultstate="collapsed" desc=" old ">
//    public void tag(String tagText, Id documentId, String branch) {
//        if (branch == null || branch.equals("")) {
//            branch = "HEAD";
//        }
//        System.out.println("branch: " + branch);
//
//        if (documentId != null) {
//            System.out.println("documentID: " + documentId);
//
//            if (tagText != null && !tagText.equals("")) {
//                System.out.println("tagText: " + tagText);
//                int versionNumber = getVersionNumber(documentId, branch);
//                System.out.println("versionNumber: " + versionNumber);
//
//                if (versionNumber > 0) {
//                    Document document = getDocument(versionNumber, branch);
//                    System.out.println("document: " + document);
//
//                    if (document != null) {
//                        HashMap<String, Object> tagProperties = document.getMetadata().getPropertiesWithPrefix(TAG_PREFIX);
//                        System.out.println("tagProperties: " + tagProperties);
//                        ArrayList<String> keys = new ArrayList<String>(tagProperties.keySet());
//                        System.out.println("keys: " + keys);
//
//                        for (int i = 0; i < keys.size(); i++) {
//                            keys.set(i, keys.get(i).substring(keys.get(i).indexOf(TAG_PREFIX)));
//                        }
//
//                        for (String key : keys) {
//                            System.out.println(key);
//                        }
//
//                        int greatestNumber = 0;
//
//                        if (keys.size() > 0) {
//                            for (String key : keys) {
//                                try {
//                                    int number = Integer.parseInt(key);
//                                    if (number > greatestNumber) {
//                                        greatestNumber = number;
//                                    }
//                                } catch (NumberFormatException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            
//                            greatestNumber++;
//                        }
//                        
//                        document.getMetadata().setProperty(TAG_PREFIX + greatestNumber, tagText);
//
//                    } // document is not NULL
//                } // version number greater than zero
//            } // tagText not empty
//        } // document ID is not NULL
//    }
    // </editor-fold> // old
    // <editor-fold defaultstate="collapsed" desc=" Test ">
    public void addNewVersion(Id vcsId, Document document, String branch, Continuation c) {
//        if (id == null || obj == null) {
//            c.receiveResult(new Boolean(false));
//            return;
//        }

        int newVersionNumber = 1;

        if (getActualVersion(branch) != null) {
            newVersionNumber = getActualVersion(branch).intValue() + 1;
        }

        document.setVersionNumber(newVersionNumber);
        document.setId(idFactory.buildId(new String(document.toByteArray())));

        versionNumbersByIdInBranch.get(branch).put(document.getId(), newVersionNumber);
        idsByVersionNumber.get(branch).add(document.getId());
        branches.get(branch).add(document);
        actualVersions.put(branch, newVersionNumber);
        actualDocuments.put(branch, document);
        //vcsIdByDocumentId.get(branch).put(document.getId(), vcsId);
        vcsIdByDocumentId.put(document.getId(), vcsId);

        c.receiveResult(new Boolean(true));
    }

    public IdSet scan(IdRange range) {
        IdSet set = new IdSet();



        return set;
    }
    // </editor-fold> // 
    // <editor-fold defaultstate="collapsed" desc=" addNewDocument with locking and release or timeout [beta] ">
    
    //TODO Which is the better place for locking, here in the storage manager or one step ahead in then P2P_IntegrationVCS_ClientImplementation?
    
    private boolean isLocked() {
        return isLocked("HEAD");
    }

    private boolean isLocked(String branch) {
        return mostActualVersionLockedHashMap.get(branch).booleanValue();
    }

    public Document getActualDocument_WithLockingCheck() {
        return getActualDocument_WithLockingCheck("HEAD");
    }

    public Document getActualDocument_WithLockingCheck(String branch) {
        // only the very most actual document may be locked
        // if it is locked, return the second most actual

        Document mostActual = null;
        DocumentList list = branches.get(branch);

        if (list != null) {
            if (isLocked(branch)) {
                mostActual = list.getSecondLast();
            } else {
                mostActual = list.getLast();
            }
        }

        return mostActual;
    }

    public boolean isActualVersion_WithLockingCheck(Id documentId) {
        return isActualVersion_WithLockingCheck(documentId, "HEAD");
    }

    /**
     * Indicates if the document in the branch may be updated.
     * The document is the actual version if the given branch doesn't exist (user want's to create a new branch) or
     * if the document's equals the id of the current document and that document is not locked. If the indicaton id
     * of the document is null, an update is invalid.
     * 
     * @param documentId id of the document to check
     * @param branch branch to use for checking
     * @return true if the document may be updated, false otherwise
     */
    public boolean isActualVersion_WithLockingCheck(Id documentId, String branch) {
        return
                documentId != null &&
                getActualDocument_WithLockingCheck(branch) == null ||
                documentId.equals(getActualDocument_WithLockingCheck(branch).getId());
    }

    public void addNewVersion_WithLocking(Id vcsId, Document document) {
        addNewVersion_WithLocking(vcsId, document, "HEAD");
    }

    public void addNewVersion_WithLocking(Id vcsId, Document document, String branch) {
        if (isLocked(branch)) {
            //TODO throw exception? simple boolean return value?
        } else  {
            int newVersionNumber = 1;
            
            if (getActualVersion(branch) != null) {
                newVersionNumber = getActualVersion(branch).intValue() + 1;
            }

            document.setVersionNumber(newVersionNumber);
            document.setId(idFactory.buildId(new String(document.toByteArray())));

            // update storage
            versionNumbersByIdInBranch.get(branch).put(document.getId(), newVersionNumber);
            idsByVersionNumber.get(branch).add(document.getId());
            branches.get(branch).add(document);
            vcsIdByDocumentId.put(document.getId(), vcsId);
            
            // update actual version and document
            actualVersions.put(branch, newVersionNumber);
            actualDocuments.put(branch, document);
            
            // lock the new document
            mostActualVersionLockedHashMap.put(branch, Boolean.TRUE);
            
            // create a timer that will release the lock and undo the changes after a timeout
            final String internalBranch = branch;
            final Document internalDocument = document;
            final int oldVersionNumber = newVersionNumber - 1;
            final Document oldDocument = branches.get(branch).getSecondLast();
            (lockReleaseTimer = new Timer(LOCK_RELEASE_TIMEOUT, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    // rollback last checkin
                    LOGGER.warn(name + ": addNewVersion_WithLocking: lock release timeout, undo last checkin!");
                    
                    lockReleaseTimer.stop();
                    lockReleaseTimer = null;    // TODO necessary?
                    
                    //TODO Is a special treatment necessary if the document was checked in initally (version number 1) ? Test!
//                    if (oldVersionNumber > 0) {
                        // restore storage
                        versionNumbersByIdInBranch.get(internalBranch).remove(internalDocument.getId());
                        idsByVersionNumber.get(internalBranch).remove(internalDocument.getId());
                        branches.get(internalBranch).remove(internalDocument);
                        vcsIdByDocumentId.remove(internalDocument.getId());
                        
                        // restore actual version and document
                        actualVersions.put(internalBranch, oldVersionNumber);
                        actualDocuments.put(internalBranch, oldDocument);

                        // unlock the new document
                        mostActualVersionLockedHashMap.put(internalBranch, Boolean.FALSE);
//                    } else {
//                        // TODO
//                    }
                }
            })).start();
        }
    }
    
    public void unlock(String branch) {
        mostActualVersionLockedHashMap.put(branch, Boolean.FALSE);
    }
    // </editor-fold> // addNewDocument with locking and release or timeout
    
    @Override
    public boolean equals(Object obj) {
        boolean equal = false;
        
        if (obj instanceof StorageManager) {
            equal = (hashCode() == ((StorageManager)obj).hashCode());
        }
        
        return equal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.versionNumbersByIdInBranch != null ? this.versionNumbersByIdInBranch.hashCode() : 0);
        hash = 29 * hash + (this.idsByVersionNumber != null ? this.idsByVersionNumber.hashCode() : 0);
        hash = 29 * hash + (this.actualVersions != null ? this.actualVersions.hashCode() : 0);
        hash = 29 * hash + (this.actualDocuments != null ? this.actualDocuments.hashCode() : 0);
        hash = 29 * hash + (this.branches != null ? this.branches.hashCode() : 0);
        hash = 29 * hash + (this.tagListOfDocumentInBranch != null ? this.tagListOfDocumentInBranch.hashCode() : 0);
        hash = 29 * hash + (this.vcsIdByDocumentId != null ? this.vcsIdByDocumentId.hashCode() : 0);
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 29 * hash + (this.mostActualVersionLockedHashMap != null ? this.mostActualVersionLockedHashMap.hashCode() : 0);
        return hash;
    }
}
