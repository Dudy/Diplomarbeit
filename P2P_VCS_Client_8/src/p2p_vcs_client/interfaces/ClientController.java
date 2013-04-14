package p2p_vcs_client.interfaces;

import java.util.ArrayList;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.IdRange;

/**
 *
 * @author podolak
 */
public interface ClientController {
    public void checkoutResponse(Id id, Object result);
    public void commitResponse(Id id, boolean success, String documentType);
    public void commitResponse(Id vcsId, Id nodeAddress, Id documentId, boolean success, String documentType);
    public void getTagsResponse(Id vcsId, String branch, Id documentId, ArrayList<String> tags);
    public void setNewRange(IdRange range);
    public void output(String text);
}
