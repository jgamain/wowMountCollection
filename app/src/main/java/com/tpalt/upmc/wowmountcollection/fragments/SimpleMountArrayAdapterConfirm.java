package com.tpalt.upmc.wowmountcollection.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tpalt.upmc.wowmountcollection.Mount;
import com.tpalt.upmc.wowmountcollection.R;
import com.tpalt.upmc.wowmountcollection.WMCApplication;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Vaniisha on 27-Mar-18.
 */

public class SimpleMountArrayAdapterConfirm extends ArrayAdapter<Mount> implements SimpleArrayAdapter {
    private static class ViewHolder {
        public TextView name;
        public ImageView icon;
        public ImageView addWish;
        public String url;
    }

    private final int layoutId;
    private final Context context;
    public final String preUrl = "http://media.blizzard.com/wow/icons/56/";

    public SimpleMountArrayAdapterConfirm(@NonNull Context context, int resource, @NonNull List<Mount> objects) {
        super(context, resource, objects);
        this.layoutId = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = createView();
        }

        final Mount item = getItem(position);
        final SimpleMountArrayAdapterConfirm.ViewHolder holder = (SimpleMountArrayAdapterConfirm.ViewHolder) convertView.getTag();
        holder.name.setText(item.getName());
        holder.url = preUrl + item.getIcon() + ".jpg";
        Log.i("URL ", "voici l'URL :" +holder.url);
        loadImageFromUrl(holder,getContext());

        holder.addWish = (ImageView) convertView.findViewById(R.id.wish);

        /*MAJ de l'état du coeur*/
        setHeartStatusOut(item,holder);

        /*MAJ de l'état du coeur au clic sur le coeur*/
        holder.addWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHeartStatus(item,holder);
            }
        });
        return convertView;
    }

    public  View createView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(layoutId, null);

        final SimpleMountArrayAdapterConfirm.ViewHolder holder = new SimpleMountArrayAdapterConfirm.ViewHolder();
        holder.name = view.findViewById(R.id.mount_name);
        holder.icon = (ImageView) view.findViewById(R.id.mount_icon);
        if(holder.icon == null){
            Log.i("holder state" , "NULL");
        }
        holder.addWish = (ImageView) view.findViewById(R.id.wish);
        holder.addWish.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        view.setTag(holder);
        return view;
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void setHeartStatusOut(Mount item, SimpleMountArrayAdapterConfirm.ViewHolder holder){
        if(WMCApplication.getWishList().contains(item)){
            holder.addWish.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        else {
            holder.addWish.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private void setHeartStatus(final Mount item, SimpleMountArrayAdapterConfirm.ViewHolder holder){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setMessage(R.string.confirmation_delete);
        alert.setTitle(R.string.alert_dialog_deleting);
        alert.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int whichButton) {
                        dialog.cancel();
                    }
                });

        alert.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int whichButton) {

                        WMCApplication.removeFromWishList(item);
                        notifyDataSetChanged();
                    }
                });
        alert.create().show(); // btw show() creates and shows it..
    }

    private void loadImageFromUrl(SimpleMountArrayAdapterConfirm.ViewHolder v, Context c) {
        final AtomicBoolean loaded = new AtomicBoolean();
        //Picasso.with(c).setLoggingEnabled(true);
        Glide.with(c)
                .load(v.url)
                .placeholder(R.drawable.ic_search_black_24dp)
                .error(R.drawable.ic_face_black_24dp)
                .into(v.icon);


    }
}
