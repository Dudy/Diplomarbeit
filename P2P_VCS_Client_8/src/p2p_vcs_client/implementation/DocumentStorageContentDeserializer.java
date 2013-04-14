/*
 * DocumentStorageContentDeserializer.java
 *
 * Created on 10. Juli 2007, 12:44
 *
 */
package p2p_vcs_client.implementation;

import java.io.IOException;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.past.PastContent;
import rice.p2p.past.rawserialization.PastContentDeserializer;

/**
 *
 * @author podolak
 */
public class DocumentStorageContentDeserializer implements PastContentDeserializer {

    public PastContent deserializePastContent(InputBuffer buf, Endpoint endpoint, short contentType) throws IOException {
//        PastContent content = new MyPastContent(buf, endpoint);
//
//        return content;
        
        return null;
    }
}
