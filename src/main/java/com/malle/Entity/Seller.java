package com.malle.Entity;

import javax.persistence.*;

@Entity
@Table(name="seller")
public class Seller extends User{
    private String IBAN;
    private String shopname;
    private int sale;
    private double rating;

    public Seller(String email, String password, String name, String surname, String phone, String address, String IBAN, String shopname, int sale, double rating) {
        super(email, password, name, surname, phone, address);
        this.IBAN = IBAN;
        this.shopname = shopname;
        this.sale = sale;
        this.rating = rating;
    }

    public Seller() {
        this.IBAN = "";
        this.shopname = "";
        this.sale = 0;
        this.rating = 5;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
