package com.tpalt.upmc.wowmountcollection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageDialogActivity extends Activity {

    private ImageView image;
    private Integer creatureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_dialog);

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                this.finish();
            }
            creatureId = extras.getInt("creatureId");
        } else {
            creatureId = savedInstanceState.getInt("creatureId");
        }

        image = findViewById(R.id.dialog_image);
        image.setClickable(true);

        loadImageFromUrl();

        //finish the activity (dismiss the image dialog) if the user clicks anywhere on the image
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("creatureId", creatureId);
        super.onSaveInstanceState(outState);
    }

    private void loadImageFromUrl() {
        Glide.with(this)
                .load(getImageUrl(creatureId))
                .error(R.drawable.ic_face_black_24dp)
                .into(image);
    }

    public String getImageUrl(Integer creatureId){
        return "http://media.blizzard.com/wow/renders/npcs/zoom/creature"+creatureId+".jpg";
    }

}
