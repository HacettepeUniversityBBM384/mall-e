package com.malle.Entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private int id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phone;
    private String address;
    @Column(name = "cart_Id")
    private int cart_Id;
    private int role;

    public User(){
        this.email = "";
        this.password = "";
        this.name = "";
        this.surname = "";
        this.phone = "";
        this.address = "";
        this.cart_Id = 0;
    }

    public User(String email, String password, String name, String surname, String phone, String address, int cartID) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.address = address;
        this.cart_Id = cartID;
    }

    public User(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.cart_Id = user.getCartID();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCart_Id(int cart_Id) {
        this.cart_Id = cart_Id;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getID() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public int getCartID() {
        return cart_Id;
    }

    public int getRoles() {
        return role;
    }

    public void setRoles(int roles) {
        this.role = roles;
    }
}
