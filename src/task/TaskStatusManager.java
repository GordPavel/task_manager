package task;

import java.util.Collection;
import java.util.List;

import task.models.TaskStatus;
import task.models.TaskType;

public interface TaskStatusManager {
    TaskStatus NEW_STATUS = new TaskStatus("NEW");
    TaskStatus DONE_STATUS = new TaskStatus("DONE");
    TaskStatus IN_PROGRESS_STATUS = new TaskStatus("IN_PROGRESS");

    List<TaskStatus> getAvailableStatusesForTaskType(TaskType type);

    List<TaskStatus> getAllStatuses(Collection<TaskType> types);

    TaskStatus getTaskStatus(String taskId);

    void saveTaskStatus(String taskId, TaskStatus status);

    void removeTaskStatus(String taskId);

    void cleanStatuses();
}
