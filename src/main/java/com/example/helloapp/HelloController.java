package com.example.helloapp;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

    private final TaskService service;

    public HelloController(TaskService service) {
        this.service = service;
    }

    // DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("total", service.total());
        model.addAttribute("pending", service.pending());
        model.addAttribute("done", service.done());
        return "dashboard";
    }

    // TASK PAGE (SEARCH + FILTER)
    @GetMapping("/tasks")
    public String tasks(@RequestParam(required = false) String keyword,
                        @RequestParam(required = false) String filter,
                        Model model) {

        List<Task> tasks = service.search(keyword);

        if (filter != null && !filter.isBlank()) {
            tasks = tasks.stream()
                    .filter(t -> t.getStatus().equals(filter))
                    .toList();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("filter", filter);

        return "tasks";
    }

    @PostMapping("/tasks")
    public String add(@RequestParam String name) {
        service.add(name);
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/delete")
    public String delete(@RequestParam Long id) {
        service.delete(id);
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/edit")
    public String edit(@RequestParam Long id, Model model) {
        model.addAttribute("task", service.getById(id));
        return "edit";
    }

    @PostMapping("/tasks/update")
    public String update(@RequestParam Long id,
                         @RequestParam String name,
                         @RequestParam String status) {
        service.update(id, name, status);
        return "redirect:/tasks";
    }
}