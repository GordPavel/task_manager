package task;

import java.util.List;

import task.models.TaskType;

public interface TaskTypesManager {
    TaskType EPIC_TYPE = new TaskType("EPIC");
    TaskType TASK_TYPE = new TaskType("TASK");
    TaskType SUBTASK_TYPE = new TaskType("SUBTASK");

    List<TaskType> listAllAvailableTypes();
}
