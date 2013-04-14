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
public class AmCapableMessage extends ApplicationSharingAbstractMessage {

    protected static final short TYPE = 2;
    private boolean capable = false;
    private Command command;
    
    /**
     *
     * @param capable
     * @param sender
     * @param command
     */
    public AmCapableMessage(boolean capable, Id sender, Command command) {
        super(sender);

        this.capable = capable;
        this.command = command;
    }

    public AmCapableMessage(Id sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender);
        
        capable = buf.readBoolean();
        command = new Command(buf);
    }

    public short getType() {
        return TYPE;
    }

    public void serialize(OutputBuffer buf) throws IOException {
        buf.writeBoolean(capable);
        command.serialize(buf);
    }

    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public Command getCommand() {
        return command;
    }

    public boolean isCapable() {
        return capable;
    }
}