package me.thirtyone.group.mindmaze.android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.core.AccountRegistry;
import me.thirtyone.group.mindmaze.modules.Module;
import me.thirtyone.group.mindmaze.modules.Priority;
import me.thirtyone.group.mindmaze.users.Student;

import java.util.List;

/**
 * Created by Abdullah on 16/03/2019 15:52
 * <p>
 * This decides what the user sees when they view the module list.
 * <p>
 * Much like the InviteListAdapter class, we choose to show only the information on the modules we need.
 */
public class ModuleListAdapter extends ArrayAdapter<Priority> {

    private static final String TAG = "ModuleListAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    public ModuleListAdapter(@NonNull Context context, int resource, @NonNull List<Priority> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String modName = getItem(position).getModule().getName();
        String priorityVal = getItem(position).getPriorityValueAsString();

        // Getting the permissions for the user
        Module.Permission permissionValue = getItem(position).getModule().getPermissions().get((Student) AccountRegistry.getInstance().getCurrentUser());
        String permission = permissionValue.toString().substring(0, 1) + permissionValue.toString().substring(1).toLowerCase();

        final View result; // Will show the animation


        // We are trying to follow best practices so in the scenario where we need to load
        // several module objects we are only loading as many as we need to see.

        // This is the android ViewHolder design pattern
        ViewHolder holder; // Will hold our objects

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.modName = convertView.findViewById(R.id.modTvName);
            holder.modPriority = convertView.findViewById(R.id.modTvPriority);
            holder.modPermission = convertView.findViewById(R.id.modTvPermission);

            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        // Deciding which animation to use to either go up or down.
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.loading_down_anim : R.anim.loading_up_anim);

        result.startAnimation(animation);
        lastPosition = position;

        holder.modName.setText(modName);
        holder.modPriority.setText(priorityVal);
        holder.modPermission.setText(permission);
        return convertView;
    }

    private static class ViewHolder {
        TextView modName;
        TextView modPriority;
        TextView modPermission;
    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }
}
