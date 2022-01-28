package view.actions.console;

import java.util.Scanner;

import task.TaskManagerFacade;

public class RemoveTaskByIdConsoleViewAction implements ConsoleViewAction {
    private final Scanner scanner;
    private final TaskManagerFacade taskManager;

    public RemoveTaskByIdConsoleViewAction(Scanner scanner, TaskManagerFacade taskManager) {
        this.scanner = scanner;
        this.taskManager = taskManager;
    }

    @Override
    public String getName() {
        return "Удалить задачу по идентификатору";
    }

    @Override
    public void performAction() {
        System.out.println("Введите идентификатор задачи для удаления");
        final var taskForRemoveId = scanner.nextLine();
        taskManager.removeTask(taskForRemoveId);
        System.out.printf("Задача %s успешно удалена%n", taskForRemoveId);
    }
}
