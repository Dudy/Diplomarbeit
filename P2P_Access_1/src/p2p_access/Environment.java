package p2p_access;

import rice.environment.logging.Logger;
import rice.environment.params.Parameters;

/**
 *
 * @author podolak
 */
public class Environment extends rice.environment.Environment {

    private static Environment instance;

    protected Environment() {
        // disable the UPnP setting (in case you are testing this on a NATted LAN)
        getParameters().setString("nat_search_policy", "never");
        getParameters().setInt("p2p_replication_maintenance_interval", 10000);   // in milliseconds

        // set log level
        getParameters().setInt("loglevel", Logger.FINER);
    }

    public static Environment getInstance() {
        if (instance == null) {
            instance = new Environment();
        }

        return instance;
    }

    private void test() {
        Parameters p = getParameters();

        System.out.println(p);

        int MAINTENANCE_INTERVAL = p.getInt("p2p_replication_maintenance_interval");
        int MAX_KEYS_IN_MESSAGE = p.getInt("p2p_replication_max_keys_in_message");

        System.out.println(MAINTENANCE_INTERVAL);
        System.out.println(MAX_KEYS_IN_MESSAGE);
    }
}
