package task.models;

public class TaskStatus {
    private final String name;

    public TaskStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TaskStatus{" +
                "name='" + name + '\'' +
                '}';
    }
}
