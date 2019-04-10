package me.thirtyone.group.mindmaze.android.activities.module.resources;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.android.adapters.ResourceListAdapter;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.data.StorageManager;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.resources.Image;
import me.thirtyone.group.mindmaze.resources.Resource;

/**
 * Created by Abdullah on 24/03/2019 18:36
 */
public class ViewImageActivity extends AppCompatActivity {

    private static final String TAG = "ViewImageActivity";

    private ImageView imageView;
    private Button backButton;
    private Image image;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        backButton = findViewById(R.id.viBackButton);
        imageView = findViewById(R.id.viImage);

        Intent incIntent = getIntent();
        Module module = AccountRegistry.getInstance().getModuleById(incIntent.getStringExtra("modId"));
        for(Resource r: module.getResources()) {
            if(r.getId().equals(incIntent.getStringExtra("resId"))) {
                image = (Image) r;
                break;
            }
        }

        StorageReference storage = StorageManager.getInstance().getStorage();

        StorageReference mRef = storage.child(image.getURL());

        final long ONE_MEGABYTE = 1024 * 1024;
        mRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        backButton.setOnClickListener(new ViewResourcesListener(this, getIntent()));
    }
}
