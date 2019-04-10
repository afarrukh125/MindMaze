package me.thirtyone.group.mindmaze.core;

import me.thirtyone.group.mindmaze.exceptions.ModuleNotFoundException;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.users.Student;
import me.thirtyone.group.mindmaze.users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class aims to be a live database object.
 * This holds data about modules, and users.
 * It is essentially a database object, updated when modules or users change.
 * <p>
 * We use this object when updating what we know about modules and users.
 * Objects of type Priority, Permissions are stored in their respective objects.
 * <p>
 * I am aware that the class diagram has not been strictly followed.
 * There is the addition of a few methods and modification of existing ones.
 * <p>
 * There are two attributes, of type Map<K, V> used in order to speed up the time taken to retrieve data (at the expense of some memory)
 * These were now added to this object.
 * <p>
 * For example, doesUserExist(String userName) is not a very useful method overall. As a matter of implementation
 * it might be easier to have a method called getStudentByName() and instead check if the value returned is null.
 */
public class AccountRegistry {
    private static AccountRegistry instance;
    private List<User> users;
    private List<Module> modules;
    private User currentUser;

    // The below instance variables are maps. These aim to decrease time taken to search for modules and users.
    // These aim to be Θ(1) in the best case instead of the possible O(n) that occurs when iterating.

    private Map<String, User> idUserMap; // Used to map a user's id to the user object
    private Map<String, Module> idModuleMap; // Used to map a module id to the module object

    private AccountRegistry() {
        this.users = new ArrayList<>();
        this.modules = new ArrayList<>();

        idUserMap = new HashMap<>();
        idModuleMap = new HashMap<>();
    }

    public static AccountRegistry getInstance() {
        if (instance == null) {
            instance = new AccountRegistry();
        }
        return instance;
    }

    /**
     * To be called exactly once. Calling it thereafter is useless.
     * This method was simply created for clarity in the PreloadActivity class.
     */
    public static void init() {
        if (instance != null)
            return;
        instance = new AccountRegistry();
    }

    public List<User> getUsers() {
        return users;
    }

    /**
     * By returning a new list with the modules, our modules list is immutable.
     *
     * @return A list of modules
     */
    public List<Module> getModules() {
        return new ArrayList<>(modules);
    }

    public void addUser(User u) {
        this.users.add(u);
        idUserMap.put(u.getId(), u);
    }

    public void addModule(Module m) {
        this.modules.add(m);
        idModuleMap.put(m.getId(), m);
    }

    public User getUserByName(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username))
                return u;
        }
        return null;
    }

    public User getUserById(String id) {
        return idUserMap.get(id);
    }

    public void setSignedInUser(User u) {
        this.currentUser = u;
    }

    public void signOutUser() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean hasModule(Student student, String moduleId) {
        for (Module m : student.getModules()) {
            if (m.getId().equals(moduleId))
                return true;
        }
        return false;
    }

    public Module getModuleById(String id) {
        return idModuleMap.get(id);
    }

    public void removeModule(Module m) {
        if (!modules.contains(m))
            throw new ModuleNotFoundException();
        modules.remove(m);
    }
}