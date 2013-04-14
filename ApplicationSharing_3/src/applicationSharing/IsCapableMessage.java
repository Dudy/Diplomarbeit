/*
 * IsCapableMessage.java
 *
 * Created on 07.10.2007, 16:10:56
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package applicationSharing;

import java.io.IOException;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.OutputBuffer;

/**
 *
 * @author podolak
 */
//public class IsCapableMessage implements RawMessage {
public class IsCapableMessage extends ApplicationSharingAbstractMessage {

    protected static final short TYPE = 1;
    private Command command;

    public IsCapableMessage(Id sender, Command command) {
        super(sender);

        this.command = command;
    }

    public IsCapableMessage(Id sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender);

        command = new Command(buf);
    }

    public void serialize(OutputBuffer buf) throws IOException {
        command.serialize(buf);
    }

    public short getType() {
        return TYPE;
    }

    public Command getCommand() {
        return command;
    }
}