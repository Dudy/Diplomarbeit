/*
 * ChatInitMessageDeserializer.java
 *
 * Created on 10. Juli 2007, 12:44
 *
 */

package applicationSharing;

import java.io.IOException;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.MessageDeserializer;

/**
 *
 * @author podolak
 */
public class ApplicationSharingMessageDeserializer implements MessageDeserializer {

    private Endpoint endpoint;

    /** Creates a new instance of ChatInitMessageDeserializer
     * @param endpoint
     */
    public ApplicationSharingMessageDeserializer(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Message deserialize(InputBuffer buf, short type, int priority, NodeHandle sender) throws IOException {
        Message message = null;

        switch (type) {
            case IsCapableMessage.TYPE:
                message = new IsCapableMessage(sender.getId(), buf, endpoint);
                break;
            case AmCapableMessage.TYPE:
                message = new AmCapableMessage(sender.getId(), buf, endpoint);
                break;
            case RequestExecutionMessage.TYPE:
                message = new RequestExecutionMessage(sender.getId(), buf, endpoint);
                break;
            case ExecutionFinishedMessage.TYPE:
                message = new ExecutionFinishedMessage(sender.getId(), buf, endpoint);
                break;
        }

        // throw IOException ?
        return message;
    }
}