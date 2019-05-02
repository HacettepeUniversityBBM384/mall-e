package com.malle.Entity;

import javax.persistence.*;

@Entity
@Table(name="customer")
public class Customer extends User{
    @Column(name = "cartid")
    private int cartid;

    public Customer(String email, String password, String name, String surname, String phone, String address, int cartid) {
        super(email, password, name, surname, phone, address);
        this.cartid = cartid;
    }
}
