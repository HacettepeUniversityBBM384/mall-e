package com.malle.Entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name="ordering")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name="customerid")
    private int customerid;
    private int sellerid;
    @ManyToOne
    private Item item;
    private Integer orderNo;
    private int itemid;
    private Date date;
    private String status;
    private double payoff;

    public Order(int customerid, int sellerid, Date date, String status, double payoff, int itemid, int orderNo) {
        this.customerid = customerid;
        this.sellerid = sellerid;
        this.date = date;
        this.status = status;
        this.payoff = payoff;
        this.itemid = itemid;
        this.orderNo = orderNo;
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

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}

