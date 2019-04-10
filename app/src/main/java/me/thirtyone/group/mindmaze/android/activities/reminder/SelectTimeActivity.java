package me.thirtyone.group.mindmaze.android.activities.reminder;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import me.thirtyone.group.mindmaze.R;

/**
 * Created by Abdullah on 20/03/2019 15:01
 * <p>
 * The user can select the time for a given reminder from here
 */
public class SelectTimeActivity extends AppCompatActivity {

    private static final String TAG = "SelectTimeActivity";
    private TimePicker timePicker;
    private Button selectTimeButton, cancelButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        timePicker = findViewById(R.id.timePicker);
        selectTimeButton = findViewById(R.id.selectTimeButton);
        cancelButton = findViewById(R.id.timeBackButton);

        timePicker.setIs24HourView(true);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectTimeActivity.this, CreateReminderActivity.class));
            }
        });

        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTimeActivity.this, CreateReminderActivity.class);
                Intent incIntent = SelectTimeActivity.this.getIntent();

                intent.putExtra("reminderMessage", incIntent.getStringExtra("reminderMessage")); // Package data back to original intent
                intent.putExtra("isChecked", incIntent.getStringExtra("isChecked"));
                intent.putExtra("date", incIntent.getIntArrayExtra("date"));

                int[] time = {timePicker.getHour(), timePicker.getMinute()}; // Tuple for hour and minute

                Log.d(TAG, "onClick: " + timePicker.getHour() + ":" + timePicker.getMinute());
                intent.putExtra("time", time);
                startActivity(intent);
            }
        });


    }
}
