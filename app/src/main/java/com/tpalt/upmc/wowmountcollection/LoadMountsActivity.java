package com.tpalt.upmc.wowmountcollection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.tpalt.upmc.wowmountcollection.search.SearchActivity;

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
    private final Handler handler = new Handler();
    private int tokenRequestCount = 0;

    private List<WowCharacter> characterList = new ArrayList<>();

    private AlertDialog connectionErrorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_mounts);

        //set connectionErrorDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.connection_error_message)
                .setTitle(R.string.alert_dialog_title);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        connectionErrorDialog = builder.create();

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
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE);
        String defaultValue = "defaultValue";
        String token = sharedPref.getString("accessToken", defaultValue);
        String code = sharedPref.getString("code", defaultValue);
        String region = sharedPref.getString("region", defaultValue);
        if(!token.equals(defaultValue) && !region.equals(defaultValue) && !code.equals(defaultValue)){
            this.accessToken = token;
            WMCApplication.setRegion(region);
            requestUserCharacters(code);

        } else {
            String appLinkAction = intent.getAction();
            Uri appLinkData = intent.getData();
            if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null){
                Log.d("OAUTH", "**** Catch the redirected URI !");
                code = appLinkData.getQueryParameter("code");

                if(this.accessToken == null){
                    new RequestAccessToken().execute(code);
                } else {
                    requestUserCharacters(code);
                }
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
            accessToken = token;
            //save the token in the SharedPreferences
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("accessToken", token);
            editor.putString("code", code);
            editor.putString("region", WMCApplication.region);
            editor.commit();
            requestUserCharacters(code);
        }
    }

    /**
     * Send a request to the API to get the user's character list and load the mount list.
     * As it's the first request with the access token, if the request fails we ask another access token.
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
                            new RequestAccessToken().execute(c);
                        }
                        else {
                            connectionErrorDialog.show();
                        }
                    }
                });

        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);

    }

    /**
     * Loads the mounts of each character in the WMCApplication.
     */
    public void loadMountList(String characters){
        try {
            JSONObject charactersJson = new JSONObject(characters);
            JSONArray charactersArray = charactersJson.getJSONArray("characters");
            Log.d("OAUTH", "nb characters: " + charactersArray.length());
            for(int i = 0; i < charactersArray.length(); i++){
                JSONObject charac = charactersArray.getJSONObject(i);
                this.characterList.add(new WowCharacter(charac));
            }
            if(!this.characterList.isEmpty()) {
                int characterCpt = 0;
                requestCharacterMountsRec(characterCpt);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        this.mRequestQueue.stop();
        connectionErrorDialog.dismiss();
        super.onDestroy();
    }

    /**
     * Send recursively requests to the API to get the mounts of the characters and fill the user mount list.
     */
    private void requestCharacterMountsRec(int characterCpt){
        if(characterCpt >= this.characterList.size()){
            //all the user mounts are loaded
            Log.d("LOAD", "changing activity");
            Intent intent = new Intent(this, MyMountsActivity.class);
            startActivity(intent);
            return;
        }
        WowCharacter character = this.characterList.get(characterCpt);
        final int newCpt = ++characterCpt;

        final String url = "https://" + WMCApplication.region + ".api.battle.net/wow/character/"
                + character.getRealm() + "/" + character.getName()
                +"?fields=mounts&locale=en_US&apikey=" + WMCApplication.getClientId(getApplicationContext());
        Log.d("LOAD", "request url: "+url);

        //delay to keep the requests number under 100 per second & 36000 per hour
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Do something with the response
                                Log.d("LOAD", "requestCharacterMounts success");
                                try {
                                    JSONObject res = new JSONObject(response);
                                    JSONArray mountsArray = res.getJSONObject("mounts").getJSONArray("collected");
                                    for(int i = 0; i < mountsArray.length(); i++){
                                        JSONObject jsonMount = mountsArray.getJSONObject(i);
                                        WMCApplication.addUserMount(jsonMount.getInt("creatureId"));
                                    }
                                    Log.d("LOAD", "userMountList size: "+WMCApplication.getUserMountList().size());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                requestCharacterMountsRec(newCpt);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // failed probably because the character is to low level to have access to mounts
                                Log.d("LOAD", "requestCharacterMounts failed");
                                error.printStackTrace();
                                requestCharacterMountsRec(newCpt);
                            }
                        });

                mRequestQueue.add(stringRequest);
            }
        }, 0);
    }

}
