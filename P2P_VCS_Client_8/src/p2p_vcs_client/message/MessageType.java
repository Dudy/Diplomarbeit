package p2p_vcs_client.message;

/**
 *
 * @author podolak
 */
public enum MessageType {
    // no valid message
    NONE,
    // validity test for updating
    IS_UPDATE_VALID,
    UPDATE_IS_VALID,
    DOCUMENT_VERSION_OUTDATED_ERROR,
    // checkout messages
    CHECKOUT_REQUEST,
    CHECKOUT_RESPONSE,
    STORAGE_MANAGER_REQUEST,
    STORAGE_MANAGER_RESPONSE,
    // commit
    COMMIT,
    COMMIT_SUCCESSFUL,
    // tagging
    TAG, TAGS_REQUEST, TAGS_RESPONSE,
    
    
    // testing
    FIND_NODE_REQUEST,
    FIND_NODE_RESPONSE,
    FIND_REPLICAS_RESPONSE,
    
    
    
    
    
    // errors
    WRONG_NODE_ERROR;
    
    public short getType() {
        return (short)this.ordinal();
    }
    
    public static MessageType fromType(short s) {
        MessageType messageType = MessageType.NONE;
        
        for (MessageType type : values()) {
            if (s == type.ordinal()) {
                messageType = type;
                break;
            }
        }
        
        return messageType;
    }
}
