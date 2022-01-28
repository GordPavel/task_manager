package task;

import java.util.List;
import java.util.Optional;

import task.models.Task;
import task.models.TaskType;

public interface TaskConnectionsManager {
    List<TaskType> taskShouldBeSubtaskOfTaskTypes(TaskType taskType);

    List<String> getAllSubTasksOfTaskById(String taskId);

    Optional<String> getParentTaskId(String taskId);

    void saveSubtask(String parentTaskId, String subtaskId);

    void removeSubtaskConnection(String subtaskId);

    void cleanConnections();
}
