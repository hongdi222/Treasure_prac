package com.feicuiedu.hunttreasure;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.feicuiedu.hunttreasure.user.UserPrefs;

/**
 * Created by gqq on 17/1/2.
 */

public class TreasureApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());

        UserPrefs.init(getApplicationContext());
    }
}
