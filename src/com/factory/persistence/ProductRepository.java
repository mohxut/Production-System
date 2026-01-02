package com.factory.persistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class ProductRepository {

    private final Map<String, Map<String, Integer>> productComponents = new HashMap<>();

    public ProductRepository(String csvFilePath) {
        loadFromCsv(csvFilePath);
    }

    public void loadFromCsv(String csvFilePath) {
        productComponents.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length < 3) continue;

                String productName = p[1].trim();
                String componentsStr = p[2].trim();

                Map<String, Integer> compMap = new HashMap<>();

                if (!componentsStr.isEmpty()) {
                    String[] comps = componentsStr.split("\\|");
                    for (String c : comps) {
                        String[] pair = c.split(":");
                        if (pair.length == 2) {
                            compMap.put(
                                    pair[0].trim(),
                                    Integer.parseInt(pair[1].trim())
                            );
                        }
                    }
                }

                productComponents.put(productName, compMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getComponentsForProduct(String productName) {
        return productComponents.getOrDefault(productName, Collections.emptyMap());
    }
}
