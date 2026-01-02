package com.factory.model;

public class Material {

    private final String id;
    private final String name;
    private final String category;
    private  double price;
    private int quantity;
    private  int minQuantity;

    public Material(String id, String name, String category,
                    double price, int quantity, int minQuantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getMinQuantity() { return minQuantity; }
    public void setMinQuantity(int minQuantity) { this.minQuantity = minQuantity; }

    public boolean hasEnough(int required) {
        return quantity >= required;
    }

    public void consume(int amount) {
        if (amount > quantity)
            throw new IllegalStateException("Not enough material: " + name);
        quantity -= amount;
    }
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
