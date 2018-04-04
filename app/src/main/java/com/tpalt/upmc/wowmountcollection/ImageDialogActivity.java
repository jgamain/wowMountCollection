package com.tpalt.upmc.wowmountcollection;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

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

        loadImageFromUrl();

        //finish the activity (dismiss the image dialog) if the user clicks anywhere on the screen
        RelativeLayout layout = findViewById(R.id.image_dialog_layout);
        layout.setOnClickListener(new View.OnClickListener() {
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
        final ProgressBar progressBar = findViewById(R.id.image_progress);
        Glide.with(this)
                .load(getImageUrl(creatureId))
                .listener(new RequestListener<String, GlideDrawable>() {

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onException(Exception e, String model, Target target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        findViewById(R.id.image_not_found).setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(image);
    }

    public String getImageUrl(Integer creatureId){
        return "http://media.blizzard.com/wow/renders/npcs/zoom/creature"+creatureId+".jpg";
    }

}
