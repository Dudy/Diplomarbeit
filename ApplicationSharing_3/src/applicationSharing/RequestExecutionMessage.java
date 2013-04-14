/*
 * AmCapableMessage.java
 *
 * Created on 07.10.2007, 16:15:49
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package applicationSharing;

import java.io.IOException;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.OutputBuffer;

/**
 *
 * @author podolak
 */
public class RequestExecutionMessage extends ApplicationSharingAbstractMessage {

    protected static final short TYPE = 3;
    private Command command;

    /**
     * @param sender
     * @param command
     * @param arguments
     */
    public RequestExecutionMessage(Id sender, Command command) {
        super(sender);

        this.command = command;
    }

    /**
     *
     * @param sender
     * @param buf
     * @param endpoint
     * @throws java.io.IOException
     */
    public RequestExecutionMessage(Id sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender);

        command = new Command(buf);
    }

    public short getType() {
        return TYPE;
    }

    public void serialize(OutputBuffer buf) throws IOException {
        command.serialize(buf);
    }

    @Override
    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public Command getCommand() {
        return command;
    }
}