package com.example.helloapp;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public List<Task> getAll() {
        return repo.findAll();
    }

    public List<Task> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return repo.findAll();
        }
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    public void add(String name) {
        Task t = new Task();
        t.setName(name);
        repo.save(t);
    }

    public Task getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void update(Long id, String name, String status) {
        Task t = repo.findById(id).orElse(null);
        if (t != null) {
            t.setName(name);
            t.setStatus(status);
            repo.save(t);
        }
    }

    public long total() {
        return repo.count();
    }

    public long pending() {
        return repo.findAll().stream()
                .filter(t -> "PENDING".equals(t.getStatus()))
                .count();
    }

    public long done() {
        return repo.findAll().stream()
                .filter(t -> "DONE".equals(t.getStatus()))
                .count();
    }
}