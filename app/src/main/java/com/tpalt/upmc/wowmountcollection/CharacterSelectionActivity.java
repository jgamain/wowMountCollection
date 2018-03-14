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

public class CharacterSelectionActivity extends AppCompatActivity {

    private String accessToken;
    private RequestQueue mRequestQueue;
    private List<WowCharacter> charactersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

        charactersList = new ArrayList<>();
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
                requestUserCharacters();
            }
        }
    }

    /**
     * Asynchronous task to get the access token. Must be call with one parameter: the "code" returned by the authentication request
     */
    class RequestAccessToken extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... strings) {
            if(strings.length != 1) return null;
            try{
                JSONObject keysJson = UsefulTools.loadJSONFromAsset(getBaseContext(), UsefulTools.KEYS_FILE);
                String clientId = keysJson.getString("clientId");
                String clientSecret = keysJson.getString("clientSecret");

                OAuthClientRequest request = null;

                request = OAuthClientRequest.tokenLocation(
                        "https://" + WMCApplication.region + ".battle.net/oauth/token")
                        .setGrantType(GrantType.AUTHORIZATION_CODE)
                        .setClientId(clientId)
                        .setClientSecret(clientSecret)
                        .setRedirectURI(WMCApplication.WMC_URL)
                        .setCode(strings[0])
                        .buildBodyMessage();

                OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

                OAuthJSONAccessTokenResponse response = oAuthClient.accessToken(request);
                return response.getAccessToken();

            } catch (JSONException e) {
                e.printStackTrace();
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
            requestUserCharacters();
        }
    }

    /**
     * Send a request to the API to set the user's characters list.
     */
    private void requestUserCharacters(){
        String url = "https://" + WMCApplication.region + ".api.battle.net/wow/user/characters?access_token=" + this.accessToken;

        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        Log.d("OAUTH", "requestUserCharacters success");
                        setCharactersList(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.d("OAUTH", "requestUserCharacters failed");
                        error.printStackTrace();
                    }
                });

        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);

    }

    public void setCharactersList(String characters){
        try {
            JSONObject charactersJson = new JSONObject(characters);
            JSONArray charactersArray = charactersJson.getJSONArray("characters");
            this.charactersList.clear();
            for(int i = 0; i < charactersArray.length(); i++){
                JSONObject charac = charactersArray.getJSONObject(i);
                this.charactersList.add(new WowCharacter(charac));
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
