package view.actions.console;

import task.TaskManagerFacade;

public class RemoveAllTasksConsoleViewAction implements ConsoleViewAction {
    private final TaskManagerFacade taskManager;

    public RemoveAllTasksConsoleViewAction(TaskManagerFacade taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public String getName() {
        return "Удалить все задачи";
    }

    @Override
    public void performAction() {
        taskManager.cleanTasks();

        System.out.println("Все задачи успешно удалены.");
    }
}
