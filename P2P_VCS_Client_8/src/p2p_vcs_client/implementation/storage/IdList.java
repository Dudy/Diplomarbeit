package p2p_vcs_client.implementation.storage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import p2p_vcs_client.implementation.SerializationUtilities;
import rice.p2p.commonapi.Id;

/**
 *
 * @author podolak
 */
public class IdList extends ArrayList<Id> {
    
    public IdList() {
    }
    
    public IdList(byte[] byteArray) {
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        int size = SerializationUtilities.getInteger(bb);
        
        for (int i = 0; i < size; i++) {
            add(SerializationUtilities.getId(bb));
        }
    }
    
    public int getByteArrayLength() {
        int length = SerializationUtilities.sizeInteger();
        
        for (Id id : this) {
            length += SerializationUtilities.sizeId(id);
        }

        
        return length;
    }
    
    public byte[] toByteArray() {
        ByteBuffer bb = ByteBuffer.allocate(getByteArrayLength());
        
        SerializationUtilities.putInteger(bb, size());
        
        for (Id id : this) {
            SerializationUtilities.putId(bb, id);
        }
        
        return bb.array();
    }
}
