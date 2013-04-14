package p2p_access;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Properties;
import org.apache.log4j.Logger;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.IdFactory;
import rice.pastry.NodeHandle;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.commonapi.PastryIdFactory;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;

/**
 *
 * @author podolak
 */
public class P2PAccess {

    static Logger LOGGER = Logger.getLogger(P2PAccess.class);
    private static Environment environment = Environment.getInstance();
    private static PastryIdFactory idFactory;
    private Properties properties;
    private int bindport;
    private int bootport;
    private InetSocketAddress bootaddress;
    private int numberOfReplicas;
    private NodeIdFactory nodeIdFactory;
    private SocketPastryNodeFactory socketPastryNodeFactory;
    private NodeHandle bootHandle;
    private PastryNode node;

    public P2PAccess(Properties properties) throws UnknownHostException, IOException {
        this.properties = properties;

        bindport = Integer.parseInt(properties.getProperty("bindport"));
        bootport = Integer.parseInt(properties.getProperty("bootaddress.port"));
        bootaddress = new InetSocketAddress(InetAddress.getByName(
                properties.getProperty("bootaddress.ip")), bootport);
        numberOfReplicas = Integer.parseInt(properties.getProperty("numberOfReplicas"));

        nodeIdFactory = new RandomNodeIdFactory(environment);

        //TODO try catch to avoid using busy ports?
        try {
            socketPastryNodeFactory = new SocketPastryNodeFactory(nodeIdFactory, bindport, environment);
        } catch (IOException e) {
            bindport = findFreePort();
            socketPastryNodeFactory = new SocketPastryNodeFactory(nodeIdFactory, bindport, environment);
        }
        // a port of zero tells Java (java.net.ServerSocket) to use any free port, but SocketPastryNodeFactory can't handle this
        //socketPastryNodeFactory = new SocketPastryNodeFactory(nodeIdFactory, 0, environment);

        bootHandle = socketPastryNodeFactory.getNodeHandle(bootaddress);

        // create node
        node = socketPastryNodeFactory.newNode((rice.pastry.NodeHandle) bootHandle);

        synchronized (node) {
            try {
                while (!node.isReady() && !node.joinFailed()) {
                    node.wait(500);

                    // abort if can't join
                    if (node.joinFailed()) {
                        throw new IOException("Could not join the FreePastry ring.  Reason:" + getNode().joinFailedReason());
                    }

                }
            } catch (InterruptedException ex) {
                LOGGER.error("interrupted while creating node", ex);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" getter ">
    public int getBindport() {
        return bindport;
    }

    public int getBootport() {
        return bootport;
    }

    public static Environment getEnvironment() {
        return environment;
    }

    public NodeIdFactory getNodeIdFactory() {
        return nodeIdFactory;
    }

    public PastryNode getNode() {
        return node;
    }

    public Id getNodeId() {
        return node.getId();
    }

    public NodeHandle getNodeHandle() {
        return node.getLocalHandle();
    }

    public InetSocketAddress getBootaddress() {
        return bootaddress;
    }

    public int getNumberOfReplicas() {
        return numberOfReplicas;
    }

    public SocketPastryNodeFactory getSocketPastryNodeFactory() {
        return socketPastryNodeFactory;
    }

    public Endpoint getEndpoint(Application application, String instance) {
        return node.buildEndpoint(application, instance);
    }

    public static IdFactory getIdFactory() {
        if (idFactory == null) {
            idFactory = new PastryIdFactory(getEnvironment());
        }

        return idFactory;
    }
    // </editor-fold> // getter
    
    // <editor-fold defaultstate="collapsed" desc=" helper ">
    /**
     * Returns a free port.
     * Typically you pass a port number of zero to ServerSocket to let the VM detect a free port.
     * But as SocketPastryNodeFactory is able to be called with zero and to establish a connection
     * on a (something like) random free port, it internally stores and uses the given parameter
     * as a port (hence the number zero). It does not ask the ServerSocket for a port but relies
     * on the integrity of the parameter.
     * 
     * @return a free port
     * @throws java.io.IOException
     */
    public static int findFreePort() throws IOException {
        ServerSocket server = new ServerSocket(0);
        int port = server.getLocalPort();
        server.close();
        return port;
    }
    // </editor-fold> // helper
}
