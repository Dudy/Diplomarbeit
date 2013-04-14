package p2p_vcs_client.message;

import java.io.IOException;
import p2p_vcs_client.Document;
import p2p_vcs_client.implementation.SerializationUtilities;
import p2p_vcs_client.implementation.document.DocumentFactory;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.OutputBuffer;

/**
 *
 * @author podolak
 */
public class CheckoutResponseMessage extends CommandMessage {
    private Document document;

    public CheckoutResponseMessage(NodeHandle sender, Id vcsId) {
        super(sender, vcsId, MessageType.CHECKOUT_RESPONSE);
    }
    
    public CheckoutResponseMessage(NodeHandle sender, Id vcsId, Document document) {
        super(sender, vcsId, MessageType.CHECKOUT_RESPONSE);
        this.document = document;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" serialization ">
    public CheckoutResponseMessage(NodeHandle sender, InputBuffer buf, Endpoint endpoint) throws IOException {
        super(sender, buf, endpoint);
        document = DocumentFactory.getInstance().documentFromByteArray(SerializationUtilities.getByteArray(buf));
        
//        int size = buf.readInt();
//        
//        if (size == 0) {
//            document = null;
//        } else {
//            byte[] documentAsByteArray = new byte[size];
//            buf.read(documentAsByteArray);
//
//            document = DocumentFactory.getInstance().documentFromByteArray(documentAsByteArray);
//            
////            short type = buf.readShort();
////        
////            if (type == 0) {
////                document.setId(null);
////            } else {
////                document.setId(endpoint.readId(buf, type));
////            }
//        }
    }

    @Override
    public void serialize(OutputBuffer buf) throws IOException {
        super.serialize(buf);
        
        if (document == null) {
            SerializationUtilities.putByteArray(buf, null);
        } else {
            SerializationUtilities.putByteArray(buf, document.toByteArray());
        }
        
        
//        if (document == null) {
//            buf.writeInt(0);
//        } else {
//            byte[] documentAsByteArray = document.toByteArray();
//
//            buf.writeInt(documentAsByteArray.length);
//            buf.write(documentAsByteArray, 0, documentAsByteArray.length);
//            
////            if (document.getId() == null) {
////                buf.writeShort((short)0);
////            } else {
////                buf.writeShort(document.getId().getType());
////                document.getId().serialize(buf);
////            }
//        }
    }
    // </editor-fold> // serialization
    
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
    
    @Override
    public String toString() {
        return
                "CheckoutResponseMessage:\n" +
                document +
                "\n";
    }
}
