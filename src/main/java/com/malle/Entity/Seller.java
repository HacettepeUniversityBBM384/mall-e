package com.malle.Entity;

import javax.persistence.*;

@Entity
@Table(name="seller")
public class Seller extends User{
    private int IBAN;
    private String shopname;
    private int sale;
    private double rating;

    public Seller(String email, String password, String name, String surname, String phone, String address, int IBAN, String shopname, int sale, double rating) {
        super(email, password, name, surname, phone, address);
        this.IBAN = IBAN;
        this.shopname = shopname;
        this.sale = sale;
        this.rating = rating;
    }

    public Seller() {
        this.IBAN = 0;
        this.shopname = "";
        this.sale = 0;
        this.rating = 5;
    }
}
