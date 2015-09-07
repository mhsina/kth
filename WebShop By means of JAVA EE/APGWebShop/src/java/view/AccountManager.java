/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.WebShopFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.*;

@Named(value = "accountManager")
@SessionScoped
public class AccountManager implements Serializable {

    @EJB
    private WebShopFacade webshopfacade;
    private String strUserName = "";
    private String strPassword = "";
    private String strConfirmPassword = "";
    private String strNewAccount = "";
    private boolean bCancelPressed = false;

    /**
     * This method is used to set <code>bCancelPressed</code> variable.

     */
    public void setCancel() {
        bCancelPressed = true;
    }

    /**
     * This method is used to retrieve <code>bCancelPressed</code> variable. This
     * method is called in the faces-config file while the cancel is pressed.

    @return      the status of cancel button if it is pressed to be redirected
     * to the login page. 
     *
     */
    public boolean getCancel() {
        return bCancelPressed;
    }

    /**
     * This method is used to retrieve <code>bCancelPressed</code> variable.

    @return      the status of cancel button if it is pressed to be redirected
     * to the login page.
     *
     */
    public boolean isbCancelPressed() {
        return bCancelPressed;
    }

    /**
     * Update the value of bCancelPressed variable when the cancel button presses.
     * 

     * This method updates the value of bCancelPressed when this button is pressed
     * in the "create new account" page and enforce the web page to be return
     * to the "login" page.
     *

    @param  bCancelPressed  The status of bCancelPressed will be updated
     *
     */
    public void setbCancelPressed(boolean bCancelPressed) {
        this.bCancelPressed = bCancelPressed;
    }

    /**
     * This method is used to retrieve <code>strNewAccount</code> variable.

    @return      the name of recent account.
     *
     */
    public String getStrNewAccount() {
        return strNewAccount;
    }

    /**
     * This method is used to set <code>strNewAccount</code> variable.

     */
    public void setStrNewAccount(String strNewAccount) {
        this.strNewAccount = strNewAccount;
    }

    /**
     * This method is used to retrieve <code>webshopfacade</code> variable.

    @return      the variable of WebShopFacade class to be used and call the
     * required functions.
     *
     */
    public WebShopFacade getWebshopfacade() {
        return webshopfacade;
    }

    /**
     * This method is used to set <code>webshopfacade</code> variable.

     *
     */
    public void setWebshopfacade(WebShopFacade webshopfacade) {
        this.webshopfacade = webshopfacade;
    }
    private boolean successfullyCreated = false;

    /**
     * This method is used to retrieve <code>successfullyCreated</code> variable.

    @return      the status of recently created account and returns true if the
     * account has been successfully created and false in the case of error or
     * failure in transaction.
     *
     */
    public boolean getRedirect() {
        return successfullyCreated;
    }

    /**
     * This method is used to create a new account in the Database. It will
     * automatically check the entered password with confirmed password field
     * in the create new account page.

     *
     */
    public void createNewAccount() {
        if (strPassword.compareTo(strConfirmPassword) == 0) {
            successfullyCreated = true;
            addUser();
        } else {
        }
    }

    /**
     * This method is used to retrieve <code>successfullyCreated</code> variable.

    @return      the status of recently created user account.
     *
     */
    public boolean isSuccessfullyCreated() {
        return successfullyCreated;
    }

    /**
     * This method is used to set <code>successfullyCreated</code> variable.

     *
     */
    public void setSuccessfullyCreated(boolean successfullyCreated) {
        this.successfullyCreated = successfullyCreated;
    }

    /**
     * This method is used to set <code>strConfirmPassword</code> variable.

     *
     */
    public void setStrConfirmPassword(String strConfirmPassword) {
        this.strConfirmPassword = strConfirmPassword;
    }

    /**
     * This method is used to retrieve <code>strConfirmPassword</code> variable.

    @return      the confirmed string value on the create new account form
     *
     */
    public String getStrConfirmPassword() {
        return strConfirmPassword;
    }

    /**
     * This method is used to set <code>strPassword</code> variable.

     *
     */
    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    /**
     * This method is used to set <code>strUserName</code> variable.

     *
     */
    public void setStrUserName(String strUserName) {
        this.strUserName = strUserName;
    }

    /**
     * This method is used to retrieve <code>strPassword</code> variable.

     @return      the entered password of user in create new account form
     *
     */
    public String getStrPassword() {
        return strPassword;
    }

    /**
     * This method is used to retrieve <code>strUserName</code> variable.

     @return      the entered username of user in create new account form
     *
     */
    public String getStrUserName() {
        return strUserName;
    }

    /**
     * This method is used to add a new user in the Database using username and
     * the password. The username will be check and the user will be informed
     * in the username exists. This method call <code>addUser</code> function via
     * <code>webshopfacade</code>.

     *
     */
    public void addUser() {
        webshopfacade.addUser(strUserName, strPassword);
    }
}
