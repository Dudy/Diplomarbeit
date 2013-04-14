/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package p2p_vcs_client.implementation.storage;

import junit.framework.TestCase;
import p2p_access.P2PAccess;
import p2p_vcs_client.implementation.SerializationUtilities;
import rice.p2p.commonapi.Id;

/**
 *
 * @author podolak
 */
public class IdVersionNumberHashMapTest extends TestCase {
    
    public IdVersionNumberHashMapTest(String testName) {
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
     * Test of getByteArrayLength method, of class IdVersionNumberHashMap.
     */
    public void testGetByteArrayLength() {
        System.out.println("getByteArrayLength");
        
        Id id = P2PAccess.getIdFactory().buildId("");
        IdVersionNumberHashMap instance = new IdVersionNumberHashMap();
        instance.put(id, 1);
        
        int expResult =
                Integer.SIZE / 8 +                  // number of stored entries
                Integer.SIZE / 8 +                  // length of id
                id.getByteArrayLength() +           // data of id
                Integer.SIZE / 8;                   // integer
        int result = instance.getByteArrayLength();
        assertEquals(expResult, result);
    }

    /**
     * Test of toByteArray method, of class IdVersionNumberHashMap.
     */
    public void testToByteArray() {
        System.out.println("toByteArray");
        
        Id id = P2PAccess.getIdFactory().buildId("");
        IdVersionNumberHashMap instance = new IdVersionNumberHashMap();
        instance.put(id, 1);
        
        byte[] instanceByteArray = instance.toByteArray();
        IdVersionNumberHashMap fromByteArray = new IdVersionNumberHashMap(instanceByteArray);
        
        assertEquals(instance, fromByteArray);
    }

}
