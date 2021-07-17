package com.example.OsProject.utils;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtils {
    private final UiModeManager uiModeManager;

    public ThemeUtils(Context context){
        uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
    }
    public void switchMode(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }else {
            if (uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
                uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
            } else {
                uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
            }
        }
    }
}
