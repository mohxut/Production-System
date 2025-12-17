package com.factory.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Product {
    private final String id;
    private String name;
    private final Map<String, Integer> recipe = new HashMap<>();

    public Product(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Product(String id, String name, Map<String, Integer> recipe) {
        this.id = id;
        this.name = name;
        if (recipe != null) this.recipe.putAll(recipe);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


    public Map<String, Integer> getRecipe() {
        return Collections.unmodifiableMap(recipe);
    }

    public void setRecipe(Map<String, Integer> newRecipe) {
        recipe.clear();
        if (newRecipe != null) recipe.putAll(newRecipe);
    }

    public void addIngredient(String itemId, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");
        recipe.put(itemId, qty);
    }

    public void removeIngredient(String itemId) {
        if(recipe.containsKey(itemId))
            recipe.remove(itemId);
        else
            System.out.println("The Item is not even there.");
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", recipe=" + recipe +
                '}';
    }
}
