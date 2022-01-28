package task.models;

public class Task {
    private final String id;
    private final String name;
    private final String description;
    private final TaskType type;

    public Task(String name, String description, TaskType type) {
        this(null, name, description, type);
    }

    public Task(String id, String name, String description, TaskType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
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

}
