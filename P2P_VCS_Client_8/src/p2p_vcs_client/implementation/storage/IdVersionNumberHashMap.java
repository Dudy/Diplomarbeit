package p2p_vcs_client.implementation.storage;

import java.nio.ByteBuffer;
import java.util.HashMap;
import p2p_vcs_client.implementation.SerializationUtilities;
import rice.p2p.commonapi.Id;

/**
 *
 * @author podolak
 */
public class IdVersionNumberHashMap extends HashMap<Id, Integer> {
    
    public  IdVersionNumberHashMap() {
    }
    
    public IdVersionNumberHashMap(byte[] byteArray) {
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        int size = SerializationUtilities.getInteger(bb);
        
        for (int i = 0; i < size; i++) {
            put(SerializationUtilities.getId(bb), SerializationUtilities.getInteger(bb));
        }
    }
    
    public int getByteArrayLength() {
        int length = SerializationUtilities.sizeInteger();
        
        for (Id id : keySet()) {
            // id
            length += SerializationUtilities.sizeId(id);
            // integer
            length += SerializationUtilities.sizeInteger();
        }
        
        return length;
    }
    
    public byte[] toByteArray() {
        ByteBuffer bb = ByteBuffer.allocate(getByteArrayLength());
        SerializationUtilities.putInteger(bb, keySet().size());
        
        for (Id id : keySet()) {
            SerializationUtilities.putId(bb, id);
            SerializationUtilities.putInteger(bb, get(id));
        }
        
        return bb.array();
    }
}
