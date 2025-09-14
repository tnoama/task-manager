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
        TaskItem updateEntity = dtoToEntity(updates);
        TaskItem updated = taskService.updateTask(id, updateEntity);
        return entityToDto(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
    }

    @GetMapping
    public Page<TaskDto> listTasks(
            @RequestParam(required = false) String assignee,
            @RequestParam(required = false) TaskItem.Category category,
            @RequestParam(required = false) TaskItem.Status status,
            @RequestParam(required = false) TaskItem.Priority priority,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Build sorting
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDir.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Call service with explicit filters
        Page<TaskItem> items = taskService.listTasksFiltered(assignee, category, status, priority, pageable);
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
