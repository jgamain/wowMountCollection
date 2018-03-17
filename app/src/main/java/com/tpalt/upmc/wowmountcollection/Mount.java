package com.tpalt.upmc.wowmountcollection;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jeanne on 16/03/2018.
 */

public class Mount {

    private int creatureId; // can not be null
    private String name;
    private String nameFr;
    private boolean isGround;
    private boolean isFlying;
    private boolean isAquatic;
    private Integer seats; // can be null
    private Integer difficulty; //can be null
    private MountSource source;

    public enum MountSource{
        VENDOR, LOOT, QUEST, PROFESSION, OTHER
    }

    public Mount(JSONObject jsonMount){
        try {
            creatureId = jsonMount.getInt("creatureId");
            name = jsonMount.getString("name");
            if(jsonMount.has("nameFr"))
                nameFr = jsonMount.getString("nameFr");
            isGround = jsonMount.getBoolean("isGround");
            isFlying = jsonMount.getBoolean("isFlying");
            isAquatic = jsonMount.getBoolean("isAquatic");
            if(jsonMount.has("seats"))
                seats = jsonMount.getInt("seats");
            if(jsonMount.has("difficulty"))
                difficulty = jsonMount.getInt("difficulty");

            if(jsonMount.has("source")){
                switch (jsonMount.getString("source")){
                    case "vendor": source = MountSource.VENDOR; break;
                    case "loot": source = MountSource.LOOT; break;
                    case "quest": source = MountSource.QUEST; break;
                    case "profession": source = MountSource.PROFESSION;
                    default: source = MountSource.OTHER;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Mount){
            return creatureId == ((Mount)obj).creatureId;
        }
        return false;
    }

    public int getCreatureId() {
        return creatureId;
    }

    public String getName() {
        return name;
    }

    public String getNameFr() {
        return nameFr;
    }

    public boolean isGround() {
        return isGround;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public boolean isAquatic() {
        return isAquatic;
    }

    public Integer getSeats() {
        return seats;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public MountSource getSource() {
        return source;
    }
}
