package me.thirtyone.group.mindmaze.exceptions;

/**
 * @author Abdullah
 * <p>
 * Thrown when an area of the code is accessed by a user type that should not be able to access it.
 */
public class PermissionException extends RuntimeException {
    public PermissionException(String message) {
        super(message);
    }
}
