package p2p_integrationVCS_client.implementation.document;

import java.nio.ByteBuffer;
import p2p_integrationVCS_client.Link;
import p2p_vcs_client.implementation.document.ApplicationStorageFactory;
import p2p_vcs_client.implementation.document.MetamodelFactory;
import rice.pastry.Id;

/**
 *
 * @author podolak
 */
public class LinkFactory extends DocumentFactory {

    private static LinkFactory instance;

    protected LinkFactory() {
    }

    public static LinkFactory getInstance() {
        if (instance == null) {
            instance = new LinkFactory();
        }

        return instance;
    }

    public Link newLink() {
        return new LinkImplementation();
    }

    // can't put that in Document as it is not possible to put static methods in interfaces
    public static Link linkFromByteArray(byte[] byteArray) {
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        Link link = new LinkImplementation();
        
        byte[] metamodelByteArray = new byte[bb.getInt()];
        byte[] metadataByteArray = new byte[bb.getInt()];
        byte[] applicationStorageByteArray = new byte[bb.getInt()];
        
        bb.get(metamodelByteArray);
        bb.get(metadataByteArray);
        bb.get(applicationStorageByteArray);
        
        link.setMetamodel(MetamodelFactory.getInstance().metamodelFromByteArray(metamodelByteArray));
        link.setMetadata(MetadataImplementaion.metadataFromByteArray(metadataByteArray));
        link.setApplicationStorage(ApplicationStorageFactory.getInstance().applicationStorageFromByteArray(applicationStorageByteArray));
        
        int size = bb.getInt();
        
        if (size > 0) {
            byte[] sourceByteArray = new byte[size];
            bb.get(sourceByteArray);
            link.setSource(Id.build(sourceByteArray));
        } else {
            link.setSource(null);
        }
        
        size = bb.getInt();
        
        if (size > 0) {
            byte[] targetByteArray = new byte[size];
            bb.get(targetByteArray);
            link.setTarget(Id.build(targetByteArray));
        } else {
            link.setTarget(null);
        }
        
        return link;
    }
}
