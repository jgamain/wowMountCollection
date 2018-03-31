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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mount_list, container, false);
        listView = view.findViewById(R.id.listView);

        mountList = mListener.getMountList();
        adapter = new SimpleMountArrayAdapterFill(
                getContext(),
                R.layout.list_item_heart,
                mountList);

        listView.setAdapter(adapter);
              return view;
    }

}
