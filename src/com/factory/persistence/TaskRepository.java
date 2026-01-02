package com.factory.persistence;

import com.factory.model.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TaskRepository {

    private final Map<String, Task> map = new ConcurrentHashMap<>();

    public TaskRepository(String csvFilePath) {
        loadFromCsv(csvFilePath);
    }

    public void loadFromCsv(String csvFilePath) {
        map.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String header = br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);

                if (p.length < 9) continue;

                String id = p[0].trim();
                String productName = p[1].trim();
                int requiredQty = Integer.parseInt(p[2].trim());
                int completedQty = Integer.parseInt(p[3].trim());
                String client = p[4].trim();
                LocalDate startDate = LocalDate.parse(p[5].trim());
                LocalDate dueDate = LocalDate.parse(p[6].trim());
                String statusStr = p[7].trim();
                String productLineId = p[8].trim();

                Task.Status status;
                switch (statusStr.toUpperCase()) {
                    case "IN_PROGRESS", "قيد التنفيذ" -> status = Task.Status.IN_PROGRESS;
                    case "COMPLETED", "مكتملة" -> status = Task.Status.COMPLETED;
                    case "CANCELLED", "ملغاة" -> status = Task.Status.CANCELLED;
                    default -> status = Task.Status.IN_PROGRESS;
                }

                Task t = new Task(id, productName, productLineId, requiredQty, client, dueDate.atStartOfDay());
                t.setCompletedQuantity(completedQty);
                t.setStatus(status);
                t.setStartedAt(startDate.atStartOfDay());

                map.put(id, t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Task save(Task t) {
        map.put(t.getId(), t);
        return t;
    }

    public Task findById(String id) {
        return map.get(id);
    }

    public List<Task> findAll() {
        return new ArrayList<>(map.values());
    }

    public List<Task> findByProductLineId(String productLineId) {
        String idTrimmed = productLineId.trim();
        return map.values().stream()
                .filter(t -> t.getProductLineId() != null && t.getProductLineId().trim().equals(idTrimmed))
                .collect(Collectors.toList());
    }

    public void remove(String id) {
        map.remove(id);
    }
}
