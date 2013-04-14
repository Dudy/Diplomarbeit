package p2p_vcs_client.message;

import java.io.IOException;
import p2p_vcs_client.implementation.SerializationUtilities;
import p2p_vcs_client.implementation.storage.StorageManager;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.OutputBuffer;

/**
 *
 * @author podolak
 */
public class StorageManagerRequestMessage extends CommandMessage {
    private StorageManager storageManager;
    
    public StorageManagerRequestMessage(NodeHandle sender, Id vcsId, StorageManager storageManager) {
        super(sender, vcsId, MessageType.STORAGE_MANAGER_REQUEST);
        this.storageManager = storageManager;
    }
        
    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public StorageManagerRequestMessage(NodeHandle sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender, buf, endpoint);
        storageManager = StorageManager.fromByteArray(SerializationUtilities.getByteArray(buf));
    }

    @Override
    public void serialize(OutputBuffer buf) throws IOException {
        super.serialize(buf);
        SerializationUtilities.putByteArray(buf, storageManager.toByteArray());
    }
    // </editor-fold> // serialization
    
    @Override
    public String toString() {
        String text =
                super.toString() + "; " +
                "StorageManagerRequestMessage";
        
        return text;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }
}
