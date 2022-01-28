package view;

import java.util.Map;
import java.util.Scanner;

import task.TaskManagerFacade;
import view.actions.console.ConsoleViewAction;
import view.actions.console.CreateTaskConsoleViewAction;
import view.actions.console.FilterTasksConsoleViewAction;
import view.actions.console.GetTaskByIdConsoleViewAction;
import view.actions.console.RemoveAllTasksConsoleViewAction;
import view.actions.console.RemoveTaskByIdConsoleViewAction;
import view.actions.console.ShowAllTasksConsoleViewAction;
import view.actions.console.UpdateTaskConsoleViewAction;

public class ConsoleTasksViewNegotiator implements TasksViewNegotiator {
    private final Map<String, ConsoleViewAction> viewActions;
    private final Scanner scanner;

    public ConsoleTasksViewNegotiator(TaskManagerFacade managerFacade) {
        this.scanner = new Scanner(System.in);
        this.viewActions = Map.of(
                "1", new CreateTaskConsoleViewAction(scanner, managerFacade),
                "2", new UpdateTaskConsoleViewAction(scanner, managerFacade),
                "3", new ShowAllTasksConsoleViewAction(managerFacade),
                "4", new GetTaskByIdConsoleViewAction(scanner, managerFacade),
                "5", new FilterTasksConsoleViewAction(scanner, managerFacade),
                "6", new RemoveTaskByIdConsoleViewAction(scanner, managerFacade),
                "7", new RemoveAllTasksConsoleViewAction(managerFacade)
        );
    }

    @Override
    public void showMenu() {
        System.out.println("Выберите действие");
        for (Map.Entry<String, ConsoleViewAction> action : viewActions.entrySet()) {
            System.out.printf("%s: %s%n", action.getKey(), action.getValue().getName());
        }
    }

    @Override
    public void greeting() {
        System.out.println("Вы вошли в трекер задач. Тут вы можете сохранять ваши текущие задачи, чтобы потом " +
                "проще было отслеживать их статус.");
    }

    @Override
    public void performAction() {
        String chooseActionInput;
        while (true) {
            chooseActionInput = scanner.nextLine();
            if (!viewActions.containsKey(chooseActionInput)) {
                System.out.println("Ошибка!!! Пожалуйста, выберите действие из меню и введите его идентификатор");
                continue;
            }
            break;
        }
        final var action = viewActions.get(chooseActionInput);
        action.performAction();
    }

}
