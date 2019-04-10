package me.thirtyone.group.mindmaze.android.activities.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import me.thirtyone.group.mindmaze.data.DatabaseAbstracter;
import me.thirtyone.group.mindmaze.users.Student;
import me.thirtyone.group.mindmaze.utils.RegistrationUtils;

/**
 * Created by Abdullah on 13/03/2019 00:18
 * This is the activity shown when a user intends to register
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText emailText, passwordText, usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        Button registerButton = (Button) findViewById(R.id.registerButton);
        Button cancelButton = (Button) findViewById(R.id.cancelRegister);
        emailText = (EditText) findViewById(R.id.emailText);
        usernameText = (EditText) findViewById(R.id.userNameText);
        passwordText = (EditText) findViewById(R.id.passText);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Going back to main homepage from register page");
                Intent intent = new Intent(RegisterActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        // Click handling for register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked registration button.");

                final String email = emailText.getText().toString();
                final String pass = passwordText.getText().toString();
                final String username = usernameText.getText().toString();

                if (email.equals("") || pass.equals("") || username.equals("")) {
                    Toast.makeText(RegisterActivity.this, "You did not fill in all fields.", Toast.LENGTH_LONG).show();
                } else if(!email.contains("@")) {
                    Toast.makeText(RegisterActivity.this, "The email format provided was not valid", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Student s = new Student(username, RegistrationUtils.hashPass(pass), email, FirebaseAuth.getInstance().getCurrentUser().getUid());
                                DatabaseAbstracter.getInstance().addUser(s);
                                AccountRegistry.getInstance().addUser(s);
                                AccountRegistry.getInstance().setSignedInUser(s);

                                Toast.makeText(RegisterActivity.this, "Registered account successfully!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, MindMazeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Failed to register account. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
