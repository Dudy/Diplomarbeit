/*
 * AplicationSharingNodeFactory.java
 *
 * Created on 2. Juli 2007, 14:56
 *
 */

package applicationSharing;

import java.io.IOException;
import java.net.InetAddress;
import rice.environment.Environment;
import rice.environment.logging.Logger;
import rice.pastry.NodeIdFactory;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;

/**
 *
 * @author podolak
 */
public class ApplicationSharingNodeFactory extends SocketPastryNodeFactory {

    private static Environment applicationSharingNodeFactoryEnvironment;
    private static NodeIdFactory nidFactory;
    static {
        applicationSharingNodeFactoryEnvironment = new Environment();
        applicationSharingNodeFactoryEnvironment.getParameters().setString("nat_search_policy", "never");
        //applicationSharingNodeFactoryEnvironment.getParameters().setInt("loglevel", Logger.ALL);
        //applicationSharingNodeFactoryEnvironment.getParameters().setBoolean("pastry_factory_multipleNodes", true);
        nidFactory = new RandomNodeIdFactory(applicationSharingNodeFactoryEnvironment);
    }

    /** Creates a new instance of AplicationSharingNodeFactory 
     * @param address 
     * @param bindport 
     * @throws java.io.IOException 
     */
    public ApplicationSharingNodeFactory(InetAddress address, int bindport) throws IOException {
        super(nidFactory, address, bindport, applicationSharingNodeFactoryEnvironment, null);
    }
}