package me.thirtyone.group.mindmaze.android.activities.module.invites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.android.activities.MindMazeActivity;
import me.thirtyone.group.mindmaze.android.adapters.InviteListAdapter;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.modules.Invite;
import me.thirtyone.group.mindmaze.users.Student;

import java.util.List;

/**
 * @author Abdullah
 * <p>
 * This activity shows the user a list of invites they have.
 * From here they can select an invite and be redirected to the ViewIndividualInvite activity where they can see
 * more information about the invite and decide whether they want to accept or reject the invite.
 * @see ViewIndividualInviteActivity
 */
public class ViewInvitesActivity extends AppCompatActivity {

    private static final String TAG = "ViewInvitesActivity";
    private ArrayAdapter adapter; // The array adapter object to be used to decide how the invite is displayed
    Button backToMenuButton; // The button that will direct the user back to the main menu
    ListView listView; // The list view that will show the list of invites for the currently signed in user.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invites);

        Log.d(TAG, "onCreate: Showing list of invites to user");

        listView = findViewById(R.id.inviteList);
        backToMenuButton = findViewById(R.id.viewInvGoBack);

        List<Invite> inviteList = ((Student) AccountRegistry.getInstance().getCurrentUser()).getInvites();

        // Notify the user if they have no invites
        if (inviteList.isEmpty())
            Toast.makeText(this, "You currently have no invites", Toast.LENGTH_SHORT).show();

        adapter = new InviteListAdapter(this, R.layout.invite_list_layout, inviteList);
        listView.setAdapter(adapter);

        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Going back to mind maze menu from viewing invites");
                startActivity(new Intent(ViewInvitesActivity.this, MindMazeActivity.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Invite invite = (Invite) parent.getItemAtPosition(position);
                Intent intent = new Intent(ViewInvitesActivity.this, ViewIndividualInviteActivity.class);

                Log.d(TAG, "onItemClick: Moving to individual invite page for an invite to " + invite.getModule().getName());

                // Adding the data to be passed in to the individual invite activity
                intent.putExtra("senderId", invite.getSender().getId());
                intent.putExtra("receiverId", invite.getReceiver().getId());
                intent.putExtra("moduleId", invite.getModule().getId());
                intent.putExtra("inviteId", invite.getId());

                startActivity(intent);
            }
        });
    }
}
