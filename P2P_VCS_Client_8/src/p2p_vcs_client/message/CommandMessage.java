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
public class CommandMessage extends DocumentStorageAbstractMessage {
    protected Id vcsId;

    public CommandMessage(NodeHandle sender, Id vcsId, MessageType messageType) {
        super(sender);

        this.vcsId =  vcsId;
        this.messageType = messageType;
    }

    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public CommandMessage(NodeHandle sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender);
        messageType = MessageType.fromType(buf.readShort());
        
        short type = buf.readShort();
        
        if (type == 0) {
            vcsId = null;
        } else {
            vcsId = endpoint.readId(buf, type);
        }
    }

    public void serialize(OutputBuffer buf) throws IOException {
        buf.writeShort(messageType.getType());
        
        if (vcsId == null) {
            buf.writeShort((short)0);
        } else {
            buf.writeShort(vcsId.getType());
            vcsId.serialize(buf);
        }
    }
    // </editor-fold> // serialization

    public Id getVcsId() {
        return vcsId;
    }
    
    @Override
    public String toString() {
        String idText = "";
        
        if (vcsId != null) {
            idText = vcsId.toStringFull();
        }
        
        String text =
                "CommandMessage [" +
                "VCS ID = " + idText + ", " +
                "Type   = " + messageType + "]\n";
        
        return text;
    }
}
