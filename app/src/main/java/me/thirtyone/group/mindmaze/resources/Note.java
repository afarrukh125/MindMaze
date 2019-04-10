package me.thirtyone.group.mindmaze.resources;

import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.users.Student;

public class Note extends Resource {

    private String content;

    public Note(String name, String content, Module module, Student uploader, String id) {
        super(name, module, uploader, id);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Document convertToDocument() {
        // TODO Complete this method
        return null;
    }

    public Image convertToImage() {
        // TODO Complete this method
        return null;
    }
}