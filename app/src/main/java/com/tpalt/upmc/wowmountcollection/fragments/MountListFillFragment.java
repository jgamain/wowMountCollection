package com.tpalt.upmc.wowmountcollection.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpalt.upmc.wowmountcollection.R;


/**
 * Fragment for the ListView with heart icon (without confirm dialog alert).
 * (used in search activity)
 */
public class MountListFillFragment extends MountListFragment {

    public MountListFillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setAdapter(){
        adapter = new SimpleMountArrayAdapterFill(
                getContext(),
                R.layout.list_item_heart,
                mountList);

        listView.setAdapter(adapter);
    }

}
