package com.tpalt.upmc.wowmountcollection.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.tpalt.upmc.wowmountcollection.Mount;
import com.tpalt.upmc.wowmountcollection.R;

import java.util.List;

/**
 * Created by Vanessa on 17-Mar-18.
 */

public class MountArrayAdapter extends ArrayAdapter<Mount> {

    private static class ViewHolder {
        public TextView name;
        // public ImageView image;
    }

    private final int layoutId;

    public MountArrayAdapter(@NonNull Context context, int resource, @NonNull List<Mount> objects) {
        super(context, resource, objects);
        layoutId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = createView();
        }

        final Mount item = getItem(position);

        MountArrayAdapter.ViewHolder holder = (MountArrayAdapter.ViewHolder)convertView.getTag();

        holder.name.setText(item.getName());

        //holder.image.setTag(item.getIcon());

        return convertView;
    }

    private View createView(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(layoutId, null);

        MountArrayAdapter.ViewHolder holder = new MountArrayAdapter.ViewHolder();
        holder.name = view.findViewById(R.id.mount_name);
        //holder.image = view.findViewById(R.id.mount_icon);

        view.setTag(holder);
        return view;
    }
}
