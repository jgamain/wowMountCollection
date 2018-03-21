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

    static class MountHolder
    {
        //ImageView icon;
        TextView name;
    }

   private Context _context;
    int _layoutId;
    List<Mount> _data = null;


    public MountArrayAdapter(Context context,
                             int layoutId,
                             List<Mount> items) {
        super(context, layoutId, items);
        _context = context;
        _layoutId = layoutId;
        _data = items;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = createView();
        }
        Mount mount = _data.get(position);

        MountHolder holder = (MountHolder) view.getTag();
        holder.name.setText(mount.getName());
        //holder.imgIcon.setImageResource(mount.getIcon());

        return view;
    }

    private View createView(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(_layoutId, null);

        MountHolder holder = new MountHolder();
        holder.name = view.findViewById(R.id.name);
        view.setTag(holder);
        return view;
    }




}
