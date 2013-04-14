package p2p_vcs_client.implementation.storage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import p2p_vcs_client.Document;
import p2p_vcs_client.implementation.SerializationUtilities;
import p2p_vcs_client.implementation.document.DocumentFactory;

/**
 *
 * @author podolak
 */
public class DocumentList extends ArrayList<Document> {
    
    public DocumentList() {
    }
    
    public DocumentList(byte[] byteArray) {
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        int size = SerializationUtilities.getInteger(bb);
        
        for (int i = 0; i < size; i++) {
            add(DocumentFactory.getInstance().documentFromByteArray(SerializationUtilities.getByteArray(bb)));
        }
    }
    
    public int getByteArrayLength() {
        int length = SerializationUtilities.sizeInteger();
        
        for (Document document : this) {
            length += SerializationUtilities.sizeByteArray(document.toByteArray());
        }
        
        return length;
    }
    
    public byte[] toByteArray() {
        ByteBuffer bb = ByteBuffer.allocate(getByteArrayLength());
        
        SerializationUtilities.putInteger(bb, size());
        
        for (Document document : this) {
            SerializationUtilities.putByteArray(bb, document.toByteArray());
        }
        
        return bb.array();
    }
    
    public Document getLast() {
        return get(size() - 1);
    }
    
    public Document getSecondLast() {
        return get(size() - 2);
    }
}
