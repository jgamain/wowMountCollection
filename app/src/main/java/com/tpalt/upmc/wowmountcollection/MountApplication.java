package com.tpalt.upmc.wowmountcollection;

import android.app.Application;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaniisha on 20-Mar-18.
 */

public class MountApplication extends Application {
    List<Mount> mounts;

    public void onCreate(){
        super.onCreate();
        this.mounts=new ArrayList<Mount>();
    }
    public  List<Mount> getMounts(){
         this.mounts = WMCApplication.getALLMountList();
         return this.mounts;

    }
}
