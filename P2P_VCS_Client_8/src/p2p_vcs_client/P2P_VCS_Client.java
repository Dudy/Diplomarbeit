package p2p_vcs_client;

import java.util.ArrayList;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Id;
import rice.p2p.replication.ReplicationClient;
import rice.p2p.commonapi.IdFactory;

/**
 *
 * @author podolak
 */
public interface P2P_VCS_Client extends Application, ReplicationClient {

    public void checkout(Id vcsId, int version);

    public void commit(Id vcsId, Document document);

    public String getBootAddress();

    public IdFactory getIdFactory();

    public String getLocalBindPort();

    public void connect();

    public void disconnect();

    public void tagDocument(Id vcsId, String tagText, Id documentId, String branch);

    public void tagDocuments(ArrayList<Id> vcsId, String tagText, ArrayList<Id> documentIds, String branch);

    public void getTags(Id vcsId, String branch, Id documentId);

    public String getOwnerName(Id vcsId);

    public Id getOwnerId(Id vcsId);
}
