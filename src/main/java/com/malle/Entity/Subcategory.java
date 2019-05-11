package com.malle.Entity;

import javax.persistence.*;

@Entity
@Table(name="subcategory")
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String categoryname;
    private String name;

    public Subcategory(String categoryname, String name) {
        this.categoryname = categoryname;
        this.name = name;
    }

    public Subcategory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
