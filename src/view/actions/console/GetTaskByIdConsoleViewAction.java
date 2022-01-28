package view.actions.console;

import java.util.Scanner;

import task.TaskManagerFacade;
import task.models.TaskDto;

public class GetTaskByIdConsoleViewAction implements ConsoleViewAction {
    private final Scanner scanner;
    private final TaskManagerFacade taskManager;

    public GetTaskByIdConsoleViewAction(Scanner scanner, TaskManagerFacade taskManager) {
        this.scanner = scanner;
        this.taskManager = taskManager;
    }

    @Override
    public String getName() {
        return "Получить задачу по идентификатору";
    }

    @Override
    public void performAction() {
        System.out.println("Введите идентификатор задачи");
        final var taskId = scanner.nextLine();
        final var optionalTask = taskManager.getTaskById(taskId);
        if (optionalTask.isEmpty()) {
            System.out.printf("К сожалению не удалось найти задачу по идентификатору %s%n", taskId);
        } else {
            final var task = optionalTask.get();
            System.out.printf("Задача %s, имя %s, описание %s, статус %s%n",
                    task.getId(),
                    task.getName(),
                    task.getDescription(),
                    task.getStatus().getName()
            );
            final var parentTask = task.getParentTask();
            if (parentTask != null) {
                System.out.printf("Родительская задача %s, имя %s, описание %s, статус %s%n",
                        parentTask.getId(),
                        parentTask.getName(),
                        parentTask.getDescription(),
                        parentTask.getStatus().getName()
                );
            }
            for (TaskDto subtask : task.getSubtasks()) {
                System.out.printf("Подзадача %s, имя %s, статус %s%n",
                        subtask.getId(),
                        subtask.getName(),
                        subtask.getStatus().getName()
                );
            }
        }
    }

}
