package com.tpalt.upmc.wowmountcollection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tpalt.upmc.wowmountcollection.fragments.BottomNavigationFragment;
import com.tpalt.upmc.wowmountcollection.fragments.MountListFragment;

import java.util.List;

public class WishListActivity extends AppCompatActivity implements BottomNavigationFragment.OnFragmentInteractionListener,
        MountListFragment.OnFragmentInteractionListener{

    private MountListFragment mountListFragement;
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        header = findViewById(R.id.wish_header);
        refreshHeader();
    }

    @Override
    public void registerFragment(BottomNavigationFragment fragment) {
        fragment.setCurrentActivity(BottomNavigationFragment.NavBarItem.WISH_LIST);
    }

    @Override
    public List<Mount> getMountList() {
        return WMCApplication.getWishList();
    }

    @Override
    public void registerFragment(MountListFragment fragment) {
        this.mountListFragement = fragment;
    }

    @Override
    public void setViewStatus(int status) {
        if(header != null) header.setVisibility(status);
    }

    @Override
    public void refreshHeader() {
        int size = WMCApplication.getWishList().size();
        if(size > 1){
            header.setText("Your wish list contains " + size + " mounts");
        } else if(size == 0) {
            header.setText("Your wish list is empty");
        } else {
            header.setText("Your wish list contains 1 mount");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mountListFragement.refresh();
    }
}