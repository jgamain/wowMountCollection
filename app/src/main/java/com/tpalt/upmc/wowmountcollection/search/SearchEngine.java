package com.tpalt.upmc.wowmountcollection.search;

import android.util.Log;

import com.tpalt.upmc.wowmountcollection.Mount;
import com.tpalt.upmc.wowmountcollection.WMCApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeanne on 17/03/2018.
 */

public class SearchEngine {

    //search criteria with default values (package private)
    static String name = "";
    //faction
    static boolean alliance = false;
    static boolean horde = false;
    //difficulty
    static boolean easy = false;
    static boolean medium = false;
    static boolean hard = false;
    static boolean removed = false;
    static boolean unavailable = false; // not yet in the game
    //seats
    static boolean oneSeat = false;
    static boolean twoSeats = false;
    static boolean threeSeats = false;
    //type
    static boolean ground = false;
    static boolean flying = false;
    //sources
    static boolean vendor = false;
    static boolean loot = false;
    static boolean quest = false;
    static boolean profession = false;
    static boolean other = false;

    public enum OriginListChoice {
        ALL_MOUNTS, MY_MOUNTS, MISSING_MOUNTS
    }
    static OriginListChoice originList = OriginListChoice.ALL_MOUNTS;

    private static List<Mount> suggestedMounts = new ArrayList<>();

    private SearchEngine(){}

    public static List<Mount> perform(){
        List<Mount> result = getOriginList();

        result = filterName(result);
        result = filterFaction(result);
        result = filterSeats(result);
        result = filterType(result);
        result = filterDifficulty(result);
        result = filterSource(result);

        return result;
    }

    private static List<Mount> getOriginList(){
        List<Mount> result = new ArrayList<>();
        switch (originList){
            case ALL_MOUNTS:
                result.addAll(WMCApplication.getALLMountList());
                break;
            case MY_MOUNTS:
                for(Mount m : WMCApplication.getALLMountList()){
                    if(WMCApplication.getUserMountList().contains(m.getCreatureId())){
                        result.add(m);
                    }
                }
                break;
            case MISSING_MOUNTS:
                for (Mount m : WMCApplication.getALLMountList()){
                    if(!WMCApplication.getUserMountList().contains(m.getCreatureId())){
                        result.add(m);
                    }
                }
                break;
        }
        return result;
    }

    private static List<Mount> filterType(List<Mount> origin){
        if(ground == flying) return origin; // no filter

        List<Mount> result = new ArrayList<>();
        for(Mount m : origin){
            if(m.isFlying() == flying){
                result.add(m);
            }
        }
        return result;
    }

    private static List<Mount> filterSeats(List<Mount> origin){
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

    private static List<Mount> filterFaction(List<Mount> origin){
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

    private static List<Mount> filterDifficulty(List<Mount> origin){
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

    private static List<Mount> filterSource(List<Mount> origin){
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

    private static List<Mount> filterName(List<Mount> origin){
        if(name.isEmpty()) return origin;

        List<Mount> result = new ArrayList<>();
        for(Mount m : origin){
            if(m.getName().toUpperCase().contains(name.toUpperCase())) result.add(m);
        }
        return result;
    }

    /**
     * Sets all the filters to their default value (except the name)
     */
    public static void resetFilters(){
        originList = OriginListChoice.ALL_MOUNTS;
        //faction
        alliance = false;
        horde = false;
        //difficulty
        easy = false;
        medium = false;
        hard = false;
        removed = false;
        unavailable = false;
        //seats
        oneSeat = false;
        twoSeats = false;
        threeSeats = false;
        //type
        ground = false;
        flying = false;
        //sources
        vendor = false;
        loot = false;
        quest = false;
        profession = false;
        other = false;
    }

    public static List<String> getSuggestions(String query){
        String constraint = query.toUpperCase();
        List<String> suggestions = new ArrayList<>();
        suggestedMounts.clear();
        for(Mount m : getOriginList()){
            if(m.getName().toUpperCase().startsWith(constraint)){
                suggestions.add(m.getName());
                suggestedMounts.add(m);
            }
        }
        return suggestions;
    }

    public static String getSuggestedMount(int i){
        if(i >= 0 && i < suggestedMounts.size()){
            return suggestedMounts.get(i).getName();
        }
        return null;
    }
}
