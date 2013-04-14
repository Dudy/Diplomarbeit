/*
 * DocumentStorageMessageDeserializer.java
 *
 * Created on 10. Juli 2007, 12:44
 *
 */

package p2p_vcs_client.implementation;

import java.io.IOException;
import p2p_vcs_client.message.CheckoutRequestMessage;
import p2p_vcs_client.message.CheckoutResponseMessage;
import p2p_vcs_client.message.CommandMessage;
import p2p_vcs_client.message.CommitMessage;
import p2p_vcs_client.message.CommitSuccessfulMessage;
import p2p_vcs_client.message.FindNodeResponseMessage;
import p2p_vcs_client.message.MessageType;
import p2p_vcs_client.message.StorageManagerRequestMessage;
import p2p_vcs_client.message.StorageManagerResponseMessage;
import p2p_vcs_client.message.TagMessage;
import p2p_vcs_client.message.TagsRequestMessage;
import p2p_vcs_client.message.TagsResponseMessage;
import p2p_vcs_client.message.UpdateValidRequestMessage;
import p2p_vcs_client.message.UpdateValidResponseMessage;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.MessageDeserializer;

/**
 *
 * @author podolak
 */
public class DocumentStorageMessageDeserializer implements MessageDeserializer {

    private Endpoint endpoint;

    /**
     * Creates a new instance of DocumentStorageMessageDeserializer.
     * 
     * @param endpoint
     */
    public DocumentStorageMessageDeserializer(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Message deserialize(InputBuffer buf, short type, int priority, NodeHandle sender) throws IOException {
        Message message = null;

        switch (MessageType.fromType(type)) {
            case CHECKOUT_REQUEST:
                message = new CheckoutRequestMessage(sender, buf, endpoint);
                break;
            case CHECKOUT_RESPONSE:
                message = new CheckoutResponseMessage(sender, buf, endpoint);
                break;
            case STORAGE_MANAGER_REQUEST:
                message = new StorageManagerRequestMessage(sender, buf, endpoint);
                break;
            case STORAGE_MANAGER_RESPONSE:
                message = new StorageManagerResponseMessage(sender, buf, endpoint);
                break;
            case COMMIT:
                message = new CommitMessage(sender, buf, endpoint);
                break;
            case COMMIT_SUCCESSFUL:
                message = new CommitSuccessfulMessage(sender, buf, endpoint);
                break;
            case DOCUMENT_VERSION_OUTDATED_ERROR:
            case WRONG_NODE_ERROR:
            case FIND_NODE_REQUEST:
                message = new CommandMessage(sender, buf, endpoint);
                break;
            case IS_UPDATE_VALID:
                message = new UpdateValidRequestMessage(sender, buf, endpoint);
                break;
            case NONE:
                break;
            case UPDATE_IS_VALID:
                message = new UpdateValidResponseMessage(sender, buf, endpoint);
                break;
            case FIND_NODE_RESPONSE:
                message = new FindNodeResponseMessage(sender, buf, endpoint);
                break;
            case TAG:
                message = new TagMessage(sender, buf, endpoint);
                break;
            case TAGS_REQUEST:
                message = new TagsRequestMessage(sender, buf, endpoint);
                break;
            case TAGS_RESPONSE:
                message = new TagsResponseMessage(sender, buf, endpoint);
                break;
        }

        // throw IOException ?
        return message;
    }
}