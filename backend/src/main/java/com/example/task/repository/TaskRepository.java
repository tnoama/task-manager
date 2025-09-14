
package com.example.task.repository;

import com.example.task.model.TaskItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface TaskRepository extends JpaRepository<TaskItem, UUID> {
    Page<TaskItem> findByAssigneeContainingIgnoreCaseAndCategoryAndStatus(
            String assignee, TaskItem.Category category, TaskItem.Status status, Pageable pageable);

    Page<TaskItem> findByPriority(TaskItem.Priority priority, Pageable pageable);

    Page<TaskItem> findByAssigneeContainingIgnoreCaseAndCategoryAndStatusAndPriority(
            String assignee, TaskItem.Category category, TaskItem.Status status, TaskItem.Priority priority,
            Pageable pageable);

    Page<TaskItem> findByAssigneeContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndStatusAndPriority(
            String assignee, String category, TaskItem.Status status, TaskItem.Priority priority, Pageable pageable);
}
