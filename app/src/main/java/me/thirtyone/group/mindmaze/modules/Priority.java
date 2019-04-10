package me.thirtyone.group.mindmaze.modules;

/**
 * This aims to be a one way association class between a student and a module.
 * Priority value enumeration will be used to decide notification handling for the currently signed in user.
 */
public class Priority {

    private Module module;
    private Priority.Value priorityValue;

    public Priority(Module module, Value priorityValue) {
        this.priorityValue = priorityValue;
        this.module = module;
    }

    public Value getPriority() {
        return priorityValue;
    }

    public Module getModule() {
        return this.module;
    }

    public enum Value {
        HIGH, STANDARD, LOW
    }

    @Override
    public String toString() {
        return module.getName();
    }

    public String getPriorityValueAsString() {
        String priToString = this.priorityValue.toString();
        return priToString.substring(0, 1) + priToString.substring(1).toLowerCase();
    }

    public void setPriorityValue(Value priorityValue) {
        this.priorityValue = priorityValue;
    }
}