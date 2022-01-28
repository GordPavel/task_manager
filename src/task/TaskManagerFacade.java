package task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import task.models.FilterTaskBundle;
import task.models.Task;
import task.models.TaskDto;
import task.models.TaskStatus;
import task.models.TaskType;

import static java.lang.String.format;

public class TaskManagerFacade {
    private final TaskManager taskManager;
    private final TaskTypesManager typesManager;
    private final TaskStatusManager defaultStatusManager;
    private final Map<TaskType, TaskStatusManager> specificStatusManagers;
    private final TaskConnectionsManager connectionsManager;

    public TaskManagerFacade(
            TaskManager taskManager,
            TaskTypesManager typesManager,
            TaskStatusManager defaultStatusManager,
            Map<TaskType, TaskStatusManager> specificStatusManagers,
            TaskConnectionsManager connectionsManager
    ) {
        this.taskManager = taskManager;
        this.typesManager = typesManager;
        this.defaultStatusManager = defaultStatusManager;
        this.specificStatusManagers = specificStatusManagers;
        this.connectionsManager = connectionsManager;
    }

    public List<TaskDto> getAllTasks() {
        final var tasks = taskManager.getAllTasks();
        final var taskDtos = new ArrayList<TaskDto>();
        for (Task task : tasks) {
            final var taskStatus = getTaskStatus(task.getId());
            new TaskDto(
                    task.getId(),
                    task.getName(),
                    task.getDescription(),
                    task.getType(),
                    taskStatus,
                    null,
                    null
            );
        }
        return taskDtos;
    }

    public Optional<TaskDto> getTaskById(String taskId) {
        final var taskOptional = taskManager.getTask(taskId);
        if (taskOptional.isEmpty()) {
            return Optional.empty();
        }
        final var task = taskOptional.get();
        final var status = getTaskStatus(taskId);
        final var subtasksIds = connectionsManager.getAllSubTasksOfTaskById(taskId);
        final var subtasks = new ArrayList<TaskDto>();

        for (String subtaskId : subtasksIds) {
            final var subtask = taskManager.getTask(subtaskId).get();
            final var subtaskStatus = getTaskStatus(subtaskId);
            subtasks.add(new TaskDto(
                    subtask.getId(),
                    subtask.getName(),
                    subtask.getDescription(),
                    subtask.getType(),
                    subtaskStatus,
                    null,
                    null
            ));
        }

        Optional<String> parentTaskId = connectionsManager.getParentTaskId(task.getId());
        TaskDto parentTaskDto;
        if (parentTaskId.isEmpty()) {
            parentTaskDto = null;
        } else {
            final var parentTask = taskManager.getTask(parentTaskId.get()).get();
            final var parentTaskStatus = getTaskStatus(parentTask.getId());
            parentTaskDto = new TaskDto(
                    parentTask.getId(),
                    parentTask.getName(),
                    parentTask.getDescription(),
                    parentTask.getType(),
                    parentTaskStatus,
                    null,
                    null
            );
        }
        return Optional.of(new TaskDto(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getType(),
                status,
                subtasks,
                parentTaskDto
        ));
    }

    public String saveTask(TaskDto task) {
        final var availableParentTaskTypes = taskShouldBeSubtaskOfTaskTypes(task.getType());
        final var parentTaskOptional = getTaskById(task.getParentTask().getId());
        if (parentTaskOptional.isEmpty()) {
            throw new IllegalArgumentException(format("Ошибка!!! Задача с идентификатором %s не найдена", task.getParentTask().getId()));
        }
        final var parentTask = parentTaskOptional.get();
        if (!availableParentTaskTypes.contains(parentTask.getType())) {
            StringBuilder errorMessage = new StringBuilder(format("Ошибка!!! Для задачи типа %s родительская задача должна быть одним из типов:%n", task.getType().getName()));
            for (TaskType type : availableParentTaskTypes) {
                errorMessage.append(type.getName()).append("\n");
            }
            errorMessage.append(format("Но введенная задача %s типа %s", parentTask.getId(), parentTask.getType().getName()));
            throw new IllegalArgumentException(errorMessage.toString());
        }
        final var newTaskId = taskManager.saveTask(new Task(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getType()
        ));
        connectionsManager.saveSubtask(task.getParentTask().getId(), task.getId());
        defaultStatusManager.saveTaskStatus(task.getId(), task.getStatus());
        return newTaskId;
    }

    public void updateTask(TaskDto oldTask, TaskDto newTask) {
        if (!oldTask.getId().equals(newTask.getId())) {
            throw new IllegalArgumentException("Ошибка!!! Идентификаторы обновляемой и новой задачи не равны");
        }
        taskManager.updateTask(new Task(
                newTask.getId(),
                newTask.getName(),
                newTask.getDescription(),
                newTask.getType()
        ));
        connectionsManager.removeSubtaskConnection(oldTask.getId());
        connectionsManager.saveSubtask(newTask.getParentTask().getId(), newTask.getId());
        defaultStatusManager.removeTaskStatus(oldTask.getId());
        defaultStatusManager.saveTaskStatus(newTask.getId(), newTask.getStatus());
    }

    public void removeTask(String taskId) {
        final var subTasks = connectionsManager.getAllSubTasksOfTaskById(taskId);
        if (!subTasks.isEmpty()) {
            throw new IllegalArgumentException(format(
                    "Ошибка!!! У задачи %s есть %d подзадач. Нельзя удалять такую задачу%n", taskId, subTasks.size()
            ));
        }
        taskManager.removeTask(taskId);
        defaultStatusManager.removeTaskStatus(taskId);
        connectionsManager.removeSubtaskConnection(taskId);
    }

    public void cleanTasks() {
        taskManager.cleanTasks();
        connectionsManager.cleanConnections();
        defaultStatusManager.cleanStatuses();
    }

    public List<TaskDto> filterTasks(FilterTaskBundle filterTaskBundle) {
        final var tasks = taskManager.filterTasks(filterTaskBundle);
        final var dtos = new ArrayList<TaskDto>(tasks.size());
        for (Task task : tasks) {
            final var taskStatus = getTaskStatus(task.getId());
            if (filterTaskBundle.getStatuses().contains(taskStatus)) {
                dtos.add(new TaskDto(
                        task.getId(),
                        task.getName(),
                        task.getDescription(),
                        task.getType(),
                        taskStatus,
                        null,
                        null
                ));
            }
        }
        return dtos;
    }

    public List<TaskStatus> getAvailableStatusesForTaskType(TaskType taskType) {
        return defaultStatusManager.getAvailableStatusesForTaskType(taskType);
    }

    public List<TaskType> listAllAvailableTypes() {
        return typesManager.listAllAvailableTypes();
    }

    public List<TaskStatus> getAvailableStatuses(Collection<TaskType> types) {
        return defaultStatusManager.getAllStatuses(types);
    }

    public List<TaskType> taskShouldBeSubtaskOfTaskTypes(TaskType taskType) {
        return connectionsManager.taskShouldBeSubtaskOfTaskTypes(taskType);
    }

    private TaskStatus getTaskStatus(String taskId) {
        final var taskOptional = taskManager.getTask(taskId);
        if (taskOptional.isEmpty()) {
            throw new IllegalArgumentException(format("Ошибка!!! Не найдена задача с идентификатором %s", taskId));
        }
        final var task = taskOptional.get();
        final var statusManager = specificStatusManagers.getOrDefault(task.getType(), defaultStatusManager);
        return statusManager.getTaskStatus(taskId);
    }
}
