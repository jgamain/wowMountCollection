package com.tpalt.upmc.wowmountcollection.fragments;

import android.os.Bundle;
import com.tpalt.upmc.wowmountcollection.R;

/**
 * Created by Vaniisha on 28-Mar-18.
 */

/**
 * Fragment for the ListView with heart icon and confirm dialog alert.
 * (used in wish list activity)
 */
public class MountListConfirmFragment extends MountListFillFragment {

    public MountListConfirmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setAdapter(){
        adapter = new SimpleMountArrayAdapterConfirm(
                getContext(),
                R.layout.list_item_heart,
                mountList, this);

        listView.setAdapter(adapter);
    }

}
