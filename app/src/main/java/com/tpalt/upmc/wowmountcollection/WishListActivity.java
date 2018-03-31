package com.tpalt.upmc.wowmountcollection;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tpalt.upmc.wowmountcollection.fragments.BottomNavigationFragment;
import com.tpalt.upmc.wowmountcollection.fragments.MountListConfirmFragment;
import com.tpalt.upmc.wowmountcollection.fragments.MountListFillFragment;
import com.tpalt.upmc.wowmountcollection.fragments.MountListFragment;

import java.util.ArrayList;
import java.util.List;

public class WishListActivity extends AppCompatActivity implements BottomNavigationFragment.OnFragmentInteractionListener,
        MountListFragment.OnFragmentInteractionListener{

    private List<Mount> wishMounts = new ArrayList<>();
    private MountListFragment mountListFragement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        wishMounts = WMCApplication.getWishList();
        //mountListFragement.refresh();

        TextView text = findViewById(R.id.wish_text);
        text.setText("Your wish list contains: "+WMCApplication.getWishList().size()+" mount");

    }

    @Override
    public void registerFragment(BottomNavigationFragment fragment) {
        fragment.setCurrentActivity(BottomNavigationFragment.NavBarItem.WISH_LIST);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public List<Mount> getMountList() {
        return WMCApplication.getWishList();
    }

    @Override
    public void registerFragment(MountListFragment fragment) {
        this.mountListFragement = fragment;
    }
}