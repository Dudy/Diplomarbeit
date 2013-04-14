package p2p_vcs_client.implementation.document;

import p2p_vcs_client.Metamodel;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 *
 * @author podolak
 */
public class MetamodelImplementation implements Serializable, Metamodel {
    
    private String content = "";

    public MetamodelImplementation() {
    }

    public MetamodelImplementation(String content) {
        this.content = content;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public byte[] toByteArray() {
        int size = 0;
        
        // size of content, four bytes
        size += Integer.SIZE / 8;
        
        // content, two bytes per character
        size += content.toCharArray().length * 2;
        
        // sizes of additional attributes
        
        // allocate space for the content (two bytes per character) plus
        // four bytes to store the size of it as an integer
        ByteBuffer bb = ByteBuffer.allocate(size);
        bb.putInt(content.toCharArray().length);
        
        for (char c : content.toCharArray()) {
            bb.putChar(c);
        }
        
        return bb.array();
    }
    
    @Override
    public String toString() {
        return
                "    Metamodel\n" +
                "    [\n" +
                "      content: " + content +
                "    ]\n";
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.content != null ? this.content.hashCode() : 0);
        return hash;
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(content);
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        content = in.readUTF();
    }
}
