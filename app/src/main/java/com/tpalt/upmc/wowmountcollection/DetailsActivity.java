package com.tpalt.upmc.wowmountcollection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String mountName = extras.getString("mountName");
                if (TextUtils.isEmpty(mountName)){
                    this.finish();
                }
                TextView name = findViewById(R.id.details_mount_name);
                name.setText(mountName);
            }
        }
    }
}
