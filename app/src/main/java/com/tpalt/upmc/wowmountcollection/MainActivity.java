package com.tpalt.upmc.wowmountcollection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tpalt.upmc.wowmountcollection.search.SearchActivity;

import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.common.exception.OAuthSystemException;

public class MainActivity extends AppCompatActivity {

    private String selectedRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set default selection for the regions
        RadioGroup radioGroup = findViewById(R.id.regionGroup);
        radioGroup.check(R.id.euRegion);
        selectedRegion = "eu";

        Button coButton = findViewById(R.id.log_in_button);
        coButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WMCApplication.offline = false;
                WMCApplication.setRegion(selectedRegion);
                new RequestAuthorize().execute();
            }
        });

        TextView offline = findViewById(R.id.offline_button);
        offline.setText(Html.fromHtml("<u>Offline mode</u>"));

        WMCApplication.loadAllMounts(getApplicationContext());
        Log.d("LOAD", "Total mounts = "+WMCApplication.getALLMountList().size());

        WMCApplication.loadWishMounts(getApplicationContext());


        //Skip the log in page if we already have the access token
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE);
        String defaultValue = "defaultValue";
        String token = sharedPref.getString("accessToken", defaultValue);
        String region = sharedPref.getString("region", defaultValue);
        String code = sharedPref.getString("code", defaultValue);
        if(!token.equals(defaultValue) && !region.equals(defaultValue) && !code.equals(defaultValue)){
            Log.d("OAUTH", "Skip log in page");
            Intent intent = new Intent(this, LoadMountsActivity.class);
            startActivity(intent);
        }
    }



    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.euRegion:
                if (checked)
                    selectedRegion = "eu";
                    break;
            case R.id.usRegion:
                if (checked)
                    selectedRegion = "us";
                    break;
            case R.id.apacRegion:
                if (checked)
                    selectedRegion = "apac";
                break;
        }
    }

    /**
     * Asynchronous task to login the user with Blizzard authentication.
     */
    class RequestAuthorize extends AsyncTask<String, Void, Intent> {

        private Exception exception;

        protected Intent doInBackground(String... strings) {
            try {
                String authenticationUrl = "https://" + WMCApplication.region + ".battle.net/oauth/authorize";
                OAuthClientRequest request = OAuthClientRequest
                        .authorizationLocation(authenticationUrl)
                        .setClientId(WMCApplication.getClientId(getApplicationContext())).setRedirectURI(WMCApplication.WMC_URL)
                        .buildQueryMessage();

                return new Intent(Intent.ACTION_VIEW,
                        Uri.parse(request.getLocationUri() + "&response_type=code&scope=wow.profile&app=oauth"));

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

    public void startOfflineMode(View view){
        WMCApplication.offline = true;
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
