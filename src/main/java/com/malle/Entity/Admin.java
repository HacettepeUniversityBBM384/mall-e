package com.malle.Entity;
import javax.persistence.*;

@Entity
@Table(name="admin")
public class Admin extends User {
    public Admin(String email, String password, String name, String surname, String phone, String address) {
        super(email, password, name, surname, phone, address);
    }

    public Admin() {
    }
}
