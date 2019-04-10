package me.thirtyone.group.mindmaze.resources;

import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.users.Student;

public class Document extends Resource implements Downloadable {

    public void download() {
        // TODO Complete this method
    }

    public Document(String name, Module module, Student uploader, String id) {
        super(name, module, uploader, id);
    }
}
