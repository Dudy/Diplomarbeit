package p2p_integrationVCS_client.message;

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
public class ReleaseLockResponseMessage extends CommandMessage {
    protected Id documentId;
    protected boolean success;
    private String branch;

    public ReleaseLockResponseMessage(NodeHandle sender, Id vcsId, Id documentId, boolean success, String branch) {
        super(sender, vcsId, MessageType.RELEASE_LOCK_RESPONSE);
        this.documentId = documentId;
        this.success = success;
        this.branch = branch;
    }

    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public ReleaseLockResponseMessage(NodeHandle sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender, buf, endpoint);
        
        short type = buf.readShort();
        
        if (type == 0) {
            documentId = null;
        } else {
            documentId = endpoint.readId(buf, type);
        }
        
        success = buf.readBoolean();
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
        
        buf.writeBoolean(success);
        buf.writeUTF(branch);
    }
    // </editor-fold> // serialization

    public Id getDocumentId() {
        return documentId;
    }
    
    public boolean getSuccess() {
        return success;
    }
    
    public String getBranch() {
        return branch;
    }
    
    @Override
    public String toString() {
        String idText = "";
        
        if (documentId != null) {
            idText = documentId.toStringFull();
        }
        
        String text =
                "ReleaseLockRequestMessage " +
                "[documentID = " + idText + ", success = " + success + ", branch = " + branch + "]";
        
        return text;
    }
}
