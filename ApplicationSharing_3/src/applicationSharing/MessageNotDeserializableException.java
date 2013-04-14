/*
 * MessageNotDeserializableException.java
 * 
 * Created on 07.10.2007, 16:21:32
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package applicationSharing;

/**
 *
 * @author podolak
 */
public class MessageNotDeserializableException extends Exception {

    /**
     * Creates a new instance of <code>MessageNotDeserializableException</code> without detail message.
     */
    public MessageNotDeserializableException() {
    }


    /**
     * Constructs an instance of <code>MessageNotDeserializableException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public MessageNotDeserializableException(String msg) {
        super(msg);
    }
}
