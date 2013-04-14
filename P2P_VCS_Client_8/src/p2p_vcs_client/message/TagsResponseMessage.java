/*
 * TagsResponseMessage.java
 *
 * Created on 3. März 2008, 12:25
 *
 */
package p2p_vcs_client.message;

import java.io.IOException;
import java.util.ArrayList;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.OutputBuffer;

/**
 *
 * @author podolak
 */
public class TagsResponseMessage extends CommandMessage {

    private ArrayList<String> tags;
    private Id documentId;
    private String branch;

    public TagsResponseMessage(NodeHandle sender, Id vcsId) {
        super(sender, vcsId, MessageType.TAGS_RESPONSE);
    }

    public TagsResponseMessage(NodeHandle sender, Id vcsId, Id documentId, String branch, ArrayList<String> tags) {
        super(sender, vcsId, MessageType.TAGS_RESPONSE);
        
        this.documentId = documentId;
        this.branch = branch;
        this.tags = tags;
    }

    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public TagsResponseMessage(NodeHandle sender, InputBuffer buf, Endpoint endpoint) throws IOException {
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
        
        // tags
        int size = buf.readInt();
        
        tags = new ArrayList<String>(size);
        
        for (int i = 0; i < size; i++) {
            tags.add(buf.readUTF());
        }
    }

    @Override
    public void serialize(OutputBuffer buf) throws IOException {
        super.serialize(buf);
        
        if (documentId == null) {
            buf.writeShort((short) 0);
        } else {
            buf.writeShort(documentId.getType());
            documentId.serialize(buf);
        }

        buf.writeUTF(branch);
        
        buf.writeInt(tags.size());
        for (int i = 0; i < tags.size(); i++) {
            buf.writeUTF(tags.get(i));
        }
    }
    // </editor-fold> // serialization
    
    @Override
    public String toString() {
        return "TagResponseMessage:\n" +
                "  tags: " + tags + "\n" +
                "\n";
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getBranch() {
        return branch;
    }

    public Id getDocumentId() {
        return documentId;
    }
}
