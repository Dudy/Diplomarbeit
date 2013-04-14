/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package p2p_vcs_client.implementation.storage;

import junit.framework.TestCase;
import p2p_access.P2PAccess;

/**
 *
 * @author podolak
 */
public class BranchIdVersionNumberHashMapTest extends TestCase {
    
    public BranchIdVersionNumberHashMapTest(String testName) {
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
     * Test of getByteArrayLength method, of class BranchIdVersionNumberHashMap.
     */
    public void testGetByteArrayLength() {
        System.out.println("getByteArrayLength");
        
        BranchIdVersionNumberHashMap instance = new BranchIdVersionNumberHashMap();
        int expResult = 0;
        int result = instance.getByteArrayLength();
        assertEquals(expResult, result);
        
        IdVersionNumberHashMap head = new IdVersionNumberHashMap();
        head.put(P2PAccess.getIdFactory().buildId(""), 1);
        instance.put("HEAD", head);
        
        expResult =
                 4 +        // Größe von IdVersionNumberHashMap
                32 +        // IdVersionNumberHashMap
                 4 +        // Größe der HashTable = 1 (int, vier byte)
                12;         // String "HEAD": vier byte Größe, vier mal zwei byte Zeichen = 12
        
        result = instance.getByteArrayLength();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of toByteArray method, of class BranchIdVersionNumberHashMap.
     */
    public void testToByteArray() {
        System.out.println("toByteArray");
        
        BranchIdVersionNumberHashMap instance = new BranchIdVersionNumberHashMap();
        IdVersionNumberHashMap head = new IdVersionNumberHashMap();
        head.put(P2PAccess.getIdFactory().buildId(""), 1);
        instance.put("HEAD", head);
        
        BranchIdVersionNumberHashMap fromByteArray = new BranchIdVersionNumberHashMap(instance.toByteArray());
        assertEquals(instance, fromByteArray);
    }

}
