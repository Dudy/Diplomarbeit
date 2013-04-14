/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package p2p_vcs_client.implementation.storage;

import junit.framework.TestCase;
import p2p_access.P2PAccess;
import rice.p2p.commonapi.Id;

/**
 *
 * @author podolak
 */
public class IdTagListHashMapTest extends TestCase {
    
    public IdTagListHashMapTest(String testName) {
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
     * Test of getByteArrayLength method, of class IdTagListHashMap.
     */
    public void testGetByteArrayLength() {
        System.out.println("getByteArrayLength");
        
        Id id = P2PAccess.getIdFactory().buildId("");
        IdTagListHashMap instance = new IdTagListHashMap(1);
        TagList tagList = new TagList(1);
        tagList.add("is gut");
        instance.put(id, tagList);
        
        int expected =
                 4 +        // Größe HashMap
                24 +        // ID 20+4
                24;         // TagList 6*2+4+4+4
        
        assertEquals(expected, instance.getByteArrayLength());
    }

    /**
     * Test of toByteArray method, of class IdTagListHashMap.
     */
    public void testToByteArray() {
        System.out.println("toByteArray");
        
        Id id = P2PAccess.getIdFactory().buildId("");
        IdTagListHashMap instance = new IdTagListHashMap(1);
        TagList tagList = new TagList(1);
        tagList.add("is gut");
        instance.put(id, tagList);
        
        IdTagListHashMap fromByteArray = new IdTagListHashMap(instance.toByteArray());
        assertEquals(instance, fromByteArray);
    }

}
