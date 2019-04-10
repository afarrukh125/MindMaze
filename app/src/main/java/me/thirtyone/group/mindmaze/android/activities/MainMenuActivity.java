package me.thirtyone.group.mindmaze.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.android.activities.registration.LoginActivity;
import me.thirtyone.group.mindmaze.android.activities.registration.RegisterActivity;
import me.thirtyone.group.mindmaze.core.AccountRegistry;

/**
 * @author Abdullah
 * This is the main menu, so this is the first thing shown after the preload activity
 * assuming the user is not already signed in.
 * <p>
 * The user can either login or register. The settings part of this is still to be completed
 */
public class MainMenuActivity extends AppCompatActivity {

    private static final String TAG = "MainMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started MindMaze!");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            AccountRegistry.getInstance()
                    .setSignedInUser(AccountRegistry.getInstance().getUserById(
                            FirebaseAuth.getInstance().getCurrentUser().getUid()
                            )
                    );

            startActivity(new Intent(MainMenuActivity.this, MindMazeActivity.class));
        }


        // Setting up click listeners for the menu.

        // Click handling for register button
        Button registerButton = findViewById(R.id.registerButtonMain);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked registration button.");

                Intent intent = new Intent(MainMenuActivity.this, RegisterActivity.class);
                startActivity(intent); //Ran Emulator First Time
            }
        });

        // Click handling for login button
        Button loginButton = findViewById(R.id.loginButtonMain);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // If the user is already signed in by email then there is no need to tell them to login when they click on this, send them straight to mind maze activity.
            // Ternary operator used to decide logic here.
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked login button on homepage");
                Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
