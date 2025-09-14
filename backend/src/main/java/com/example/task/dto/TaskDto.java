package com.example.task.dto;

import com.example.task.model.TaskItem;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class TaskDto {
    private UUID id;

    @jakarta.validation.constraints.NotBlank(message = "title must not be blank")
    @jakarta.validation.constraints.Size(max = 200, message = "title must be at most 200 characters")
    private String title;

    @jakarta.validation.constraints.Size(max = 1000, message = "description must be at most 1000 characters")
    private String description;

    @jakarta.validation.constraints.Size(max = 120, message = "assignee must be at most 120 characters")
    private String assignee;

    private TaskItem.Category category = TaskItem.Category.OTHER;

    public TaskItem.Category getCategory() {
        return category;
    }

    public void setCategory(TaskItem.Category category) {
        this.category = category;
    }

    private TaskItem.Status status;

    private TaskItem.Priority priority = TaskItem.Priority.MEDIUM;

    @jakarta.validation.constraints.FutureOrPresent(message = "dueDate must be today or in the future")
    private LocalDate dueDate;
    private Instant createdAt;
    private Instant updatedAt;

    public TaskItem.Priority getPriority() {
        return priority;
    }

    public void setPriority(TaskItem.Priority priority) {
        this.priority = priority;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public TaskItem.Status getStatus() {
        return status;
    }

    public void setStatus(TaskItem.Status status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Mapper from entity to DTO
    public static TaskDto fromEntity(TaskItem entity) {
        TaskDto dto = new TaskDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setAssignee(entity.getAssignee());
        dto.setCategory(entity.getCategory());
        dto.setStatus(entity.getStatus());
        dto.setDueDate(entity.getDueDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
