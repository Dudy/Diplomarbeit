/*
 * TagsRequestMessage.java
 *
 * Created on 3. März 2008, 12:25
 * 
 */

package p2p_vcs_client.message;

import java.io.IOException;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.OutputBuffer;

/**
 *
 * @author podolak
 */
public class TagsRequestMessage extends CommandMessage {
    private Id documentId;
    private String branch;
    
    public TagsRequestMessage(NodeHandle sender, Id vcsId) {
        super(sender, vcsId, MessageType.TAGS_REQUEST);
    }
    
    public TagsRequestMessage(NodeHandle sender, Id vcsId, Id documentId, String branch) {
        super(sender, vcsId, MessageType.TAGS_REQUEST);
        
        this.documentId = documentId;
        this.branch = branch;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public TagsRequestMessage(NodeHandle sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender, buf, endpoint);
        
        // document id
        short type = buf.readShort();
        
        if (type == 0) {
            documentId = null;
        } else {
            documentId = endpoint.readId(buf, type);
        }
        
        // branch name
        branch = buf.readUTF();
    }
    
    @Override
    public void serialize(OutputBuffer buf) throws IOException {
        super.serialize(buf);
        
        if (documentId == null) {
            buf.writeShort((short)0);
        } else {
            buf.writeShort(documentId.getType());
            documentId.serialize(buf);
        }
        
        buf.writeUTF(branch);
    }
    // </editor-fold> // serialization
    
    @Override
    public String toString() {
        return
                "TagMessage:\n" +
                "  id: " + documentId + "\n" +
                "  branch: " + branch + "\n" +
                "\n";
    }
    
    public Id getDocumentId() {
        return documentId;
    }
    
    public String getBranch() {
        return branch;
    }
}
