package com.factory;

import com.factory.persistence.ProductLineRepository;
import com.factory.persistence.TaskRepository;
import com.factory.persistence.MaterialRepository;
import com.factory.persistence.ProductRepository;

public class AppContext {

    private final ProductLineRepository productLineRepository;
    private final TaskRepository taskRepository;
    private final MaterialRepository materialRepository;
    private final ProductRepository productRepository;

    public AppContext(
            ProductLineRepository plRepo,
            TaskRepository taskRepo,
            MaterialRepository materialRepo,
            ProductRepository productRepo
    ) {
        this.productLineRepository = plRepo;
        this.taskRepository = taskRepo;
        this.materialRepository = materialRepo;
        this.productRepository = productRepo;
    }

    public ProductLineRepository getProductLineRepository() {
        return productLineRepository;
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public MaterialRepository getMaterialRepository() {
        return materialRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }
}
