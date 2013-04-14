/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_vcs_client.implementation.document;

import java.util.Random;
import junit.framework.TestCase;
import p2p_access.P2PAccess;
import rice.p2p.commonapi.IdFactory;

/**
 *
 * @author podolak
 */
public class DocumentImplementationTest extends TestCase {

    public DocumentImplementationTest(String testName) {
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

//    /**
//     * Test of toString method, of class DocumentImplementation.
//     */
//    public void testToString() {
//        System.out.println("toString");
//        DocumentImplementation original = new DocumentImplementation();
//        String expResult = "";
//        String result = original.toString();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
    /**
     * Test of toByteArray method, of class DocumentImplementation.
     */
    public void testToByteArray() {
        smallDocument0();
        smallDocument1();
        smallDocument2();
        smallDocument3();
        smallDocument4();
        smallDocument5();
        smallDocument6();
        smallDocument7();
        smallDocument8();
        smallDocument9();


        System.out.println("toByteArray");

//        DocumentImplementation original = new DocumentImplementation();
//        byte[] expResult = null;
//        byte[] result = original.toByteArray();
//
//        for (byte b : result) {
//            System.out.print(b + " ");
//        }
//        System.out.println("");
//
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    private void smallDocument0() {
        DocumentImplementation original = new DocumentImplementation();
        //printDocument(original);
        DocumentImplementation copy = (DocumentImplementation) DocumentFactory.getInstance().documentFromByteArray(original.toByteArray());
        //printDocument(copy);
        System.out.println("0: original = copy: " + original.equals(copy));
        
        assertEquals(original, copy);
    }

    private void smallDocument1() {
        DocumentImplementation original = new DocumentImplementation();
        original.setVersionNumber(1);
        //printDocument(original);
        DocumentImplementation copy = (DocumentImplementation) DocumentFactory.getInstance().documentFromByteArray(original.toByteArray());
        //printDocument(copy);
        System.out.println("1: original = copy: " + original.equals(copy));
        
        assertEquals(original, copy);
    }

    private void smallDocument2() {
        DocumentImplementation original = new DocumentImplementation();
        original.setVersionNumber(2);
        //printDocument(original);
        DocumentImplementation copy = (DocumentImplementation) DocumentFactory.getInstance().documentFromByteArray(original.toByteArray());
        //printDocument(copy);
        System.out.println("2: original = copy: " + original.equals(copy));
        
        assertEquals(original, copy);
    }

    private void smallDocument3() {
        DocumentImplementation original = new DocumentImplementation();
        original.getMetamodel().setContent("Dudy");
        //printDocument(original);
        DocumentImplementation copy = (DocumentImplementation) DocumentFactory.getInstance().documentFromByteArray(original.toByteArray());
        //printDocument(copy);
        System.out.println("3: original = copy: " + original.equals(copy));
        
        assertEquals(original, copy);
    }

    private void smallDocument4() {
        DocumentImplementation original = new DocumentImplementation();
        original.getMetamodel().setContent("Dudy");
        original.getMetadata().setType("Type");
        //printDocument(original);
        DocumentImplementation copy = (DocumentImplementation) DocumentFactory.getInstance().documentFromByteArray(original.toByteArray());
        //printDocument(copy);
        System.out.println("4: original = copy: " + original.equals(copy));
        
        assertEquals(original, copy);
    }

    private void smallDocument5() {
        DocumentImplementation original = new DocumentImplementation();
        original.getMetadata().setProperty("gras", "grün");
        //printDocument(original);
        DocumentImplementation copy = (DocumentImplementation) DocumentFactory.getInstance().documentFromByteArray(original.toByteArray());
        //printDocument(copy);
        System.out.println("5: original = copy: " + original.equals(copy));
        
        assertEquals(original, copy);
    }

    private void smallDocument6() {
        DocumentImplementation original = new DocumentImplementation();
        original.getMetadata().setProperty("alter", 32);
        //printDocument(original);
        DocumentImplementation copy = (DocumentImplementation) DocumentFactory.getInstance().documentFromByteArray(original.toByteArray());
        //printDocument(copy);
        System.out.println("6: original = copy: " + original.equals(copy));
        
        assertEquals(original, copy);
    }

    private void smallDocument7() {
        DocumentImplementation original = new DocumentImplementation();
        original.getMetadata().setProperty("männlich", true);
        //printDocument(original);
        DocumentImplementation copy = (DocumentImplementation) DocumentFactory.getInstance().documentFromByteArray(original.toByteArray());
        //printDocument(copy);
        System.out.println("7: original = copy: " + original.equals(copy));
        
        assertEquals(original, copy);
    }

    private void smallDocument8() {
        DocumentImplementation original = new DocumentImplementation();
        byte[] ba = {(byte) 127, (byte) 127, (byte) 127};
        original.getMetadata().setProperty("zahlen", ba);
        //printDocument(original);
        DocumentImplementation copy = (DocumentImplementation) DocumentFactory.getInstance().documentFromByteArray(original.toByteArray());
        //printDocument(copy);
        System.out.println("8: original = copy: " + original.equals(copy));
        
        assertEquals(original, copy);
    }

    private void smallDocument9() {
        DocumentImplementation original = new DocumentImplementation();
        IdFactory idFactory = P2PAccess.getIdFactory();
        original.setId(idFactory.buildId("Test ID"));
        printDocument(original);
        DocumentImplementation copy = (DocumentImplementation) DocumentFactory.getInstance().documentFromByteArray(original.toByteArray());
        printDocument(copy);
        System.out.println("9: original = copy: " + original.equals(copy));
        
        assertEquals(original, copy);
    }

    private void printDocument(DocumentImplementation document) {
        System.out.println(document);
        for (byte b : document.toByteArray()) {
            System.out.print(b + " ");
        }
        System.out.println("");
    }
//
//    /**
//     * Test of getMetamodel method, of class DocumentImplementation.
//     */
//    public void testGetMetamodel() {
//        System.out.println("getMetamodel");
//        DocumentImplementation original = new DocumentImplementation();
//        Metamodel expResult = null;
//        Metamodel result = original.getMetamodel();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setMetamodel method, of class DocumentImplementation.
//     */
//    public void testSetMetamodel() {
//        System.out.println("setMetamodel");
//        Metamodel metamodel = null;
//        DocumentImplementation original = new DocumentImplementation();
//        original.setMetamodel(metamodel);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getMetadata method, of class DocumentImplementation.
//     */
//    public void testGetMetadata() {
//        System.out.println("getMetadata");
//        DocumentImplementation original = new DocumentImplementation();
//        Metadata expResult = null;
//        Metadata result = original.getMetadata();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setMetadata method, of class DocumentImplementation.
//     */
//    public void testSetMetadata() {
//        System.out.println("setMetadata");
//        Metadata metadata = null;
//        DocumentImplementation original = new DocumentImplementation();
//        original.setMetadata(metadata);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getApplicationStorage method, of class DocumentImplementation.
//     */
//    public void testGetApplicationStorage() {
//        System.out.println("getApplicationStorage");
//        DocumentImplementation original = new DocumentImplementation();
//        ApplicationStorage expResult = null;
//        ApplicationStorage result = original.getApplicationStorage();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setApplicationStorage method, of class DocumentImplementation.
//     */
//    public void testSetApplicationStorage() {
//        System.out.println("setApplicationStorage");
//        ApplicationStorage applicationStorage = null;
//        DocumentImplementation original = new DocumentImplementation();
//        original.setApplicationStorage(applicationStorage);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getId method, of class DocumentImplementation.
//     */
//    public void testGetId() {
//        System.out.println("getId");
//        DocumentImplementation original = new DocumentImplementation();
//        Id expResult = null;
//        Id result = original.getId();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setId method, of class DocumentImplementation.
//     */
//    public void testSetId() {
//        System.out.println("setId");
//        Id id = null;
//        DocumentImplementation original = new DocumentImplementation();
//        original.setId(id);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getVersionNumber method, of class DocumentImplementation.
//     */
//    public void testGetVersionNumber() {
//        System.out.println("getVersionNumber");
//        DocumentImplementation original = new DocumentImplementation();
//        int expResult = 0;
//        int result = original.getVersionNumber();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setVersionNumber method, of class DocumentImplementation.
//     */
//    public void testSetVersionNumber() {
//        System.out.println("setVersionNumber");
//        int versionNumber = 0;
//        DocumentImplementation original = new DocumentImplementation();
//        original.setVersionNumber(versionNumber);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
