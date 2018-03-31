package com.tpalt.upmc.wowmountcollection.fragments;

import android.content.Context;
import android.content.Intent;
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

import com.tpalt.upmc.wowmountcollection.DetailsActivity;
import com.tpalt.upmc.wowmountcollection.Mount;
import com.tpalt.upmc.wowmountcollection.R;
import com.tpalt.upmc.wowmountcollection.WMCApplication;

import java.util.List;

/**
 * Created by Vanessa on 17-Mar-18.
 */

public class SimpleMountArrayAdapterFill extends ArrayAdapter<Mount> {

    protected static class ViewHolder {
        public TextView name;
        public ImageView icon;
        public ImageView addWish;
    }

    private final int layoutId;

    public SimpleMountArrayAdapterFill(@NonNull Context context, int resource, @NonNull List<Mount> objects) {
        super(context, resource, objects);
        layoutId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = createView();
        }

        final Mount item = getItem(position);
        final SimpleMountArrayAdapterFill.ViewHolder holder = (SimpleMountArrayAdapterFill.ViewHolder) convertView.getTag();
        holder.name.setText(item.getName());
        holder.icon.setImageResource(getDrawableId(item.getIcon()));

        holder.addWish = (ImageView) convertView.findViewById(R.id.wish);
        if(holder.addWish != null){
            /*MAJ de l'état du coeur*/
            setHeartStatusOut(item,holder);

        /*MAJ de l'état du coeur au clic sur le coeur*/
            holder.addWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setHeartStatus(item,holder);
                }
            });
        }

        return convertView;
    }

    private int getDrawableId(String name){
        Resources resources = getContext().getResources();
        int resourceId = resources.getIdentifier(name, "drawable",
                getContext().getPackageName());
        if(resourceId == 0){
            resourceId = resources.getIdentifier(Mount.DEFAULT_ICON, "drawable",
                    getContext().getPackageName());
        }
        return resourceId;
    }

    public  View createView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(layoutId, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = view.findViewById(R.id.mount_name);
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("mountName",textView.getText());
                getContext().startActivity(intent);
            }
        });
        final SimpleMountArrayAdapterFill.ViewHolder holder = new SimpleMountArrayAdapterFill.ViewHolder();
        holder.name = view.findViewById(R.id.mount_name);
        holder.icon = (ImageView) view.findViewById(R.id.mount_icon);
        if(holder.icon == null){
            Log.d("holder state" , "NULL");
        }
        holder.addWish = (ImageView) view.findViewById(R.id.wish);
        if(holder.addWish != null){
            holder.addWish.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        view.setTag(holder);
        return view;
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void setHeartStatusOut(Mount item, ViewHolder holder){
        if(WMCApplication.getWishList().contains(item)){
            holder.addWish.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        else {
            holder.addWish.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    protected void setHeartStatus(final Mount item, ViewHolder holder){
        if(WMCApplication.getWishList().contains(item)){
            WMCApplication.removeFromWishList(item);
            holder.addWish.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            notifyDataSetChanged();

        }
        else {
            WMCApplication.addToWishList(item);
            holder.addWish.setImageResource(R.drawable.ic_favorite_black_24dp);
            notifyDataSetChanged();

        }
    }
/*
    private void loadImageFromUrl(ViewHolder v, Context c) {
        Glide.with(c)
                .load(v.url)
                .placeholder(R.drawable.ic_search_black_24dp)
                .error(R.drawable.ic_face_black_24dp)
                .into(v.icon);
    }
    */
}
