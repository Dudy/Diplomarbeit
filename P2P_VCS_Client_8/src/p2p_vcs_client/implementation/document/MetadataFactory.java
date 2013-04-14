package p2p_vcs_client.implementation.document;

import java.nio.ByteBuffer;
import p2p_vcs_client.Metadata;

/**
 *
 * @author podolak
 */
public class MetadataFactory {
    
    private static MetadataFactory instance;

    protected MetadataFactory() {
    }
    
    public static MetadataFactory getInstance() {
        if (instance == null) {
            instance = new MetadataFactory();
        }
        
        return instance;
    }
    
//    public Metadata metadataFromByteArray(byte[] byteArray) {
//        ByteBuffer bb = ByteBuffer.wrap(byteArray);
//        Metadata metadata = new MetadataImplementaion();
//        
//        // version number
//        metadata.setVersionNumber(bb.getInt());
//        
//        // type
//        int size = bb.getInt();
//        
//        char[] contentAsCharArray = new char[size];
//        
//        for (int i = 0; i < size; i++) {
//            contentAsCharArray[i] = bb.getChar();
//        }
//        
//        metadata.setType(new String(contentAsCharArray));
//        
//        return metadata;
//    }
}
