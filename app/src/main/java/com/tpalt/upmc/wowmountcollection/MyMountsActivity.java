package com.tpalt.upmc.wowmountcollection;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tpalt.upmc.wowmountcollection.fragments.BottomNavigationFragment;
import com.tpalt.upmc.wowmountcollection.fragments.MountListFillFragment;

import java.util.List;

public class MyMountsActivity extends AppCompatActivity implements BottomNavigationFragment.OnFragmentInteractionListener,
        MountListFillFragment.OnFragmentInteractionListener{

    private MountListFillFragment mountListFragement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mounts);

        TextView text = findViewById(R.id.myMounts_text_view);
        text.setText("Collected: "+WMCApplication.getUserMountList().size()+"/"+WMCApplication.getALLMountList().size());
    }

    @Override
    public void registerFragment(BottomNavigationFragment fragment) {
        fragment.setCurrentActivity(BottomNavigationFragment.NavBarItem.MY_MOUNTS);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public List<Mount> getMountList() {
        return WMCApplication.getMyMountsList();
    }

    @Override
    public void registerFragment(MountListFillFragment fragment) {
        this.mountListFragement = fragment;
    }
}
