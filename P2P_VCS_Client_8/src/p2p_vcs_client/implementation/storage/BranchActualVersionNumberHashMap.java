package p2p_vcs_client.implementation.storage;

import java.nio.ByteBuffer;
import java.util.HashMap;
import p2p_vcs_client.implementation.SerializationUtilities;

/**
 *
 * @author podolak
 */
public class BranchActualVersionNumberHashMap  extends HashMap<String, Integer>{

    public BranchActualVersionNumberHashMap() {
    }
    
    public BranchActualVersionNumberHashMap(byte[] byteArray) {
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        int size = SerializationUtilities.getInteger(bb);
        
        for (int i = 0; i < size; i++) {
            put(
                    SerializationUtilities.getString(bb),
                    SerializationUtilities.getInteger(bb));
        }
    }
    
    public int getByteArrayLength() {
        int length = SerializationUtilities.sizeInteger();
        
        for (String branch : keySet()) {
            length += SerializationUtilities.sizeString(branch);
            length += SerializationUtilities.sizeInteger();
        }
        
        return length;
    }
    
    public byte[] toByteArray() {
        ByteBuffer bb = ByteBuffer.allocate(getByteArrayLength());
        SerializationUtilities.putInteger(bb, keySet().size());
        
        for (String branch : keySet()) {
            SerializationUtilities.putString(bb, branch);
            SerializationUtilities.putInteger(bb, get(branch));
        }
        
        return bb.array();
    }
}
