package com.jadd.easykds;

public class Item {

    String name;
    long price;
    long id;

    public Item(String name, long price, long id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public Item() {
    }

    public void setId(long id) {
        this.id = id;
    }
}
