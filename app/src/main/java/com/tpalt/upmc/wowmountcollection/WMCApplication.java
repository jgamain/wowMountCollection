package com.tpalt.upmc.wowmountcollection;

import android.app.Application;
import android.util.Log;

/**
 * Created by Jeanne on 14/03/2018.
 */

public class WMCApplication extends Application {

    public static final String WMC_URL = "https://www.wowmountcollection.ovh";
    public static String region = "eu"; //default value

    public static void setRegion(String r){
        Log.i("OAUTH", "set region to: "+r);
        region = r;
    }

}
