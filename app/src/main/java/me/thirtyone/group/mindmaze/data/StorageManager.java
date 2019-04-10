package me.thirtyone.group.mindmaze.data;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import io.grpc.Context;
import me.thirtyone.group.mindmaze.android.activities.module.resources.UploadImageActivity;
import me.thirtyone.group.mindmaze.android.activities.module.resources.ViewResourcesActivity;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.resources.Image;

import java.io.File;

/**
 * @author Abdullah [17/03/2019 01:11]
 */
public class StorageManager {

    private StorageReference storage = FirebaseStorage.getInstance().getReference();
    private static StorageManager instance;

    private static final String TAG = "StorageManager";

    private StorageManager() {

    }

    /**
     * Returns the static instance of the StorageManager
     *
     * @return The static instance of the StorageManager
     */
    public static StorageManager getInstance() {
        if (instance == null)
            instance = new StorageManager();
        return instance;
    }

    /**
     * Much like the other singleton classes in this application, this method is a single use method.
     * It is to be called once when the application is initialised, likely in android.PreloadActivity.
     */
    public static void init() {
        if (instance == null)
            instance = new StorageManager();
    }

    public StorageReference getStorage() {
        return this.storage;
    }


}
