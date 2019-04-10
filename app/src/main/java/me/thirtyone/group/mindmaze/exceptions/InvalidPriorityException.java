package me.thirtyone.group.mindmaze.exceptions;

/**
 * @author Abdullah
 * <p>
 * Thrown when an invalid priority is parsed in the method in DatabaseUtils when parsing
 * a Priority.Enumeration value from a string.
 */
public class InvalidPriorityException extends RuntimeException {
    public InvalidPriorityException() {
        super("Please provide a valid string to parse.");
    }
}
