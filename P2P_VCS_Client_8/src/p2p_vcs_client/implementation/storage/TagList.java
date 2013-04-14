package p2p_vcs_client.implementation.storage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import p2p_vcs_client.implementation.SerializationUtilities;

/**
 *
 * @author podolak
 */
public class TagList extends ArrayList<String> {
    
    public TagList(byte[] byteArray) {
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        int size = SerializationUtilities.getInteger(bb);
        
        for (int i = 0; i < size; i++) {
            add(SerializationUtilities.getString(bb));
        }
    }

    public TagList(int initialCapacity) {
        super(initialCapacity);
    }
    
    public int getByteArrayLength() {
        int length = SerializationUtilities.sizeInteger();
        
        for (String tag : this) {
            length += SerializationUtilities.sizeString(tag);
        }
        
        return length;
    }
    
    public byte[] toByteArray() {
        ByteBuffer bb = ByteBuffer.allocate(getByteArrayLength());
        
        SerializationUtilities.putInteger(bb, size());
        
        for (String tag : this) {
            SerializationUtilities.putString(bb, tag);
        }
        
        return bb.array();
    }
}
