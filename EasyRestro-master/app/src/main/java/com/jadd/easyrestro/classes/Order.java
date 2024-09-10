package com.jadd.easyrestro.classes;

import com.jadd.easyrestro.classes.Cart;

import java.util.ArrayList;

public class Order {
    ArrayList<Cart> list;
    //String name;
    //String phone;
    String date;
    String time;
    int tableNumber;
    float total,subTotal,tax;

    public ArrayList<Cart> getList() {
        return list;
    }

    public void setList(ArrayList<Cart> list) {
        this.list = list;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public Order(ArrayList<Cart> list, String date, String time, int tableNumber, float total, float subTotal, float tax) {
        this.list = list;
        this.date = date;
        this.time = time;
        this.tableNumber = tableNumber;
        this.total = total;
        this.subTotal = subTotal;
        this.tax = tax;
    }

    public Order() {
    }

}
