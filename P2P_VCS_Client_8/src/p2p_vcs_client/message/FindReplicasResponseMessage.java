/*
 * FindReplicasResponseMessage.java
 *
 * Created on 3. März 2008, 12:25
 *
 */

package p2p_vcs_client.message;

import java.io.IOException;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.NodeHandleSet;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.OutputBuffer;

/**
 *
 * @author podolak
 */
public class FindReplicasResponseMessage extends CommandMessage {
    private NodeHandleSet replicas;
    
    public FindReplicasResponseMessage(NodeHandle sender, Id vcsId) {
        this(sender, vcsId, null);
    }
    
    public FindReplicasResponseMessage(NodeHandle sender, Id vcsId, NodeHandleSet replicas) {
        super(sender, vcsId, MessageType.FIND_REPLICAS_RESPONSE);
        
        this.replicas = replicas;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public FindReplicasResponseMessage(NodeHandle sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender, buf, endpoint);
        
        short type = buf.readShort();
        if (type > 0) {
            replicas = endpoint.readNodeHandleSet(buf, type);
        }
    }
    
    @Override
    public void serialize(OutputBuffer buf) throws IOException {
        super.serialize(buf);
        
        if (replicas == null) {
            buf.writeShort((short)0);
        } else {
            replicas.serialize(buf);
        }
    }
    // </editor-fold> // serialization
    
    @Override
    public String toString() {
        return
                "FindReplicasResponseMessage:\n" +
                "  num of replicas: " + replicas.size() + "\n" +
                "\n";
    }

    public NodeHandleSet getReplicas() {
        return replicas;
    }
}
