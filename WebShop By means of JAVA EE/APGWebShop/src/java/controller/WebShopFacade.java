/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import model.Inventory;
import model.InventoryDTO;
import model.UserAuth;
import model.UserAuthDTO;

/**
 *
 * @author Sina
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class WebShopFacade {

    @PersistenceContext(unitName = "APGWebShopPU")
    private EntityManager em;

    /**
     * Returns the list of items from the Database.

     * This method queries the Database to find out the items exist in it as the
     * list of items.
     * 
     *

    @return      the list of items exist in Inventory table
     *
     */

    public List<Inventory> getItems() {
        try {
            em = Persistence.createEntityManagerFactory("APGWebShopPU").createEntityManager();
            return em.createQuery("SELECT c FROM Inventory c").getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Returns the list-name of items in the Database.
     * 
     * This method queries the Database to find out the name of items in
     * Inventory table and return the result as a list.
     *
     *
    @return      the list of item-names in Inventory table
     *
     */
    public List<String> getGnomes() {
        try {
            em = Persistence.createEntityManagerFactory("APGWebShopPU").createEntityManager();
            return em.createQuery("SELECT c.gnomename FROM Inventory c ").getResultList();
        } finally {
            em.close();
        }

    }

    /**
     * Returns the list of user ids in Database.
     *

     * This method queries the Database to find out the ids of all users in
     * UserAuth table where the user is not admin.
     *
     *

    @return      the list of normal user ids in UserAuth table
     *

     */
    public List<String> getUsers() {
        try {
            em = Persistence.createEntityManagerFactory("APGWebShopPU").createEntityManager();
            return em.createQuery("SELECT c.id FROM UserAuth c WHERE c.adminuser=0 ").getResultList();
        } finally {
            em.close();
        }

    }

    /**
     * Returns the correspondent price of a specific item in the Database.

     * This method receives the name of a gnome item, finds this item in the
     * Database and returns the correspondent price form the Database.
     *
     *

    @param  gnome the name of gnome item in the Database
     *

    @return      the price of a gnome name in the String format
     *

     */
    public String getPrice(String gnome) {
        InventoryDTO found = null;
        try {
            em = Persistence.createEntityManagerFactory("APGWebShopPU").createEntityManager();
            found = em.find(Inventory.class, gnome);
        } finally {
            em.close();
            return found.getPrice();
        }
    }

    /**
     * Returns the number of items in stock in the Database.


     * This method queries the Inventory table to find out the stocked items
     * in the database.
     *
     *

    @param  gnome  the name of item in String format to find out items in Stock.
     *

    @return      the number of items in Stock
     *

     */
    public String getStock(String gnome) {
        InventoryDTO found = null;
        try {
            em = Persistence.createEntityManagerFactory("APGWebShopPU").createEntityManager();
            found = em.find(Inventory.class, gnome);
        } finally {
            em.close();
            return found.getStock();
        }
    }

    /**
     * Updates the number of a specific item in the Database

     * This method receives the name and number of a specific item in the
     * Database to be updated.
     *
     *

    @param  gnome  name of the item in Inventory table
     *

    @param  number number of items
     *

     */
    public void stockUpdate(String gnome, String number) {
        Inventory found = null;
        try {
            em = Persistence.createEntityManagerFactory("APGWebShopPU").createEntityManager();
            found = em.find(Inventory.class, gnome);
            int a = Integer.valueOf(found.getStock()) - Integer.valueOf(number);
            found.setStock(String.valueOf(a));
            em.persist(found);
        } finally {
            em.close();
        }

    }

    /**
     * Add a user in the Database as a normal user.

     * This method requires a username and a password to add in the UserAuth table
     * as a normal (not-Admin) user.
     *
     *

    @param  username the id/name of user in the table
     *

    @param  password the correspondent password of recent user.
     *

    @return      a boolean value to notify the caller
     *

     */
    public boolean addUser(String username, String password) {
        UserAuthDTO user = null;
        boolean bCreated = false;

        try {
            em = Persistence.createEntityManagerFactory("APGWebShopPU").createEntityManager();
            user = em.find(UserAuth.class, username);
            if (user == null) {
                bCreated = true;
                user = new UserAuth(username, password, false, false);
                em.persist(user);
            }
        } finally {
            em.close();
        }

        return bCreated;
    }

    /**
     * Authenticate the user based on the correct existing username and
     * correspondent password.

     * This method needs the user to enter correct username and the correspondent
     * password to be authenticated.
     *
     *

    @param  username  the id/username of user which has been entered
     *

    @param  password the correspondent password expected to be entered
     *

    @return      The status of user if he/she is authenticated. This method returns
     * <code>admin</code> if the user is an admin; returns <code>user</code> if
     * the user is a normal one and <code>block</code> if the user has been
     * already blocked.
     *
     *

     */
    public String authenticateUser(String username, String password) {
        try {
            em = Persistence.createEntityManagerFactory("APGWebShopPU").createEntityManager();
            UserAuth user = em.find(UserAuth.class, username);

            if (user != null) {
                if (user.getId().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                    if (user.isAdminuser()) {
                        return "admin";
                    }
                    if (user.isBanned()) {
                        return "block";
                    } else {
                        return "user";
                    }

                } else {
                    return "block";
                }
            }
            return "block";

        } finally {
            em.close();
        }
    }

    /**
     * Add a gnome item, the number of items in stock and the price to the Database.

     * This method, used by the admin of the webshop, is specified to add new
     * gnome items to the inventory. Admin provides the gnome name, the stock number
     * and also the price of it, which will be saved in inventory table.
     *
     *

    @param  gnomename  the name of gnome item in the table
     *

    @param  stock the number of item in stock
     *

    @param  price the correspondent price of this item
     *

     */
    public void AddGnome(String gnomename, String stock, String price) {
        try {
            em = Persistence.createEntityManagerFactory("APGWebShopPU").createEntityManager();
            Inventory newGnome = new Inventory(gnomename, stock, price);
            em.persist(newGnome);
        } finally {
            em.close();
        }
    }

    /**
     * Banned a specific user to be not allowed use the system anymore.

     * This method which is used by the admin, to block or ban a specific user, so that
     * he/she cannot login to the system. If the admin choose one user whose username doesn't
     * exist in the database, it will throw an exception. This method is user by
     * admin user only.
     *
     *

    @param  banneduser  the name/id of a user to be banned
     *

    @see         Image
     */
    public void ban(String baneduser) {
        try {
            em = Persistence.createEntityManagerFactory("APGWebShopPU").createEntityManager();
            UserAuth foundUser = em.find(UserAuth.class, baneduser);
            if (foundUser == null) {
                throw new EntityNotFoundException("This username does not exist!");
            } else {
                foundUser.setBanned(true);
                em.persist(foundUser);
            }
        } finally {
            em.close();
        }
    }

    /**
     * Unblock a specific user, who has been blocked using the system.

     * This method is also used by the admin for unblocking the banned user, 
     * by setting the privilege of that specific user to 1 (true) in banned field
     * of UserAuth table. 
     *
     *

    @param  banneduser  the name/id of a user to be unblocked and able to user the system
     *

     */
    public void unblock(String baneduser) {
        try {
            em = Persistence.createEntityManagerFactory("APGWebShopPU").createEntityManager();
            UserAuth foundUser = null;
            foundUser = em.find(UserAuth.class, baneduser);


            if (foundUser == null) {
                throw new EntityNotFoundException("This username does not exist!");
            } else {
                foundUser.setBanned(false);
                em.persist(foundUser);
            }
        } finally {
            em.close();
        }
    }

    /**
     * Deletes a specific gnome item from the table

     * This method is used by the admin of the webshop to delete one gnome from
     * the inventory table. The admin provides gnomename, and since gnomename is
     * the primary key in inventory table, and subsequently, the whole item with
     * its stock and price will be removed.
     *
     *

    @param  gnomeName  the name of gnome item in Inventory table
     *

    @param  stock the number of items in stock in Inventory table
     *

     */
    public void DeleteGnome(String gnomeName, String stock) {
        try {
            em = Persistence.createEntityManagerFactory("APGWebShopPU").createEntityManager();
            Inventory found = em.find(Inventory.class, gnomeName);
            em.remove(found);

        } finally {
            em.close();
        }
    }

    /**
     * Updates the list of items-specifications in the Database.

     * This method is used to update the list of available gnomes in the inventory.
     * The admin can update number of items and also the price of the items. 
     *
     *

    @param  gnome  the name of gnome item in Inventory table
     *

    @param  number the number of items in stock
     *

    @param  price the correspondent price of this item
     *

     */
    public void UpdateList(String gnome, String number, String price) {
        Inventory found = null;
        try {
            em = Persistence.createEntityManagerFactory("APGWebShopPU").createEntityManager();
            found = em.find(Inventory.class, gnome);
            found.setStock(number);
            found.setPrice(price);
            em.persist(found);
        } finally {
            em.close();
        }
    }
}
