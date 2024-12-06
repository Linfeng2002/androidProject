package com.example.myapplication.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Order implements Serializable {
    public int id;
    public int orderUserId;
    public int orderNumber;
    public int State;
    public int productNumber;
    public Object productPrice;
    public int productId;
    public Timestamp orderCreateTime;

    public Order(int id, int orderUserId, int orderNumber, int state, int productNumber, Object productPrice, int productId, Timestamp orderCreateTime) {
        this.id = id;
        this.orderUserId = orderUserId;
        this.orderNumber = orderNumber;
        State = state;
        this.productNumber = productNumber;
        this.productPrice = productPrice;
        this.productId = productId;
        this.orderCreateTime = orderCreateTime;
    }

    public int getState() {
        return State;
    }
}
