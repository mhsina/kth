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
 * @author Sina
 */
@Entity
public class Inventory implements Serializable, InventoryDTO {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String gnomename;    //primary key
    private String stock;
    private String price;

    /**
     * This method is the default constructor of Inventory class.

     *
     */
    public Inventory() {
    }

    /**
     * This is the constructor of Inventory class with three parameters.


    @param  gnomename  the name of gnome item in the table
     *

    @param  stock  the number of items in stock in the table
     *

    @param  price  the price of this item in the table
     *

     *
     */

    public Inventory(String gnomename, String stock, String price) {
        this.gnomename = gnomename;
        this.stock = stock;
        this.price = price;
    }

    /**
     * This method is used to retrieve <code>gnomename</code> variable.

    @return      the gnomename field of the table
     *
     */
    public String getGnomename() {
        return gnomename;
    }

    /**
     * This method is used to set <code>gnomename</code> variable.

     *
     */
    public void setGnomename(String gnomename) {
        this.gnomename = gnomename;
    }

    /**
     * This method is used to retrieve <code>price</code> variable.

    @return      the price field of the table
     *
     */
    public String getPrice() {
        return price;
    }

    /**
     * This method is used to set <code>price</code> variable.

     *
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * This method is used to retrieve <code>stock</code> variable.

    @return      the stock field of the table
     *
     */
    public String getStock() {
        return stock;
    }

    /**
     * This method is used to set <code>stock</code> variable.

     *
     */
    public void setStock(String stock) {
        this.stock = stock;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gnomename != null ? gnomename.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inventory)) {
            return false;
        }
        Inventory other = (Inventory) object;
        if ((this.gnomename == null && other.gnomename != null) || (this.gnomename != null && !this.gnomename.equals(other.gnomename))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Inventory[gnomename=" + gnomename + "]";
    }
}
