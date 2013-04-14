package p2p_vcs_client.implementation.document;

import p2p_vcs_client.ApplicationStorage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 *
 * @author podolak
 */
public class ApplicationStorageImplementation implements Serializable, ApplicationStorage {
    
    public byte[] toByteArray() {
        int size = 0;
        
        // sizes of additional attributes
        
        ByteBuffer bb = ByteBuffer.allocate(size);
        
        // if this class gets some attributes, serialize them here
        
        return bb.array();
    }
    
    @Override
    public String toString() {
        return
                "    ApplicationStorage\n" +
                "    [\n" +
                "    ]\n";
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {

    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {

    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
}
