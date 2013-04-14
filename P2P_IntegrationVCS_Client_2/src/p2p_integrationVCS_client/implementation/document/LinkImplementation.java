package p2p_integrationVCS_client.implementation.document;

import java.nio.ByteBuffer;
import p2p_integrationVCS_client.Link;
import rice.p2p.commonapi.Id;

/**
 *
 * @author podolak
 */
public class LinkImplementation extends DocumentImplementation implements Link {
    
    private Id source;
    private Id target;

    public LinkImplementation() {
        super();
    }

    public LinkImplementation(Id source, Id target) {
        super();
        this.source = source;
        this.target = target;
    }
    
    public Id getSource() {
        return source;
    }

    public Id getTarget() {
        return target;
    }

    public void setTarget(Id target) {
        this.target = target;
    }

    public void setSource(Id source) {
        this.source = source;
    }
    
    @Override
    public byte[] toByteArray() {
        int size = 0;
        byte[] parentByteArray = super.toByteArray();
        
        size += parentByteArray.length;
        size += Integer.SIZE / 8;
        size += source.getByteArrayLength();
        size += Integer.SIZE / 8;
        size += target.getByteArrayLength();
        
        ByteBuffer bb = ByteBuffer.allocate(size);
        
        bb.put(parentByteArray);
        
        if (source == null) {
            bb.putInt(0);
        } else {
            bb.putInt(source.getByteArrayLength());
            bb.put(source.toByteArray());
        }
        
        if (target == null) {
            bb.putInt(0);
        } else {
            bb.putInt(target.getByteArrayLength());
            bb.put(target.toByteArray());
        }
        
        return bb.array();
    }
}
