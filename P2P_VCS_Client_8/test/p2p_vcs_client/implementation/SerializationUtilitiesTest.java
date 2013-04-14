/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package p2p_vcs_client.implementation;

import java.nio.ByteBuffer;
import java.util.Random;
import junit.framework.TestCase;
import p2p_access.P2PAccess;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.IdFactory;

/**
 *
 * @author podolak
 */
public class SerializationUtilitiesTest extends TestCase {
    
    public SerializationUtilitiesTest(String testName) {
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
//     * Test of getString method, of class SerializationUtilities.
//     */
//    public void testGetString() {
//        System.out.println("getString");
//        ByteBuffer bb = null;
//        String expResult = "";
//        String result = SerializationUtilities.getString(bb);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of putString method, of class SerializationUtilities.
//     */
//    public void testPutString() {
//        System.out.println("putString");
//        ByteBuffer bb = null;
//        String text = "";
//        SerializationUtilities.putString(bb, text);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of sizeString method, of class SerializationUtilities.
//     */
//    public void testSizeString() {
//        System.out.println("sizeString");
//        String text = "";
//        int expResult = 0;
//        int result = SerializationUtilities.sizeString(text);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getInteger method, of class SerializationUtilities.
//     */
//    public void testGetInteger() {
//        System.out.println("getInteger");
//        ByteBuffer bb = null;
//        Integer expResult = null;
//        Integer result = SerializationUtilities.getInteger(bb);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of putInteger method, of class SerializationUtilities.
//     */
//    public void testPutInteger() {
//        System.out.println("putInteger");
//        ByteBuffer bb = null;
//        Integer i = null;
//        SerializationUtilities.putInteger(bb, i);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of sizeInteger method, of class SerializationUtilities.
//     */
//    public void testSizeInteger() {
//        System.out.println("sizeInteger");
//        int expResult = 0;
//        int result = SerializationUtilities.sizeInteger();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getBoolean method, of class SerializationUtilities.
//     */
//    public void testGetBoolean() {
//        System.out.println("getBoolean");
//        ByteBuffer bb = null;
//        Boolean expResult = null;
//        Boolean result = SerializationUtilities.getBoolean(bb);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of putBoolean method, of class SerializationUtilities.
//     */
//    public void testPutBoolean() {
//        System.out.println("putBoolean");
//        ByteBuffer bb = null;
//        Boolean b = null;
//        SerializationUtilities.putBoolean(bb, b);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of sizeBoolean method, of class SerializationUtilities.
//     */
//    public void testSizeBoolean() {
//        System.out.println("sizeBoolean");
//        int expResult = 0;
//        int result = SerializationUtilities.sizeBoolean();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getByteArray method, of class SerializationUtilities.
     */
    public void testGetByteArray() {
        System.out.println("getByteArray");
        
        // a filled byte array
        ByteBuffer bb = ByteBuffer.allocate(4 + Integer.SIZE / 8);
        bb.putInt(4);
        bb.put((byte)5);
        bb.put((byte)7);
        bb.put((byte)1);
        bb.put((byte)-6);
        bb.position(0);
        
        byte[] expResult = { 5, 7, 1, -6 };
        byte[] result = SerializationUtilities.getByteArray(bb);
        
        for (int i = 0; i < result.length; i++) {
            byte expectedByte = expResult[i];
            byte resultByte = result[i];
            
            assertEquals(expectedByte, resultByte);
        }
        
        // an empty byte array
        bb = ByteBuffer.allocate(Integer.SIZE / 8);
        bb.putInt(0);
        bb.position(0);
        
        expResult = new byte[0];
        result = SerializationUtilities.getByteArray(bb);
        
        assertNotNull(result);
        assertEquals(result.length, 0);
        
    }

//    /**
//     * Test of getString method, of class SerializationUtilities.
//     */
//    public void testGetString() {
//        System.out.println("getString");
//        ByteBuffer bb = null;
//        String expResult = "";
//        String result = SerializationUtilities.getString(bb);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of putString method, of class SerializationUtilities.
//     */
//    public void testPutString() {
//        System.out.println("putString");
//        ByteBuffer bb = null;
//        String text = "";
//        SerializationUtilities.putString(bb, text);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of sizeString method, of class SerializationUtilities.
//     */
//    public void testSizeString() {
//        System.out.println("sizeString");
//        String text = "";
//        int expResult = 0;
//        int result = SerializationUtilities.sizeString(text);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getInteger method, of class SerializationUtilities.
//     */
//    public void testGetInteger() {
//        System.out.println("getInteger");
//        ByteBuffer bb = null;
//        Integer expResult = null;
//        Integer result = SerializationUtilities.getInteger(bb);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of putInteger method, of class SerializationUtilities.
//     */
//    public void testPutInteger() {
//        System.out.println("putInteger");
//        ByteBuffer bb = null;
//        Integer i = null;
//        SerializationUtilities.putInteger(bb, i);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of sizeInteger method, of class SerializationUtilities.
//     */
//    public void testSizeInteger() {
//        System.out.println("sizeInteger");
//        int expResult = 0;
//        int result = SerializationUtilities.sizeInteger();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getBoolean method, of class SerializationUtilities.
//     */
//    public void testGetBoolean() {
//        System.out.println("getBoolean");
//        ByteBuffer bb = null;
//        Boolean expResult = null;
//        Boolean result = SerializationUtilities.getBoolean(bb);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of putBoolean method, of class SerializationUtilities.
//     */
//    public void testPutBoolean() {
//        System.out.println("putBoolean");
//        ByteBuffer bb = null;
//        Boolean b = null;
//        SerializationUtilities.putBoolean(bb, b);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of sizeBoolean method, of class SerializationUtilities.
//     */
//    public void testSizeBoolean() {
//        System.out.println("sizeBoolean");
//        int expResult = 0;
//        int result = SerializationUtilities.sizeBoolean();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of putByteArray method, of class SerializationUtilities.
//     */
//    public void testPutByteArray() {
//        System.out.println("putByteArray");
//        ByteBuffer bb = null;
//        byte[] byteArray = null;
//        SerializationUtilities.putByteArray(bb, byteArray);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of sizeByteArray method, of class SerializationUtilities.
//     */
//    public void testSizeByteArray() {
//        System.out.println("sizeByteArray");
//        byte[] byteArray = null;
//        int expResult = 0;
//        int result = SerializationUtilities.sizeByteArray(byteArray);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getId method, of class SerializationUtilities.
     */
    public void testGetId() {
        System.out.println("getId");
        
        IdFactory idFactory = P2PAccess.getIdFactory();
        Id original = idFactory.buildRandomId(new Random());
        ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / 8 + original.getByteArrayLength());
        
        SerializationUtilities.putId(bb, original);
        bb.position(0);
        Id result = SerializationUtilities.getId(bb);
        
        assertEquals(original, result);
    }

//    /**
//     * Test of putId method, of class SerializationUtilities.
//     */
//    public void testPutId() {
//        System.out.println("putId");
//        ByteBuffer bb = null;
//        Id id = null;
//        SerializationUtilities.putId(bb, id);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of sizeId method, of class SerializationUtilities.
//     */
//    public void testSizeId() {
//        System.out.println("sizeId");
//        Id id = null;
//        int expResult = 0;
//        int result = SerializationUtilities.sizeId(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of putByteArray method, of class SerializationUtilities.
//     */
//    public void testPutByteArray() {
//        System.out.println("putByteArray");
//        ByteBuffer bb = null;
//        byte[] byteArray = null;
//        SerializationUtilities.putByteArray(bb, byteArray);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of sizeByteArray method, of class SerializationUtilities.
//     */
//    public void testSizeByteArray() {
//        System.out.println("sizeByteArray");
//        byte[] byteArray = null;
//        int expResult = 0;
//        int result = SerializationUtilities.sizeByteArray(byteArray);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getId method, of class SerializationUtilities.
//     */
//    public void testGetId() {
//        System.out.println("getId");
//        ByteBuffer bb = null;
//        Id expResult = null;
//        Id result = SerializationUtilities.getId(bb);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setId method, of class SerializationUtilities.
//     */
//    public void testSetId() {
//        System.out.println("setId");
//        ByteBuffer bb = null;
//        Id id = null;
//        SerializationUtilities.setId(bb, id);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
    /**
     * Test of sizeId method, of class SerializationUtilities.
     */
    public void testSizeId() {
        System.out.println("sizeId");
        
        Id id = P2PAccess.getIdFactory().buildId("");
        int expResult = Integer.SIZE / 8 + id.getByteArrayLength();
        int result = SerializationUtilities.sizeId(id);
        assertEquals(expResult, result);
    }

}
