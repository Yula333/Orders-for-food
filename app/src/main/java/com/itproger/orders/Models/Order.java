package com.itproger.orders.Models;

public class Order {

    private String order_list;
    private String user;

    public Order(){

    }

    public Order(String order_list, String user) {
        this.order_list = order_list;
        this.user = user;
    }


    public String getOrder_list() {
        return order_list;
    }

    public void setOrder_list(String order_list) {
        this.order_list = order_list;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
