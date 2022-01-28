package task.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import task.TaskStatusManager;
import task.models.TaskStatus;
import task.models.TaskType;

import static java.util.Collections.emptyList;

public class DefaultTaskStatusManager implements TaskStatusManager {

    protected static final Map<TaskType, List<TaskStatus>> TYPES_AVAILABLE_STATUSES = Map.of(
            new TaskType("TASK"), List.of(NEW_STATUS, IN_PROGRESS_STATUS, DONE_STATUS),
            new TaskType("SUBTASK"), List.of(NEW_STATUS, IN_PROGRESS_STATUS, DONE_STATUS)
    );
    protected final Map<String, TaskStatus> tasksStatuses = new HashMap<>();

    @Override
    public List<TaskStatus> getAvailableStatusesForTaskType(TaskType type) {
        return TYPES_AVAILABLE_STATUSES.getOrDefault(type, emptyList());
    }

    @Override
    public List<TaskStatus> getAllStatuses(Collection<TaskType> types) {
        final var result = new HashSet<TaskStatus>();
        for (TaskType type : types) {
            result.addAll(getAvailableStatusesForTaskType(type));
        }
        return new ArrayList<>(result);
    }

    @Override
    public TaskStatus getTaskStatus(String taskId) {
        return tasksStatuses.get(taskId);
    }

    @Override
    public void saveTaskStatus(String taskId, TaskStatus status) {
        tasksStatuses.put(taskId, status);
    }

    @Override
    public void removeTaskStatus(String taskId) {
        tasksStatuses.remove(taskId);
    }

    @Override
    public void cleanStatuses() {
        tasksStatuses.clear();
    }
}
