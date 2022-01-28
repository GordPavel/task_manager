package view.actions.console;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import task.TaskManagerFacade;
import task.models.TaskDto;
import task.models.TaskStatus;
import task.models.TaskType;

import static view.actions.console.ConsoleActionsUtils.chooseFromList;

public class UpdateTaskConsoleViewAction implements ConsoleViewAction {
    private final Scanner scanner;
    private final TaskManagerFacade taskManager;

    public UpdateTaskConsoleViewAction(Scanner scanner, TaskManagerFacade taskManager) {
        this.scanner = scanner;
        this.taskManager = taskManager;
    }

    @Override
    public String getName() {
        return "Обновить задачу";
    }

    @Override
    public void performAction() {
        System.out.println("Введите идентификатор задачи, которую хотите обновить");
        final var taskId = scanner.nextLine();
        Optional<TaskDto> oldTaskOptional = taskManager.getTaskById(taskId);
        if (oldTaskOptional.isEmpty()) {
            System.out.printf("Ошибка!!! По идентификатору %s задача не найдена%n", taskId);
            return;
        }
        final var oldTask = oldTaskOptional.get();

        System.out.println("Введите название задачи");
        final var taskName = scanner.nextLine();
        System.out.println("Введите описание задачи");
        final var taskDescription = scanner.nextLine();

        TaskStatus status;
        final List<TaskStatus> availableStatuses = taskManager.getAvailableStatusesForTaskType(oldTask.getType());
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

        final List<TaskType> parentTasksTypes = taskManager.taskShouldBeSubtaskOfTaskTypes(oldTask.getType());
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

        final var newTask = new TaskDto(
                oldTask.getId(),
                taskName,
                taskDescription,
                oldTask.getType(),
                status,
                null,
                new TaskDto(parentTaskId, null, null, null, null, null, null)
        );
        taskManager.updateTask(oldTask, newTask);
        System.out.printf("Задача %s успешно обновлена%n", taskId);
    }
}
