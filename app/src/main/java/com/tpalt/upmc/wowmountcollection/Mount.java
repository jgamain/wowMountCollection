package com.tpalt.upmc.wowmountcollection;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jeanne on 16/03/2018.
 */

public class Mount {

    public final static String DEFAULT_ICON = "ability_mount_dreadsteed";

    private int creatureId; // can not be null
    private int spellId; // can not be null
    private int itemId; // can not be null
    private String name;
    private String nameFr;
    private boolean isGround;
    private boolean isFlying;
    private boolean isAquatic;
    private Integer seats; // can be null
    private Integer difficulty; //can be null
    private MountSource source;
    private Faction faction;
    private String icon;

    public enum MountSource{
        VENDOR, LOOT, QUEST, PROFESSION, OTHER
    }

    public enum Faction {HORDE, ALLIANCE}

    public Mount(JSONObject jsonMount){
        try {
            creatureId = jsonMount.getInt("creatureId");
            spellId = jsonMount.getInt("spellId");
            itemId = jsonMount.getInt("itemId");
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
                switch (jsonMount.getString("source").toUpperCase()){
                    case "VENDOR": source = MountSource.VENDOR; break;
                    case "LOOT": source = MountSource.LOOT; break;
                    case "QUEST": source = MountSource.QUEST; break;
                    case "PROFESSION": source = MountSource.PROFESSION; break;
                    default: source = MountSource.OTHER;
                }
            }
            if(jsonMount.has("faction")){
                switch (jsonMount.getString("faction").toUpperCase()){
                    case "ALLIANCE": faction = Faction.ALLIANCE; break;
                    case "HORDE": faction = Faction.HORDE; break;
                }
            }
            setIcon(jsonMount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setIcon(JSONObject jsonMount) throws JSONException {
        String iconName = jsonMount.getString("icon");
        icon = iconName.replace("-", "_");
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

    public int getSpellId() {
        return spellId;
    }

    public int getItemId() {
        return itemId;
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

    public Faction getFaction() {
        return faction;
    }

    public String getIcon (){
        return icon;
    }
}
