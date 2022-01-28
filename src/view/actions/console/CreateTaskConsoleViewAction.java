package view.actions.console;

import java.util.List;
import java.util.Scanner;

import task.TaskManagerFacade;
import task.models.TaskDto;
import task.models.TaskStatus;
import task.models.TaskType;

import static view.actions.console.ConsoleActionsUtils.chooseFromList;

public class CreateTaskConsoleViewAction implements ConsoleViewAction {
    private final Scanner scanner;
    private final TaskManagerFacade taskManager;

    public CreateTaskConsoleViewAction(Scanner scanner, TaskManagerFacade taskManager) {
        this.scanner = scanner;
        this.taskManager = taskManager;
    }

    @Override
    public String getName() {
        return "Создать новую задачу";
    }

    @Override
    public void performAction() {
        final List<TaskType> taskTypes = taskManager.listAllAvailableTypes();
        TaskType taskType;
        if (taskTypes.isEmpty()) {
            throw new IllegalStateException("Empty task types list");
        } else if (taskTypes.size() == 1) {
            taskType = taskTypes.get(0);
        } else {
            System.out.println("Выберите тип задачи");
            taskType = chooseFromList(
                    taskTypes,
                    scanner,
                    "Ошибка!!! Пожалуйста, выберите тип задачи из предложенных и введите идентификатор"
            );
        }

        System.out.println("Введите название задачи");
        final var taskName = scanner.nextLine();
        System.out.println("Введите описание задачи");
        final var taskDescription = scanner.nextLine();

        TaskStatus status;
        final List<TaskStatus> availableStatuses = taskManager.getAvailableStatusesForTaskType(taskType);
        if (availableStatuses.isEmpty()) {
            status = null;
        } else if (availableStatuses.size() == 1) {
            status = availableStatuses.get(0);
        } else {
            System.out.println("Выберите статус задачи");
            status = chooseFromList(
                    availableStatuses,
                    scanner,
                    "Ошибка!!! Пожалуйста, выберите статус задачи из предложенных и введите идентификатор"
            );
        }

        final List<TaskType> parentTasksTypes = taskManager.taskShouldBeSubtaskOfTaskTypes(taskType);
        final String parentTaskId;
        if (!parentTasksTypes.isEmpty()) {
            System.out.println("Вы создаете подзадачу. Нужно ввести идентификатор задачи одного из следующих типов, " +
                    "в состав которой вы хотите добавить новую задачу");
            for (TaskType type : parentTasksTypes) {
                System.out.println(type.getName());
            }
            System.out.println("Введите идентификатор родительской задачи");
            parentTaskId = scanner.nextLine();
        } else {
            parentTaskId = null;
        }

        final var task = new TaskDto(
                null,
                taskName,
                taskDescription,
                taskType,
                status,
                null,
                new TaskDto(parentTaskId, null, null, null, null, null, null)
        );
        final String taskId = taskManager.saveTask(task);

        System.out.printf("Задача %s успешно создана%n", taskId);
    }

}
