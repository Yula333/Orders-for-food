package com.itproger.orders.Models;

public class Food {

    private String price, full_text;

    public Food() {
    }

    public Food(String price, String full_text) {
        this.price = price;
        this.full_text = full_text;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }
}
