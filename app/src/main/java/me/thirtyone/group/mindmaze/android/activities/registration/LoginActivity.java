package me.thirtyone.group.mindmaze.android.activities.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.android.activities.MainMenuActivity;
import me.thirtyone.group.mindmaze.android.activities.MindMazeActivity;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.users.User;

/**
 * Created by Abdullah on 13/03/2019 18:41
 * This activity is for when the user logs in to the system
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Our UI objects
    private EditText mUsername, mPassword;
    private Button loginButton;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Click handling for cancel button for login
        Button cancelButton = (Button) findViewById(R.id.cancelLoginButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        // Click handling for login button
        loginButton = (Button) findViewById(R.id.signInButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = findViewById(R.id.loginEmail);
                mPassword = findViewById(R.id.loginPass);

                String username = mUsername.getText().toString();
                String pass = mPassword.getText().toString();
                if (username.equals("") || pass.equals("")) {
                    Toast.makeText(v.getContext(), "You did not fill all fields.", Toast.LENGTH_LONG).show();
                } else {

                    // The extra checks here are done in order to allow signing in by username.
                    // We loop through the users and find the first user with that username.
                    // We then retrieve the email associated with this username and then sign in with that email.

                    String email = null;
                    for (User u : AccountRegistry.getInstance().getUsers()) {
                        if (u.getUsername().equals(username))
                            email = u.getEmail();
                    }

                    if (email == null) {
                        Toast.makeText(LoginActivity.this, "There was no user found by this name. Please try again, otherwise register.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // We now attempt to sign in the user
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User signedInUser = AccountRegistry.getInstance().getUserById(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                AccountRegistry.getInstance().setSignedInUser(signedInUser);
                                Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginActivity.this, MindMazeActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Login failed. Please double check your details and try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (AccountRegistry.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MindMazeActivity.class));
        }
    }
}
