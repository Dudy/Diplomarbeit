package p2p_integrationVCS_client.message;

import java.io.IOException;
import p2p_vcs_client.Document;
import p2p_vcs_client.implementation.SerializationUtilities;
import p2p_vcs_client.implementation.document.DocumentFactory;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.OutputBuffer;

/**
 *
 * @author podolak
 */
public class CommitMessage extends CommandMessage {
    protected Document document;
    protected String branch;

    public CommitMessage(NodeHandle sender, Id vcsId) {
        super(sender, vcsId, MessageType.COMMIT);
    }
    
    public CommitMessage(NodeHandle sender, Id vcsId, Document document, String branch) {
        super(sender, vcsId, MessageType.COMMIT);
        this.document = document;
        this.branch = branch;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public CommitMessage(NodeHandle sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender, buf, endpoint);
        
        //TODO Should we use the integration specific DocumentFactory here? We don't need it at the
        // moment and possibly it get's kicked soon, so stay to the general document here.
        document = DocumentFactory.getInstance().documentFromByteArray(SerializationUtilities.getByteArray(buf));
        branch = buf.readUTF();
    }

    @Override
    public void serialize(OutputBuffer buf) throws IOException {
        super.serialize(buf);
        
        if (document == null) {
            SerializationUtilities.putByteArray(buf, null);
        } else {
            SerializationUtilities.putByteArray(buf, document.toByteArray());
        }
        
        buf.writeUTF(branch);
    }
    // </editor-fold> // serialization
    
    public Document getDocument() {
        return document;
    }
    
    public String getBranch() {
        return branch;
    }
    
    @Override
    public String toString() {
        return
                "CommitMessage:\n" +
                document +
                "\n";
    }
}
