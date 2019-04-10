package me.thirtyone.group.mindmaze.data.loaders.resources;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.resources.Image;
import me.thirtyone.group.mindmaze.users.Student;

/**
 * Created by Abdullah on 24/03/2019 20:46
 */
public class ImageLoader extends ResourceLoader {

    private static final String TAG = "ImageLoader";

    public ImageLoader(Module module, DataSnapshot dataSnapshot) {
        super(module, dataSnapshot);
    }

    @Override
    public void run() {
        for(DataSnapshot child: dataSnapshot.getChildren()) {
            // For every child in the module
            String id = "";
            String uploaderId = "";
            String location = "";
            String name = "";
            for (DataSnapshot grandChild : child.getChildren()) {
                Log.d(TAG, "run: " + grandChild);
                if (grandChild.getKey().equals("id"))
                    id = grandChild.getValue().toString();
                if (grandChild.getKey().equals("uploaderId"))
                    uploaderId = grandChild.getValue().toString();
                if (grandChild.getKey().equals("location"))
                    location = grandChild.getValue().toString();
                if (grandChild.getKey().equals("name"))
                    name = grandChild.getValue().toString();
            }
            Student student = (Student) AccountRegistry.getInstance().getUserById(uploaderId);
            module.addResource(new Image(name, module, student, id, location));
        }
    }
}
