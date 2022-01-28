package task.models;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class FilterTaskBundle {
    Pattern idPattern;
    Pattern namePattern;
    Pattern descriptionPattern;
    Set<TaskStatus> statuses;
    Set<TaskType> types;

    public FilterTaskBundle(
            Pattern idPattern,
            Pattern namePattern,
            Pattern descriptionPattern,
            Set<TaskStatus> statuses,
            Set<TaskType> types
    ) {
        this.idPattern = idPattern;
        this.namePattern = namePattern;
        this.descriptionPattern = descriptionPattern;
        this.statuses = statuses;
        this.types = types;
    }

    public Optional<Pattern> getIdPattern() {
        return Optional.ofNullable(idPattern);
    }

    public Optional<Pattern> getNamePattern() {
        return Optional.ofNullable(namePattern);
    }

    public Optional<Pattern> getDescriptionPattern() {
        return Optional.ofNullable(descriptionPattern);
    }

    public Set<TaskStatus> getStatuses() {
        return statuses;
    }

    public Set<TaskType> getTypes() {
        return types;
    }
}
