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
import java.util.ArrayList;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.OutputBuffer;

/**
 *
 * @author podolak
 */
//public class IsCapableMessage implements RawMessage {
public class IsCapableMessage_bak extends ApplicationSharingAbstractMessage {

    protected static final short TYPE = 1;
    private String command;
    private String[] arguments;

    public IsCapableMessage_bak(Id sender, String command, String... arguments) {
        super(sender);
        
        this.command = command;
        this.arguments = arguments;
    }

    public IsCapableMessage_bak(Id sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender);
        int groesse = buf.readInt();

        command = buf.readUTF();
        arguments = new String[groesse];
        for (int i = 0; i < groesse; i++) {
            arguments[i] = buf.readUTF();
        }
    }

    public short getType() {
        return TYPE;
    }

    public void serialize(OutputBuffer buf) throws IOException {
        buf.writeInt(arguments.length);
        buf.writeUTF(command);

        for (String argument : arguments) {
            buf.writeUTF(argument);
        }
    }

    public String getCommand() {
        return command;
    }
    
    public String[] getArguments() {
        return arguments;
    }
}