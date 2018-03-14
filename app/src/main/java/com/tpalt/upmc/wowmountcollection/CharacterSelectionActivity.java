package com.tpalt.upmc.wowmountcollection;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class CharacterSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        Log.d("INTENT", "**** handle intent");
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null){
            Log.d("INTENT", "**** Catch the redireted URI !");
            String code = appLinkData.getQueryParameter("code");

            JSONObject keysJson = loadJSONFromAsset(this, "private_keys.json");
            try {
                String clientId = keysJson.getString("clientId");

                Log.d("INTENT", "clientId: "+clientId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONObject loadJSONFromAsset(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            return new JSONObject(json);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
