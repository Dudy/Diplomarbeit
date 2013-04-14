package p2p_vcs_client.implementation.document;

import p2p_vcs_client.Metadata;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Set;
import p2p_vcs_client.implementation.SerializationUtilities;
import p2p_vcs_client.implementation.document.InternalMetadata.PropertyType;

/**
 *
 * @author podolak
 */
public class MetadataImplementaion implements Serializable, Metadata {

    //TODO Die Lösung mit dem type gefällt mir nicht. Was anderes überlegen.
    protected int versionNumber = 0;
    protected String type = "";
    //TODO move version number and type to properties
    private InternalMetadata properties = new InternalMetadata();

    public static Metadata metadataFromByteArray(byte[] byteArray) {
        Metadata metadata = new MetadataImplementaion();

        if (byteArray != null && byteArray.length > 0) {
            ByteBuffer bb = ByteBuffer.wrap(byteArray);

            // version number
            metadata.setVersionNumber(SerializationUtilities.getInteger(bb));

            // type
            metadata.setType(SerializationUtilities.getString(bb));

            // properties
            int propertyNumber = SerializationUtilities.getInteger(bb);

            for (int i = 0; i < propertyNumber; i++) {
                // key
                String key = SerializationUtilities.getString(bb);

                // value
                PropertyType propertyType = PropertyType.fromConstant(SerializationUtilities.getInteger(bb));
                Object value = null;

                switch (propertyType) {
                    case STRING:
                        value = SerializationUtilities.getString(bb);
                        break;
                    case INTEGER:
                        value = SerializationUtilities.getInteger(bb);
                        break;
                    case BOOLEAN:
                        value = SerializationUtilities.getBoolean(bb);
                        break;
                    case BYTE_ARRAY:
                        value = SerializationUtilities.getByteArray(bb);
                        break;
                    case INVALID:
                    default:
                        // noop, should never occur
                        break;
                }

                metadata.setProperty(key, value);
            }
        }

        return metadata;
    }

    public byte[] toByteArray() {
        // <editor-fold defaultstate="collapsed" desc=" determine size ">
        int size = 0;

        // version number
        size += Integer.SIZE / 8;

        // size of content, four bytes
        size += Integer.SIZE / 8;

        // content, two bytes per character
        size += type.toCharArray().length * 2;

        // properties
        // number of properties
        size += Integer.SIZE / 8;

        // properties
        for (String key : (Set<String>) properties.keySet()) {
            // key
            size += Integer.SIZE / 8;
            size += key.toCharArray().length * 2;

            // value
            // type
            size += Integer.SIZE / 8;

            // content
            Object valueObject = properties.get(key);

            if (valueObject instanceof String) {
                size += Integer.SIZE / 8;
                size += ((String) valueObject).toCharArray().length * 2;
            } else if (valueObject instanceof Integer) {
                size += Integer.SIZE / 8;
            } else if (valueObject instanceof Boolean) {
                size += 1;
            } else if (valueObject instanceof byte[]) {
                size += Integer.SIZE / 8;
                size += ((byte[]) valueObject).length;
            }
        }

        // sizes of additional attributes
        // </editor-fold> // determine size

        ByteBuffer bb = ByteBuffer.allocate(size);
        SerializationUtilities.putInteger(bb, versionNumber);
        SerializationUtilities.putString(bb, type);

        // properties
        SerializationUtilities.putInteger(bb, properties.keySet().size());

        for (String key : (Set<String>) properties.keySet()) {
            // key
            SerializationUtilities.putString(bb, key);

            // value
            Object valueObject = properties.get(key);

            if (valueObject instanceof String) {
                bb.putInt(PropertyType.STRING.getConstant());
                SerializationUtilities.putString(bb, (String) valueObject);
            } else if (valueObject instanceof Integer) {
                bb.putInt(PropertyType.INTEGER.getConstant());
                SerializationUtilities.putInteger(bb, (Integer) valueObject);
            } else if (valueObject instanceof Boolean) {
                bb.putInt(PropertyType.BOOLEAN.getConstant());
                SerializationUtilities.putBoolean(bb, (Boolean) valueObject);
            } else if (valueObject instanceof byte[]) {
                bb.putInt(PropertyType.BYTE_ARRAY.getConstant());
                SerializationUtilities.putByteArray(bb, (byte[]) valueObject);
            } else {
                bb.putInt(PropertyType.INVALID.getConstant());
            }
        }

        return bb.array();
    }

    @Override
    public String toString() {
        StringBuilder propertiesText = new StringBuilder();

        for (String s : properties.keySet()) {
            propertiesText.append("      (" + s + ":" + properties.get(s) + ")\n");
        }


        return "    Metadata\n" +
                "    [\n" +
                "      version number: " + versionNumber + "\n" +
                "      type          : " + type + "\n" +
                propertiesText.toString() +
                "    ]\n";
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + this.versionNumber;
        hash = 13 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 13 * hash + (this.properties != null ? this.properties.hashCode() : 0);
        return hash;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {

    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {

    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public HashMap<String, Object> getProperties() {
//        return properties;
//    }
//
//    public void setProperties(InternalMetadata properties) {
//        this.properties = properties;
//    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

//    public HashMap<String, Object> getPropertiesWithPrefix(String prefix) {
//        HashMap<String, Object> prefixedProperties = new HashMap<String, Object>();
//
//        for (String key : properties.keySet()) {
//            if (key.startsWith(prefix)) {
//                prefixedProperties.put(key, properties.get(key));
//            }
//        }
//
//        return prefixedProperties;
//    }
}
