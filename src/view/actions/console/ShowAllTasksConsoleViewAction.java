package view.actions.console;

import java.util.List;
import java.util.Map;

import task.TaskManagerFacade;
import task.models.Task;
import task.models.TaskDto;
import task.models.TaskStatus;

public class ShowAllTasksConsoleViewAction implements ConsoleViewAction {
    private final TaskManagerFacade taskManager;

    public ShowAllTasksConsoleViewAction(TaskManagerFacade taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public String getName() {
        return "Получить список всех задач";
    }

    @Override
    public void performAction() {
        final List<TaskDto> tasks = taskManager.getAllTasks();
        for (TaskDto task : tasks) {
            System.out.printf("Задача %s, имя %s, статус %s%n",
                    task.getId(),
                    task.getName(),
                    task.getStatus().getName()
            );
        }
    }

}
