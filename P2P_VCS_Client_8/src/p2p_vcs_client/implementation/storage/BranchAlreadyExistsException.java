/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package p2p_vcs_client.implementation.storage;

/**
 *
 * @author podolak
 */
public class BranchAlreadyExistsException extends Exception {

    /**
     * Creates a new instance of <code>BranchAlreadyExistsException</code> without detail message.
     */
    public BranchAlreadyExistsException() {
    }


    /**
     * Constructs an instance of <code>BranchAlreadyExistsException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BranchAlreadyExistsException(String msg) {
        super(msg);
    }
}
