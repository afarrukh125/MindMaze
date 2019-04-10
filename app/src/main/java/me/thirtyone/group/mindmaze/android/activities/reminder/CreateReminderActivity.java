package me.thirtyone.group.mindmaze.android.activities.reminder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.data.DatabaseAbstracter;
import me.thirtyone.group.mindmaze.users.Student;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Abdullah
 * This activity is where the user can choose parameters for a new reminder
 */
public class CreateReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "CreateReminderActivity";

    private Button createButton, cancelButton, selectDateButton;
    private TextView selectedDate;
    private EditText reminderMessageText;
    private int day, month, year;
    private int hour, minute;
    private CheckBox repeatBox;
    private boolean isChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);

        Log.d(TAG, "onCreate: Moved to create reminder activity");

        createButton = findViewById(R.id.crCreateButton);
        cancelButton = findViewById(R.id.crCancelButton);
        selectDateButton = findViewById(R.id.crSelectDate);
        selectedDate = findViewById(R.id.crSelectedDate);
        reminderMessageText = findViewById(R.id.reminderMsg);
        repeatBox = findViewById(R.id.isRepeating);
        isChecked = false;

        parseIncomingIntents(); // Going to deal with data coming in from other intents. Since a lot needs to be done, we make a separate method for this

        final Student student = (Student) AccountRegistry.getInstance().getCurrentUser();

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateReminderActivity.this, ViewRemindersActivity.class));
            }
        });

        repeatBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = !isChecked;
            }
        });

        // Create the reminder after some final checks.
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderMessage = reminderMessageText.getText().toString();
                if (reminderMessage.equals(""))
                    reminderMessage = "Reminder";

                if (selectedDate.getText().equals("")) {
                    Toast.makeText(CreateReminderActivity.this, "You must select a date", Toast.LENGTH_SHORT).show();
                    return;
                }

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hour, minute);

                Date date = new Date(calendar.getTimeInMillis());

                Log.d(TAG, "onClick: " + date.getYear());
                String id = Long.toString(System.currentTimeMillis()); // Store id to add to database

                DatabaseAbstracter.getInstance().createReminder(id, student.getId(), reminderMessage, isChecked, date);
                student.createReminder(reminderMessage, date, isChecked, id);
                startActivity(new Intent(CreateReminderActivity.this, ViewRemindersActivity.class));
            }
        });
    }

    /**
     * Shows the date picker dialog to the user
     */
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    /**
     * Deciding what happens when ok is pressed on the date picker dialog
     *
     * @param view       The context, so what view this is part of
     * @param year       The year provided
     * @param month      The month provided
     * @param dayOfMonth The day of month provided
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d(TAG, "onDateSet: Now moving to set time activity");

        this.day = dayOfMonth;
        this.month = month;
        this.year = year;

        Intent timeIntent = new Intent(CreateReminderActivity.this, SelectTimeActivity.class);
        // We need to provide the data for the reminder message so that when we enter this activity again the user doesn't need to enter stuff again
        timeIntent.putExtra("reminderMessage", this.reminderMessageText.getText().toString());
        timeIntent.putExtra("isChecked", this.repeatBox.isChecked() ? "true" : "false");

        int[] data = {dayOfMonth, month, year}; // The tuple to be passed in containing day, month and year

        timeIntent.putExtra("date", data);

        startActivity(timeIntent);
    }

    private void parseIncomingIntents() {
        Intent incIntent = getIntent(); // Get the incoming intent. Needed to automatically fill the reminder message and isChecked if needed

        if (incIntent.getStringExtra("reminderMessage") != null) {
            Log.d(TAG, "parseIncomingIntents: Found message extra.");
            reminderMessageText.setText(incIntent.getStringExtra("reminderMessage"));
        }

        if (incIntent.getStringExtra("isChecked") != null) {
            Log.d(TAG, "parseIncomingIntents: Found checked extra");
            boolean check = incIntent.getStringExtra("isChecked").equals("true");
            repeatBox.setChecked(check);
            isChecked = check;
        }

        if (incIntent.getIntArrayExtra("date") != null) {
            Log.d(TAG, "parseIncomingIntents: Found array extra for date");
            int[] data = incIntent.getIntArrayExtra("date");
            this.day = data[0];
            this.month = data[1];
            this.year = data[2];
        }

        if (incIntent.getIntArrayExtra("time") != null) {
            Log.d(TAG, "parseIncomingIntents: Found array extra for time. Now setting value");
            int[] data = incIntent.getIntArrayExtra("time");
            this.hour = data[0];
            this.minute = data[1];

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hour, minute);

            Date date = new Date(calendar.getTimeInMillis());

            selectedDate.setText(date.toString());
        }
    }


}
