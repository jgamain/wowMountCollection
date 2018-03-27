package com.tpalt.upmc.wowmountcollection.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tpalt.upmc.wowmountcollection.Mount;
import com.tpalt.upmc.wowmountcollection.R;
import com.tpalt.upmc.wowmountcollection.WMCApplication;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Vanessa on 17-Mar-18.
 */

public class MountArrayAdapter extends ArrayAdapter<Mount> {

    private static class ViewHolder {
        public TextView name;
        public ImageView icon;
        public ImageView addWish;
        public String url;
    }

    private final int layoutId;
    private Context context;
    public final String preUrl = "https://media.blizzard.com/wow/icons/56/";

    public MountArrayAdapter(@NonNull Context context, int resource, @NonNull List<Mount> objects) {
        super(context, resource, objects);
        layoutId = resource;
        context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = createView();
        }

        final Mount item = getItem(position);
        MountArrayAdapter.ViewHolder holder = (MountArrayAdapter.ViewHolder) convertView.getTag();
        holder.name.setText(item.getName());
        holder.url = preUrl + item.getIcon() + ".jpg";
        //System.out.println("LINK = " + holder.url);

        holder.addWish = (ImageView) convertView.findViewById(R.id.wish);
        holder.addWish.setTag(R.id.POSITION, new Integer(position));
        /*key = 2 -> 1 cliqué, 0 non cliqué*/
        holder.addWish.setTag(R.id.CLICK, new Integer(0));

        return convertView;
    }

    private View createView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(layoutId, null);
        final MountArrayAdapter.ViewHolder holder = new MountArrayAdapter.ViewHolder();
        holder.name = view.findViewById(R.id.mount_name);
        holder.icon = (ImageView) view.findViewById(R.id.mount_icon);

        holder.addWish = (ImageView) view.findViewById(R.id.wish);
        holder.addWish.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        loadImageFromUrl(holder,getContext());
        holder.addWish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println("CLIC vaut " + view.getTag(R.id.CLICK) );
                Integer idC = (Integer)view.getTag(R.id.CLICK);
                if (idC.intValue() == 1) {
                    System.out.println("CLIC 1");
                    view.setTag(R.id.CLICK, new Integer(0));
                    notifyDataSetChanged();

                    System.out.println("RemoveWish");
                    WMCApplication.removeFromAllMountToWishList((Integer) view.getTag(R.id.POSITION));
                    holder.addWish.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    notifyDataSetChanged();


                } else {
                    System.out.println("CLIC 2");
                    view.setTag(R.id.CLICK, new Integer(1));
                    notifyDataSetChanged();

                    System.out.println("Wish");
                    WMCApplication.addToWishList((Integer) view.getTag(R.id.POSITION));
                    holder.addWish.setImageResource(R.drawable.ic_favorite_black_24dp);
                    notifyDataSetChanged();
                }
            }
        });


        view.setTag(holder);
        return view;
    }

    private void changeHeart(boolean b){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(layoutId, null);
        MountArrayAdapter.ViewHolder holder = new MountArrayAdapter.ViewHolder();
        holder.addWish = (ImageView) view.findViewById(R.id.wish);

        if(b){
            holder.addWish.setImageResource(R.drawable.ic_favorite_black_24dp);

        }
        holder.addWish.setImageResource(R.drawable.ic_favorite_border_black_24dp);
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void loadImageFromUrl(ViewHolder v, Context c) {
        System.out.println("LOADIMAGE");
        final AtomicBoolean loaded = new AtomicBoolean();
        Picasso.with(c).setLoggingEnabled(true);
        Picasso
                .with(c)
                .load(v.url)
                .resize(dpToPx(80), dpToPx(80))
                .placeholder(R.drawable.ic_face_black_24dp)
                .error(R.drawable.ic_search_black_24dp)
                .into(v.icon,new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        loaded.set(true);
                        System.out.println("YEEEEEEEEES" );
                    }

                    @Override
                    public void onError() {
                        System.out.println("ERROOOOOOR" );
                    }

                });


    }
}
