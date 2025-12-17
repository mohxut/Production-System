package com.factory.persistence;

import com.factory.model.ProductLine;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProductLineRepository {
    private final Map<String, ProductLine> map = new ConcurrentHashMap<>();

    public ProductLineRepository() {
        ProductLine p1 = new ProductLine(UUID.randomUUID().toString(), "Line A");
        ProductLine p2 = new ProductLine(UUID.randomUUID().toString(), "Line B");
        map.put(p1.getId(), p1);
        map.put(p2.getId(), p2);
    }

    public ProductLine add(String name) {
        String id = UUID.randomUUID().toString();
        ProductLine pl = new ProductLine(id, name);
        map.put(id, pl);
        return pl;
    }

    public ProductLine findById(String id) {
        return map.get(id);
    }

    public List<ProductLine> findAll() {
        return new ArrayList<>(map.values());
    }

    public void update(ProductLine pl) {
        map.put(pl.getId(), pl);
    }

    public void remove(String id) {
        map.remove(id);
    }
}
