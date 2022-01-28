package task.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import task.TaskManager;
import task.models.FilterTaskBundle;
import task.models.Task;

public class SimpleTaskManager implements TaskManager {

    private static int idCounter = 0;
    private final Map<String, Task> tasks = new HashMap<>();

    @Override
    public String saveTask(Task task) {
        final var newTaskId = String.valueOf(idCounter++);
        tasks.put(newTaskId, new Task(newTaskId, task.getName(), task.getDescription(), task.getType()));
        return newTaskId;
    }

    @Override
    public Optional<Task> getTask(String taskId) {
        return Optional.ofNullable(tasks.get(taskId));
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void cleanTasks() {
        tasks.clear();
    }

    @Override
    public Task removeTask(String taskId) {
        final var removed = tasks.remove(taskId);
        if (removed == null) {
            throw new IllegalArgumentException(String.format("Ошибка!!! Не найдена задача с идентификатором %s", taskId));
        }
        return removed;
    }

    @Override
    public List<Task> filterTasks(FilterTaskBundle filterBundle) {
        final var allTasks = tasks.values();
        final var filteredTasks = new ArrayList<Task>();
        for (Task task : allTasks) {
            if (taskMatchFilterBundle(task, filterBundle)) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }

    @Override
    public void updateTask(Task task) {
        final var oldTask = tasks.get(task.getId());
        if (oldTask == null) {
            throw new IllegalArgumentException(String.format("Ошибка!!! Не найдена задача с идентификатором %s", task.getId()));
        }
        tasks.put(task.getId(), task);
    }

    private boolean taskMatchFilterBundle(Task task, FilterTaskBundle bundle) {
        final var idMatched = bundle.getIdPattern().map(pattern -> pattern.asPredicate().test(task.getId())).orElse(true);
        if (!idMatched) {
            return false;
        }
        final var nameMatched = bundle.getNamePattern().map(pattern -> pattern.asPredicate().test(task.getName())).orElse(true);
        if (!nameMatched) {
            return false;
        }
        final var descriptionMatched = bundle.getDescriptionPattern().map(pattern -> pattern.asPredicate().test(task.getDescription())).orElse(true);
        if (!descriptionMatched) {
            return false;
        }
        final var typeMatched = bundle.getTypes().contains(task.getType());
        return typeMatched;
    }
}
