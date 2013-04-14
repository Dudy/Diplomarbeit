package p2p_integrationVCS_client.implementation.document;

import java.nio.ByteBuffer;
import p2p_vcs_client.Document;
import p2p_vcs_client.implementation.document.ApplicationStorageFactory;
import p2p_vcs_client.implementation.document.MetamodelFactory;
import rice.pastry.Id;

/**
 *
 * @author podolak
 */
public class DocumentFactory extends p2p_vcs_client.implementation.document.DocumentFactory {
    
    @Override
    public Document newDocument() {
        return new DocumentImplementation();
    }

//    //TODO verwenden
//    public Document newDocument(DocumentType type) {
//        if (type == DocumentType.LINK) {
//            return new LinkDocument();
//        } else {
//            return new DocumentImplementation();
//        }
//    }
    
    @Override
    public Document documentFromByteArray(byte[] byteArray) {
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        Document document = new DocumentImplementation();
        
        byte[] metamodelByteArray = new byte[bb.getInt()];
        byte[] metadataByteArray = new byte[bb.getInt()];
        byte[] applicationStorageByteArray = new byte[bb.getInt()];
        
        bb.get(metamodelByteArray);
        bb.get(metadataByteArray);
        bb.get(applicationStorageByteArray);
        
        document.setMetamodel(MetamodelFactory.getInstance().metamodelFromByteArray(metamodelByteArray));
        //document.setMetadata(MetadataFactory.getInstance().metadataFromByteArray(metadataByteArray));
        document.setMetadata(MetadataImplementaion.metadataFromByteArray(metadataByteArray));
        document.setApplicationStorage(ApplicationStorageFactory.getInstance().applicationStorageFromByteArray(applicationStorageByteArray));
        
        int size = bb.getInt();
        
        if (size > 0) {
            byte[] idByteArray = new byte[bb.getInt()];
            bb.get(idByteArray);
            document.setId(Id.build(idByteArray));
        } else {
            document.setId(null);
        }
        
        return document;
    }
}
