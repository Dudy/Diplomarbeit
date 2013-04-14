/*
 * Command.java
 *
 * Created on 08.10.2007, 16:15:17
 *
 */

package applicationSharing;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.OutputBuffer;

/**
 *
 * @author podolak
 */
public class Command implements Serializable {

    private long UID;
    private String command;
    private String[] parameter;

    /**
     * Creates an empty Command object. The command string is the empty string and the parameter array is of length zero.
     */
    public Command() {
        UID = new Random().nextLong();
        command = "";
        parameter = new String[0];
    }

    /**
     * Creates a Command object from a command line. The line contains at least a single command, which is parsed into the local command string and an
     * arbitrary number of optional arguments that are stored in the parameter array.
     * @param commandLine
     */
    public Command(String commandLine) {
        UID = new Random().nextLong();
        String[] commandAndParameter = commandLine.split(" ");
        command = commandAndParameter[0];
        parameter = new String[commandAndParameter.length - 1];
        System.arraycopy(commandAndParameter, 1, parameter, 0, parameter.length);
    }

    /**
     * Creates a Command object with the given command string and parameter array.
     * @param command
     * @param parameter
     */
    public Command(String command, String[] parameter) {
        UID = new Random().nextLong();
        this.command = command;
        this.parameter = parameter;
    }

    /**
     * Creates a Command object and read it's values from the InputStream.
     * @param buf
     * @throws java.io.IOException
     */
    public Command(InputBuffer buf) throws IOException {
        UID = buf.readLong();
        command = buf.readUTF();

        int len = buf.readInt();
        parameter = new String[len];
        for (int i = 0; i < len; i++) {
            parameter[i] = buf.readUTF();
        }
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getParameter() {
        return parameter;
    }

    public void setParameter(String[] parameter) {
        this.parameter = parameter;
    }

    public void serialize(OutputBuffer buf) throws IOException {
        buf.writeLong(UID);
        buf.writeUTF(command);
        buf.writeInt(parameter.length);

        for (String param : parameter) {
            buf.writeUTF(param);
        }
    }

    public String getCommandLine() {
        StringBuilder commandLine = new StringBuilder(command);

        for (int i = 0; i < parameter.length; i++) {
            commandLine.append(" " + parameter[i]);
        }


        return commandLine.toString();
    }

    public long getUID() {
        return UID;
    }

    public void setUID(long UID) {
        this.UID = UID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("command: " + command + "\n");
        
        for (int i = 0; i < parameter.length; i++) {
            sb.append("Parameter " + i + ": " + parameter[i] + "\n");
        }
        
        return sb.toString();
    }
}