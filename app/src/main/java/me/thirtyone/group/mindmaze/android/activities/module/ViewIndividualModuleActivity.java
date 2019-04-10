package me.thirtyone.group.mindmaze.android.activities.module;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.android.activities.module.invites.InviteUserActivity;
import me.thirtyone.group.mindmaze.android.activities.module.resources.ViewResourcesActivity;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.data.DatabaseAbstracter;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.users.Student;

/**
 * @author Abdullah
 * <p>
 * This activiity allows the user to view an individual module in detail.
 * It serves as a gateway to viewing the module details and managing the module.
 * Here, the user can choose to invite other users to the selected module.
 * <p>
 * We have passed in the module that is currently being viewed, through the modId extra in the intent that we
 * are moving from.
 */
public class ViewIndividualModuleActivity extends AppCompatActivity {

    private static final String TAG = "ViewIndividualModuleAct";

    Button leaveModuleButton, viewResourcesButton, backButton, inviteUserButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_indiv_module);


        // Declaring our buttons
        viewResourcesButton = findViewById(R.id.vmViewResource);
        leaveModuleButton = findViewById(R.id.leaveModuleButton);
        backButton = findViewById(R.id.indivBackButton);
        inviteUserButton = findViewById(R.id.vmInvite);

        Log.d(TAG, "onCreate: Viewing individual module");

        final TextView modName = findViewById(R.id.vmName);
        final TextView priVal = findViewById(R.id.indivPriorityVal);

        Intent intent = getIntent();

        final String incId = intent.getStringExtra("modId"); // Getting module id passed in by the activity that started this activity.
        final String priorityValue = intent.getStringExtra("priorityValue"); // Getting module priority

        final Module module = AccountRegistry.getInstance().getModuleById(incId);

        final Student s = (Student) AccountRegistry.getInstance().getCurrentUser();

        final boolean isOwner = s.getId().equals(module.getCreatorId()); // Deciding if the user is the owner or not and changing button names accordingly

        modName.setText(module.getName()); // Display the module name
        priVal.setText(priorityValue + " priority"); // Display the module priority


        viewResourcesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewResourcesIntent = new Intent(ViewIndividualModuleActivity.this, ViewResourcesActivity.class);
                viewResourcesIntent.putExtra("modId", incId);
                viewResourcesIntent.putExtra("priorityValue", priorityValue);
                startActivity(viewResourcesIntent);
            }
        });

        inviteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inviteIntent = new Intent(ViewIndividualModuleActivity.this, InviteUserActivity.class);
                inviteIntent.putExtra("modId", incId);
                inviteIntent.putExtra("priorityValue", priorityValue);
                startActivity(inviteIntent);
            }
        });


        // Button to leave the module
        if (isOwner) {// If the student is the module owner
            leaveModuleButton.setText("Delete module");
            leaveModuleButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorWarning));
        }

        leaveModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeletionDialog(s, module, isOwner);
            }
        });

        // Button to go back to the module list
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Going back to modules list from individual module " + module.getName());
                startActivity(new Intent(ViewIndividualModuleActivity.this, ViewModulesActivity.class));
            }
        });

    }

    /**
     * Showing a dialog and defining the logic for each of the dialog options
     *
     * @param student The student associated with this dialog
     * @param module  The module associated with this dialog
     * @param isOwner Whether or not the student is the owner of the module
     */
    public void showDeletionDialog(final Student student, final Module module, final boolean isOwner) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.warning_delmod);

        builder.setTitle(module.getName());
        builder.setMessage("Are you sure you wish to " + (isOwner ? "delete " : "leave ") + "this module?");

        // If the user presses the ok button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: OK Clicked");
                modDelOk(student, module, isOwner);
            }
        });

        // If the user presses the cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: Cancel clicked");
                modDelCancel(); // Do nothing essentially
            }
        });

        builder.show();
    }

    private void modDelCancel() {
        Log.d(TAG, "modDelCancel: Cancel button clicked in dialog");
    }

    private void modDelOk(Student student, Module module, boolean isOwner) {
        student.leaveModule(module); // Passing this to the student class to deal with.

        module.removePermission(student); // Remove the permission for this student from the module
        DatabaseAbstracter.getInstance().removeModuleFromStudent(student, module); // Remove the module entry in the student

        // Deciding what message to show to the user based on whether they are the owner or not

        if (isOwner) {
            DatabaseAbstracter.getInstance().deleteModule(module);
            student.deleteModule(module);

            // Additionally delete all students from this module and references to this module from database
            for (Student s : module.getStudents()) {
                s.leaveModule(module);
                DatabaseAbstracter.getInstance().removeModuleFromStudent(s, module);
            }
        }

        // Go back to list of modules
        startActivity(new Intent(ViewIndividualModuleActivity.this, ViewModulesActivity.class));
        String toastString = "You have successfully " +
                (isOwner ? "deleted " : "left ") +
                "the module " + module.getName();
        Toast.makeText(ViewIndividualModuleActivity.this, toastString, Toast.LENGTH_LONG).show();
    }
}
