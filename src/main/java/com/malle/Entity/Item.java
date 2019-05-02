package com.malle.Entity;

import javax.persistence.*;

@Entity
@Table(name="item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private int sellerid;
    private String name;
    private int subcategoryid;
    private double price;
    private int ordercount;
    private double rating;
    private String description;
    private int stock;

    public Item(int sellerid, String name, int subcategoryid, double price, int ordercount, double rating, String description, int stock) {
        this.sellerid = sellerid;
        this.name = name;
        this.subcategoryid = subcategoryid;
        this.price = price;
        this.ordercount = ordercount;
        this.rating = rating;
        this.description = description;
        this.stock = stock;
    }


}
