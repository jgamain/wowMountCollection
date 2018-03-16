package com.tpalt.upmc.wowmountcollection;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeanne on 14/03/2018.
 */

public class WMCApplication {

    //Can not be instantiate
    private WMCApplication(){}

    public static final String KEYS_FILE = "private_keys.json";
    private static String clientId;
    private static String clientSecret;

    public static final String WMC_URL = "https://www.wowmountcollection.ovh";
    public static String region = "eu"; //default value
    public static List<Mount> mountList = new ArrayList<>();

    public static void setRegion(String r){
        Log.i("OAUTH", "set region to: "+r);
        region = r;
    }

    public static void addMount(Mount mount){
        if(!mountList.contains(mount)){
            mountList.add(mount);
        }
    }

    public static JSONObject loadJSONFromAsset(Context context, String fileName) {
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

    public static String getClientId(Context context){
        if(clientId == null){
            JSONObject keysJson = loadJSONFromAsset(context, KEYS_FILE);
            try {
                clientId = keysJson.getString("clientId");
                clientSecret = keysJson.getString("clientSecret");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return clientId;
    }

    public static String getClientSecret(Context context){
        if(clientSecret == null){
            JSONObject keysJson = loadJSONFromAsset(context, KEYS_FILE);
            try {
                clientId = keysJson.getString("clientId");
                clientSecret = keysJson.getString("clientSecret");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return clientSecret;
    }
}
