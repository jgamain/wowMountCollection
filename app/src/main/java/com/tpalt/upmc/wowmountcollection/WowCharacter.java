package com.tpalt.upmc.wowmountcollection;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
            Log.d("LOAD", "Create new character: "+name+" "+realm+" lvl"+level);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String getRealm() {
        return realm;
    }

    public int getLevel() {
        return level;
    }

    public String getAvatarURL() {
        return avatarURL;
    }
}
