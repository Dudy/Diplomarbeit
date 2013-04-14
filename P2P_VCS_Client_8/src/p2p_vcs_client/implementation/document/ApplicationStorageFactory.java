package p2p_vcs_client.implementation.document;

import p2p_vcs_client.ApplicationStorage;

/**
 *
 * @author podolak
 */
public class ApplicationStorageFactory {
    private static ApplicationStorageFactory instance;

    protected ApplicationStorageFactory() {
    }
    
    public static ApplicationStorageFactory getInstance() {
        if (instance == null) {
            instance = new ApplicationStorageFactory();
        }
        
        return instance;
    }
    
    public ApplicationStorage applicationStorageFromByteArray(byte[] byteArray) {
        ApplicationStorage applicationStorage = new ApplicationStorageImplementation();
        
        if (byteArray != null && byteArray.length > 0) {
            // if this class gets some attributes, deserialize them here
        }
        
        return applicationStorage;
    }
}
