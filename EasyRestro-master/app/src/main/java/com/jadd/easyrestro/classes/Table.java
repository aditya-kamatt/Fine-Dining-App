package com.jadd.easyrestro.classes;

import com.jadd.easyrestro.classes.Cart;

public class Table {
    int tableNumber;
    boolean isEmpty;
    boolean sendToKitchen;
    boolean isReady;

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public Table(int tableNumber, boolean isEmpty, boolean sendToKitchen, boolean isReady, Cart cart) {
        this.tableNumber = tableNumber;
        this.isEmpty = isEmpty;
        this.sendToKitchen = sendToKitchen;
        this.isReady = isReady;
        this.cart = cart;
    }

    Cart cart;

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Table(int tableNumber, boolean isEmpty, Cart cart) {
        this.tableNumber = tableNumber;
        this.isEmpty = isEmpty;
        this.cart = cart;
    }

    public boolean isSendToKitchen() {
        return sendToKitchen;
    }

    public void setSendToKitchen(boolean sendToKitchen) {
        this.sendToKitchen = sendToKitchen;
    }

    public Table(int tableNumber, boolean isEmpty, boolean sendToKitchen) {
        this.tableNumber = tableNumber;
        this.isEmpty = isEmpty;
        this.sendToKitchen=sendToKitchen;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }


}
