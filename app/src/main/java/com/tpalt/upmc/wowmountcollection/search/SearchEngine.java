package com.tpalt.upmc.wowmountcollection.search;

import com.tpalt.upmc.wowmountcollection.Mount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeanne on 17/03/2018.
 */

public class SearchEngine {

    //search criteria with default values (package private)
    static String name = "";
    //faction
    static boolean alliance = true;
    static boolean horde = true;
    //difficulty
    static boolean easy = true;
    static boolean medium = true;
    static boolean hard = true;
    static boolean removed = true;
    static boolean unavailable = true; // not yet in the game
    //seats
    static boolean oneSeat = true;
    static boolean twoSeats = true;
    static boolean threeSeats = true;
    //type
    static boolean ground = true;
    static boolean flying = true;

    //sources
    static boolean vendor = true;
    static boolean loot = true;
    static boolean quest = true;
    static boolean profession = true;
    static boolean other = true;

    public List<Mount> performOn(List<Mount> originList){
        List<Mount> result = new ArrayList<>(originList);

        result = filterName(result);
        result = filterFaction(result);
        result = filterSeats(result);
        result = filterType(result);
        result = filterDifficulty(result);
        result = filterSource(result);

        return result;
    }

    private List<Mount> filterType(List<Mount> origin){
        if(ground == flying) return origin; // no filter

        List<Mount> result = new ArrayList<>();
        for(Mount m : origin){
            if(m.isFlying() == flying){
                result.add(m);
            }
        }
        return result;
    }

    private List<Mount> filterSeats(List<Mount> origin){
        if(oneSeat == twoSeats && oneSeat == threeSeats) return origin; // no filter

        List<Mount> result = new ArrayList<>();
        for(Mount m : origin){
            if(m.getSeats() == null) continue;
            switch (m.getSeats()){
                case 1: if(oneSeat) result.add(m); break;
                case 2: if(twoSeats) result.add(m); break;
                case 3: if (threeSeats) result.add(m); break;
            }
        }
        return result;
    }

    private List<Mount> filterFaction(List<Mount> origin){
        if(alliance == horde) return origin;

        List<Mount> result = new ArrayList<>();
        for(Mount m : origin){
            if(m.getFaction() == null) continue;
            switch (m.getFaction()){
                case ALLIANCE: if(alliance) result.add(m); break;
                case HORDE: if(horde) result.add(m); break;
            }
        }
        return result;
    }

    private List<Mount> filterDifficulty(List<Mount> origin){
        if(easy == medium && easy == hard && easy == removed && easy == unavailable) return origin;

        List<Mount> result = new ArrayList<>();
        for(Mount m : origin){
            if(m.getDifficulty() == null) continue;
            switch (m.getDifficulty()){
                case -1: if(removed) result.add(m); break;
                case 1: if(easy) result.add(m); break;
                case 2: if(medium) result.add(m); break;
                case 3: if(hard) result.add(m); break;
                case 4: if(unavailable) result.add(m); break;
            }
        }
        return result;
    }

    private List<Mount> filterSource(List<Mount> origin){
        if(vendor == loot && vendor == quest && vendor == profession && vendor == other) return origin;

        List<Mount> result = new ArrayList<>();
        for(Mount m : origin){
            if(m.getSource() == null) continue;
            switch (m.getSource()){
                case VENDOR: if(vendor) result.add(m); break;
                case LOOT: if(loot) result.add(m); break;
                case QUEST: if(quest) result.add(m); break;
                case PROFESSION: if(profession) result.add(m); break;
                case OTHER: if(other) result.add(m); break;
            }
        }
        return result;
    }

    private List<Mount> filterName(List<Mount> origin){
        if(name.isEmpty()) return origin;

        List<Mount> result = new ArrayList<>();
        for(Mount m : origin){
            if(m.getName().contains(name)) result.add(m);
        }
        return result;
    }
}
