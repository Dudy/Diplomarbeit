package p2p_vcs_client.message;

import java.io.IOException;
import p2p_vcs_client.implementation.SerializationUtilities;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.OutputBuffer;

/**
 *
 * @author podolak
 */
public class CheckoutRequestMessage extends CommandMessage {
    private int versionNumber = 0;
    
    public CheckoutRequestMessage(NodeHandle sender, Id vcsId) {
        this(sender, vcsId, 0);
    }
    
    public CheckoutRequestMessage(NodeHandle sender, Id vcsId,
            int versionNumber) {
        super(sender, vcsId, MessageType.CHECKOUT_REQUEST);
        this.versionNumber = versionNumber;
    }
    
    /**
     * Returns the version number of the requested document. A number of
     * zero denotes the request for the most actual document.
     * 
     * @return version number of requested document
     */
    public int getVersionNumber() {
        return versionNumber;
    }
    
    /**
     * Sets the version number of the requested document.
     * 
     * @param versionNumber requested version number of the document
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public CheckoutRequestMessage(NodeHandle sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender, buf, endpoint);
        versionNumber = SerializationUtilities.getInteger(buf);
    }

    @Override
    public void serialize(OutputBuffer buf) throws IOException {
        super.serialize(buf);
        SerializationUtilities.putInteger(buf, versionNumber);
    }
    // </editor-fold> // serialization
    
    @Override
    public String toString() {
        String text =
                super.toString() + "; " +
                "CheckoutRequestMessage " +
                "[Version Number = " + versionNumber + "]";
        
        return text;
    }
}
