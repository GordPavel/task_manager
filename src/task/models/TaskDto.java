package task.models;

import java.util.List;

public class TaskDto {
    private final String id;
    private final String name;
    private final String description;
    private final TaskType type;
    private final TaskStatus status;
    private final List<TaskDto> subtasks;
    private final TaskDto parentTask;

    public TaskDto(String id, String name, String description, TaskType type, TaskStatus status, List<TaskDto> subtasks, TaskDto parentTask) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = status;
        this.subtasks = subtasks;
        this.parentTask = parentTask;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskType getType() {
        return type;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public List<TaskDto> getSubtasks() {
        return subtasks;
    }

    public TaskDto getParentTask() {
        return parentTask;
    }
}
