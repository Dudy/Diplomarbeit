package p2p_integrationVCS_client.implementation.document;

/**
 * @author Dirk Podolak
 */
public class DocumentImplementation extends p2p_vcs_client.implementation.document.DocumentImplementation {

    
//    // xmi file
//    private Metamodel metamodel;
//    // metadata
//    private Metadata metadata;
//    // application specific storage file (e.g. ".eap" in enterprise Architect)
//    private ApplicationStorage applicationStorage;
//    // Id
//    private Id id;
//
//    public DocumentImplementation() {
//        metamodel = new Metamodel();
//        metadata = new Metadata();
//        applicationStorage = new ApplicationStorage();
//    }
//
//    public static DocumentImplementation fromByteArray(byte[] byteArray) {
//        ByteBuffer bb = ByteBuffer.wrap(byteArray);
//        DocumentImplementation document = new DocumentImplementation();
//
//        byte[] metamodelByteArray = new byte[bb.getInt()];
//        byte[] metadataByteArray = new byte[bb.getInt()];
//        byte[] applicationStorageByteArray = new byte[bb.getInt()];
//
//        bb.get(metamodelByteArray);
//        bb.get(metadataByteArray);
//        bb.get(applicationStorageByteArray);
//
//        document.setMetamodel(Metamodel.fromByteArray(metamodelByteArray));
//        document.setMetadata(Metadata.fromByteArray(metadataByteArray));
//        document.setApplicationStorage(ApplicationStorage.fromByteArray(applicationStorageByteArray));
//
//        //TODO don't know which instance of id, now serialization of IDs is done in messages
////        int size = bb.getInt();
////        byte[] idByteArray = new byte[size];
////        bb.get(byteArray);
//
//
//
//        return document;
//    }
//
//    /**
//     * A descriptive toString()
//     */
//    @Override
//    public String toString() {
//        return "\n" +
//                "--- Document ---\n" +
//                "    ID            " + id + "\n" +
//                metamodel +
//                metadata +
//                applicationStorage +
//                "--- /Document ---\n";
//    }
//
//    public byte[] toByteArray() {
//        byte[] metamodelByteArray = metamodel.toByteArray();
//        byte[] metadataByteArray = metadata.toByteArray();
//        byte[] applicationStorageByteArray = applicationStorage.toByteArray();
//        int size = 0;
//
//        // length values of the three subdocuments, three ints with four bytes each
//        size += 3 * (Integer.SIZE / 8);
//
//        // metamodel
//        size += metamodelByteArray.length;
//
//        // metadata
//        size += metadataByteArray.length;
//
//        // application storage
//        size += applicationStorageByteArray.length;
//
//        // see fromByteArray
////        // storage for the size of the id
////        size += Integer.SIZE / 8;
////        
////        // length of id
////        size += id.getByteArrayLength();
//
//        // sizes or additional attributes
//
//        ByteBuffer bb = ByteBuffer.allocate(size);
//
//        // the sizes
//        bb.putInt(metamodelByteArray.length);
//        bb.putInt(metadataByteArray.length);
//        bb.putInt(applicationStorageByteArray.length);
//
//        // the buffers
//        bb.put(metamodelByteArray);
//        bb.put(metadataByteArray);
//        bb.put(applicationStorageByteArray);
//
//        // see fromByteArray
////        bb.putInt(id.getByteArrayLength());
////        bb.put(id.toByteArray());
//
//        return bb.array();
//    }
//
//    public Metamodel getMetamodel() {
//        return metamodel;
//    }
//
//    public void setMetamodel(Metamodel metamodel) {
//        this.metamodel = metamodel;
//    }
//
//    public Metadata getMetadata() {
//        return metadata;
//    }
//
//    public void setMetadata(Metadata metadata) {
//        this.metadata = metadata;
//    }
//
//    public ApplicationStorage getApplicationStorage() {
//        return applicationStorage;
//    }
//
//    public void setApplicationStorage(ApplicationStorage applicationStorage) {
//        this.applicationStorage = applicationStorage;
//    }
//
////    public void initID(IdFactory idFactory) {
////        System.out.println("idFactory: " + idFactory);
////        System.out.println("toByteArray(): " + toByteArray());
////        System.out.println("new String(toByteArray()): " + new String(toByteArray()));
////        System.out.println("idFactory.buildId(new String(toByteArray())): " + idFactory.buildId(new String(toByteArray())));
////        
////        // only buildId with String does a new hash, buildId with a byte array uses the bytes to form an id !!
////        setId(idFactory.buildId(new String(toByteArray())));
////    }
//    public Id getId() {
//        return id;
//    }
//
//    public void setId(Id id) {
//        this.id = id;
//    }
//
//    public int getVersionNumber() {
//        return metadata.getVersionNumber();
//    }
//
//    public void setVersionNumber(int versionNumber) {
//        metadata.setVersionNumber(versionNumber);
//    }
//
//    private void writeObject(ObjectOutputStream out) throws IOException {
//        out.writeObject(metamodel);
//        out.writeObject(metadata);
//        out.writeObject(applicationStorage);
//        out.writeObject(id);
//    }
//
//    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
//        metamodel = (Metamodel) in.readObject();
//        metadata = (Metadata) in.readObject();
//        applicationStorage = (ApplicationStorage) in.readObject();
//        id = (Id) in.readObject();
//    }
}
