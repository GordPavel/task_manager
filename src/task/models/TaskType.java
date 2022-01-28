package task.models;

public class TaskType {
    private final String name;

    public TaskType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TaskType{" +
                "name='" + name + '\'' +
                '}';
    }
}
