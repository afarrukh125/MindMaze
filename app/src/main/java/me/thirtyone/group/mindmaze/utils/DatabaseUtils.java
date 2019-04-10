package me.thirtyone.group.mindmaze.utils;

import me.thirtyone.group.mindmaze.exceptions.InvalidPriorityException;
import me.thirtyone.group.mindmaze.exceptions.NoSuchPermissionException;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.modules.Priority;

import static me.thirtyone.group.mindmaze.modules.Module.Permission.*;

/***
 * @author Abdullah
 *
 * Some methods to help with parsing data from the database into the system from startup, or vice versa.
 */
public class DatabaseUtils {

    /**
     * Method that aims to map to an enumeration value in Priority.Value from a string.
     *
     * @param s The string to parse into an enumeration field
     * @return The priority value it corresponds to. If it is not found then we throw an InvalidPriorityException
     */
    public static Priority.Value parsePriorityFromString(String s) {
        if (s.equals("STANDARD"))
            return Priority.Value.STANDARD;
        if (s.equals("HIGH"))
            return Priority.Value.HIGH;
        if (s.equals("LOW"))
            return Priority.Value.LOW;
        throw new InvalidPriorityException();
    }

    /**
     * Parses a string into an enumeration value
     *
     * @param string The string to parse into the enumeration value
     * @return The enumeration value corresponding to the string
     */
    public static Module.Permission parsePermissionsFromString(String string) {
        if (string.equalsIgnoreCase("view"))
            return VIEW;
        if (string.equalsIgnoreCase("edit"))
            return EDIT;
        if (string.equalsIgnoreCase("owner"))
            return OWNER;
        throw new NoSuchPermissionException();

    }
}
