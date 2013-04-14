/*
 * TagMessage.java
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
public class TagMessage extends CommandMessage {
    private String tagText;
    private Id documentId;
    private String branch;
    
    public TagMessage(NodeHandle sender, Id vcsId) {
        super(sender, vcsId, MessageType.TAG);
    }
    
    public TagMessage(NodeHandle sender, Id vcsId, String tagText, Id documentId, String branch) {
        super(sender, vcsId, MessageType.TAG);
        
        this.tagText = tagText;
        this.documentId = documentId;
        this.branch = branch;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public TagMessage(NodeHandle sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender, buf, endpoint);
        
        // text
        tagText = buf.readUTF();
        
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
        
        buf.writeUTF(tagText);
        
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
                "  tagText: " + tagText + "\n" +
                "  id: " + documentId + "\n" +
                "  branch: " + branch + "\n" +
                "\n";
    }
    
    public String getTagText() {
        return tagText;
    }
    
    public Id getDocumentId() {
        return documentId;
    }
    
    public String getBranch() {
        return branch;
    }
}
