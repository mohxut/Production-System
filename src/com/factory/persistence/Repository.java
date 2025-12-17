package com.factory.persistence;

import com.factory.model.Item ;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Repository {

    private final Map<String, Item> map = new ConcurrentHashMap<>();

    public Repository()
    {}

    public Item addItem(String name ,String category ,double price , int quantity , int minQuantity)
    {
        String id = UUID.randomUUID().toString();
        Item tl = new Item(id ,name ,category ,price ,quantity ,minQuantity ) ;
        map.put(tl.getId() , tl) ;

        return tl ;
    }



}
