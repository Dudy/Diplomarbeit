package p2p_integrationVCS_client.implementation.document;

/**
 *
 * @author podolak
 */
public class MetadataImplementaion extends p2p_vcs_client.implementation.document.MetadataImplementaion {
    
    
//    //TODO Die Lösung mit dem type gefällt mir nicht. Was anderes überlegen.
//    
//    protected int versionNumber = 0;
//    protected String type = "";
//    
//    public byte[] toByteArray() {
//        int size = 0;
//        
//        // version number
//        size += Integer.SIZE / 8;
//        
//        // size of content, four bytes
//        size += Integer.SIZE / 8;
//        
//        // content, two bytes per character
//        size += type.toCharArray().length * 2;
//        
//        // sizes of additional attributes
//        
//        ByteBuffer bb = ByteBuffer.allocate(size);
//        bb.putInt(versionNumber);
//        bb.putInt(type.toCharArray().length);
//        
//        for (char c : type.toCharArray()) {
//            bb.putChar(c);
//        }
//        
//        return bb.array();
//    }
//    
//    @Override
//    public String toString() {
//        return
//                "    Metadata\n" +
//                "    [\n" +
//                "      version number: " + versionNumber + "\n" +
//                "      type          : " + type +
//                "    ]\n";
//    }
//    
//    private void writeObject(ObjectOutputStream out) throws IOException {
//
//    }
//
//    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
//
//    }
//
//    public int getVersionNumber() {
//        return versionNumber;
//    }
//
//    public void setVersionNumber(int versionNumber) {
//        this.versionNumber = versionNumber;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
}
