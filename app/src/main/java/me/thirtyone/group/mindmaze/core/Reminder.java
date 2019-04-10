package me.thirtyone.group.mindmaze.core;

import com.google.firebase.database.Exclude;
import me.thirtyone.group.mindmaze.users.Student;

import java.util.Date;

public class Reminder {
    private Date time;
    private String message;
    private boolean repeat;
    private String id;
    private Student student;

    public Reminder(String name, Date time, String id, Student student, boolean repeat) {
        this.time = time;
        this.message = name;
        this.id = id;
        this.student = student;
        this.repeat = repeat;
    }

    public void markAsComplete() {
        // TODO Complete this method
    }

    public boolean isRepeating() {
        return repeat;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Exclude
    public Date getTime() {
        return this.time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String newName) {
        this.message = newName;
    }

    @Exclude
    public Student getCreator() {
        return student;
    }

    public String getId() {
        return id;
    }
}