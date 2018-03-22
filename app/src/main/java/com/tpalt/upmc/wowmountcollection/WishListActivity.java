package com.tpalt.upmc.wowmountcollection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tpalt.upmc.wowmountcollection.fragments.BottomNavigationFragment;

public class WishListActivity extends AppCompatActivity implements BottomNavigationFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
    }

    @Override
    public void registerFragment(BottomNavigationFragment fragment) {
        fragment.setCurrentActivity(BottomNavigationFragment.NavBarItem.WISH_LIST);
    }
}
