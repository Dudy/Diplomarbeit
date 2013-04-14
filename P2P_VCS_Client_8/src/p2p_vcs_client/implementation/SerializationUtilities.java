package p2p_vcs_client.implementation;

import java.io.IOException;
import java.nio.ByteBuffer;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.rawserialization.InputBuffer;
import rice.p2p.commonapi.rawserialization.OutputBuffer;

/**
 *
 * @author podolak
 */
public class SerializationUtilities {
    
    //TODO lasse überall auch null zu (oder empty string oder so)
    
    private static long counter;
    
    // <editor-fold defaultstate="collapsed" desc=" String ">
    public static String getString(ByteBuffer bb) {
        int size = bb.getInt();

        char[] stringAsCharArray = new char[size];

        for (int i = 0; i < size; i++) {
            stringAsCharArray[i] = bb.getChar();
        }

        return new String(stringAsCharArray);
    }

    public static void putString(ByteBuffer bb, String text) {
        bb.putInt(text.toCharArray().length);
        counter += 4;

        for (char c : text.toCharArray()) {
            bb.putChar(c);
            
            counter += 2;
        }
        
        //System.out.println("String written with " + Integer.toString(4 + 2 * text.toCharArray().length) + " byte length");
    }
    
    public static int sizeString(String text) {
        // four bytes for the size, two bytes for each character
        
        return
                Integer.SIZE / 8 +
                text.toCharArray().length * 2;
    }
    // </editor-fold> // String
    
    // <editor-fold defaultstate="collapsed" desc=" Integer ">
    public static Integer getInteger(ByteBuffer bb) {
        return bb.getInt();
    }
    
    public static Integer getInteger(InputBuffer buf) throws IOException {
        return buf.readInt();
    }
    
    public static void putInteger(ByteBuffer bb, Integer i) {
        bb.putInt(i);
        counter += 4;
        
        //System.out.println("Integer written with 4 byte length");
    }
    
    public static void putInteger(OutputBuffer out, Integer i) throws IOException {
        out.writeInt(i);
        counter += 4;
        //System.out.println("Integer written with 4 byte length");
    }
    
    public static int sizeInteger() {
        // four bytes
        
        return Integer.SIZE / 8;
    }
    // </editor-fold> // Integer
    
    // <editor-fold defaultstate="collapsed" desc=" Boolean ">
    public static Boolean getBoolean(ByteBuffer bb) {
        return bb.get() != 0;
    }

    public static void putBoolean(ByteBuffer bb, Boolean b) {
        if (b.booleanValue()) {
            bb.put((byte) 1);
        } else {
            bb.put((byte) 0);
        }
        
        counter += 1;
        //System.out.println("Boolean written with 1 byte length");
    }
    
    public static int sizeBoolean() {
        // one byte
        
        return 1;
    }
    // </editor-fold> // Boolean
    
    // <editor-fold defaultstate="collapsed" desc=" Byte Array ">
    public static byte[] getByteArray(ByteBuffer bb) {
        int size = bb.getInt();
        byte[] byteArray = new byte[size];

        bb.get(byteArray);

        return byteArray;
    }
    
    public static byte[] getByteArray(InputBuffer buf) throws IOException {
        int size = buf.readInt();
        byte[] byteArray = new byte[size];
        
        buf.read(byteArray);
        
        return byteArray;
    }

    public static void putByteArray(ByteBuffer bb, byte[] byteArray) {
        if (byteArray == null) {
            bb.putInt(0);
            counter += 4;
            //System.out.println("Byte array written with 4 byte length");
        } else {
            bb.putInt(byteArray.length);
            counter += 4;
            
//            //System.out.println(bb.position());
//            //System.out.println(bb.limit());
            
            bb.put(byteArray);
            counter += byteArray.length;
            //System.out.println("Byte array written with " + Integer.toString(4 + byteArray.length) + " byte length");
        }
    }
    
    public static void putByteArray(OutputBuffer out, byte[] byteArray) throws IOException {
        if (byteArray == null) {
            out.writeInt(0);
            counter += 4;
            //System.out.println("Byte array written with 4 byte length");
        } else {
            out.writeInt(byteArray.length);
            counter += 4;
            out.write(byteArray, 0, byteArray.length);
            counter += byteArray.length;
            //System.out.println("Byte array written with " + Integer.toString(4 + byteArray.length) + " byte length");
        }
    }
    
    public static int sizeByteArray(byte[] byteArray) {
        // four bytes for the size, one byte for each byte
        
        int size = 0;
        
        size += Integer.SIZE / 8;
        
        if (byteArray != null) {
            size += byteArray.length;
        }
        
        return size;
    }
    // </editor-fold> // Byte Array
    
    // <editor-fold defaultstate="collapsed" desc=" Id ">
    public static Id getId(ByteBuffer bb) {
        Id id = null;
        int size = bb.getInt();

        if (size > 0) {
            byte[] idByteArray = new byte[size];
            bb.get(idByteArray);
            id = rice.pastry.Id.build(idByteArray);
        }
        
        return id;
    }
    
    public static void putId(ByteBuffer bb, Id id) {
        if (id == null) {
            bb.putInt(0);
            counter += 4;
            //System.out.println("ID written with 4 byte length");
        } else {
            bb.putInt(id.getByteArrayLength());
            counter += 4;
            bb.put(id.toByteArray());
            counter += id.getByteArrayLength();
            //System.out.println("ID written with " + Integer.toString(4 + id.getByteArrayLength()) + " byte length");
        }
    }
    
    public static int sizeId(Id id) {
        // four bytes for the length, one byte for each byte
        
        int size = 0;
        
        size += Integer.SIZE / 8;
        
        if (id != null) {
            size += id.getByteArrayLength();
        }
        
        return size;
    }
    // </editor-fold> // Id
    
    public static long getCounter() {
        return counter;
    }
}
