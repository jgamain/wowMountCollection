package com.tpalt.upmc.wowmountcollection;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import net.smartam.leeloo.client.OAuthClient;
import net.smartam.leeloo.client.URLConnectionClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthJSONAccessTokenResponse;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.message.types.GrantType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadMountsActivity extends AppCompatActivity {

    private String accessToken;
    private RequestQueue mRequestQueue;
    private int tokenRequestCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_mounts);

        startRequestQueue();
        handleIntent(getIntent());
    }

    private void startRequestQueue(){
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();
    }

    private void handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null){
            Log.d("OAUTH", "**** Catch the redireted URI !");
            String code = appLinkData.getQueryParameter("code");

            if(this.accessToken == null){
                new RequestAccessToken().execute(code);
            } else {
                requestUserCharacters(code);
            }
        }
    }

    /**
     * Asynchronous task to get the access token. Must be call with one parameter: the "code" returned by the authentication request
     */
    class RequestAccessToken extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String code;

        protected String doInBackground(String... strings) {
            if(strings.length != 1) return null;
            code = strings[0];
            try{
                OAuthClientRequest request = null;

                request = OAuthClientRequest.tokenLocation(
                        "https://" + WMCApplication.region + ".battle.net/oauth/token")
                        .setGrantType(GrantType.AUTHORIZATION_CODE)
                        .setClientId(WMCApplication.getClientId(getApplicationContext()))
                        .setClientSecret(WMCApplication.getClientSecret(getApplicationContext()))
                        .setRedirectURI(WMCApplication.WMC_URL)
                        .setCode(code)
                        .buildBodyMessage();

                OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

                OAuthJSONAccessTokenResponse response = oAuthClient.accessToken(request);
                return response.getAccessToken();

            } catch (OAuthSystemException e) {
                e.printStackTrace();
            } catch (OAuthProblemException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String token) {
            // TODO: check this.exception
            accessToken = token;
            requestUserCharacters(code);
        }
    }

    /**
     * Send a request to the API to set the user's characters list.
     */
    private void requestUserCharacters(String code){
        String url = "https://" + WMCApplication.region + ".api.battle.net/wow/user/characters?access_token=" + this.accessToken;
        final String c = code;
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        Log.d("OAUTH", "requestUserCharacters success");
                        loadMountList(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("OAUTH", "requestUserCharacters failed");
                        error.printStackTrace();
                        //try to get a new token (3 times max)
                        if(tokenRequestCount <= 3){
                            tokenRequestCount++;
                            Log.d("OAUTH", "requesting another token...");
                            try {
                                //delay to keep the requests number under 100 per second & 36000 per hour
                                Thread.sleep(1000);
                                new RequestAccessToken().execute(c);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            //TODO: inform the user
                        }
                    }
                });

        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);

    }

    /**
     * Loads the mounts of each character in the WMCApplication.
     */
    public void loadMountList(String characterList){
        try {
            JSONObject charactersJson = new JSONObject(characterList);
            JSONArray charactersArray = charactersJson.getJSONArray("characters");
            Log.d("OAUTH", "nb characters: " + charactersArray.length());
            for(int i = 0; i < charactersArray.length(); i++){
                JSONObject charac = charactersArray.getJSONObject(i);
                WowCharacter wowCharacter = new WowCharacter(charac);

                try {
                    //delay to keep the requests number under 100 per second & 36000 per hour
                    Thread.sleep(1000);
                    for(Mount m : wowCharacter.getMountList(getApplicationContext())){
                        WMCApplication.addMount(m);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        this.mRequestQueue.stop();
        super.onDestroy();
    }

}