package com.example.task.controller;

import com.example.task.model.TaskItem;
import com.example.task.service.TaskService;
import com.example.task.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
// import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public TaskDto createTask(@Valid @RequestBody TaskDto taskDto) {
        TaskItem task = dtoToEntity(taskDto);
        TaskItem saved = taskService.createTask(task);
        return entityToDto(saved);
    }

    @GetMapping("/{id}")
    public TaskDto getTask(@PathVariable UUID id) {
        Optional<TaskItem> result = taskService.getTask(id);
        return result.map(this::entityToDto).orElse(null);
    }

    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable UUID id, @RequestBody TaskDto updates) {
        TaskItem updated = taskService.updateTask(id, updates);
        return entityToDto(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
    }

    @GetMapping
    public Page<TaskDto> listTasks(
            @RequestParam(required = false) String assignee,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Debug logging for filter params
        System.out.println("Filter params: assignee=" + assignee + ", category=" + category + ", status=" + status
                + ", priority=" + priority);

        // Convert string params to enums if present
        TaskItem.Category categoryEnum = null;
        TaskItem.Status statusEnum = null;
        TaskItem.Priority priorityEnum = null;
        try {
            if (category != null && !category.isBlank())
                categoryEnum = TaskItem.Category.valueOf(category);
        } catch (Exception e) {
            System.out.println("Invalid category: " + category);
        }
        try {
            if (status != null && !status.isBlank())
                statusEnum = TaskItem.Status.valueOf(status);
        } catch (Exception e) {
            System.out.println("Invalid status: " + status);
        }
        try {
            if (priority != null && !priority.isBlank())
                priorityEnum = TaskItem.Priority.valueOf(priority);
        } catch (Exception e) {
            System.out.println("Invalid priority: " + priority);
        }

        // Build sorting
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDir.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Call service with explicit filters
        Page<TaskItem> items = taskService.listTasksFiltered(assignee, categoryEnum, statusEnum, priorityEnum,
                pageable);
        return items.map(this::entityToDto);
    }

    // Utility methods for mapping between DTO and entity
    private TaskItem dtoToEntity(TaskDto dto) {
        TaskItem item = new TaskItem();
        item.setId(dto.getId());
        item.setTitle(dto.getTitle());
        item.setDescription(dto.getDescription());
        item.setAssignee(dto.getAssignee());
        item.setCategory(dto.getCategory());
        item.setStatus(dto.getStatus());
        if (dto.getPriority() != null) {
            item.setPriority(dto.getPriority());
        }
        item.setDueDate(dto.getDueDate());
        item.setCreatedAt(dto.getCreatedAt());
        item.setUpdatedAt(dto.getUpdatedAt());
        return item;
    }

    private TaskDto entityToDto(TaskItem item) {
        TaskDto dto = new TaskDto();
        dto.setId(item.getId());
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDescription());
        dto.setAssignee(item.getAssignee());
        dto.setCategory(item.getCategory());
        dto.setStatus(item.getStatus());
        dto.setPriority(item.getPriority());
        dto.setDueDate(item.getDueDate());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        return dto;
    }
}
