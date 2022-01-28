package view.actions.console;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import task.TaskManagerFacade;
import task.models.FilterTaskBundle;
import task.models.TaskDto;
import task.models.TaskStatus;
import task.models.TaskType;

import static view.actions.console.ConsoleActionsUtils.chooseMultipleFromList;

public class FilterTasksConsoleViewAction implements ConsoleViewAction {
    private final Scanner scanner;
    private final TaskManagerFacade taskManager;

    public FilterTasksConsoleViewAction(Scanner scanner, TaskManagerFacade taskManager) {
        this.scanner = scanner;
        this.taskManager = taskManager;
    }

    @Override
    public String getName() {
        return "Найти задачи по фильтру";
    }

    @Override
    public void performAction() {
        System.out.println("Введите шаблон идентификатора, по которому хотите найти или оставьте пустым, если не хотите искать по идентификатору");
        final Pattern idPattern = requestInputTextFieldPattern();
        System.out.println("Введите шаблон названия, по которому хотите найти или оставьте пустым, если не хотите искать по названию");
        final Pattern namePattern = requestInputTextFieldPattern();
        System.out.println("Введите шаблон идентификатора, по которому хотите найти или оставьте пустым, если не хотите искать по идентификатору");
        final Pattern descriptionPattern = requestInputTextFieldPattern();
        System.out.println("Выберите 1 или несколько типов задач, по которым хотите фильтровать. " +
                "Для поиска по всем оставьте пустым. " +
                "Для выбора нескольких вариантов введите их идентификаторы через ,");
        final List<TaskType> types = taskManager.listAllAvailableTypes();
        for (int i = 0; i < types.size(); i++) {
            System.out.printf("%d: %s%n", i + 1, types.get(i).getName());
        }
        final var typesPattern = chooseMultipleFromList(types, scanner, "Ошибка!!! Пожалуйста, выберите тип из списка");
        System.out.println("Выберите 1 или несколько статусов задач, по которым хотите фильтровать. " +
                "Для поиска по всем оставьте пустым. " +
                "Для выбора нескольких вариантов введите их идентификаторы через ,");

        final List<TaskStatus> statuses = taskManager.getAvailableStatuses(types);
        for (int i = 0; i < statuses.size(); i++) {
            System.out.printf("%d: %s%n", i + 1, statuses.get(i).getName());
        }
        final var statusesPattern = chooseMultipleFromList(statuses, scanner, "Ошибка!!! Пожалуйста, выберите статус из списка");

        final List<TaskDto> filteredTasks = taskManager.filterTasks(new FilterTaskBundle(
                idPattern,
                namePattern,
                descriptionPattern,
                new HashSet<>(statusesPattern),
                new HashSet<>(typesPattern)
        ));

        for (TaskDto task : filteredTasks) {
            System.out.printf("Задача %s, имя %s, статус %s%n",
                    task.getId(),
                    task.getName(),
                    task.getStatus().getName()
            );
        }
    }

    private Pattern requestInputTextFieldPattern() {
        final var regexInput = scanner.nextLine();
        Pattern pattern = null;
        if (!regexInput.isBlank()) {
            while (true) {
                try {
                    pattern = Pattern.compile(regexInput);
                    break;
                } catch (PatternSyntaxException e) {
                    System.out.println("Ошибка!!! Введен некорректный паттерн для поиска");
                }
            }
        }
        return pattern;
    }

}
