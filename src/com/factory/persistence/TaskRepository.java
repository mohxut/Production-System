package com.factory.persistence;

import com.factory.model.Task;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TaskRepository {
    private final Map<String, Task> map = new ConcurrentHashMap<>();

//    public TaskRepository() {
//    }

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
        return map.values().stream()
                .filter(t -> productLineId.equals(t.getProductLineId()))
                .collect(Collectors.toList());
    }

    public void remove(String id) {
        map.remove(id);
    }
}
