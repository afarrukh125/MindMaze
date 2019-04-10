package me.thirtyone.group.mindmaze.android.activities.module.resources;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

/**
 * Created by Abdullah on 23/03/2019 15:58
 * <p>
 * A type of View.OnClickListener. I made this since there is plenty of repeated behaviour in some activities
 * with only one thing changing each time.
 * <p>
 * This type of listener just takes the user back from the provided context to the ViewResourcesActivity.
 */
public class ViewResourcesListener implements View.OnClickListener {

    private Context context;
    private Intent incIntent;
    private static final String TAG = "ViewResourcesListener";


    public ViewResourcesListener(Context context, Intent incIntent) {
        this.context = context;
        this.incIntent = incIntent;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: Resource listener clicked!");

        Intent intent = new Intent(context, ViewResourcesActivity.class);

        intent.putExtra("modId", incIntent.getStringExtra("modId"));
        intent.putExtra("priorityValue", incIntent.getStringExtra("priorityValue"));
        context.startActivity(intent);
    }
}
