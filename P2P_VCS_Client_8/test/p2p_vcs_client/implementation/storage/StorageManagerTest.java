package p2p_vcs_client.implementation.storage;

import java.util.ArrayList;
import java.util.Random;
import junit.framework.TestCase;
import p2p_access.Environment;
import p2p_access.P2PAccess;
import p2p_vcs_client.Document;
import p2p_vcs_client.implementation.document.DocumentFactory;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.IdFactory;
import rice.pastry.IdRange;
import rice.pastry.IdSet;
import rice.pastry.commonapi.PastryIdFactory;

/**
 *
 * @author podolak
 */
public class StorageManagerTest extends TestCase {

    public StorageManagerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of tag method, of class StorageManager.
     */
    public void testTag() {
        System.out.println("tag");

        String tagText = "";

        // setup system
        Environment environment = Environment.getInstance();
        IdFactory idFactory = new PastryIdFactory(environment);
        StorageManager storageManager = new StorageManager("");
        Id vcsId = idFactory.buildRandomId(new Random());

        // generate three versions
        Document document1 = DocumentFactory.getInstance().newDocument();
        storageManager.addNewVersion(vcsId, document1);
        Document document2 = DocumentFactory.getInstance().newDocument();
        storageManager.addNewVersion(vcsId, document2);
        Document document3 = DocumentFactory.getInstance().newDocument();
        storageManager.addNewVersion(vcsId, document3);
        
        // create expected results
        ArrayList<String> document1Tags = new ArrayList<String>();
        document1Tags.add("Eins: erste Markierung");
        document1Tags.add("Eins: zweite Markierung");
        document1Tags.add("Eins: dritte Markierung");
        document1Tags.add("Eins: vierte Markierung");
        document1Tags.add("Eins: fünfte Markierung");
        
        ArrayList<String> document2Tags = new ArrayList<String>();
        document2Tags.add("Zwei: erste Markierung");
        document2Tags.add("Zwei: zweite Markierung");
        document2Tags.add("Zwei: dritte Markierung");
        
        ArrayList<String> document3Tags = new ArrayList<String>();
        document3Tags.add("Drei: erste Markierung");
        document3Tags.add("Drei: zweite Markierung");
        document3Tags.add("Drei: dritte Markierung");
        document3Tags.add("Drei: vierte Markierung");

        // tag documents
        for (String tag : document1Tags) {
            storageManager.tag(tag, document1.getId());
        }
        
        for (String tag : document2Tags) {
            storageManager.tag(tag, document2.getId());
        }
        
        for (String tag : document3Tags) {
            storageManager.tag(tag, document3.getId());
        }
        
        // test tagging
        ArrayList<String> document1TagsRead = storageManager.getTags(document1.getId());
        ArrayList<String> document2TagsRead = storageManager.getTags(document2.getId());
        ArrayList<String> document3TagsRead = storageManager.getTags(document3.getId());
        
        assertEquals(document1Tags.size(), document1TagsRead.size());
        assertEquals(document2Tags.size(), document2TagsRead.size());
        assertEquals(document3Tags.size(), document3TagsRead.size());
        
        for (int i = 0; i < document1Tags.size(); i++) {
            assertEquals(document1Tags.get(i), document1TagsRead.get(i));
        }
        
        for (int i = 0; i < document2Tags.size(); i++) {
            assertEquals(document2Tags.get(i), document2TagsRead.get(i));
        }
        
        for (int i = 0; i < document3Tags.size(); i++) {
            assertEquals(document3Tags.get(i), document3TagsRead.get(i));
        }
    }

//    /**
//     * Test of getActualDocument method, of class StorageManager.
//     */
//    public void testGetActualDocument() {
//        System.out.println("getActualDocument");
//        StorageManager instance = new StorageManager();
//        Document expResult = null;
//        Document result = instance.getActualDocument();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getActualVersion method, of class StorageManager.
//     */
//    public void testGetActualVersion() {
//        System.out.println("getActualVersion");
//        StorageManager instance = new StorageManager();
//        Integer expResult = null;
//        Integer result = instance.getActualVersion();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getDocument method, of class StorageManager.
//     */
//    public void testGetDocument() {
//        System.out.println("getDocument");
//        int version = 0;
//        StorageManager instance = new StorageManager();
//        Document expResult = null;
//        Document result = instance.getDocument(version);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getVersionNumber method, of class StorageManager.
//     */
//    public void testGetVersionNumber() {
//        System.out.println("getVersionNumber");
//        Id documentId = null;
//        StorageManager instance = new StorageManager();
//        int expResult = 0;
//        int result = instance.getVersionNumber(documentId);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of isActualVersion method, of class StorageManager.
//     */
//    public void testIsActualVersion() {
//        System.out.println("isActualVersion");
//        Id documentId = null;
//        StorageManager instance = new StorageManager();
//        boolean expResult = false;
//        boolean result = instance.isActualVersion(documentId);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addNewVersion method, of class StorageManager.
//     */
//    public void testAddNewVersion() {
//        System.out.println("addNewVersion");
//        Id vcsId = null;
//        Document document = null;
//        StorageManager instance = new StorageManager();
//        instance.addNewVersion(vcsId, document);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of createBranch method, of class StorageManager.
//     */
//    public void testCreateBranch() throws Exception {
//        System.out.println("createBranch");
//        String newBranchName = "";
//        StorageManager instance = new StorageManager();
//        instance.createBranch(newBranchName);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getTags method, of class StorageManager.
//     */
//    public void testGetTags() {
//        System.out.println("getTags");
//        Id documentId = null;
//        StorageManager instance = new StorageManager();
//        ArrayList<String> expResult = null;
//        ArrayList<String> result = instance.getTags(documentId);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of toByteArray method, of class StorageManager.
     */
    public void testToByteArray() {
        System.out.println("toByteArray");
        
        Id id = P2PAccess.getIdFactory().buildId("");
        Document document = DocumentFactory.getInstance().newDocument();
        document.getMetamodel().setContent("Dudy");
        document.getMetadata().setProperty("alter", 32);
        
        StorageManager instance = new StorageManager();
        instance.addNewVersion(id, document);
        StorageManager fromByteArray = StorageManager.fromByteArray(instance.toByteArray());
        assertEquals(instance, fromByteArray);
    }

//    /**
//     * Test of fromByteArray method, of class StorageManager.
//     */
//    public void testFromByteArray() {
//        System.out.println("fromByteArray");
//        byte[] byteArray = null;
//        StorageManager expResult = null;
//        StorageManager result = StorageManager.fromByteArray(byteArray);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of scan method, of class StorageManager.
//     */
//    public void testScan() {
//        System.out.println("scan");
//        IdRange range = null;
//        StorageManager instance = new StorageManager();
//        IdSet expResult = null;
//        IdSet result = instance.scan(range);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getActualDocument_WithLockingCheck method, of class StorageManager.
//     */
//    public void testGetActualDocument_WithLockingCheck() {
//        System.out.println("getActualDocument_WithLockingCheck");
//        StorageManager instance = new StorageManager();
//        Document expResult = null;
//        Document result = instance.getActualDocument_WithLockingCheck();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of isActualVersion_WithLockingCheck method, of class StorageManager.
//     */
//    public void testIsActualVersion_WithLockingCheck() {
//        System.out.println("isActualVersion_WithLockingCheck");
//        Id documentId = null;
//        StorageManager instance = new StorageManager();
//        boolean expResult = false;
//        boolean result = instance.isActualVersion_WithLockingCheck(documentId);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addNewVersion_WithLocking method, of class StorageManager.
//     */
//    public void testAddNewVersion_WithLocking() {
//        System.out.println("addNewVersion_WithLocking");
//        Id vcsId = null;
//        Document document = null;
//        StorageManager instance = new StorageManager();
//        instance.addNewVersion_WithLocking(vcsId, document);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of unlock method, of class StorageManager.
//     */
//    public void testUnlock() {
//        System.out.println("unlock");
//        String branch = "";
//        StorageManager instance = new StorageManager();
//        instance.unlock(branch);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
