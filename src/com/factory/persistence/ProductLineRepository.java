package com.factory.persistence;

import com.factory.model.ProductLine;
import com.factory.model.ProductionLineStatus;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductLineRepository {

    private final String filePath;
    private final List<ProductLine> productLines = new ArrayList<>();

    public ProductLineRepository(String filePath) {
        this.filePath = filePath;
        load();
    }

    public List<ProductLine> findAll() {
        return new ArrayList<>(productLines);
    }

    public ProductLine findById(String id) {
        for (ProductLine p : productLines) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    private String generateNextId() {
        int max = 0;
        for (ProductLine p : productLines) {
            try {
                max = Math.max(max, Integer.parseInt(p.getId()));
            } catch (Exception ignored) {}
        }
        return String.valueOf(max + 1);
    }

    public void add(String name, ProductionLineStatus status) {
        ProductLine p = new ProductLine(generateNextId(), name);
        p.setStatus(status);
        productLines.add(p);
        save();
    }

    public void update(ProductLine updated) {
        save();
    }

    public void deleteById(String id) {
        productLines.removeIf(p -> p.getId().equals(id));
        save();
    }

    private void save() {
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write("id,name,status,taskId,rating,note,createdAt\n");

            for (ProductLine p : productLines) {

                if (p.getPerformanceEntries().isEmpty()) {
                    fw.write(p.getId() + "," + p.getName() + "," +
                            p.getStatus().name() + ",,,,\n");
                } else {
                    for (ProductLine.PerformanceEntry e : p.getPerformanceEntries()) {
                        fw.write(String.join(",",
                                p.getId(),
                                p.getName(),
                                p.getStatus().name(),
                                e.getTaskId(),
                                String.valueOf(e.getRating()),
                                e.getNote().replace(",", ";"),
                                e.getCreatedAt().toString()
                        ) + "\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        productLines.clear();
        File file = new File(filePath);

        if (!file.exists()) {
            save();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length < 3) continue;

                String id = p[0].trim();
                String name = p[1].trim();
                String statusStr = p[2].trim();

                ProductLine pl = findById(id);
                if (pl == null) {
                    pl = new ProductLine(id, name);
                    try {
                        pl.setStatus(ProductionLineStatus.valueOf(statusStr));
                    } catch (Exception e) {
                        pl.setStatus(ProductionLineStatus.ACTIVE);
                    }
                    productLines.add(pl);
                }

                if (p.length >= 7 && !p[3].isEmpty()) {
                    String taskId = p[3].trim();
                    int rating = Integer.parseInt(p[4].trim());
                    String note = p[5].trim();
                    LocalDateTime createdAt = LocalDateTime.parse(p[6].trim());

                    pl.addPerformance(new ProductLine.PerformanceEntry(
                            taskId, rating, note, createdAt
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
