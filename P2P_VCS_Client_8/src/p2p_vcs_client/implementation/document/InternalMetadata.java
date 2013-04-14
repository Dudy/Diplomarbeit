package p2p_vcs_client.implementation.document;

import java.util.HashMap;

/**
 *
 * @author podolak
 */
public class InternalMetadata extends HashMap<String, Object> {

    @Override
    public boolean equals(Object o) {
        //return super.equals(o);
        return o instanceof InternalMetadata && ((InternalMetadata)o).hashCode() == hashCode();
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        
        for (String key : keySet()) {
            Object o = get(key);
            
            switch (PropertyType.fromClass(o.getClass())) {
                case BOOLEAN:
                case INTEGER:
                case STRING:
                    hash += o.hashCode();
                    break;
                case BYTE_ARRAY:
                    byte[] arr = (byte[])o;
                    hash += 31 * arr.length;
                    for (int i = 0; i < arr.length; i++) {
                        hash += 31 * i + arr[i];
                    }
                    break;
            }
        }
        
        return hash;
    }
    
    public enum PropertyType {

        INVALID(0), STRING(1), INTEGER(2), BOOLEAN(3), BYTE_ARRAY(4);
        private int constant;

        private PropertyType(int constant) {
            this.constant = constant;
        }

        public int getConstant() {
            return constant;
        }

        public static PropertyType fromConstant(int constant) {
            PropertyType propertyType = INVALID;

            for (PropertyType type : values()) {
                if (constant == type.ordinal()) {
                    propertyType = type;
                    break;
                }
            }

            return propertyType;
        }
        
        public static PropertyType fromClass(Class T) {
            PropertyType type = INVALID;
            
            if (T == String.class) {
                type = STRING;
            } else if (T == Integer.class) {
                type = INTEGER;
            } else if (T == Boolean.class) {
                type = BOOLEAN;
            } else if (T == byte[].class) {
                type = BYTE_ARRAY;
            }
            
            return type;
        }
    }
}
