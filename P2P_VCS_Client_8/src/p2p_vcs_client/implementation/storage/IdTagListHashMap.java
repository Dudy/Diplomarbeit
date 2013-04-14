package p2p_vcs_client.implementation.storage;

import java.nio.ByteBuffer;
import java.util.HashMap;
import p2p_vcs_client.implementation.SerializationUtilities;
import rice.p2p.commonapi.Id;

/**
 *
 * @author podolak
 */
public class IdTagListHashMap extends HashMap<Id, TagList> {
    
    public IdTagListHashMap(byte[] byteArray) {
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        int size = SerializationUtilities.getInteger(bb);
        
        for (int i = 0; i < size; i++) {
            put(
                    SerializationUtilities.getId(bb),
                    new TagList(SerializationUtilities.getByteArray(bb)));
        }
    }

    public IdTagListHashMap(int initialCapacity) {
        super(initialCapacity);
    }
    
    public int getByteArrayLength() {
        int length = SerializationUtilities.sizeInteger();
        
        for (Id id : keySet()) {
            length += SerializationUtilities.sizeId(id);
            length += SerializationUtilities.sizeByteArray(get(id).toByteArray());
        }
        
        return length;
    }
    
    public byte[] toByteArray() {
        System.out.println("allocate " + getByteArrayLength() + " byte");
        ByteBuffer bb = ByteBuffer.allocate(getByteArrayLength());
        
        SerializationUtilities.putInteger(bb, keySet().size());
        
        for (Id id : keySet()) {
            SerializationUtilities.putId(bb, id);
            SerializationUtilities.putByteArray(bb, get(id).toByteArray());
        }
        
        return bb.array();
    }
}
