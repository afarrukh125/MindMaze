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
import android.widget.TextView;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.modules.Invite;

import java.util.List;

/**
 * @author Abdullah
 * This is an adapter that is to map an invite object into a textview.
 * We choose what we want to display in the listview based on this adapter.
 * <p>
 * It essentially maps the array into the listview.
 */
public class InviteListAdapter extends ArrayAdapter<Invite> {

    private static final String TAG = "InviteListAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    public InviteListAdapter(@NonNull Context context, int resource, @NonNull List<Invite> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String modName = getItem(position).getModule().getName();
        String priorityVal = getItem(position).getSender().getUsername();

        final View result; // Will show the animation


        // We are trying to follow best practices so in the scenario where we need to load
        // several module objects we are only loading as many as we need to see.

        // This is the android ViewHolder design pattern
        InviteListAdapter.ViewHolder holder; // Will hold our objects

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new InviteListAdapter.ViewHolder();
            holder.modName = convertView.findViewById(R.id.inviteModName);
            holder.sender = convertView.findViewById(R.id.inviteSender);

            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (InviteListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        // Deciding which animation to use to either go up or down.
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.loading_down_anim : R.anim.loading_up_anim);

        result.startAnimation(animation);
        lastPosition = position;

        holder.modName.setText(modName);
        holder.sender.setText(priorityVal);
        return convertView;
    }

    private static class ViewHolder {
        TextView modName;
        TextView sender;
    }
}
