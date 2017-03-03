package com.hkm.dis;

import android.app.Application;

import me.drakeet.library.CrashWoodpecker;
import me.drakeet.library.PatchMode;

/**
 * Created by zJJ on 11/7/2015.
 *
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashWoodpecker.instance()
                .setPatchMode(PatchMode.SHOW_LOG_PAGE)
                .flyTo(this);
    }
}
