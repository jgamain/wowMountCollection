package com.tpalt.upmc.wowmountcollection;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeanne on 14/03/2018.
 */

public class WowCharacter {
    private String name;
    private String realm;
    private int level;
    // http://+<region>+.battle.net/static-render/+<region>+/+<avatarURL>
    private String avatarURL;

    public WowCharacter(JSONObject characterJson){
        try {
            this.name = characterJson.getString("name");
            this.realm = characterJson.getString("realm");
            this.level = characterJson.getInt("level");
            this.avatarURL = characterJson.getString("thumbnail");
            Log.d("OAUTH", "Create new character: "+name+" "+realm+" lvl"+level);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Mount> getMountList(Context context){
        //TODO: get mounts from the api
        return new ArrayList<>();
    }
}
