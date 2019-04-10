package me.thirtyone.group.mindmaze.data.loaders.resources;

import com.google.firebase.database.DataSnapshot;
import me.thirtyone.group.mindmaze.modules.Module;

/**
 * Created by Abdullah on 23/03/2019 21:49
 * <p>
 * This is a loader for each module and is to be run on a separate thread
 */
public abstract class ResourceLoader implements Runnable {

    protected Module module;
    protected DataSnapshot dataSnapshot;

    public ResourceLoader(Module module, DataSnapshot dataSnapshot) {
        this.module = module;
        this.dataSnapshot = dataSnapshot;
    }
}
