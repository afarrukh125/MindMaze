package me.thirtyone.group.mindmaze.android.activities.module.invites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.android.activities.module.ViewIndividualModuleActivity;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.modules.Invite;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.users.Student;

/**
 * @author Abdullah
 * <p>
 * This activity is shown to the user when they wish to invite another user to the selected module
 * <p>
 * The selected module and priority are passed in to this activity. We access these using the a variable that
 * references the incoming intent. This incoming intent has methods getStringExtra(). As a matter of convention
 * I have chosen these to be the module ID and the priority value passed in.
 */
public class InviteUserActivity extends AppCompatActivity {

    private Button inviteButton, cancelButton;
    private EditText userInput;
    private static final String TAG = "InviteUserActivity";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_user);

        Intent incIntent = getIntent();
        final String modId = incIntent.getStringExtra("modId"); // Getting the module ID

        final Module module = AccountRegistry.getInstance().getModuleById(modId);

        // We need to store this so that we can return it to the previous activity when the user finishes
        final String priorityValue = incIntent.getStringExtra("priorityValue");

        inviteButton = findViewById(R.id.actInviteUserButton);
        cancelButton = findViewById(R.id.cancelInviteUserButton);
        userInput = findViewById(R.id.invUserText);

        // Adding onClickListener to the invite user button. Most of the processing is done in the inviteStudent method of the student.
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String invitedUserName = userInput.getText().toString();

                if (invitedUserName.equals("")) {
                    Toast.makeText(InviteUserActivity.this, "You must enter a username to invite.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (AccountRegistry.getInstance().getUserByName(invitedUserName) == null) {
                    Toast.makeText(InviteUserActivity.this, "The user " + invitedUserName + " does not exist. Please check again.", Toast.LENGTH_LONG).show();
                    return;
                }
                Student s = (Student) AccountRegistry.getInstance().getUserByName(invitedUserName);

                if (s.getId().equals(AccountRegistry.getInstance().getCurrentUser().getId())) {
                    Toast.makeText(InviteUserActivity.this, "You cannot invite yourself", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Checking if the student already has this module. This matches with the sequence diagram.
                if (AccountRegistry.getInstance().hasModule(s, module.getId())) {
                    Toast.makeText(InviteUserActivity.this, "The user you are inviting is already a member of this module.", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (Invite i : s.getInvites()) {
                    if (i.getModule().equals(module)) {
                        Toast.makeText(InviteUserActivity.this, "The user you are inviting already has an invite to this module", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // All checks are fine. Begin to process inviting the student in the inviteStudent() method of the Student class.
                ((Student) AccountRegistry.getInstance().getCurrentUser()).inviteStudent(s, module);
                Toast.makeText(InviteUserActivity.this, "Successfully invited " + s.getUsername() + " to " + module.getName(), Toast.LENGTH_SHORT).show();
                userInput.setText(""); // Clear textview to allow adding more users.
            }
        });

        // Adding click listener to the cancel button. Just send them back to the previous activity.
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Cancel button clicked on invite user screen");
                Intent intent = new Intent(InviteUserActivity.this, ViewIndividualModuleActivity.class);
                intent.putExtra("modId", modId);
                intent.putExtra("priorityValue", priorityValue);

                startActivity(intent);
            }
        });
    }
}
