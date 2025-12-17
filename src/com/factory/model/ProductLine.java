package com.factory.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProductLine {
    private final String id;
    private String name;

    private final AtomicBoolean active = new AtomicBoolean(true);

    private final List<String> assignedTaskIds = new ArrayList<>();

    public static class PerformanceEntry {
        private final String taskId;
        private final int rating;
        private final String note;
        private final LocalDateTime createdAt;

        public PerformanceEntry(String taskId, int rating, String note) {
            this.taskId = taskId;
            this.rating = rating;
            this.note = note;
            this.createdAt = LocalDateTime.now();
        }

        public String getTaskId() { return taskId; }
        public int getRating() { return rating; }
        public String getNote() { return note; }
        public LocalDateTime getCreatedAt() { return createdAt; }

        @Override
        public String toString() {
            return "PerformanceEntry{" +
                    "taskId='" + taskId + '\'' +
                    ", rating=" + rating +
                    ", note='" + note + '\'' +
                    ", createdAt=" + createdAt +
                    '}';
        }
    }

    private final List<PerformanceEntry> performanceEntries = new ArrayList<>();

    public ProductLine(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isActive() { return active.get(); }
    public void setActive(boolean value) { active.set(value); }

    public List<String> getAssignedTaskIds() {
        return assignedTaskIds;
    }

    public void assignTask(String taskId) {
        if (taskId != null && !assignedTaskIds.contains(taskId)) assignedTaskIds.add(taskId);
    }

    public void unassignTask(String taskId) {
        if(taskId != null && assignedTaskIds.contains(taskId))
            assignedTaskIds.remove(taskId);
    }

    // Performance APIs
    public synchronized void addPerformance(String taskId, int rating, String note) {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("rating must be between 1 and 5");
        performanceEntries.add(new PerformanceEntry(taskId, rating, note));
    }

    public synchronized List<PerformanceEntry> getPerformanceEntries() {
        return new ArrayList<>(performanceEntries);
    }

    @Override
    public String toString() {
        return "ProductLine{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", assignedTaskIds=" + assignedTaskIds +
                ", performanceEntries=" + performanceEntries.size() +
                '}';
    }
}
