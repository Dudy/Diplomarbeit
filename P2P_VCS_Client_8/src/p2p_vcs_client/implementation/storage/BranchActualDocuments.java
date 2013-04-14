package p2p_vcs_client.implementation.storage;

import java.nio.ByteBuffer;
import java.util.HashMap;
import p2p_vcs_client.Document;
import p2p_vcs_client.implementation.SerializationUtilities;
import p2p_vcs_client.implementation.document.DocumentFactory;

/**
 *
 * @author podolak
 */
public class BranchActualDocuments extends HashMap<String, Document> {

    public BranchActualDocuments() {
    }
    
    public BranchActualDocuments(byte[] byteArray) {
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        int size = SerializationUtilities.getInteger(bb);
        
        for (int i = 0; i < size; i++) {
            put(
                    SerializationUtilities.getString(bb),
                    DocumentFactory.getInstance().documentFromByteArray(SerializationUtilities.getByteArray(bb)));
        }
    }
    
    public int getByteArrayLength() {
        int length = SerializationUtilities.sizeInteger();
        
        for (String branch : keySet()) {
            length += SerializationUtilities.sizeString(branch);
            length += SerializationUtilities.sizeByteArray(get(branch).toByteArray());
        }
        
        return length;
    }
    
    public byte[] toByteArray() {
        ByteBuffer bb = ByteBuffer.allocate(getByteArrayLength());
        SerializationUtilities.putInteger(bb, keySet().size());
        
        for (String branch : keySet()) {
            SerializationUtilities.putString(bb, branch);
            SerializationUtilities.putByteArray(bb, get(branch).toByteArray());
        }
        
        return bb.array();
    }
}
