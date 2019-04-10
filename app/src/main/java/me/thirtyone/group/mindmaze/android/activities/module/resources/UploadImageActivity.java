package me.thirtyone.group.mindmaze.android.activities.module.resources;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.data.DatabaseAbstracter;
import me.thirtyone.group.mindmaze.data.StorageManager;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.resources.Image;
import me.thirtyone.group.mindmaze.users.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdullah on 23/03/2019 15:22
 */
public class UploadImageActivity extends AppCompatActivity {

    private static final String TAG = "UploadImageActivity";
    private static final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private Button backButton, uploadButton, prevButton, nextButton;
    private TextView label;
    private ImageView imageView;
    private ProgressDialog progressDialog;
    private EditText fileName;

    private final static int mWidth = 512;
    private final static int mLength = 512;

    private List<String> pathList;
    private int pathPos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        checkAndroidPermissions();

        Intent incIntent = getIntent();

        backButton = findViewById(R.id.uiBack);
        uploadButton = findViewById(R.id.uiUpload);
        prevButton = findViewById(R.id.uiPrev);
        nextButton = findViewById(R.id.uiNext);
        label = findViewById(R.id.uiMessage);
        imageView = findViewById(R.id.uploadedImage);
        progressDialog = new ProgressDialog(this); // Keeping track of whether or not the file was uploaded
        fileName = findViewById(R.id.uiImageName);

        pathList = new ArrayList<>();
        pathPos = 0;

        Module module = AccountRegistry.getInstance().getModuleById(incIntent.getStringExtra("modId"));

        label.setText("Uploading image resource to " + module.getName());

        // The back and forward buttons check if they need to loop around

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadImageActivity.this, CreateResourceActivity.class);
                intent.putExtra("modId", incIntent.getStringExtra("modId"));
                intent.putExtra("priorityValue", incIntent.getStringExtra("priorityValue"));
                startActivity(intent);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pathPos > 0)
                    pathPos -= 1;
                else
                    pathPos = pathList.size() -1;
                loadImageFromStorage();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pathPos < pathList.size() -1)
                    pathPos += 1;
                else
                    pathPos = 0;
                loadImageFromStorage();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fName = fileName.getText().toString();

                if(fName.equals("")) {
                    Toast.makeText(UploadImageActivity.this, "Please enter a file name", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "onClick: Uploading image...");
                progressDialog.setMessage("Uploading file");
                progressDialog.show();

                uploadImage(pathList.get(pathPos), module, progressDialog, fName);
            }
        });

        addFilePaths();
    }

    /**
     * File storage stuff, finding files from camera roll
     */
    private void addFilePaths() {
        Log.d(TAG, "addFilePaths: Adding file paths to UploadImageActivity");
        String path = System.getenv("EXTERNAL_STORAGE") + "/DCIM/Camera";

        for(File file: new File(path).listFiles()) {
            pathList.add(path + "/" + file.getName());
        }
        loadImageFromStorage();
    }

    private void loadImageFromStorage() {
        String path = pathList.get(pathPos);
        File f = new File(path, "");
        Bitmap b = null;
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            Log.d(TAG, "loadImageFromStorage: " + e.getMessage());
        }
        imageView.setImageBitmap(b);
    }

    private void checkAndroidPermissions() {
        Log.d(TAG, "checkAndroidPermissions: Checking android permissions...");

        int permissionWrite = ActivityCompat.checkSelfPermission(UploadImageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadImageActivity.this, PERMISSIONS, 1);
        }
    }

    private void uploadImage(String url, Module module, ProgressDialog progressDialog, String fileName) {
        Uri uri = Uri.fromFile(new File(url));

        String userId = AccountRegistry.getInstance().getCurrentUser().getId();

        String location = "images/" + module.getId() + "/" + userId + "/" + fileName + ".jpg";

        // Put the file at the URI into the firebase storage
        StorageManager.getInstance().getStorage()
                .child(location).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(progressDialog.getContext(), "Uploaded file successfully", Toast.LENGTH_SHORT).show();

                // Adding image resource to module list.
                String id = Long.toString(System.currentTimeMillis());

                Image image = new Image(fileName, module, ((Student)AccountRegistry.getInstance().getCurrentUser()), id, location);
                module.addResource(image);
                DatabaseAbstracter.getInstance().createImageReference(image);

                Intent intent = new Intent(UploadImageActivity.this, ViewResourcesActivity.class);
                intent.putExtra("modId", module.getId());
                intent.putExtra("priorityValue", getIntent().getStringExtra("priorityValue"));
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(progressDialog.getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
