package com.tpalt.upmc.wowmountcollection.fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Vaniisha on 27-Mar-18.
 */

public interface SimpleArrayAdapter {

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent);

    public View createView();
}
