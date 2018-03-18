package com.tpalt.upmc.wowmountcollection;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpalt.upmc.wowmountcollection.Mount;
import com.tpalt.upmc.wowmountcollection.R;

import java.util.List;

/**
 * Created by Vanessa on 17-Mar-18.
 */

public class MountArrayAdapter extends ArrayAdapter<Mount> {
    private Context _context;
    int _layoutId;
    Mount _data [] = null;


    public MountArrayAdapter(Context context,
                             int layoutId,
                             List<Mount> items) {
        super(context, layoutId, items);
        _context = context;
        _layoutId = layoutId;
        _data = (Mount[])items.toArray();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View row = view;
        MountHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = ((Activity)_context).getLayoutInflater();
            view = inflater.inflate(_layoutId, parent,false);
            holder = new MountHolder();
            //holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            //row.setTag(holder);

        }else
        {
            holder = (MountHolder)row.getTag();

        }

        Mount mount = _data[position];
        holder.txtTitle.setText(mount.getName());
        //holder.imgIcon.setImageResource(mount.getIcon());

        return row;
    }


    static class MountHolder
    {
        //ImageView imgIcon;
        TextView txtTitle;
    }

}
