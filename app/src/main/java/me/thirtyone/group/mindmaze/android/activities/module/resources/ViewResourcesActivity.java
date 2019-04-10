package me.thirtyone.group.mindmaze.android.activities.module.resources;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.android.activities.module.ViewIndividualModuleActivity;
import me.thirtyone.group.mindmaze.android.adapters.ResourceListAdapter;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.resources.Image;
import me.thirtyone.group.mindmaze.resources.Note;
import me.thirtyone.group.mindmaze.resources.Resource;
import me.thirtyone.group.mindmaze.users.Student;

import java.util.Comparator;

/**
 * @author Abdullah [17/03/2019 01:54]
 * <p>
 * This is called when a user wishes to view a list of resources for a given module
 */
public class ViewResourcesActivity extends AppCompatActivity {

    private static final String TAG = "ViewResourcesActivity";
    private Module module;
    private Student student;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        Log.d(TAG, "onCreate: Viewing resources");

        final Intent incIntent = getIntent();

        this.module = AccountRegistry.getInstance().getModuleById(this.getIntent().getStringExtra("modId")); // Storing the module passed in by the intent
        ListView lv = findViewById(R.id.itemList);
        this.student = (Student) AccountRegistry.getInstance().getCurrentUser();

        if (module.getResources().isEmpty())
            Toast.makeText(this, "There are currently no resources for this module", Toast.LENGTH_SHORT).show();


        module.getResources().sort(new Comparator<Resource>() {
            @Override
            public int compare(Resource o1, Resource o2) {
                if(o1.getId().compareTo(o2.getId()) > 0)
                    return 1;
                return -1;
            }
        });

        lv.setAdapter(new ResourceListAdapter(this, R.layout.resource_list_layout, module.getResources()));
        // Going back to the module page
        Button button = findViewById(R.id.backFromItemList);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Going back to individual module page from resource button");
                Intent individualPageIntent = new Intent(ViewResourcesActivity.this, ViewIndividualModuleActivity.class);
                individualPageIntent.putExtra("modId", incIntent.getStringExtra("modId")); // Send the module ID back
                individualPageIntent.putExtra("priorityValue", incIntent.getStringExtra("priorityValue")); // Send the priority value back
                startActivity(individualPageIntent);
            }
        });

        Button addResourceButton = findViewById(R.id.addItemButton);
        addResourceButton.setText("Add Resource");
        addResourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Checking permissions
                Student student = (Student) AccountRegistry.getInstance().getCurrentUser();

                // If they can only view
                if(student.hasPermission(module, Module.Permission.VIEW)) {
                    Toast.makeText(ViewResourcesActivity.this, "You do not have permission to create new resources for this module", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(ViewResourcesActivity.this, CreateResourceActivity.class);
                intent.putExtra("modId", module.getId());
                intent.putExtra("priorityValue", incIntent.getStringExtra("priorityValue"));
                startActivity(intent);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Resource resource = (Resource) parent.getItemAtPosition(position);

                Class<?> activityClass;

                // Logic to decide which activity to direct to
                if(resource instanceof Note) {
                    // If they only have view permissions and they aren't the uploader, then they can only view the note
                    if(student.hasPermission(module, Module.Permission.VIEW) && !student.getId().equals(resource.getUploader().getId()))
                        activityClass = ViewNoteActivity.class;
                    else
                        activityClass = EditNoteActivity.class;
                }
                else if(resource instanceof Image) {
                    activityClass = ViewImageActivity.class;
                } else {
                    throw new UnsupportedOperationException();
                }

                Intent intent = new Intent(ViewResourcesActivity.this, activityClass);
                intent.putExtra("resId", resource.getId());
                intent.putExtra("modId", module.getId());
                intent.putExtra("priorityValue", incIntent.getStringExtra("priorityValue"));
                startActivity(intent);
            }
        });

    }
}
