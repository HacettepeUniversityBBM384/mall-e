package com.malle.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private int customerid;
    private int sellerid;
    private Date date;
    private String status;
    private double payoff;

    public Order(int customerid, int sellerid, Date date, String status, double payoff) {
        this.customerid = customerid;
        this.sellerid = sellerid;
        this.date = date;
        this.status = status;
        this.payoff = payoff;
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public int getSellerid() {
        return sellerid;
    }

    public void setSellerid(int sellerid) {
        this.sellerid = sellerid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPayoff() {
        return payoff;
    }

    public void setPayoff(double payoff) {
        this.payoff = payoff;
    }
}

