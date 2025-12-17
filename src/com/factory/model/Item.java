package com.factory.model;

import java.util.concurrent.locks.ReentrantLock;

public class Item {
    private final String id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private int minQuantity;

    private final ReentrantLock lock = new ReentrantLock();

    public Item(String id, String name, String category, double price, int quantity, int minQuantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getMinQuantity() { return minQuantity; }
    public void setMinQuantity(int minQuantity) { this.minQuantity = minQuantity; }

    public void lock() { lock.lock(); }
    public void unlock() { lock.unlock(); }


    public void reduceQuantity(int amount) {
        if (amount < 0) throw new IllegalArgumentException("amount must be >= 0");
        if (quantity < amount) throw new IllegalStateException("Not enough stock for item " + id);
        quantity -= amount;
    }

    public void increaseQuantity(int amount) {
        if (amount < 0) throw new IllegalArgumentException("amount must be >= 0");
        quantity += amount;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", minQuantity=" + minQuantity +
                '}';
    }
}
