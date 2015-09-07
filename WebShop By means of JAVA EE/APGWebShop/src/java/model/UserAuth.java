/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Mohammad
 */
@Entity
public class UserAuth implements Serializable, UserAuthDTO {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String password;
    private boolean adminuser = false;

    /**
     * This method is the default constructor of UserAuth class.

     *
     */
    public UserAuth() {
    }

    /**
     * This is the constructor of UserAuth class with four parameters.


    @param  username  the username/id of users in the table
     *

    @param  password  the password of users in the table
     *

    @param  bAdmin  the flag to show if a user belongs to admin group
     *

    @param  bBanned  the flag to show if a user belongs to access-banned group
     *

     *
     */
    public UserAuth(String username, String password, boolean bAdmin, boolean bBanned) {
        this.id = username;
        this.password = password;
        this.adminuser = bAdmin;
        this.banned = bBanned;
    }

    /**
     * This method is used to retrieve <code>id</code> variable.

    @return      the id field of the table
     *
     */
    public String getId() {
        return id;
    }

    /**
     * This method is used to set <code>id</code> variable.

     *
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method is used to retrieve <code>password</code> variable.

    @return      the password field of the table
     *
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method is used to set <code>password</code> variable.

    @return      the password field of the table
     *
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method is used to retrieve <code>adminuser</code> variable.

    @return      the adminuser field of the table
     *
     */
    public boolean isAdminuser() {
        return adminuser;
    }

    /**
     * This method is used to set <code>adminuser</code> variable.

    @return      the adminuser field of the table
     *
     */
    public void setAdminuser(boolean adminuser) {
        this.adminuser = adminuser;
    }

    /**
     * This method is used to retrieve <code>banned</code> variable.

    @return      the banned field of the table
     *
     */
    public boolean isBanned() {
        return banned;
    }

    /**
     * This method is used to set <code>banned</code> variable.

    @return      the banned field of the table
     *
     */
    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    private boolean banned = false;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserAuth)) {
            return false;
        }
        UserAuth other = (UserAuth) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.UserAuth[id=" + id + "]";
    }
}
