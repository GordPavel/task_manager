package task.implementations;

import java.util.List;

import task.TaskTypesManager;
import task.models.TaskType;

public class HardCodedTaskTypesManager implements TaskTypesManager {

    private static final List<TaskType> ALL_TASK_TYPES = List.of(EPIC_TYPE, TASK_TYPE, SUBTASK_TYPE);

    @Override
    public List<TaskType> listAllAvailableTypes() {
        return ALL_TASK_TYPES;
    }
}
