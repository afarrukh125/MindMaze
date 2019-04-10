package me.thirtyone.group.mindmaze.android.activities.module.resources;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.data.DatabaseAbstracter;
import me.thirtyone.group.mindmaze.exceptions.ModuleNotFoundException;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.resources.Note;
import me.thirtyone.group.mindmaze.users.Student;

/**
 * Created by Abdullah on 23/03/2019 14:50
 * <p>
 * Activity where we create an individual note
 */
public class CreateNoteActivity extends AppCompatActivity {

    private static final String TAG = "CreateNoteActivity";
    Button createButton, cancelButton;
    EditText noteTitle, noteContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        createButton = findViewById(R.id.cnCreate);
        noteTitle = findViewById(R.id.cnTitle);
        noteContent = findViewById(R.id.cnContent);
        cancelButton = findViewById(R.id.cnCancel);

        Intent incIntent = getIntent();

        Module module = AccountRegistry.getInstance().getModuleById(getIntent().getStringExtra("modId"));

        if (module == null)
            throw new ModuleNotFoundException();


        // Quite a lot needs to happen here
        // Module is added to module resources. Note is also then added to the database
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validating parameters
                if (noteContent.getText().toString().equals("")) {
                    Toast.makeText(CreateNoteActivity.this, "Please enter a message for this note.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = noteContent.getText().toString();
                String title = noteTitle.getText().toString();

                if (title.equals(""))
                    title = "Note";

                Note note = new Note(title, content.trim(), module, ((Student) AccountRegistry.getInstance().getCurrentUser()), Long.toString(System.currentTimeMillis()));
                module.addResource(note);
                DatabaseAbstracter.getInstance().addNote(note);

                Intent intent = new Intent(CreateNoteActivity.this, ViewResourcesActivity.class);
                intent.putExtra("modId", module.getId());
                intent.putExtra("priorityValue", incIntent.getStringExtra("priorityValue"));
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNoteActivity.this, CreateResourceActivity.class);
                intent.putExtra("modId", module.getId());
                intent.putExtra("priorityValue", incIntent.getStringExtra("priorityValue"));
                startActivity(intent);
            }
        });
    }
}
