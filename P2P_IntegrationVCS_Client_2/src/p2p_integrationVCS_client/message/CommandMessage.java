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
public class CommandMessage extends p2p_vcs_client.message.CommandMessage {
    private MessageType integrationSpecificType;

    public CommandMessage(NodeHandle sender, Id vcsId, MessageType messageType) {
        super(sender, vcsId, messageType.getSupertype());
    }

    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public CommandMessage(NodeHandle sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender, buf, endpoint);
        integrationSpecificType = MessageType.fromType(buf.readShort());
    }

    @Override
    public void serialize(OutputBuffer buf) throws IOException {
        super.serialize(buf);
        buf.writeShort(integrationSpecificType.getType());
    }
    // </editor-fold> // serialization
    
    public MessageType getIntegrationSpecificType() {
        return integrationSpecificType;
    }

    public void setIntegrationSpecificType(MessageType integrationSpecificType) {
        this.integrationSpecificType = integrationSpecificType;
    }
}
