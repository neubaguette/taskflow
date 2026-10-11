
package ua.khnu.kpp;

import java.util.Objects;

public final class User {

    private final long id;
    private final String name;

    public User(long id, String name) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid user name");
        }

        this.id = id;
        this.name = name;
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof User other)) {
            return false;
        }

        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}