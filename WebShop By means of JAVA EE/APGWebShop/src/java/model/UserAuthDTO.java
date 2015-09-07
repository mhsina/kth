/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Mohammad
 */
public interface UserAuthDTO {
    String getId();
    String getPassword();
    boolean isAdminuser();
    boolean isBanned();
}
