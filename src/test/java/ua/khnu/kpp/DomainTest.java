
package ua.khnu.kpp;
import java.time.LocalDate;

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
    
    
    @Test
    void taskInitialState() {
        User owner = new User(1, "Roman");
        Project project = new Project(1, "Java Course", owner);

        Task task = new Task(
                1, "Record video lessons", project,
                java.time.LocalDate.of(2026, 9, 10),
                java.time.LocalDate.of(2026, 9, 12),
                new VariantPolicy()
        );

        assertEquals(Status.NEW, task.status());
        assertNull(task.assignee());
        assertEquals(1, task.id());
        assertEquals("Record video lessons", task.title());
        assertSame(project, task.project());
    }

    @Test
    void taskAssignmentAndStart() {
        User owner = new User(1, "Roman");
        Project project = new Project(1, "Java Course", owner);

        Task task = new Task(
                1, "Record lessons", project,
                java.time.LocalDate.of(2026, 9, 10),
                java.time.LocalDate.of(2026, 9, 12),
                new VariantPolicy()
        );

        assertThrows(IllegalStateException.class, task::start);
        assertEquals(Status.NEW, task.status());

        task.assign(owner);
        assertSame(owner, task.assignee());

        task.start();
        assertEquals(Status.ACTIVE, task.status());
    }

    @Test
    void variantPolicyRules() {
        VariantPolicy policy = new VariantPolicy();

        assertTrue(policy.allows(3, 2, true));
        assertFalse(policy.allows(3, 0, false));
        assertFalse(policy.allows(2, 0, true));
        assertTrue(policy.allows(5, 10, true));
    }

    @Test
    void rejectedCompletionKeepsState() {
        User owner = new User(1, "Roman");
        Project project = new Project(1, "Java Course", owner);

        Task task = new Task(
                1, "Record lessons", project,
                java.time.LocalDate.of(2026, 9, 10),
                java.time.LocalDate.of(2026, 9, 12),
                new VariantPolicy()
        );

        task.assign(owner);
        task.start();

        assertThrows(IllegalStateException.class,
                () -> task.complete(
                        java.time.LocalDate.of(2026, 9, 12),
                        2, 0, true));

        assertEquals(Status.ACTIVE, task.status());
        assertSame(owner, task.assignee());
    }

    @Test
    void successfulCompletion() {
        User owner = new User(1, "Roman");
        Project project = new Project(1, "Java Course", owner);

        Task task = new Task(
                1, "Record lessons", project,
                java.time.LocalDate.of(2026, 9, 10),
                java.time.LocalDate.of(2026, 9, 12),
                new VariantPolicy()
        );

        task.assign(owner);
        task.start();

        task.complete(
                java.time.LocalDate.of(2026, 9, 15),
                3, 2, true);

        assertEquals(Status.DONE, task.status());

        assertThrows(IllegalStateException.class,
                () -> task.complete(
                        java.time.LocalDate.of(2026, 9, 15),
                        3, 0, true));
    }

    
    @Test
    void invalidTaskDates() {
        User owner = new User(1, "Roman");
        Project project = new Project(1, "Java Course", owner);
        VariantPolicy policy = new VariantPolicy();

        LocalDate created = LocalDate.of(2026, 9, 10);
        LocalDate earlier = LocalDate.of(2026, 9, 9);

        assertThrows(IllegalArgumentException.class,
                () -> new Task(1, "Lessons", project,
                        created, earlier, policy));

        Task task = new Task(1, "Lessons", project,
                created, created, policy);

        assertEquals(Status.NEW, task.status());
        assertEquals(created, task.due());
    }

    @Test
    void invalidCompletionArguments() {
        User owner = new User(1, "Roman");
        Project project = new Project(1, "Java Course", owner);

        Task task = new Task(1, "Lessons", project,
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 12),
                new VariantPolicy());

        task.assign(owner);
        task.start();

        LocalDate date = LocalDate.of(2026, 9, 12);

        assertThrows(IllegalArgumentException.class,
                () -> task.complete(null, 3, 0, true));

        assertThrows(IllegalArgumentException.class,
                () -> task.complete(date, -1, 0, true));

        assertThrows(IllegalArgumentException.class,
                () -> task.complete(date, 3, -1, true));

        assertThrows(IllegalArgumentException.class,
                () -> task.complete(
                        LocalDate.of(2026, 9, 9),
                        3, 0, true));

        assertEquals(Status.ACTIVE, task.status());
        assertSame(owner, task.assignee());
    }

    @Test
    void forbiddenStateTransitions() {
        User owner = new User(1, "Roman");
        Project project = new Project(1, "Java Course", owner);

        Task task = new Task(1, "Lessons", project,
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 12),
                new VariantPolicy());

        assertThrows(IllegalStateException.class,
                () -> task.complete(
                        LocalDate.of(2026, 9, 12),
                        3, 0, true));

        task.assign(owner);
        task.start();

        assertThrows(IllegalStateException.class,
                task::start);

        assertThrows(IllegalStateException.class,
                () -> task.assign(new User(2, "Other")));

        assertEquals(Status.ACTIVE, task.status());
        assertSame(owner, task.assignee());
    }

    @Test
    void variantBoundaryValues() {
        VariantPolicy policy = new VariantPolicy();

        assertFalse(policy.allows(2, 0, true));
        assertTrue(policy.allows(3, 0, true));
        assertFalse(policy.allows(3, 0, false));

        assertTrue(policy.allows(
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                true));
    }

    
    
}