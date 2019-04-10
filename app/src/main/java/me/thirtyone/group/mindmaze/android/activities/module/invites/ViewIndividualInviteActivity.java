package me.thirtyone.group.mindmaze.android.activities.module.invites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.android.activities.MindMazeActivity;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.data.DatabaseAbstracter;
import me.thirtyone.group.mindmaze.modules.Invite;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.modules.Priority;
import me.thirtyone.group.mindmaze.users.Student;

/**
 * @author Abdullah
 * <p>
 * This activity is shown when a user wishes to view an individual invite and here they can
 * either accept or reject the invite. They can also choose to go back and not do anything,
 * leaving the invite as it is.
 */
public class ViewIndividualInviteActivity extends AppCompatActivity {
    private static final String TAG = "ViewIndividualInviteAct";

    private Button acceptButton, rejectButton, backButton;
    private TextView inviteMessage, modName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_indiv_invite);
        Log.d(TAG, "onCreate: ");

        acceptButton = findViewById(R.id.acceptButton);
        rejectButton = findViewById(R.id.rejectButton);
        backButton = findViewById(R.id.viewInvBackButton);
        inviteMessage = findViewById(R.id.invMessage);
        modName = findViewById(R.id.modNameInvView);

        Intent incIntent = getIntent();
        final Module module = AccountRegistry.getInstance().getModuleById(incIntent.getStringExtra("moduleId"));
        Student sender = ((Student) AccountRegistry.getInstance().getUserById(incIntent.getStringExtra("senderId")));
        final Student receiver = ((Student) AccountRegistry.getInstance().getUserById(incIntent.getStringExtra("receiverId")));
        final String inviteId = incIntent.getStringExtra("inviteId");

        inviteMessage.setText("You have been invited to join " + module.getName() + " by " + sender.getUsername());
        modName.setText(module.getName());


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewIndividualInviteActivity.this, ViewInvitesActivity.class);
                startActivity(intent);
            }
        });

        // A lot happens when a user accepts an invite. It adds the module to the student, sets up permissions and deletes the invite.
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Invite i : receiver.getInvites()) {
                    if (i.getId().equals(inviteId))
                        i.accept(); // Accept the invite in the object
                }
                DatabaseAbstracter.getInstance().addModuleToStudent(module, receiver, Priority.Value.STANDARD); // Add the module to the receiver in the database
                DatabaseAbstracter.getInstance().setPermission(module, receiver, Module.Permission.VIEW); // Setup permissions in the database
                DatabaseAbstracter.getInstance().deleteInviteById(receiver.getId(), inviteId); // Delete the invite from the database

                Toast.makeText(ViewIndividualInviteActivity.this, "You now have access to " + module.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewIndividualInviteActivity.this, MindMazeActivity.class);
                startActivity(intent);
            }
        });

        // Not much to do here. Just reject the invite in the invite class
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Invite i : receiver.getInvites()) {
                    if (i.getId().equals(inviteId))
                        i.reject();
                }

                DatabaseAbstracter.getInstance().deleteInviteById(receiver.getId(), inviteId);
                Toast.makeText(ViewIndividualInviteActivity.this, "You have rejected an invite to " + module.getName(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ViewIndividualInviteActivity.this, ViewInvitesActivity.class));
            }
        });
    }
}
