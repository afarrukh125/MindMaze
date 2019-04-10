package me.thirtyone.group.mindmaze.resources;

import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.users.Student;

public abstract class Resource {
    protected String name;
    protected String id;
    protected String url;
    protected Module module;
    protected Student uploader;

    public Resource(String name, Module module, Student uploader, String id) {
        this.name = name;
        this.id = id;
        this.module = module;
        this.uploader = uploader;
    }

    public String getURL() {
        return url;
    }

    public Module getModule() {
        return this.module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Student getUploader() {
        return uploader;
    }
}