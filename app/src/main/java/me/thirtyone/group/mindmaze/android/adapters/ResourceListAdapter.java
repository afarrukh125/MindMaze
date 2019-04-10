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
import android.widget.ImageView;
import android.widget.TextView;
import me.thirtyone.group.mindmaze.R;
import me.thirtyone.group.mindmaze.exceptions.ResourceException;
import me.thirtyone.group.mindmaze.resources.Resource;

import java.util.List;

/**
 * Created by Abdullah on 23/03/2019 17:37
 */
public class ResourceListAdapter extends ArrayAdapter<Resource> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    public ResourceListAdapter(@NonNull Context context, int resource, @NonNull List<Resource> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Resource resource = getItem(position);

        String resourceName = resource.getName();
        String type = resource.getClass().getSimpleName();
        String uploader = resource.getUploader().getUsername();

        final View result;

        ResourceListAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ResourceListAdapter.ViewHolder();
            holder.resourceName = convertView.findViewById(R.id.resourceName);
            holder.uploader = convertView.findViewById(R.id.resourceUploader);
            holder.type = convertView.findViewById(R.id.resourceType);
            holder.image = convertView.findViewById(R.id.resourceImage);

            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ResourceListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.loading_down_anim : R.anim.loading_up_anim);

        result.startAnimation(animation);
        lastPosition = position;

        holder.uploader.setText(uploader);
        holder.resourceName.setText(resourceName);
        holder.type.setText(type);
        holder.image.setImageResource(decideResource(type));

        return convertView;
    }

    private static class ViewHolder {
        ImageView image;
        TextView resourceName;
        TextView uploader;
        TextView type;
    }

    public int decideResource(String type) {
        switch (type) {
            case ("Note"):
                return R.drawable.note;
            case ("Image"):
                return R.drawable.image;
        }
        throw new ResourceException("The resource " + type + " was not found.");
    }
}
