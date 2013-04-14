package p2p_integrationVCS_client;

import p2p_vcs_client.Document;
import rice.p2p.commonapi.Id;

/**
 *
 * @author podolak
 */
public interface Link extends Document {
    
    // You may also put this into properties. I'm not sure which is better.
    
    public Id getSource();
    public void setSource(Id source);
    public Id getTarget();
    public void setTarget(Id target);
}
