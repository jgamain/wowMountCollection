package com.tpalt.upmc.wowmountcollection;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.common.exception.OAuthSystemException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button coButton = findViewById(R.id.button);
        coButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String authenticationUrl = "https://eu.battle.net/oauth/authorize";
                OAuthClientRequest request = null;
                try {
                    request = OAuthClientRequest
                            .authorizationLocation(authenticationUrl)
                            .setClientId("qb82s5wputvqjgbscrh3dpqfb3z8mcn9").setRedirectURI("https://www.wowmountcollection.ovh")
                            .buildQueryMessage();
                } catch (OAuthSystemException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(request.getLocationUri() + "&response_type=code&scope=wow.profile&app=oauth"));
                startActivity(intent);
            }
        });

    }

}
