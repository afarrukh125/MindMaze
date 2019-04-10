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
import me.thirtyone.group.mindmaze.core.Reminder;

import java.util.List;

/**
 * Created by Abdullah on 20/03/2019 22:20
 */
public class ReminderListAdapter extends ArrayAdapter<Reminder> {

    private static final String TAG = "ReminderListAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    public ReminderListAdapter(@NonNull Context context, int resource, @NonNull List<Reminder> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String message = getItem(position).getMessage();
        String date = getItem(position).getTime().toString();

        final View result;

        ReminderListAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ReminderListAdapter.ViewHolder();
            holder.reminderMessage = convertView.findViewById(R.id.itemName);
            holder.time = convertView.findViewById(R.id.itemInfo);

            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ReminderListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        // Deciding which animation to use
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.loading_down_anim : R.anim.loading_up_anim);

        result.startAnimation(animation);
        lastPosition = position;

        holder.reminderMessage.setText(message);
        holder.time.setText(date);
        return convertView;
    }

    private static class ViewHolder {
        TextView reminderMessage;
        TextView time;
    }
}
