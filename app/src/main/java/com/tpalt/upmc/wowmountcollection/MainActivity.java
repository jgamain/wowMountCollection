package com.tpalt.upmc.wowmountcollection;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.common.exception.OAuthSystemException;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button coButton = findViewById(R.id.button);
        coButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RequestAuthorize().execute();
            }
        });

    }

    /**
     * Asynchronous task to login the user with Blizzard authentication.
     */
    class RequestAuthorize extends AsyncTask<String, Void, Intent> {

        private Exception exception;

        protected Intent doInBackground(String... strings) {
            try {
                JSONObject keysJson = UsefulTools.loadJSONFromAsset(getBaseContext(), UsefulTools.KEYS_FILE);
                String clientId = keysJson.getString("clientId");

                String authenticationUrl = "https://" + WMCApplication.region + ".battle.net/oauth/authorize";
                OAuthClientRequest request = OAuthClientRequest
                        .authorizationLocation(authenticationUrl)
                        .setClientId(clientId).setRedirectURI(WMCApplication.WMC_URL)
                        .buildQueryMessage();


                return new Intent(Intent.ACTION_VIEW,
                        Uri.parse(request.getLocationUri() + "&response_type=code&scope=wow.profile&app=oauth"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (OAuthSystemException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Intent intent) {
            // TODO: check this.exception
            startActivity(intent);
        }
    }

}
