package me.thirtyone.group.mindmaze.resources;

import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.users.Student;

public class Image extends Resource implements Downloadable {
    public Image(String name, Module module, Student uploader, String id, String url) {
        super(name, module, uploader, id);
        this.url = url;
    }

    public void download() {
        // TODO Complete this method
    }
}