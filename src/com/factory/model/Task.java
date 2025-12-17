package com.factory.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Task {
    public enum Status {
        PENDING,
        RESERVED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        POSTPONED
    }

    private final String id;
    private final String productId;
    private final String productLineId;
    private final String clientName;
    private final int totalQuantity;
    private int completedQuantity = 0;
    private Status status = Status.PENDING;
    private final LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime requiredBy;
    private LocalDateTime finishedAt;

    public Task(String productId, String productLineId, int totalQuantity, String clientName, LocalDateTime requiredBy) {
        this.id = UUID.randomUUID().toString();
        this.productId = productId;
        this.productLineId = productLineId;
        this.totalQuantity = totalQuantity;
        this.clientName = clientName;
        this.requiredBy = requiredBy;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getProductId() { return productId; }
    public String getProductLineId() { return productLineId; }
    public String getClientName() { return clientName; }
    public int getTotalQuantity() { return totalQuantity; }

    public int getCompletedQuantity() { return completedQuantity; }
    public void setCompletedQuantity(int completedQuantity) { this.completedQuantity = completedQuantity; }

    public void incrementCompleted(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be > 0");
        completedQuantity += amount;
        if (completedQuantity > totalQuantity) completedQuantity = totalQuantity;
    }

    public double getProgressPercent() {
        if (totalQuantity == 0) return 100.0;
        return (completedQuantity * 100.0) / totalQuantity;
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getRequiredBy() { return requiredBy; }
    public void setRequiredBy(LocalDateTime requiredBy) { this.requiredBy = requiredBy; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", productLineId='" + productLineId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", totalQuantity=" + totalQuantity +
                ", completedQuantity=" + completedQuantity +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", requiredBy=" + requiredBy +
                ", startedAt=" + startedAt +
                ", finishedAt=" + finishedAt +
                '}';
    }
}
