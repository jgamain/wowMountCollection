package com.tpalt.upmc.wowmountcollection;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jeanne on 14/03/2018.
 */

public class WMCApplication {

    public static final String MOUNTS_FILE = "mountsComplete.json";
    public static final String KEYS_FILE = "private_keys.json";
    public static final String WISH_FILE = "wish_mounts.json";

    private static String clientId;
    private static String clientSecret;

    public static final String WMC_URL = "https://www.wowmountcollection.ovh";
    public static String region = "eu"; //default value

    private static List<Mount> allMountList = new ArrayList<>();
    private static List<Integer> userMountList = new ArrayList<>(); // store the creatureId
    private static List<Mount> myMountsList = new ArrayList<>();


    private static List<Integer> userWishList = new ArrayList<>(); // store the creatureId
    private static List<Mount> wishList = new ArrayList<>();

    public static boolean offline = false;

    //Can not be instantiate
    private WMCApplication() {
    }

    /**
     * Loads the list of all the mounts from the json file of the application.
     */
    public static void loadAllMounts(Context context) {
        try {
            JSONObject json = loadJSONFromAsset(context, MOUNTS_FILE);
            JSONArray mountsArray = json.getJSONArray("mounts");
            for (int i = 0; i < mountsArray.length(); i++) {
                JSONObject mount = mountsArray.getJSONObject(i);
                addToAllMounts(new Mount(mount));
            }
            Collections.sort(allMountList, new Comparator<Mount>() {
                @Override
                public int compare(Mount mount, Mount t1) {
                    return mount.getName().compareTo(t1.getName());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setRegion(String r) {
        Log.i("OAUTH", "set region to: " + r);
        region = r;
    }

    public static void addUserMount(int creatureId) {
        if (!userMountList.contains(creatureId)) {
            userMountList.add(creatureId);
        }
    }

    public static void addUserWishMount(int creatureId) {
        if (!userWishList.contains(creatureId)) {
            userWishList.add(creatureId);
        }
    }

    private static void addToAllMounts(Mount mount) {
        if (!allMountList.contains(mount)) {
            allMountList.add(mount);
        }
    }

    public static List<Integer> getUserMountList() {
        return userMountList;
    }

    public static List<Mount> getALLMountList() {
        return allMountList;
    }

    public static List<Mount> getMyMountsList() {
        if (myMountsList.isEmpty() && !userMountList.isEmpty()) {
            for (Mount mount : allMountList) {
                if (userMountList.contains(mount.getCreatureId())) {
                    myMountsList.add(mount);
                }
            }
        }
        Log.d("My" , "= "+ myMountsList.size());
        return myMountsList;
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

    public static JSONObject loadJSONFromFile(File f) {
        try {
            InputStream is = new FileInputStream(f);
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

    public static String getClientId(Context context) {
        if (clientId == null) {
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

    public static String getClientSecret(Context context) {
        if (clientSecret == null) {
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

    public static List<Mount> getWishList() {
        return wishList;
    }

    public static void addToWishList(Mount m,Context context) {
        if(!wishList.contains(m)){
            wishList.add(m);
            addMountToWishFile(m,context);
        }
    }

    /*remove from all mounts list*/
    public static void removeFromWishList(Mount m,Context context) {
        wishList.remove(m);
        deleteMountToWishFile(m,context);
    }

    /**
     * Loads the list of all wish mounts from the json file of the application.
     */
    public static void loadWishMounts(Context context) {
        Log.d("LOADWISH", "YES");
        File directory = context.getFilesDir();
        AccessFile.readFile(context,directory);
    }


    public static void addMountToWishFile(Mount m,Context context){
        Log.d("WRITEFILE_ADD", "YES");
        File directory = context.getFilesDir();
        AccessFile.writeFile(m,context,directory,true);
    }

    public static void deleteMountToWishFile(Mount m,Context context){
        Log.d("WRITEFILE_DELETE", "YES");
        File directory = context.getFilesDir();
        AccessFile.writeFile(m,context,directory,false);

    }

    public static List<Integer> getIntegerWishList(){
        return userWishList;
    }


    public static void addAllWishIntegerMounts(){
        for(Mount m : getALLMountList()){
            for(Integer i : getIntegerWishList()){
                if(i.equals(m.getCreatureId()) && !wishList.contains(m)){
                    wishList.add(m);
                }
            }
        }
    }

    public static Mount getMountById(int creatureId){
        for(Mount m : allMountList){
            if(m.getCreatureId() == creatureId) return m;
        }
        return null;
    }

    /**
     * Returns the first mount found with the given name (mounts' names are not unique).
     */
    public static Mount getMountByName(String mountName){
        for(Mount m : allMountList){
            if(m.getName().equalsIgnoreCase(mountName)) return m;
        }
        return null;
    }

    /**
     * Returns the drawable id of the given file name or the default mount icon if the resource is not found.
     */
    public static int getDrawableId(String iconName, Context context){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(iconName, "drawable",
                context.getPackageName());
        if(resourceId == 0){
            resourceId = resources.getIdentifier(Mount.DEFAULT_ICON, "drawable",
                    context.getPackageName());
        }
        return resourceId;
    }

}
