/*
 * FindNodeResponseMessage.java
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
public class FindNodeResponseMessage extends CommandMessage {
    private Id nodeAddress;
    private String instanceName;
    
    public FindNodeResponseMessage(NodeHandle sender, Id vcsId) {
        super(sender, vcsId, MessageType.FIND_NODE_RESPONSE);
    }
    
    public FindNodeResponseMessage(NodeHandle sender, Id vcsId, Id nodeAddress, String instanceName) {
        super(sender, vcsId, MessageType.FIND_NODE_RESPONSE);
        
        this.nodeAddress = nodeAddress;
        this.instanceName = instanceName;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public FindNodeResponseMessage(NodeHandle sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender, buf, endpoint);
        
        // document id
        short type = buf.readShort();
        
        if (type == 0) {
            nodeAddress = null;
        } else {
            nodeAddress = endpoint.readId(buf, type);
        }
        
        instanceName = buf.readUTF();
    }
    
    @Override
    public void serialize(OutputBuffer buf) throws IOException {
        super.serialize(buf);
        
        if (nodeAddress == null) {
            buf.writeShort((short)0);
        } else {
            buf.writeShort(nodeAddress.getType());
            nodeAddress.serialize(buf);
        }
        
        buf.writeUTF(instanceName);
    }
    // </editor-fold> // serialization
    
    @Override
    public String toString() {
        return
                "FindNodeRequestMessage:\n" +
                "  node address : " + nodeAddress + "\n" +
                "  instance name: " + instanceName + "\n" +
                "\n";
    }
    
    public Id getNodeAddress() {
        return nodeAddress;
    }
    
    public String getInstanceName() {
        return instanceName;
    }
}
