
package ua.khnu.kpp;

import java.time.LocalDate;

public class App {

    public static void main(String[] args) {

        User editor = new User(1, "Roman");
        User author = new User(2, "Video Author");

        Project project = new Project(
                1, "Java Video Course", editor);

        Task task = new Task(
                1,
                "Record Java video lessons",
                project,
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 12),
                new VariantPolicy()
        );

        System.out.println("Project: " + project.title());
        System.out.println("Task: " + task.title());
        System.out.println("Initial status: " + task.status());

        task.assign(author);
        task.start();

        System.out.println("After start: " + task.status());

        try {
            task.complete(
                    LocalDate.of(2026, 9, 12),
                    2, 0, true
            );
        } catch (IllegalStateException e) {
            System.out.println("Completion rejected: "
                    + e.getMessage());
        }

        System.out.println("Status after rejection: "
                + task.status());

        task.complete(
                LocalDate.of(2026, 9, 15),
                3, 2, true
        );

        System.out.println("Final status: " + task.status());
    }
}
