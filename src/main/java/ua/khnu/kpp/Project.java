
package ua.khnu.kpp;

public final class Project {

    private final long id;
    private final String title;
    private final User owner;

    public Project(long id, String title, User owner) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid project ID");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Invalid project title");
        }

        if (owner == null) {
            throw new IllegalArgumentException("Project owner is required");
        }

        this.id = id;
        this.title = title;
        this.owner = owner;
    }

    public long id() {
        return id;
    }

    public String title() {
        return title;
    }

    public User owner() {
        return owner;
    }
}