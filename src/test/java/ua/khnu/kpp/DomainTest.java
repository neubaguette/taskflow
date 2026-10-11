
package ua.khnu.kpp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DomainTest {

    @Test
    void userFieldsAndIdentity() {
        User user1 = new User(1, "Roman");
        User user2 = new User(1, "Another User");
        User user3 = new User(2, "Roman");

        assertEquals(1, user1.id());
        assertEquals("Roman", user1.name());

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1, user3);
    }

    @Test
    void invalidUsers() {
        assertThrows(IllegalArgumentException.class,
                () -> new User(0, "Roman"));

        assertThrows(IllegalArgumentException.class,
                () -> new User(-1, "Roman"));

        assertThrows(IllegalArgumentException.class,
                () -> new User(1, null));

        assertThrows(IllegalArgumentException.class,
                () -> new User(1, ""));

        assertThrows(IllegalArgumentException.class,
                () -> new User(1, "   "));
    }

    @Test
    void projectContract() {
        User owner = new User(1, "Roman");
        Project project = new Project(10, "Java Video Course", owner);

        assertEquals(10, project.id());
        assertEquals("Java Video Course", project.title());
        assertSame(owner, project.owner());

        assertThrows(IllegalArgumentException.class,
                () -> new Project(0, "Course", owner));

        assertThrows(IllegalArgumentException.class,
                () -> new Project(1, "", owner));

        assertThrows(IllegalArgumentException.class,
                () -> new Project(1, "Course", null));
    }
}