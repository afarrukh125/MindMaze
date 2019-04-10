package me.thirtyone.group.mindmaze.modules;

import me.thirtyone.group.mindmaze.core.Reminder;
import me.thirtyone.group.mindmaze.users.Student;

import java.util.Date;

public class Deadline {
    private String id;
    private Module module;
    private Reminder reminder;
    private Student creator;

    public Deadline(Module module, Reminder reminder) {
        // TODO Complete this method
    }

    public Deadline(String name, Module module, Date dueDate, Student student) {
        // TODO Complete this method
    }

    public Reminder getReminder() {
        return this.reminder;
    }

    public String getId() {
        return this.id;
    }

    public Student getCreator() {
        return this.creator;
    }
}