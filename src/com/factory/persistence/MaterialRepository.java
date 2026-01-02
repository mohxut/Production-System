package com.factory.persistence;

import com.factory.model.Material;

import java.io.* ;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MaterialRepository {

    private final String filePath;

    private final Map<String, Material> materials = new ConcurrentHashMap<>();

    public MaterialRepository(String filePath) {
        this.filePath = filePath;
        loadFromCsv(filePath);
    }

    public void loadFromCsv(String csvFilePath) {
        materials.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length < 6) continue;

                String id = p[0].trim();
                String name = p[1].trim();
                String category = p[2].trim();
                double price = Double.parseDouble(p[3].trim());
                int quantity = Integer.parseInt(p[4].trim());
                int minQuantity = Integer.parseInt(p[5].trim());

                Material m = new Material(id, name, category, price, quantity, minQuantity);
                materials.put(id, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveToFill() {
        try (FileWriter fw = new FileWriter(filePath)) {

            // header
            fw.write("id,name,category,price,quantity,minQuantity\n");

            for (Material m : materials.values()) {
                fw.write(String.join(",",
                        m.getId(),
                        m.getName().replace(",", ";"),
                        m.getCategory(),
                        String.valueOf(m.getPrice()),
                        String.valueOf(m.getQuantity()),
                        String.valueOf(m.getMinQuantity())
                ));
                fw.write("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Material findById(String id) {
        return materials.get(id);
    }
    public List<Material> findAllByName(String keyword) {
        List<Material> result = new ArrayList<>();

        for (Material m : materials.values()) {
            if (m.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Material> findAllByCategory(String category) {
        List<Material> result = new ArrayList<>();

        for (Material m : materials.values()) {
            if (m.getCategory().equalsIgnoreCase(category)) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Material> findAllBelowMinimum() {
        List<Material> result = new ArrayList<>();

        for (Material m : materials.values()) {
            if (m.getQuantity() <= m.getMinQuantity()) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Material> findOutOfStock() {
        List<Material> result = new ArrayList<>();

        for (Material m : materials.values()) {
            if (m.getQuantity() == 0) {
                result.add(m);
            }
        }
        return result;
    }


    public Collection<Material> findAll() {
        return new ArrayList<>(materials.values());
    }

    public boolean hasEnoughForProduction(Map<String, Integer> perUnitMaterials, int unitsToProduce) {
        for (Map.Entry<String, Integer> entry : perUnitMaterials.entrySet()) {
            Material m = materials.get(entry.getKey());
            int required = entry.getValue() * unitsToProduce;

            if (m == null || !m.hasEnough(required)) {
                return false;
            }
        }
        return true;
    }

    public void consumeForProduction(Map<String, Integer> perUnitMaterials, int unitsProduced) {
        for (Map.Entry<String, Integer> entry : perUnitMaterials.entrySet()) {
            Material m = materials.get(entry.getKey());
            if (m != null) {
                int amount = entry.getValue() * unitsProduced;
                m.consume(amount);
            }
        }
    }

    public Material editMateral (String id , double newPrice , int  newQuantity , int newMinQuantity)
    {
        for(Material m : materials.values())
            if(m.getId().equals(id))
            {
                m.setPrice(newPrice);
                m.setQuantity(newQuantity);
                m.setMinQuantity(newMinQuantity);
                saveToFill();
            }

        return materials.get(id) ;
    }




    public Material addMaterial(String name , String category , double price , int quantity , int minQuantity)
    {
        String id = UUID.randomUUID().toString();
        Material ml = new Material (id ,name ,category ,price ,quantity ,minQuantity ) ;
        materials.put(ml.getId() , ml) ;

        saveToFill();
        return ml ;
    }

    public void remove (String id)
    {
        materials.remove(id) ;
    }




}

