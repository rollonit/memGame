package com.rollonit.edugame;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

/**
 * Application class for the Android app.
 */
public class MemGame extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Apply android material colours to the app based on the user's phone
        DynamicColors.applyToActivitiesIfAvailable(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
