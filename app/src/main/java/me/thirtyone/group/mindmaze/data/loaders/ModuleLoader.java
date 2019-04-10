package me.thirtyone.group.mindmaze.data.loaders;

import com.google.firebase.database.DataSnapshot;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.data.loaders.resources.ImageLoader;
import me.thirtyone.group.mindmaze.data.loaders.resources.NoteLoader;
import me.thirtyone.group.mindmaze.exceptions.ModuleNotFoundException;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.users.Student;
import me.thirtyone.group.mindmaze.utils.DatabaseUtils;

/**
 * Created by Abdullah on 23/03/2019 19:49
 * Provides some helper methods to set up modules from the DatabaseAbstracter class.
 */
public class ModuleLoader {

    private static final String TAG = "ModuleLoader";

    public static void addModulesFromDatabase(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String moduleName = "";
            String moduleId = "";
            String moduleOwnerId = "";
            for (DataSnapshot gds : ds.getChildren()) {
                if (gds.getKey().equals("id"))
                    moduleId = gds.getValue().toString();
                if (gds.getKey().equals("name"))
                    moduleName = gds.getValue().toString();
                if (gds.getKey().equals("creatorId"))
                    moduleOwnerId = gds.getValue().toString();
            }

            AccountRegistry.getInstance().addModule(
                    new Module(moduleName, moduleId, moduleOwnerId)
            );
        }
    }

    public static void setupModulePermissions(DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            // We are now in the module
            Module m = AccountRegistry.getInstance().getModuleById(child.getKey());
            if (m == null)
                throw new ModuleNotFoundException();

            for (DataSnapshot grandChild : child.getChildren()) {
                if (grandChild.getKey().equals("permissions"))
                    addPermissionsToModule(m, grandChild); // The grandchild datasnapshot contains the mappings
            }
        }
    }


    /**
     * Adds permissions to a particular module
     *
     * @param module   The module to add the permissions to
     * @param snapshot The data snapshot containing mappings of user ids to permissions
     */
    private static void addPermissionsToModule(Module module, DataSnapshot snapshot) {
        for (DataSnapshot child : snapshot.getChildren()) {
            module.setPermission((Student) AccountRegistry.getInstance().getUserById(child.getKey()), DatabaseUtils.parsePermissionsFromString((String) child.getValue()));
        }
    }

    public static void setupModuleResources(DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Module module = AccountRegistry.getInstance().getModuleById(child.getKey());
            for (DataSnapshot grandChild : child.getChildren()) {
                if (grandChild.getKey().equals("resources"))
                    addResourcesToModule(module, grandChild);
            }
        }
    }

    /**
     * A method that calls for multiple threads to add resources to modules.
     * Since there may be many modules and many resources it is important to use as much CPU power as possible
     * Hence threads have been used here, as a lightweight solution
     *
     * @param module
     * @param dataSnapshot
     */
    private static void addResourcesToModule(Module module, DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            // Load all the notes for a particular module on a separate thread
            if (child.getKey().equals("notes")) {
                new Thread(new NoteLoader(module, child)).start();
            }
            if(child.getKey().equals("images")) {
                new Thread(new ImageLoader(module, child)).start();
            }
        }
    }
}
