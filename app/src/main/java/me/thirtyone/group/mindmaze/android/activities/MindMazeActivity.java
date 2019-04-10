package me.thirtyone.group.mindmaze.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.android.activities.module.CreateModuleActivity;
import me.thirtyone.group.mindmaze.android.activities.module.ViewModulesActivity;
import me.thirtyone.group.mindmaze.android.activities.module.invites.ViewInvitesActivity;
import me.thirtyone.group.mindmaze.android.activities.reminder.ViewRemindersActivity;
import me.thirtyone.group.mindmaze.core.AccountRegistry;

/**
 * @author Abdullah
 * <p>
 * This is the activity that is shown as the main menu of the application after the user logs in.
 */
public class MindMazeActivity extends AppCompatActivity {

    private static final String TAG = "MindMazeActivity";

    private Button signoutButton, createModuleButton, viewModulesButton, viewInvitesButton, createReminderButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mindmaze);

        if (AccountRegistry.getInstance().getCurrentUser() == null) {
            Log.d(TAG, "onCreate: Going back to main menu as user was set to null");
            startActivity(new Intent(MindMazeActivity.this, MainMenuActivity.class));
            return;
        }

        signoutButton = findViewById(R.id.signOutButton);

        TextView userNameView = findViewById(R.id.mindMazeUsername);
        userNameView.setText(AccountRegistry.getInstance().getCurrentUser().getUsername());


        // If the user presses the sign out button...
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Log.d(TAG, "onClick: Clicked log out button");
                Toast.makeText(MindMazeActivity.this, "Signed out successfully", Toast.LENGTH_LONG).show();
                AccountRegistry.getInstance().signOutUser();
                startActivity(new Intent(MindMazeActivity.this, MainMenuActivity.class));
            }
        });

        // If the user presses the create module button then take them to that activity
        createModuleButton = findViewById(R.id.createModuleButton);
        createModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked on create module button");
                startActivity(new Intent(MindMazeActivity.this, CreateModuleActivity.class));
            }
        });

        // If the user presses the view modules button

        viewModulesButton = findViewById(R.id.viewModulesButton);
        viewModulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Moving to view modules page");
                startActivity(new Intent(MindMazeActivity.this, ViewModulesActivity.class));
            }
        });

        viewInvitesButton = findViewById(R.id.viewInvitesButton);
        viewInvitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MindMazeActivity.this, ViewInvitesActivity.class));
            }
        });

        createReminderButton = findViewById(R.id.remindersButton);
        createReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MindMazeActivity.this, ViewRemindersActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
