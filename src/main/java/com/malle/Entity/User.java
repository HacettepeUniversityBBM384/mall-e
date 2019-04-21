package com.malle.Entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phone;
    private String address;
    private int cartid;

    private int role;

    public User(){}

    public User(int id, String email, String password, String name, String surname, String phone, String address, int cartID) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.address = address;
        this.cartid = cartID;
    }

    public User(User user) {
        this.id = user.getID();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.cartid = user.getCartID();
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
        return cartid;
    }

    public int getRole() {
        return role;
    }
    public void setRole(int role) {
        this.role = role;
    }
}
