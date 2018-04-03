package com.tpalt.upmc.wowmountcollection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpalt.upmc.wowmountcollection.search.SearchEngine;

public class DetailsActivity extends AppCompatActivity {

    private Mount mount;
    private boolean wished;
    private ImageView favIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                this.finish();
            }
            String mountName = extras.getString("mountName");
            if (TextUtils.isEmpty(mountName)){
                this.finish();
            }
            TextView name = findViewById(R.id.details_mount_name);
            name.setText(mountName);
            mount = WMCApplication.getMountByName(mountName);
        }

        favIcon = findViewById(R.id.details_fav);
        wished = WMCApplication.getWishList().contains(mount);
        setFavIconStatus();

        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wished = !wished;
                setFavIconStatus();
                updateWishList();
            }
        });

    }

    private void setFavIconStatus(){
        if(wished) favIcon.setImageResource(R.drawable.ic_favorite_black_36dp);
        else favIcon.setImageResource(R.drawable.ic_favorite_border_black_36dp);
    }
    private void updateWishList(){
        if(wished) WMCApplication.addToWishList(mount, getBaseContext());
        else WMCApplication.removeFromWishList(mount, getBaseContext());
    }

}
