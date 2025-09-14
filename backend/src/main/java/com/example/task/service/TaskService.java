package com.example.task.service;

import com.example.task.model.TaskItem;
import com.example.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public TaskItem createTask(TaskItem task) {
        task.setId(UUID.randomUUID());
        task.setCreatedAt(java.time.Instant.now());
        task.setUpdatedAt(java.time.Instant.now());
        return taskRepository.save(task);
    }

    public Optional<TaskItem> getTask(UUID id) {
        return taskRepository.findById(id);
    }

    public TaskItem updateTask(UUID id, TaskItem updates) {
        return taskRepository.findById(id).map(task -> {
            if (updates.getDescription() != null)
                task.setDescription(updates.getDescription());
            if (updates.getStatus() != null)
                task.setStatus(updates.getStatus());
            if (updates.getDueDate() != null)
                task.setDueDate(updates.getDueDate());
            // Only update priority if provided, otherwise keep existing
            if (updates.getPriority() != null) {
                task.setPriority(updates.getPriority());
            }
            // else: do not overwrite priority
            task.setUpdatedAt(java.time.Instant.now());
            return taskRepository.save(task);
        }).orElse(null);
    }

    public void deleteTask(UUID id) {
        taskRepository.deleteById(id);
    }

    public Page<TaskItem> listTasksFiltered(String assignee, TaskItem.Category category, TaskItem.Status status,
            TaskItem.Priority priority, Pageable pageable) {
        boolean filterAssignee = assignee != null && !assignee.isBlank();
        boolean filterCategory = category != null;
        boolean filterStatus = status != null;
        boolean filterPriority = priority != null;

        // No filters: return all
        if (!filterAssignee && !filterCategory && !filterStatus && !filterPriority) {
            return taskRepository.findAll(pageable);
        }

        // Only priority filter
        if (filterPriority && !filterAssignee && !filterCategory && !filterStatus) {
            return taskRepository.findByPriority(priority, pageable);
        }

        // All filters: use custom method
        if (filterAssignee && filterCategory && filterStatus && filterPriority) {
            return taskRepository.findByAssigneeContainingIgnoreCaseAndCategoryAndStatusAndPriority(
                    assignee, category, status, priority, pageable);
        }

        // Assignee, category, status
        if (filterAssignee && filterCategory && filterStatus && !filterPriority) {
            return taskRepository.findByAssigneeContainingIgnoreCaseAndCategoryAndStatus(
                    assignee, category, status, pageable);
        }

        // Add more combinations as needed for clarity
        // Fallback: return all
        return taskRepository.findAll(pageable);
    }
}
