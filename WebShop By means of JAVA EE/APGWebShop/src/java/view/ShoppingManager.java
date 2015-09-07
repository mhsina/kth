/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.*;
import model.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.*;

/**
 *
 * @author Sina
 */
@Named(value = "shoppingManager")
@SessionScoped
public class ShoppingManager implements Serializable {

    @EJB
    private WebShopFacade webshopfacade;
    private Exception transactionFailure;
    private String selectedGnome;
    private List<Inventory> Basket;
    private String gnomeNumber;
    private String gnomePrice;
    private String totalPrice = "0";
    private String message = null;
    private String strUserName = "";
    private String strErrMsg = "";
    private String gnomeName;
    private String selectedUser;
    private String gnomename2;
    private boolean newAccount = false;
    private boolean isUserAuthenticated = false;
    private boolean isUserAdmin = false;

    /**
     * Class constructor to create a new instance of ShoppingManager.
     */
    public ShoppingManager() {
        //isUserAuthenticated = false;
        Basket = new ArrayList<Inventory>();
    }

    /**
     * This is used to logout and destroy the session.

     */
    public void logout() {
        isUserAdmin = false;
    }

    /**
     * This is the constructor of the class and will initialise the variables
     * to the default values.

     */
    public void authenticateUser() {

        isUserAuthenticated = false;
        isUserAdmin = false;
        newAccount = false;

        if (webshopfacade.authenticateUser(strUserName, strPassword).equalsIgnoreCase("user")) {
            isUserAuthenticated = true;
            return;
        }
        if (webshopfacade.authenticateUser(strUserName, strPassword).equalsIgnoreCase("admin")) {
            isUserAdmin = true;
            return;
        }
    }

    /**
     * This method is used to set the <code>isUserAuthenticated</code> variable.

    @return      Return a boolean value to show the status of a user if he/she
     * is authenticated. 
     */
    public boolean getStatus() {
        return isUserAuthenticated;
    }

    /**
     * This method is used to retrieve the <code>isUserAdmin</code> variable.

    @return      Return the value of <code>isUserAdmin</code> variable
     *
     */
    public boolean getStatus2() {
        return isUserAdmin;
    }

    /**
     * This method is used to retrieve the <code>newAccount</code> variable.

    @return      Return the value of <code>newAccount</code> variable
     *
     */
    public boolean getRedirect() {
        return newAccount;
    }

    /**
     * This method is used to retrieve the <code>isUserAdmin</code> variable.

    @return      Return the value of <code>isUserAdmin</code> variable
     *
     */
    public boolean isIsUserAdmin() {
        return isUserAdmin;
    }

    /**
     * This method is used to set the <code>isUserAdmin</code> variable.

     */
    public void setIsUserAdmin(boolean isUserAdmin) {
        this.isUserAdmin = isUserAdmin;
    }

    /**
     * This method is used to retrieve the <code>gnomename2</code> variable.

    @return      Return the value of <code>gnomename2</code> variable
     *
     */
    public String getGnomename2() {
        return gnomename2;
    }

    /**
     * This method is used to set the <code>gnomename2</code> variable.

     */
    public void setGnomename2(String gnomename2) {
        this.gnomename2 = gnomename2;
    }

    /**
     * This method is used to retrieve the <code>selectedUser</code> variable.

    @return      Return the value of <code>selectedUser</code> variable
     *

     */
    public String getSelectedUser() {
        return selectedUser;
    }

    /**
     * This method is used to set the <code>selectedUser</code> variable.

     */
    public void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser;
    }

    /**
     * This method is used to retrieve the <code>gnomeName</code> variable.

    @return      Return the value of <code>gnomeName</code> variable
     *
     */
    public String getGnomeName() {
        return gnomeName;
    }

    /**
     * This method is used to set the <code>gnomeName</code> variable.

     */
    public void setGnomeName(String gnomeName) {
        this.gnomeName = gnomeName;
    }

    /**
     * This method is used to retrieve the <code>strErrMsg</code> variable.

    @return      Return the value of <code>strErrMsg</code> variable
     *

     */
    public String getStrErrMsg() {
        return strErrMsg;
    }

    /**
     * This method is used to set the <code>strErrMsg</code> variable.

     */
    public void setStrErrMsg(String strErrMsg) {
        this.strErrMsg = strErrMsg;
    }

    /**
     * This method is used to retrieve the <code>isUserAuthenticated</code> variable.
    
    @return      the status of user after authentication
     *
    
     */
    public boolean isIsUserAuthenticated() {
        return isUserAuthenticated;
    }

    /**
     * This method is used to set the <code>isUserAuthenticated</code> variable.

     */
    public void setIsUserAuthenticated(boolean isUserAuthenticated) {
        this.isUserAuthenticated = isUserAuthenticated;
    }

    /**
     * This method is used to retrieve the <code>newAccount</code> variable.

    @return      the status of user after creating an account
     *

     */
    public boolean isNewAccount() {
        return newAccount;
    }

    /**
     * This method is called when the user clicks on "Create new account" button
     * and as a result, the variables will be initialised.

     */
    public void createNewAccount() {
        newAccount = true;
        isUserAuthenticated = false;
        isUserAdmin = false;
    }

    /**
     * This method is used to set the <code>newAccount</code> variable.

     */
    public void setNewAccount(boolean newAccount) {
        this.newAccount = newAccount;
    }
    private boolean signout = false;

    /**
     * This method is used to retrieve the <code>signout</code> variable. Having
     * pressed the sign-out button, this variable will be evaluated.

    @return      the status of user if sign-out has been pressed
     *
     */
    public boolean isSignout() {
        return signout;
    }

    /**
     * This method is used to set the <code>signout</code> variable. Having
     * pressed the sign-out button, this variable will be initialised.

     */
    public void setSignout(boolean signout) {
        this.signout = signout;
    }

    /**
     * This method is used to retrieve the <code>signout</code> variable.

    @return      the status of user if the sign-out has been pressed
     *
     */
    public boolean getSignout() {
        return signout;
    }

    /**
     * This method is used to update the <code>signout</code> and
     * <code>isUserAuthenticated</code> variables to redirect user to the
     * login page.

     */
    public void signout() {
        signout = true;
        isUserAuthenticated = false;
    }

    /**
     * This method is used to set the <code>strPassword</code> variable.

     */
    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    /**
     * This method is used to set the <code>strUserName</code> variable.

     */
    public void setStrUserName(String strUserName) {
        this.strUserName = strUserName;
    }

    /**
     * This method is used to retrieve the <code>strPassword</code> variable.

    @return      the user password which has been entered on the page to be
     * sent to the authentication procedure.
     *
     */
    public String getStrPassword() {
        return strPassword;
    }

    /**
     * This method is used to retrieve the <code>strUserName</code> variable.

    @return      the username which has been entered on the page to be
     * sent to the authentication procedure.
     *
     */
    public String getStrUserName() {
        return strUserName;
    }
    private String strPassword = "";

    /**
     * This method is used to retrieve the <code>message</code> variable. This
     * message will notify the user about the errors in authentication or checking
     * out the basket.

    @return      the message to help and notify users as a feedback.
     */
    public String getMessage() {
        return message;
    }

    /**
     * This method is used to set the <code>message</code> variable. to show the
     * message to the user.

     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * This method is used to retrieve the <code>gnomeNumber</code> variable.

    @return      the number of gnome-items in the Database
     */
    public String getGnomeNumber() {
        return gnomeNumber;
    }

    /**
     * This method is used to set the <code>gnomeNumber</code> variable.

     */
    public void setGnomeNumber(String gnomeNumber) {
        this.gnomeNumber = gnomeNumber;
    }

    /**
     * This method is used to retrieve the <code>Basket</code> variable as a list
     * of selected items.

    @return      the list of selected items by the user in the basket.
     */
    public List<Inventory> getBasket() {
        return Basket;
    }

    /**
     * This method is used to set the <code>Basket</code> variable as a list
     * of selected items.

     */
    public void setBasket(List<Inventory> Basket) {
        this.Basket = Basket;
    }

    /**
     * This method is used to retrieve the <code>totalPrice</code> variable.

    @return      the total price of selected items in the basket

     *
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     * This method is used to set the <code>totalPrice</code> variable after
     * checking out the basket.

     */
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * This method is used to update the <code>message</code> variable after
     * checking out the basket to notify the user and empty the basket.

     */
    public void checkout() {
        message = "Purchase is approved";
        Basket.clear();
    }

    /**
     * Add the selected items to the basket and ready to check out.
     * 

     * This method will be called when the authenticated user has selected items
     * and wanted to add them to the basket. In this case, the items will be
     * ready to be checked out.
     *
     *

     */
    public void addtobasket() {
        if (Integer.valueOf(webshopfacade.getStock(selectedGnome)) >= Integer.valueOf(gnomeNumber)) {
            int price = Integer.valueOf(webshopfacade.getPrice(selectedGnome));
            price *= Integer.valueOf(gnomeNumber);
            totalPrice = String.valueOf(price + Integer.valueOf(totalPrice));
            Inventory e = new Inventory(selectedGnome, gnomeNumber, String.valueOf(price));
            Basket.add(e);
            webshopfacade.stockUpdate(selectedGnome, gnomeNumber);
        }
    }

    /**
     * This method is used to retrieve the items in stock via
     * <code>webshopfacade</code> class.

    @return      the list of items available in the Database via
     * <code>webshopfacade</code> class.

     */
    public List<Inventory> getItems() {
        return webshopfacade.getItems();
    }

    /**
     * This method is used to retrieve the gnome-name in stock via
     * <code>webshopfacade</code> class.

    @return      the list of item-names available in the Database via
     * <code>webshopfacade</code> class.
     */
    public List<String> getGnomes() {
        return webshopfacade.getGnomes();
    }

    /**
     * This method is used to retrieve the user-name in the Database via
     * <code>webshopfacade</code> class.

    @return      the list of users in the Database via
     * <code>webshopfacade</code> class.
     */
    public List<String> getUsers() {
        return webshopfacade.getUsers();
    }

    /**
     * This method is used to retrieve <code>selectedGnome</code> variable.

    @return      the selected gnome as a String value
     */
    public String getSelectedGnome() {
        return selectedGnome;
    }

    /**
     * This method is used to set <code>selectedGnome</code> variable.

     */
    public void setSelectedGnome(String selectedGnome) {
        this.selectedGnome = selectedGnome;
    }

    /**
     * This method is used to retrieve <code>transactionFailure</code> variable.

    @return      the exception occurred in Exception format to be user as a
     * notification to the user.

     */
    public Exception getTransactionFailure() {
        return transactionFailure;
    }

    /**
     * This method is used to set <code>transactionFailure</code> variable.

     */
    public void setTransactionFailure(Exception transactionFailure) {
        this.transactionFailure = transactionFailure;
    }

    /**
     * This method is used to retrieve <code>webshopfacade</code> variable.

    @return      the return value is a variable from <code>WebShopFacade</code>
     * type to use and call other functions in this class.

     */
    public WebShopFacade getWebshopfacade() {
        return webshopfacade;
    }

    /**
     * This method is used to set <code>webshopfacade</code> variable.

     */
    public void setWebshopfacade(WebShopFacade webshopfacade) {
        this.webshopfacade = webshopfacade;
    }

    /**
     *
     * Returns <code>false</code> if the latest transaction failed,
     * otherwise <code>false</code>.

     @return      the return value shows if the transaction has been failed.
     *
     */
    public boolean getSuccess() {
        return transactionFailure == null;
    }

    /**
     * Returns the latest thrown exception.

     @return      the most recently exception is returned in Exception format.
     */
    public Exception getException() {
        return transactionFailure;
    }

    /**
     * This method is used to retrieve <code>gnomePrice</code> variable.

    @return      the price of a specific item in the list of available items.
     */
    public String getGnomePrice() {
        return gnomePrice;
    }

    /**
     * This method is used to set <code>gnomePrice</code> variable.
    
     */
    public void setGnomePrice(String gnomePrice) {
        this.gnomePrice = gnomePrice;
    }

    /**
     * This method is used to add gnome items to the database via
     * <code>webshopfacade</code> class.

     */
    public void AddGnome() {
        webshopfacade.AddGnome(gnomeName, gnomeNumber, gnomePrice);
    }

    /**
     * This method is used to update gnome items specifications in the database.
     * The name, number and the price can be updated via
     * <code>webshopfacade</code> class.

     */
    public void UpdateList() {
        webshopfacade.UpdateList(gnomeName, gnomeNumber, gnomePrice);
    }

    /**
     * This method is used to ban a specific user in the Database via
     * <code>webshopfacade</code> class.

     */
    public void ban() {
        webshopfacade.ban(selectedUser);

    }

    /**
     * This method is used to unblock a specific user in the Database via
     * <code>webshopfacade</code> class.

     */
    public void unblock() {
        webshopfacade.unblock(selectedUser);

    }

    /**
     * This method is used to delete a specific gnome item in the Database via
     * <code>webshopfacade</code> class. 

     */
    public void DeleteGnome() {
        webshopfacade.DeleteGnome(gnomename2, gnomeNumber);
    }
}
