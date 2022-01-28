package task;

import java.util.List;
import java.util.Optional;

import task.models.FilterTaskBundle;
import task.models.Task;

public interface TaskManager {
    String saveTask(Task task);

    Optional<Task> getTask(String id);

    List<Task> getAllTasks();

    void cleanTasks();

    Task removeTask(String id);

    List<Task> filterTasks(FilterTaskBundle filterBundle);

    void updateTask(Task task);
}
