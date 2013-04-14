package p2p_vcs_client;

import java.util.HashMap;

/**
 *
 * @author podolak
 */
public interface Metadata {

    String getType();

    int getVersionNumber();

    //HashMap<String, Object> getProperties();

    Object getProperty(String key);

    //HashMap<String, Object> getPropertiesWithPrefix(String prefix);

    void setType(String type);

    void setVersionNumber(int versionNumber);

    //void setProperties(HashMap<String, Object> properties);

    void setProperty(String key, Object value);

    byte[] toByteArray();
}
