package me.thirtyone.group.mindmaze.android.activities.module;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.android.activities.MindMazeActivity;
import me.thirtyone.group.mindmaze.android.adapters.ModuleListAdapter;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.exceptions.PermissionException;
import me.thirtyone.group.mindmaze.modules.Priority;
import me.thirtyone.group.mindmaze.users.Student;

import java.util.Comparator;

/**
 * @author Abdullah
 * <p>
 * This will show a list of all modules to the user.
 */
public class ViewModulesActivity extends AppCompatActivity {

    private static final String TAG = "ViewModulesActivity";

    private ArrayAdapter adapter;

    private Button backToMenuButton;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_modules);

        EditText searchFilter = findViewById(R.id.moduleSearchBar);

        backToMenuButton = findViewById(R.id.backFromModuleListButton);

        // Going back to the main menu from the list of modules
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Going back to mind maze menu from modules list");
                startActivity(new Intent(ViewModulesActivity.this, MindMazeActivity.class));
            }
        });

        // Now deciding logic to show the modules to the user

        // Several android specific elements are used here.

        Log.d(TAG, "onCreate: Showing list of modules to user.");

        ListView listView = findViewById(R.id.moduleListView);

        if (AccountRegistry.getInstance().getCurrentUser() instanceof Student) {
            final Student s = (Student) AccountRegistry.getInstance().getCurrentUser();
            adapter = new ModuleListAdapter(this, R.layout.module_list_layout, s.getPriorities());

            s.getPriorities().sort(new Comparator<Priority>() {
                @Override
                public int compare(Priority o1, Priority o2) {
                    if(o1.getModule().getName().compareTo(o2.getModule().getName()) > 0)
                        return 1;
                    return -1;
                }
            });

            listView.setAdapter(adapter);

            if (s.getPriorities().isEmpty()) {
                Toast.makeText(this, "You currently have no modules! Create one from the menu.", Toast.LENGTH_SHORT).show();
            }

            // Clicking a module will take to an activity with more data regarding the module
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ViewModulesActivity.this, ViewIndividualModuleActivity.class);

                    // We want to pass in the module ID and the priority for that module.
                    intent.putExtra("modId", s.getPriorities().get(position).getModule().getId());
                    intent.putExtra("priorityValue", s.getPriorities().get(position).getPriorityValueAsString());

                    startActivity(intent);
                }
            });

        } else {
            // TODO Administrator can view ALL modules.
            throw new PermissionException("Cannot access own modules as administrator. Please use student account to view personal modules.");
        }


        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (ViewModulesActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
