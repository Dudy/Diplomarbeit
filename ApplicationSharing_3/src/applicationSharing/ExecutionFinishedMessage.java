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
public class ExecutionFinishedMessage extends ApplicationSharingAbstractMessage {

    protected static final short TYPE = 4;
    
    private Command command;
    private String output;

    public ExecutionFinishedMessage(Id sender, Command command, String output) {
        super(sender);
        
        this.command = command;
        this.output = output;
    }
    
    public ExecutionFinishedMessage(Id sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender);
        
        command = new Command(buf);
        output = buf.readUTF();
    }

    public short getType() {
        return TYPE;
    }

    public void serialize(OutputBuffer buf) throws IOException {
        command.serialize(buf);
        buf.writeUTF(output);
    }

    @Override
    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}