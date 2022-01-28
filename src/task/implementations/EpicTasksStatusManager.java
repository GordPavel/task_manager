package task.implementations;

import java.util.HashMap;

import task.TaskConnectionsManager;
import task.models.TaskStatus;

public class EpicTasksStatusManager extends DefaultTaskStatusManager {
    private final TaskConnectionsManager connectionsManager;

    public EpicTasksStatusManager(TaskConnectionsManager connectionsManager) {
        this.connectionsManager = connectionsManager;
    }

    @Override
    public TaskStatus getTaskStatus(String taskId) {
        final var subtasks = connectionsManager.getAllSubTasksOfTaskById(taskId);
        final var subtaskStatuses = new HashMap<TaskStatus, Integer>();
        for (String subtaskId : subtasks) {
            final var status = tasksStatuses.get(subtaskId);
            final var statusesCount = subtaskStatuses.getOrDefault(status, 0);
            subtaskStatuses.put(status, statusesCount);
        }
        int
                inProgressCount = subtaskStatuses.getOrDefault(IN_PROGRESS_STATUS, 0),
                doneCount = subtaskStatuses.getOrDefault(DONE_STATUS, 0),
                newCount = subtaskStatuses.getOrDefault(NEW_STATUS, 0);

        if (newCount == 0 && inProgressCount == 0) {
            return DONE_STATUS;
        } else if (doneCount == 0 && inProgressCount == 0) {
            return NEW_STATUS;
        } else {
            return IN_PROGRESS_STATUS;
        }
    }

    @Override
    public void saveTaskStatus(String taskId, TaskStatus status) {

    }

    @Override
    public void removeTaskStatus(String taskId) {

    }

}
