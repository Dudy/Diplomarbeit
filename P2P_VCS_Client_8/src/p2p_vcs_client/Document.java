package p2p_vcs_client;

import rice.p2p.commonapi.Id;

/**
 *
 * @author podolak
 */
public interface Document {

    Id getId();
    
    ApplicationStorage getApplicationStorage();
    
    Metadata getMetadata();

    Metamodel getMetamodel();

    int getVersionNumber();
    
    void setApplicationStorage(ApplicationStorage applicationStorage);

    void setId(Id id);

    void setMetadata(Metadata metadata);

    void setMetamodel(Metamodel metamodel);

    void setVersionNumber(int versionNumber);

    byte[] toByteArray();
}
