package com.tpalt.upmc.wowmountcollection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tpalt.upmc.wowmountcollection.fragments.BottomNavigationFragment;
import com.tpalt.upmc.wowmountcollection.fragments.MountListFragment;

import java.util.List;

public class MyMountsActivity extends AppCompatActivity implements BottomNavigationFragment.OnFragmentInteractionListener,
        MountListFragment.OnFragmentInteractionListener{

    private MountListFragment mountListFragement;
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mounts);

        header = findViewById(R.id.myMounts_header);
        refreshHeader();
    }

    @Override
    public void registerFragment(BottomNavigationFragment fragment) {
        fragment.setCurrentActivity(BottomNavigationFragment.NavBarItem.MY_MOUNTS);
    }

    @Override
    public List<Mount> getMountList() {
        return WMCApplication.getMyMountsList();
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
        header.setText("Collected: "+WMCApplication.getUserMountList().size()+"/"+WMCApplication.getALLMountList().size());
    }
}
