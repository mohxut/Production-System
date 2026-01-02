package com.factory.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ProductLine {

    private final String id;
    private String name;

    private final AtomicReference<ProductionLineStatus> status =
            new AtomicReference<>(ProductionLineStatus.ACTIVE);

    public static class PerformanceEntry {
        private final String taskId;
        private final int rating;
        private final String note;
        private final LocalDateTime createdAt;

        public PerformanceEntry(String taskId, int rating, String note, LocalDateTime createdAt) {
            this.taskId = taskId;
            this.rating = rating;
            this.note = note;
            this.createdAt = createdAt;
        }

        public PerformanceEntry(String taskId, int rating, String note) {
            this(taskId, rating, note, LocalDateTime.now());
        }

        public String getTaskId() { return taskId; }
        public int getRating() { return rating; }
        public String getNote() { return note; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }

    private final List<PerformanceEntry> performanceEntries = new ArrayList<>();

    public ProductLine(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ProductionLineStatus getStatus() {
        return status.get();
    }

    public void setStatus(ProductionLineStatus s) {
        status.set(s);
    }

    public void cycleStatus() {
        switch (status.get()) {
            case ACTIVE -> status.set(ProductionLineStatus.STOPPED);
            case STOPPED -> status.set(ProductionLineStatus.MAINTENANCE);
            case MAINTENANCE -> status.set(ProductionLineStatus.ACTIVE);
        }
    }


    public synchronized void addPerformance(String taskId, int rating, String note) {
        performanceEntries.add(new PerformanceEntry(taskId, rating, note));
    }

    public synchronized void addPerformance(PerformanceEntry entry) {
        performanceEntries.add(entry);
    }

    public synchronized List<PerformanceEntry> getPerformanceEntries() {
        return new ArrayList<>(performanceEntries);
    }

    public synchronized void setPerformanceEntries(List<PerformanceEntry> entries) {
        performanceEntries.clear();
        if (entries != null) performanceEntries.addAll(entries);
    }

    public double calculateProgress(List<Task> tasks) {
        if (tasks.isEmpty()) return 0.0;
        double total = 0.0;
        for (Task t : tasks) {
            total += t.getProgressPercent();
        }
        return total / tasks.size();
    }

    public synchronized boolean removePerformanceEntryAt(int index) {
        if (index < 0 || index >= performanceEntries.size()) return false;
        performanceEntries.remove(index);
        return true;
    }

}
