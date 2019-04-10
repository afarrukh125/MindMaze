package me.thirtyone.group.mindmaze.modules;

import me.thirtyone.group.mindmaze.users.Student;

public class Invite {
    private String id;
    private Student receiver;
    private Module module;
    private Student sender;

    public Invite(String id, Student sender, Student receiver, Module module) {
        this.id = id;
        this.receiver = receiver;
        this.module = module;
        this.sender = sender;
    }

    public void accept() {
        receiver.joinModule(module); // Add the module to the user in the account registry
        receiver.removeInvite(this);
    }

    public void reject() {
        receiver.removeInvite(this);
    }

    public String getId() {
        return id;
    }

    public Module getModule() {
        return this.module;
    }

    public Student getSender() {
        return this.sender;
    }

    public Student getReceiver() {
        return this.receiver;
    }
}