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
import me.thirtyone.group.mindmaze.resources.Resource;

/**
 * Created by Abdullah on 23/03/2019 23:54
 */
public class EditNoteActivity extends AppCompatActivity {

    private Button backButton, editButton;
    private EditText nameText, contentText;
    private Note note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        Intent incIntent = getIntent();
        this.note = findModule(incIntent);

        // Setting up buttons
        editButton = findViewById(R.id.cnCreate);
        nameText = findViewById(R.id.cnTitle);
        contentText = findViewById(R.id.cnContent);
        backButton = findViewById(R.id.cnCancel);


        nameText.setText(note.getName());
        contentText.setText(note.getContent());
        editButton.setText("Save");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditNoteActivity.this, ViewResourcesActivity.class);
                intent.putExtra("modId", incIntent.getStringExtra("modId"));
                intent.putExtra("priorityValue", incIntent.getStringExtra("priorityValue"));
                startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                note.setName(nameText.getText().toString());
                note.setContent(contentText.getText().toString());

                DatabaseAbstracter.getInstance().addNote(note);

                Toast.makeText(EditNoteActivity.this, "Your note has been edited.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(EditNoteActivity.this, ViewResourcesActivity.class);
                intent.putExtra("modId", incIntent.getStringExtra("modId"));
                intent.putExtra("priorityValue", incIntent.getStringExtra("priorityValue"));
                startActivity(intent);
            }
        });


    }

    static Note findModule(Intent incIntent) {
        Module module = AccountRegistry.getInstance().getModuleById(incIntent.getStringExtra("modId"));

        if(module == null)
            throw new ModuleNotFoundException();

        for(Resource r: module.getResources()) {
            if (r.getId().equals(incIntent.getStringExtra("resId"))) {
                return (Note) r;
            }
        }
        throw new IllegalStateException("Cannot continue this activity without a valid note.");
    }
}
