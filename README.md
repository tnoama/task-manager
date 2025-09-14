# Task Manager Project

## Overview

This is a full-stack Task Manager application built with Java Spring Boot (backend) and a vanilla JavaScript/HTML frontend. It allows you to create, view, update, filter, sort, and delete tasks. Each task has fields for title, assignee, description, category, priority, status, and due date. The backend uses an H2 database for persistence.

## Features

- Add, edit, delete tasks
- Mark tasks as done
- Filter and sort tasks by category, priority, status, and more
- Pagination support
- Responsive dashboard UI

## How to Run

### Backend (Spring Boot)

1. Make sure you have Java 17+ and Maven installed.
2. From the project root, run:
   ```bash
   mvn spring-boot:run
   ```
3. The backend will start on `http://localhost:8080`.

### Frontend (Vanilla JS)

The frontend is served automatically by Spring Boot from `src/main/resources/static/index.html`.
Just open `http://localhost:8080` in your browser.

## API Endpoints

- `GET /api/tasks` - List tasks (supports filtering, sorting, pagination)
- `POST /api/tasks` - Create a new task
- `PATCH /api/tasks/{id}` - Update a task
- `DELETE /api/tasks/{id}` - Delete a task

## Database

Uses H2 embedded database. Data files are in `data/`.
