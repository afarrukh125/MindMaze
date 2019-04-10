package me.thirtyone.group.mindmaze.android.activities.module;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.android.activities.MindMazeActivity;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.users.Student;
import me.thirtyone.group.mindmaze.users.User;

/**
 * Created by Abdullah on 14/03/2019 13:09
 * This is the activity where a user can create a module.
 */
public class CreateModuleActivity extends AppCompatActivity {

    private static final String TAG = "CreateModuleActivity";

    private Button createButton; // The button to create a module
    private Button cancelButton; // The  button to cancel module creation
    private EditText moduleNameText; // The input field for the module name

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_module);

        TextView userName = findViewById(R.id.cmUserLabel);
        userName.setText(AccountRegistry.getInstance().getCurrentUser().getUsername());

        moduleNameText = findViewById(R.id.cmModuleName);

        // Click handling for clicking create button
        createButton = findViewById(R.id.cmCreateModuleButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Creating module based on user parameters...");

                if (moduleNameText.getText().toString().equals("")) {
                    Log.d(TAG, "onClick: Module creation failed: user did not enter a module name");
                    Toast.makeText(CreateModuleActivity.this, "Please enter a module name", Toast.LENGTH_SHORT).show();
                    return;
                }

                User u = AccountRegistry.getInstance().getCurrentUser();

                if ((u instanceof Student)) {
                    ((Student) u).createModule(moduleNameText.getText().toString());
                } else {
                    throw new IllegalStateException("Cannot create module as an administrator.");
                }
                // We send this to the student class, as our class diagram specified.

                Toast.makeText(CreateModuleActivity.this, "Successfully created module " + moduleNameText.getText().toString(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(CreateModuleActivity.this, MindMazeActivity.class));
            }
        });

        // Click handling for clicking cancel button
        cancelButton = findViewById(R.id.cmCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked cancel button on create module page.");
                // Move to the activity shown after the user logs in
                startActivity(new Intent(CreateModuleActivity.this, MindMazeActivity.class));
            }
        });

    }
}
