package p2p_vcs_client.implementation.document;

import java.nio.ByteBuffer;
import p2p_vcs_client.Metamodel;

/**
 *
 * @author podolak
 */
public class MetamodelFactory {

    private static MetamodelFactory instance;

    protected MetamodelFactory() {
    }

    public static MetamodelFactory getInstance() {
        if (instance == null) {
            instance = new MetamodelFactory();
        }

        return instance;
    }

    public Metamodel metamodelFromByteArray(byte[] byteArray) {
        Metamodel metamodel = new MetamodelImplementation();

        if (byteArray != null && byteArray.length > 0) {
            ByteBuffer bb = ByteBuffer.wrap(byteArray);

            int size = bb.getInt();

            char[] contentAsCharArray = new char[size];

            for (int i = 0; i < size; i++) {
                contentAsCharArray[i] = bb.getChar();
            }

            metamodel.setContent(new String(contentAsCharArray));
        }

        return metamodel;
    }
}
