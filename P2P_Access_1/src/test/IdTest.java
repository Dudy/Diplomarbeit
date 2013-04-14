package test;


import java.util.Random;
import p2p_access.P2PAccess;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.IdFactory;

/**
 *
 * @author podolak
 */
public class IdTest {

    public IdTest() {
        IdFactory idFactory = P2PAccess.getIdFactory();
        Id id = idFactory.buildRandomId(new Random());
        System.out.println("id: " + id);
        System.out.println("id.getByteArrayLength(): " + id.getByteArrayLength());
        System.out.println("id.toByteArray(): " + id.toByteArray());
        System.out.println("id.toByteArray().length: " + id.toByteArray().length);
        System.out.println("id.toByteArray().length * 8: " + id.toByteArray().length * 8);
        System.out.println("id.toStringFull(): " + id.toStringFull());
    }

    public static void main(String[] args) {
        new IdTest();
    }
}
