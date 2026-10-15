
package ua.khnu.kpp;

import java.time.LocalDate;

public final class Task {

    private final long id;
    private final String title;
    private final Project project;
    private final LocalDate created;
    private final LocalDate due;
    private final CompletionPolicy policy;

    private Status status;
    private User assignee;

    public Task(long id, String title, Project project,
                LocalDate created, LocalDate due,
                CompletionPolicy policy) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid task ID");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Invalid task title");
        }

        if (project == null || created == null ||
            due == null || policy == null) {
            throw new IllegalArgumentException("Required value is null");
        }

        if (due.isBefore(created)) {
            throw new IllegalArgumentException(
                    "Due date cannot be before creation date");
        }

        this.id = id;
        this.title = title;
        this.project = project;
        this.created = created;
        this.due = due;
        this.policy = policy;

        this.status = Status.NEW;
        this.assignee = null;
    }

    public long id() {
        return id;
    }

    public String title() {
        return title;
    }

    public Project project() {
        return project;
    }

    public LocalDate created() {
        return created;
    }

    public LocalDate due() {
        return due;
    }

    public Status status() {
        return status;
    }

    public User assignee() {
        return assignee;
    }

    public void assign(User user) {
        if (status != Status.NEW) {
            throw new IllegalStateException(
                    "Assignment is only allowed in NEW status");
        }

        if (user == null) {
            throw new IllegalArgumentException(
                    "Assignee cannot be null");
        }

        this.assignee = user;
    }

    public void start() {
        if (status != Status.NEW) {
            throw new IllegalStateException(
                    "Task is not in NEW status");
        }

        if (assignee == null) {
            throw new IllegalStateException(
                    "Task has no assignee");
        }

        this.status = Status.ACTIVE;
    }

    public void complete(LocalDate date, int units,
                         int defects, boolean approved) {

        if (status != Status.ACTIVE) {
            throw new IllegalStateException(
                    "Task must be ACTIVE to complete");
        }

        if (date == null || date.isBefore(created)) {
            throw new IllegalArgumentException(
                    "Invalid completion date");
        }

        if (units < 0 || defects < 0) {
            throw new IllegalArgumentException(
                    "Units and defects cannot be negative");
        }

        if (!policy.allows(units, defects, approved)) {
            throw new IllegalStateException(
                    "Completion policy rejected the result");
        }

        this.status = Status.DONE;
    }
}