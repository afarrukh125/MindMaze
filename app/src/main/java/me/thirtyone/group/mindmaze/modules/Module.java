package me.thirtyone.group.mindmaze.modules;

import com.google.firebase.database.Exclude;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.resources.Resource;
import me.thirtyone.group.mindmaze.users.Student;
import me.thirtyone.group.mindmaze.users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Module {
    private String name;
    private String moduleId;
    private Map<Student, Permission> permissionsMap;
    private String creatorId;
    private List<Resource> resources;
    private List<Deadline> deadlines;

    public Module(String name, String id, String ownerId) {
        this.name = name;
        this.moduleId = id;
        this.creatorId = ownerId;

        permissionsMap = new HashMap<>();

        this.resources = new ArrayList<>();
        this.deadlines = new ArrayList<>();

    }

    public String getCreatorId() {
        return this.creatorId;
    }

    public List<Resource> getResources() {
        return resources;
    }

    @Exclude
    public User getOwner() {
        return AccountRegistry.getInstance().getUserById(this.getCreatorId());
    }

    /**
     * Returns a list of students for the module
     *
     * @return A list of students in the module
     */
    public List<Student> getStudents() {
        return new ArrayList<>(permissionsMap.keySet());
    }

    public List<Deadline> getDeadlines() {
        return deadlines;
    }

    public void addDeadline(Deadline deadline) {
        deadlines.add(deadline);
    }

    public void addResource(Resource r) {
        resources.add(r);
    }

    public String getId() {
        return moduleId;
    }

    public String getName() {
        return name;
    }

    public void setPermission(Student student, Permission permission) {
        permissionsMap.put(student, permission);
    }

    public void removePermission(Student student) {
        permissionsMap.remove(student);
    }

    @Exclude
    public Map<Student, Permission> getPermissions() {
        return this.permissionsMap;
    }

    public enum Permission {
        VIEW,
        EDIT,
        OWNER;
    }
}