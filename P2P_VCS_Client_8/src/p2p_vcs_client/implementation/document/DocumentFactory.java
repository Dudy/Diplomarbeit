package p2p_vcs_client.implementation.document;

import java.nio.ByteBuffer;
import p2p_vcs_client.Document;
import p2p_vcs_client.implementation.SerializationUtilities;

/**
 *
 * @author podolak
 */
public class DocumentFactory {

    private static DocumentFactory instance;

    protected DocumentFactory() {
    }

    public static DocumentFactory getInstance() {
        if (instance == null) {
            instance = new DocumentFactory();
        }

        return instance;
    }

    public Document newDocument() {
        return new DocumentImplementation();
    }

    // can't put that in Document as it is not possible to put static methods in interfaces
    public Document documentFromByteArray(byte[] byteArray) {
        Document document = null;

        if (byteArray != null && byteArray.length > 0) {
            ByteBuffer bb = ByteBuffer.wrap(byteArray);

            byte[] metamodelByteArray = SerializationUtilities.getByteArray(bb);
            byte[] metadataByteArray = SerializationUtilities.getByteArray(bb);
            byte[] applicationStorageByteArray = SerializationUtilities.getByteArray(bb);

            document = new DocumentImplementation();
            document.setMetamodel(MetamodelFactory.getInstance().metamodelFromByteArray(metamodelByteArray));
            document.setMetadata(MetadataImplementaion.metadataFromByteArray(metadataByteArray));
            document.setApplicationStorage(ApplicationStorageFactory.getInstance().applicationStorageFromByteArray(applicationStorageByteArray));

            document.setId(SerializationUtilities.getId(bb));
        }

        return document;
    }
}
