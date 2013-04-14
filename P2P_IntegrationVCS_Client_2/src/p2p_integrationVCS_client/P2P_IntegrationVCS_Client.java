package p2p_integrationVCS_client;

import java.util.ArrayList;
import p2p_vcs_client.Document;
import p2p_vcs_client.P2P_VCS_Client;
import rice.p2p.commonapi.Id;

/**
 *
 * @author podolak
 */
public interface P2P_IntegrationVCS_Client extends P2P_VCS_Client {
//[-]   getLastConsistentTriple()
//[-]   getConsistentTriple(singleDocument)
//[-]   getLink(source, target)
//[-]   getLatestTriple()
//[x]   commitSingleDocument(document, documentType)
//[-]   commitTriple(triple)
//[-]   getHistory(document, branch)
//[-]   getNodeStatus (all branches, all document versions, GUI?)
    
    public ArrayList<Document> getLastConsistentTriple();
    public ArrayList<Document> getConsistentTriple(Id documentId);
    public Link getLink();
    public ArrayList<Document> getLatestTriple();
    public void commitTripel(Id vcsId, ArrayList<Document> triple);
    public void commitTripel(Id vcsId, ArrayList<Document> triple, String branch);
    public ArrayList<String> getDocumentHistory(String branch, Id documentId);
    public void getNodeStatus();
    
}
