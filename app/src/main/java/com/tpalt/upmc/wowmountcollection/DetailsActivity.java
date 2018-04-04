package com.tpalt.upmc.wowmountcollection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private Mount mount;
    private boolean wished;
    private ImageView favIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        int creatureId;
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                this.finish();
            }
            creatureId = extras.getInt("creatureId");
        } else {
            creatureId = savedInstanceState.getInt("creatureId");
        }

        mount = WMCApplication.getMountById(creatureId);
        TextView name = findViewById(R.id.details_mount_name);
        name.setText(mount.getName());

        //set favorite icon
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

        //set mount icon
        ImageView mountIcon = findViewById(R.id.details_mount_icon);
        mountIcon.setImageResource(WMCApplication.getDrawableId(mount.getIcon(), this));

        setInformation();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("creatureId", mount.getCreatureId());
        super.onSaveInstanceState(outState);
    }

    private void setFavIconStatus(){
        if(wished) favIcon.setImageResource(R.drawable.ic_favorite_black_36dp);
        else favIcon.setImageResource(R.drawable.ic_favorite_border_black_36dp);
    }
    private void updateWishList(){
        if(wished) WMCApplication.addToWishList(mount, getBaseContext());
        else WMCApplication.removeFromWishList(mount, getBaseContext());
    }

    private void setInformation(){
        //difficulty
        TextView difficulty = findViewById(R.id.details_difficulty);
        if(mount.getDifficulty() == null){
            difficulty.setVisibility(View.GONE);
        } else {
            switch (mount.getDifficulty()){
                case -1: difficulty.append(Html.fromHtml("<font color='#CD0000'>removed</font>")); break;
                case 1: difficulty.setCompoundDrawablesWithIntrinsicBounds
                        (0, 0, R.drawable.ic_favorite_black_24dp,0);
                    break;
                case 2: difficulty.setCompoundDrawablesWithIntrinsicBounds
                        (0, 0, R.drawable.ic_favorite_border_black_24dp,0);
                    break;
                case 3: difficulty.setCompoundDrawablesWithIntrinsicBounds
                        (0, 0, R.drawable.ic_face_black_24dp,0);
                    break;
                case 4: difficulty.append(Html.fromHtml("<font color='#CD0000'>not yet available</font>")); break;
            }
        }

        //source
        TextView source = findViewById(R.id.details_source);
        if(mount.getSource() == null || mount.getSource().equals(Mount.MountSource.OTHER)){
            source.setVisibility(View.GONE);
        } else {
            source.append(mount.getSource().toString().toLowerCase());
        }

        //type
        TextView type = findViewById(R.id.details_type);
        if(mount.isGround()) type.append(Html.fromHtml("<font color='#026200'>ground   </font>"));
        if(mount.isFlying()) type.append(Html.fromHtml("<font color='#FF7500'>flying   </font>"));
        if(mount.isAquatic()) type.append(Html.fromHtml("<font color='#0055AA'>aquatic</font>"));

        //faction
        if(mount.getFaction() == null){
            findViewById(R.id.details_faction_layout).setVisibility(View.GONE);
        } else {
            ImageView faction = findViewById(R.id.details_faction);
            if(mount.getFaction().equals(Mount.Faction.ALLIANCE)) {
                faction.setImageResource(R.drawable.ic_alliance_color);
            } else {
                faction.setImageResource(R.drawable.ic_horde_red);
            }
        }

        //seats
        TextView seats = findViewById(R.id.details_seats);
        if(mount.getSeats() == null){
            seats.setVisibility(View.GONE);
        } else {
            seats.append(Integer.toString(mount.getSeats()));
        }
    }

    public void showImage(View view){
        Intent intent = new Intent(this, ImageDialogActivity.class);
        intent.putExtra("creatureId", mount.getCreatureId());
        startActivity(intent);
    }
}
