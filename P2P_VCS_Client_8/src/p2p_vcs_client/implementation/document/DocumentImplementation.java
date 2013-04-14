package p2p_vcs_client.implementation.document;

import p2p_vcs_client.Document;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import p2p_vcs_client.ApplicationStorage;
import p2p_vcs_client.Metadata;
import p2p_vcs_client.Metamodel;
import p2p_vcs_client.implementation.SerializationUtilities;
import rice.p2p.commonapi.Id;

/**
 * @author Dirk Podolak
 */
public class DocumentImplementation implements Serializable, Document {

    // xmi file
    protected Metamodel metamodel;
    // metadata
    protected Metadata metadata;
    // application specific storage file (e.g. ".eap" in enterprise Architect)
    protected ApplicationStorage applicationStorage;
    // Id
    protected Id id;

    protected DocumentImplementation() {
        metamodel = new MetamodelImplementation();
        metadata = new MetadataImplementaion();
        applicationStorage = new ApplicationStorageImplementation();
    }

    /**
     * A descriptive toString()
     */
    @Override
    public String toString() {
        return "\n" +
                "--- Document ---\n" +
                "    ID            " + id + "\n" +
                metamodel +
                metadata +
                applicationStorage +
                "--- /Document ---\n";
    }

    @Override
    public boolean equals(Object obj) {
        //return super.equals(obj);
        return obj instanceof DocumentImplementation && ((DocumentImplementation)obj).hashCode() == hashCode();
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.metamodel != null ? this.metamodel.hashCode() : 0);
        hash = 53 * hash + (this.metadata != null ? this.metadata.hashCode() : 0);
        hash = 53 * hash + (this.applicationStorage != null ? this.applicationStorage.hashCode() : 0);
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
    public byte[] toByteArray() {
        byte[] metamodelByteArray = metamodel.toByteArray();
        byte[] metadataByteArray = metadata.toByteArray();
        byte[] applicationStorageByteArray = applicationStorage.toByteArray();
        int size = 0;

        size += SerializationUtilities.sizeByteArray(metamodelByteArray);
        size += SerializationUtilities.sizeByteArray(metadataByteArray);
        size += SerializationUtilities.sizeByteArray(applicationStorageByteArray);
        size += SerializationUtilities.sizeId(id);

        // sizes or additional attributes

        ByteBuffer bb = ByteBuffer.allocate(size);

        SerializationUtilities.putByteArray(bb, metamodelByteArray);
        SerializationUtilities.putByteArray(bb, metadataByteArray);
        SerializationUtilities.putByteArray(bb, applicationStorageByteArray);
        
        SerializationUtilities.putId(bb, id);

        return bb.array();
    }

    public Metamodel getMetamodel() {
        return metamodel;
    }

    public void setMetamodel(Metamodel metamodel) {
        this.metamodel = metamodel;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public ApplicationStorage getApplicationStorage() {
        return applicationStorage;
    }

    public void setApplicationStorage(ApplicationStorage applicationStorage) {
        this.applicationStorage = applicationStorage;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public int getVersionNumber() {
        return metadata.getVersionNumber();
    }

    public void setVersionNumber(int versionNumber) {
        metadata.setVersionNumber(versionNumber);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(metamodel);
        out.writeObject(metadata);
        out.writeObject(applicationStorage);
        out.writeObject(id);
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        metamodel = (MetamodelImplementation) in.readObject();
        metadata = (MetadataImplementaion) in.readObject();
        applicationStorage = (ApplicationStorageImplementation) in.readObject();
        id = (Id) in.readObject();
    }
}
