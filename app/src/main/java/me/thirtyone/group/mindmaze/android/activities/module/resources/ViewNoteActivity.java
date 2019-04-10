package me.thirtyone.group.mindmaze.android.activities.module.resources;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.resources.Note;

/**
 * Created by Abdullah on 23/03/2019 23:45
 */
public class ViewNoteActivity extends AppCompatActivity {

    private Button backButton;
    private TextView noteName, noteContent;

    private Note note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        noteName = findViewById(R.id.vnName);
        noteContent = findViewById(R.id.contentText);
        backButton = findViewById(R.id.vnBackButton);

        Intent incIntent = getIntent();

        note = EditNoteActivity.findModule(incIntent);

        noteName.setText(note.getName());

        noteContent.setText(note.getContent());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewNoteActivity.this, ViewResourcesActivity.class);
                intent.putExtra("modId", note.getModule().getId());
                intent.putExtra("priorityValue", incIntent.getStringExtra("priorityValue"));
                startActivity(intent);
            }
        });

    }
}
