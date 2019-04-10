package me.thirtyone.group.mindmaze.android.activities.module.resources;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import me.thirtyone.group.mindmaze.R;

/**
 * Created by Abdullah on 17/03/2019 02:51
 */
public class CreateResourceActivity extends AppCompatActivity {

    private static final String TAG = "CreateResourceActivity";
    private static final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private Button createNoteButton, createImageButton, backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_resource);

        TextView crWhichLabel = findViewById(R.id.crWhich);
        createNoteButton = findViewById(R.id.crNote);
        createImageButton = findViewById(R.id.crImage);
        backButton = findViewById(R.id.crDecideBack);

        crWhichLabel.setText("What kind of resource would you like to create?");

        Intent incIntent = getIntent();

        createNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateResourceActivity.this, CreateNoteActivity.class);
                intent.putExtra("modId", incIntent.getStringExtra("modId"));
                intent.putExtra("priorityValue", incIntent.getStringExtra("priorityValue"));
                startActivity(intent);
            }
        });


        backButton.setOnClickListener(new ViewResourcesListener(this, incIntent));

        createImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateResourceActivity.this, UploadImageActivity.class);
                intent.putExtra("modId", incIntent.getStringExtra("modId"));
                intent.putExtra("priorityValue", incIntent.getStringExtra("priorityValue"));
                startActivity(intent);
            }
        });

        Log.d(TAG, "onCreate: Creating resource activity started.");
        checkAndroidPermissions();
    }

    private void checkAndroidPermissions() {
        Log.d(TAG, "checkAndroidPermissions: Checking android permissions...");

        int permissionWrite = ActivityCompat.checkSelfPermission(CreateResourceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreateResourceActivity.this, PERMISSIONS, 1);
        }
    }
}

