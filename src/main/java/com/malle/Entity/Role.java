package com.malle.Entity;

import javax.persistence.*;

@Entity
@Table(name = "Role")
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    @Column(name = "role")
    private String role;

    public int getRoleId() {
        return Id;
    }

    public void setRoleId(int roleId) {
        this.Id = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Role(int roleId, String role) {
        this.Id = roleId;
        this.role = role;
    }

    public Role(){}

}
