package com.example.artisanconnect.model;

import javafx.collections.ObservableList;

public class Order {
    private final String orderId;
    private final ObservableList<CartItem> items;
    private final double total;
    private OrderStatus status; // Changer String en OrderStatus

    public Order(String orderId, ObservableList<CartItem> items, double total, OrderStatus status) {
        this.orderId = orderId;
        this.items = items;
        this.total = total;
        this.status = status;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public ObservableList<CartItem> getItems() { return items; }
}

