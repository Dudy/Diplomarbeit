/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package p2p_vcs_client;

/**
 *
 * @author podolak
 */
public interface Metamodel {

    String getContent();

    void setContent(String content);

    byte[] toByteArray();

}
