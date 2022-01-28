package task.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import task.TaskConnectionsManager;
import task.models.TaskType;

import static java.util.Collections.emptyList;
import static task.TaskTypesManager.EPIC_TYPE;
import static task.implementations.HardCodedTaskTypesManager.SUBTASK_TYPE;

public class SimpleTaskConnectionsManager implements TaskConnectionsManager {

    private static final Map<TaskType, List<TaskType>> SUBTYPES = Map.of(
            SUBTASK_TYPE, List.of(EPIC_TYPE)
    );

    private final Map<String, List<String>> parentTasks = new HashMap<>();

    @Override
    public List<TaskType> taskShouldBeSubtaskOfTaskTypes(TaskType taskType) {
        return SUBTYPES.getOrDefault(taskType, emptyList());
    }

    @Override
    public List<String> getAllSubTasksOfTaskById(String taskId) {
        return parentTasks.getOrDefault(taskId, emptyList());
    }

    @Override
    public Optional<String> getParentTaskId(String taskId) {
        for (Map.Entry<String, List<String>> subtasks : parentTasks.entrySet()) {
            if (subtasks.getValue().contains(taskId)) {
                return Optional.of(subtasks.getKey());
            }
        }
        return Optional.empty();
    }

    @Override
    public void cleanConnections() {
        parentTasks.clear();
    }

    @Override
    public void saveSubtask(String parentTaskId, String subtaskId) {
        final var subtasks = parentTasks.getOrDefault(parentTaskId, new ArrayList<>());
        subtasks.add(subtaskId);
        parentTasks.put(parentTaskId, subtasks);
    }

    @Override
    public void removeSubtaskConnection(String subtaskId) {
        final var parentTaskIdOptional = getParentTaskId(subtaskId);
        if (parentTaskIdOptional.isPresent()) {
            final var parentTaskId = parentTaskIdOptional.get();
            final var subtasks = parentTasks.get(parentTaskId);
            subtasks.remove(subtaskId);
            if (subtasks.isEmpty()) {
                parentTasks.remove(parentTaskId);
            } else {
                parentTasks.put(parentTaskId, subtasks);
            }
        }
    }
}
