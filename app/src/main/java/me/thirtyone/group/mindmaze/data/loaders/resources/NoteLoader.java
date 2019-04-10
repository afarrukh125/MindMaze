package me.thirtyone.group.mindmaze.data.loaders.resources;

import com.google.firebase.database.DataSnapshot;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.resources.Note;
import me.thirtyone.group.mindmaze.users.Student;

/**
 * Created by Abdullah on 23/03/2019 21:52
 */
public class NoteLoader extends ResourceLoader {

    public NoteLoader(Module module, DataSnapshot dataSnapshot) {
        super(module, dataSnapshot);
    }

    @Override
    public void run() {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            // For each note
            String id = "";
            String uploaderId = "";
            String content = "";
            String name = "";
            for (DataSnapshot grandChild : child.getChildren()) {
                if (grandChild.getKey().equals("id"))
                    id = grandChild.getValue().toString();
                if (grandChild.getKey().equals("name"))
                    name = grandChild.getValue().toString();
                if (grandChild.getKey().equals("content"))
                    content = grandChild.getValue().toString();
                if (grandChild.getKey().equals("uploaderId"))
                    uploaderId = grandChild.getValue().toString();
            }
            module.addResource(new Note(name, content, module, ((Student) AccountRegistry.getInstance().getUserById(uploaderId)), id));
        }
    }
}
