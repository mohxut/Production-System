package com.factory;

import com.factory.persistence.ProductLineRepository;
import com.factory.persistence.TaskRepository;

public class AppContext {
    private final ProductLineRepository productLineRepository;
    private final TaskRepository taskRepository;

    public AppContext(ProductLineRepository plRepo, TaskRepository taskRepo) {
        this.productLineRepository = plRepo;
        this.taskRepository = taskRepo;
    }

    public ProductLineRepository getProductLineRepository() {
        return productLineRepository;
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
}
