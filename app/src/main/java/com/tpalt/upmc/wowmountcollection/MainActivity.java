package com.tpalt.upmc.wowmountcollection;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.common.exception.OAuthSystemException;

import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

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

        Button coButton = findViewById(R.id.button);
        coButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WMCApplication.setRegion(selectedRegion);
                new RequestAuthorize().execute();
            }
        });

        WMCApplication.loadAllMounts(getApplicationContext());
        Log.d("LOAD", "Total mounts = "+WMCApplication.getALLMountList().size());
        for(int i = 0; i < 15; i++){
            Mount m = WMCApplication.getALLMountList().get(i);
            Log.d("LOAD", "mount: "+m.getName()+ " - "+m.getNameFr());
        }
        /*This was added to check if fragment works or not
        setContentView(R.layout.activity_my_mounts);
        */
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
}
