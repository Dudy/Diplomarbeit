package p2p_integrationVCS_client.implementation.document;

import java.nio.ByteBuffer;
import p2p_vcs_client.Metadata;

/**
 *
 * @author podolak
 */
public class MetadataFactory extends p2p_vcs_client.implementation.document.MetadataFactory {

    public Metadata metadataFromByteArray(byte[] byteArray) {
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        Metadata metadata = new MetadataImplementaion();

        // version number
        metadata.setVersionNumber(bb.getInt());

        // type
        int size = bb.getInt();

        char[] contentAsCharArray = new char[size];

        for (int i = 0; i < size; i++) {
            contentAsCharArray[i] = bb.getChar();
        }

        metadata.setType(new String(contentAsCharArray));

        return metadata;
    }
}
