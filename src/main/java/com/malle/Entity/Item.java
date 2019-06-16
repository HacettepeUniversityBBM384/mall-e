package com.malle.Entity;

import javax.persistence.*;

@Entity
@Table(name="item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String shopname;
    private String name;
    private int subcategoryid;
    private double price;
    private int ordercount;
    private double rating;
    private String description;
    private int stock;


    public Item(String shopname, String name, int subcategoryid, double price, int ordercount, double rating, String description, int stock) {
        this.shopname = shopname;
        this.name = name;
        this.subcategoryid = subcategoryid;
        this.price = price;
        this.ordercount = ordercount;
        this.rating = rating;
        this.description = description;
        this.stock = stock;
    }

    public Item() {
    }

    public Item(int id) {
        this.setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopname() { return shopname; }

    public void setShopname(String shopname) { this.shopname = shopname; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubcategoryid() {
        return subcategoryid;
    }

    public void setSubcategoryid(int subcategoryid) {
        this.subcategoryid = subcategoryid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getOrdercount() {
        return ordercount;
    }

    public void setOrdercount(int ordercount) {
        this.ordercount = ordercount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return this.id == item.id;
    }


}
