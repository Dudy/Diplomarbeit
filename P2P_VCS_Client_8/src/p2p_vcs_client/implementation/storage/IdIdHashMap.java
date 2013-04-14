package p2p_vcs_client.implementation.storage;

import java.nio.ByteBuffer;
import java.util.HashMap;
import p2p_vcs_client.implementation.SerializationUtilities;
import rice.p2p.commonapi.Id;

/**
 *
 * @author podolak
 */
public class IdIdHashMap extends HashMap<Id, Id> {
    
    public IdIdHashMap(byte[] byteArray) {
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        int size = SerializationUtilities.getInteger(bb);
        
        for (int i = 0; i < size; i++) {
            put(
                    SerializationUtilities.getId(bb),
                    SerializationUtilities.getId(bb));
        }
    }

    public IdIdHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public int getByteArrayLength() {
        int length = SerializationUtilities.sizeInteger();

        for (Id id : keySet()) {
            length += SerializationUtilities.sizeId(id);
            length += SerializationUtilities.sizeId(get(id));
        }

        return length;
    }

    public byte[] toByteArray() {
        ByteBuffer bb = ByteBuffer.allocate(getByteArrayLength());
        
        SerializationUtilities.putInteger(bb, keySet().size());
        
        for (Id id : keySet()) {
            SerializationUtilities.putId(bb, id);
            SerializationUtilities.putId(bb, get(id));
        }
        
        return bb.array();
    }
}
