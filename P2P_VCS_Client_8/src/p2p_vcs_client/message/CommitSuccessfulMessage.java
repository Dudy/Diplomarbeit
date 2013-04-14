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
public class CommitSuccessfulMessage extends CommandMessage {
    protected Id documentId;

    public CommitSuccessfulMessage(NodeHandle sender, Id vcsId, Id documentId) {
        super(sender, vcsId, MessageType.COMMIT_SUCCESSFUL);
        
        this.documentId = documentId;
    }

    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public CommitSuccessfulMessage(NodeHandle sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender, buf, endpoint);
        
        short type = buf.readShort();
        
        if (type == 0) {
            documentId = null;
        } else {
            documentId = endpoint.readId(buf, type);
        }
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
    }
    // </editor-fold> // serialization

    public Id getDocumentId() {
        return documentId;
    }
    
    @Override
    public String toString() {
        String idText = "";
        
        if (documentId != null) {
            idText = documentId.toStringFull();
        }
        
        String text =
                "CommitSuccessfulMessage " +
                "[documentID = " + idText + "]";
        
        return text;
    }
}
