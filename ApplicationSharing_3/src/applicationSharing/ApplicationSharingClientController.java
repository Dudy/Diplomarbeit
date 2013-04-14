/*
 * ApplicationSharingClient.java
 *
 * Created on 2. Juli 2007, 14:41
 *
 */

package applicationSharing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandleSet;
import rice.pastry.NodeHandle;
import rice.p2p.commonapi.RouteMessage;

/**
 *
 * @author podolak
 */
public class ApplicationSharingClientController extends Thread implements Application, ActionListener, KeyListener {

    private ApplicationSharingClientGUI gui;
    private PrintStream printStream;
    private Endpoint endpoint;
    private ApplicationSharingMessageDeserializer deserializer;
    private NodeHandle localNodeHandle;
    private String localName;
    private HashMap<Long, Command> commands;

    ApplicationSharingClientController(PrintStream printStream, NodeHandle localNodeHandle, String localName) {
        System.setOut(printStream);
        System.setErr(printStream);

        this.printStream = printStream;
        this.localNodeHandle = localNodeHandle;
        this.localName = localName;
        this.gui = new ApplicationSharingClientGUI(this);
        this.endpoint = localNodeHandle.getLocalNode().buildEndpoint(this, "ApplicationSharingClient");
        this.deserializer = new ApplicationSharingMessageDeserializer(this.endpoint);
        this.endpoint.setDeserializer(deserializer);
        this.endpoint.register();
        this.commands = new HashMap<Long, Command>();

        System.out.println("my name is " + localName);
        System.out.println("my ID is " + localNodeHandle.getId());
        System.out.println();
    }

    @Override
    public void run() {
        synchronized (localNodeHandle.getLocalNode()) {
            while (!localNodeHandle.getLocalNode().isReady() && !localNodeHandle.getLocalNode().joinFailed()) {
                try {
                    localNodeHandle.getLocalNode().wait(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                if (localNodeHandle.getLocalNode().joinFailed()) {
                    //throw new IOException("Could not join the FreePastry ring.  Reason: " + ego.getNode().joinFailedReason());
                    //TODO
                    System.exit(-1);
                }
            }

            this.gui.setVisible(true);
        }
    }

    public boolean forward(RouteMessage routeMessage) {
        return true;
    }

    public void deliver(Id id, Message message) {
        System.out.println(localName + ": deliver: message class = " + message.getClass());

        if (message != null && message instanceof ApplicationSharingAbstractMessage) {
            ApplicationSharingAbstractMessage applicationSharingAbstractMessage = (ApplicationSharingAbstractMessage)message;

            if (applicationSharingAbstractMessage.getSender() != localNodeHandle.getId()) {

                switch (applicationSharingAbstractMessage.getType()) {
                    case 1:
                        System.out.println(localName + ": IsCapableMessage von " + applicationSharingAbstractMessage.getSender());
                        testCapability((IsCapableMessage)applicationSharingAbstractMessage);
                        break;
                    case 2:
                        System.out.println(localName + ": AmCapableMessage von " + applicationSharingAbstractMessage.getSender());
                        sendRequest((AmCapableMessage)applicationSharingAbstractMessage);
                        break;
                    case 3:
                        System.out.println(localName + ": RequestExecutionMessage von " + applicationSharingAbstractMessage.getSender());
                        executeRequest((RequestExecutionMessage)applicationSharingAbstractMessage);
                        break;
                    case 4:
                        System.out.println(localName + ": ExecutionFinishedMessage von " + applicationSharingAbstractMessage.getSender());
//                        gui.addText("execution finished on node " + applicationSharingAbstractMessage.getSender());
//                        gui.addText("command output:");
//                        gui.addText(((ExecutionFinishedMessage)applicationSharingAbstractMessage).getOutput());
                        printExecutionOutput((ExecutionFinishedMessage)applicationSharingAbstractMessage);
                        break;
                    default:
                        System.out.println(localName + ": unknown message type von " + applicationSharingAbstractMessage.getSender());
                        break;
                }
            } else {
                System.out.println(localName + ": I don't ask myself :-)");
            }
        }
    }

    public void update(rice.p2p.commonapi.NodeHandle handle, boolean joined) {
        System.out.println("Update von " + localName);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(localName + " requests execution of command:\n'" + gui.getCommandLine() + "'\n");

        Command command = new Command(gui.getCommandLine());
        commands.put(command.getUID(), command);

        NodeHandleSet neighborhood = endpoint.neighborSet(5);
        for (int i = 0; i < 5; i++) {
            NodeHandle nodeHandle = (NodeHandle)neighborhood.getHandle(i);
            if (nodeHandle != null) {
                endpoint.route(nodeHandle.getId(), new IsCapableMessage(nodeHandle.getId(), command), nodeHandle);
            }
        }

        gui.addText(localName + ": " + gui.getCommandLine());
        gui.clearCommandLine();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
//        if (((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) == KeyEvent.CTRL_DOWN_MASK) && e.getKeyCode() == KeyEvent.VK_ENTER) {
//            actionPerformed(null);
//        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            actionPerformed(null);
        }
    }

    @Override
    public String toString() {
        return localName;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public NodeHandle getLocalNodeHandle() {
        return localNodeHandle;
    }

    public void setLocalNodeHandle(NodeHandle localNodeHandle) {
        this.localNodeHandle = localNodeHandle;
    }

    private void testCapability(IsCapableMessage isCapableMessage) {
        System.out.println(localName + ": send AmCapable(true) message to " + isCapableMessage.getSender());

        AmCapableMessage amCapableMessage = new AmCapableMessage(true, localNodeHandle.getId(), isCapableMessage.getCommand());

        endpoint.route(isCapableMessage.getSender(), amCapableMessage, null);
    }

    private void sendRequest(AmCapableMessage amCapableMessage) {
        System.out.println(localName + ": send request for execution message to " + amCapableMessage.getSender());

        RequestExecutionMessage requestExecutionMessage =
                new RequestExecutionMessage(localNodeHandle.getId(), amCapableMessage.getCommand());

        endpoint.route(amCapableMessage.getSender(), requestExecutionMessage, null);
    }

    private void executeRequest(RequestExecutionMessage requestExecutionMessage) {
        System.out.println(localName + ": execute request");
        System.out.println(localName + ": " + requestExecutionMessage.getCommand().getCommandLine());
        System.out.println(localName + ": from " + requestExecutionMessage.getSender());

        StringBuilder output = new StringBuilder("");

        System.out.println("execute: " + requestExecutionMessage.getCommand().getCommandLine());

        try {
            String line;
            
            String[] cmds = requestExecutionMessage.getCommand().getCommandLine().split(" ");
            
            for (int i = 0; i < cmds.length; i++) {
                System.out.println(i + ": " + cmds[i]);
            }
            
            Process p = Runtime.getRuntime().exec(cmds);
            //Process p = Runtime.getRuntime().exec(requestExecutionMessage.getCommand().getCommandLine());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                output.append(line + "\n");
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
            output.append(err.getClass() + ": " + err.getMessage());
        }

        ExecutionFinishedMessage executionFinishedMessage =
                new ExecutionFinishedMessage(localNodeHandle.getId(), requestExecutionMessage.getCommand(), output.toString());

        endpoint.route(requestExecutionMessage.getSender(), executionFinishedMessage, null);
    }

    private void printExecutionOutput(ExecutionFinishedMessage executionFinishedMessage) {
        if (commands.containsKey(executionFinishedMessage.getCommand().getUID())) {
            gui.addText(executionFinishedMessage.getOutput());
        } else {
            System.out.println("Command execution was not requested from this host!");
        }
    }
}