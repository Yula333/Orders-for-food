package com.itproger.orders.Models;

public class Cart {

    private int categoryID, amount;

    public Cart(int categoryID, int amount) {
        this.categoryID = categoryID;
        this.amount = amount;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString(){
        return categoryID + ": " + amount;
    }
}
