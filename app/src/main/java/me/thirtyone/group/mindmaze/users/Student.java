package me.thirtyone.group.mindmaze.users;

import com.google.firebase.database.Exclude;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.core.Reminder;
import me.thirtyone.group.mindmaze.data.DatabaseAbstracter;
import me.thirtyone.group.mindmaze.exceptions.InviteNotFoundException;
import me.thirtyone.group.mindmaze.exceptions.NoSuchStudentException;
import me.thirtyone.group.mindmaze.modules.Deadline;
import me.thirtyone.group.mindmaze.modules.Invite;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.modules.Priority;
import me.thirtyone.group.mindmaze.resources.Note;
import me.thirtyone.group.mindmaze.resources.Resource;

import java.util.*;

public class Student extends User {
    private String displayName;
    private List<Invite> invites;
    private List<Reminder> reminders;
    private List<Priority> priorities;
    private List<Note> personalNotes;

    public Student(String username, String password, String email, String id) {
        super(username, password, email, id);

        invites = new ArrayList<>();
        reminders = new ArrayList<>();
        priorities = new ArrayList<>();
        personalNotes = new ArrayList<>();
        this.displayName = username;
    }

    /**
     * Adds the module to the registry and database.
     * Creates a priority in this class as well which holds a priority value and the associated module
     * <p>
     * The ID is simply the current time in milliseconds elapsed since unix epoch
     *
     * @param name The name of the module to be created
     */
    public void createModule(String name) {
        Module m = new Module(name, Long.toString(System.currentTimeMillis()), this.getId());

        // Add a default priority for the module in the student object.
        Priority p = new Priority(m, Priority.Value.STANDARD);
        priorities.add(p);

        // Now update the database with this entry
        DatabaseAbstracter.getInstance().addModule(m);
        DatabaseAbstracter.getInstance().setPriorityValue(this, p);

        // Set up the permissions.
        m.setPermission(this, Module.Permission.OWNER);
        DatabaseAbstracter.getInstance().setPermission(m, this, Module.Permission.OWNER);

        // Update the account registry with this entry
        AccountRegistry.getInstance().addModule(m);
    }

    /**
     * Leaves the given module
     *
     * @param module The module to leave
     */
    public void leaveModule(Module module) {
        Iterator<Priority> iter = priorities.iterator();
        while (iter.hasNext()) {
            Priority p = iter.next();
            if (p.getModule().equals(module)) {
                iter.remove();
                return;
            }
        }
    }

    /**
     * Invites the given student
     *
     * @param student The student to invite
     */
    public void inviteStudent(Student student, Module module) {
        Invite invite = new Invite(Long.toString(System.currentTimeMillis()), this, student, module);
        student.addInvite(invite);
        DatabaseAbstracter.getInstance().createInvite(invite);
    }

    /**
     * Simply removes the module from the account registry.
     *
     * @param module The module to be removed from the registry.
     */
    public void deleteModule(Module module) {
        AccountRegistry.getInstance().removeModule(module);
    }

    public void createDeadline(String name, Module module, Date dueDate) {
        // TODO Complete this method
    }

    public void addInvite(Invite invite) {
        invites.add(invite);
    }

    public void joinModule(Module module) {
        priorities.add(new Priority(module, Priority.Value.STANDARD));
        module.setPermission(this, Module.Permission.VIEW); // Add the student permission to the module
    }


    /**
     * Returns a list of priorities which contains a module each
     *
     * @return A list of priority objects.
     */
    public List<Priority> getPriorities() {
        return priorities;
    }

    /**
     * Returns only the modules
     *
     * @return The list of modules for this student object
     */
    public List<Module> getModules() {
        List<Module> moduleList = new LinkedList<>();
        for (Priority p : priorities) {
            moduleList.add(p.getModule());
        }
        return moduleList;
    }

    public boolean hasPermission(Module module, Module.Permission permission) {

        if (module.getPermissions().get(this) == null)
            throw new NoSuchStudentException();

        return module.getPermissions().get(this).equals(permission); // Returns true if the permission is equal (and so user has it)
    }

    public void modifyPermission(Student student, Module module, Module.Permission permission) {
        module.setPermission(student, permission);
    }

    public void setPriority(Module module, Priority.Value priorityValue) {
        for (Priority p : this.getPriorities()) {
            if (p.getModule().equals(module)) {
                p.setPriorityValue(priorityValue);
                DatabaseAbstracter.getInstance().setPriorityValue(this, p);
            }
        }
    }

    public void setPrioritiesList(List<Priority> prioritiesList) {
        this.priorities = prioritiesList;
    }

    public void removeInvite(Invite invite) {
        if (!this.getInvites().contains(invite))
            throw new InviteNotFoundException();

        this.getInvites().remove(invite);
    }


    public void createReminder(String name, Date time, boolean repeat, String id) {
        Reminder reminder = new Reminder(name, time, id, this, repeat);
        this.reminders.add(reminder);
    }

    public void editReminder(Reminder reminder) {
        // TODO Complete this method
    }

    public void deleteReminder(Reminder reminder) {
        // TODO Complete this method
    }

    public void createDeadline(Module module, Reminder reminder) {
        // TODO Complete this method
    }

    public void editDeadline(Deadline deadline) {
        // TODO Complete this method
    }

    public void removeDeadline(Deadline deadline) {
        // TODO Complete this method
    }

    public void makePersonalNote() {
        // TODO Complete this method
    }

    public void editPersonalNote(Note note) {
        // TODO Complete this method
    }

    public void deletePersonalNote(Note note) {
        // TODO Complete this method
    }

    public void addResource() {
        // TODO Complete this method
    }

    public void makeNote(Module module) {
        // TODO Complete this method
    }

    public void editNote(Module module) {
        // TODO Complete this method
    }

    public void removeResource(Module module, Resource resource) {
        // TODO Complete this method
    }

    public boolean removeStudent(Student student, Module module) {
        // TODO Complete this method
        return false;
    }

    public void transferOwnership(Student student) {
        // TODO Complete this method
    }

    public void pinResource(Resource resource) {
        // TODO Complete this method
    }

    public List<Reminder> getReminders() {
        return this.reminders;
    }

    public List<Note> getPersonalNotes() {
        return personalNotes;
    }

    public void setInvites(List<Invite> inviteList) {
        this.invites = inviteList;
    }

    public void setReminders(List<Reminder> reminderList) {
        this.reminders = reminderList;
    }

    @Exclude
    public List<Invite> getInvites() {
        return invites;
    }
}