package me.thirtyone.group.mindmaze.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.constants.Constants;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.data.DatabaseAbstracter;
import me.thirtyone.group.mindmaze.users.User;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Abdullah on 13/03/2019 00:57
 * <p>
 * This is the entry point of the system
 * <p>
 * Here, we load the applications dependencies.
 * This includes the database abstracter, the account registry. There is a delay here to allow time for this to happen
 * Once the delay has finished we go to either the main menu page if the user is not logged in.
 * Otherwise we go to the page after they log in if they are already logged in
 * <p>
 * We determine a user is logged in based on authentication status.
 */
public class PreloadActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "PreloadActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);

        // Initialise all values here
        FirebaseApp.initializeApp(this);
        AccountRegistry.init();
        DatabaseAbstracter.init();

        mAuth = FirebaseAuth.getInstance();

        ImageView logo = findViewById(R.id.logo);
        logo.setImageResource(R.drawable.logo);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Event triggered when a user signs in or out essentially
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: User has signed in: " + user.getUid());
                    for (User u : AccountRegistry.getInstance().getUsers()) {
                        if (u.getId().equals(user.getUid()))
                            AccountRegistry.getInstance().setSignedInUser(u);
                    }
                } else {
                    if (AccountRegistry.getInstance().getCurrentUser() != null)
                        AccountRegistry.getInstance().signOutUser();
                    Log.d(TAG, "onAuthStateChanged: No user currently signed in");
                }
            }
        };


        // We aim to run a timer before switching activities.
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {

                // If there is no user logged in then we go to main menu. Otherwise we can go straight into the app.
                boolean loggedIn = false;
                if (FirebaseAuth.getInstance().getCurrentUser() != null)
                    loggedIn = true;

                // Deciding the intent here
                Intent intent = (loggedIn) ? new Intent(PreloadActivity.this, MindMazeActivity.class) :
                        new Intent(PreloadActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        }, 1000 * Constants.LOAD_TIME);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DatabaseAbstracter.getInstance().setupModulePermissions(); // Load up module permissions
        DatabaseAbstracter.getInstance().setupInvitesAndReminders(); // Load invites and reminders from the database into the system
        DatabaseAbstracter.getInstance().setupModuleResources(); // Load the resources into the modules
    }
}
