package me.thirtyone.group.mindmaze.android.activities.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.android.activities.MindMazeActivity;
import me.thirtyone.group.mindmaze.android.adapters.ReminderListAdapter;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.core.Reminder;
import me.thirtyone.group.mindmaze.users.Student;

import java.util.List;

/**
 * @author Abdullah
 * <p>
 * The user here can view a list of their reminders
 */
public class ViewRemindersActivity extends AppCompatActivity {

    private static final String TAG = "ViewRemindersActivity";

    Button addReminderButton, cancelButton;
    ListView remindersListView;
    ListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        List<Reminder> reminderList = ((Student) AccountRegistry.getInstance().getCurrentUser()).getReminders();

        if (reminderList.isEmpty()) // If there are no reminders for the currently logged in student
            Toast.makeText(this, "You currently have no reminders.", Toast.LENGTH_SHORT).show();

        adapter = new ReminderListAdapter(this, R.layout.list_item_layout, reminderList);

        addReminderButton = findViewById(R.id.addItemButton);
        addReminderButton.setText("Create new reminder");
        cancelButton = findViewById(R.id.backFromItemList);
        remindersListView = findViewById(R.id.itemList);

        remindersListView.setAdapter(adapter);

        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewRemindersActivity.this, CreateReminderActivity.class));
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewRemindersActivity.this, MindMazeActivity.class));
            }
        });


    }
}
