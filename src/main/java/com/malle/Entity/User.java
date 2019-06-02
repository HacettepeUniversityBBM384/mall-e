package com.malle.Entity;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Inheritance(strategy=InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phone;
    private String address;
    @Column(name = "role")
    private String role;

    public User(){
        this.email = "";
        this.password = "";
        this.name = "";
        this.surname = "";
        this.phone = "";
        this.address = "";
    }

    public User(String email, String password, String name, String surname, String phone, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.address = address;
    }

    public User(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.phone = user.getPhone();
        this.address = user.getAddress();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
