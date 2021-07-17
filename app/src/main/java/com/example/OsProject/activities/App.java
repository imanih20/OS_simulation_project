package com.example.OsProject.activities;

import android.app.Application;
import android.util.Log;
import java.io.*;

public class App extends Application {
    public static File HOME_DIR;
    public static File ROOT_DIR;
    public static File PUBLIC_DIR;
    public static File LOG_DIR;
    @Override
    public void onCreate() {
        super.onCreate();
        ROOT_DIR = createDir(getBaseContext().getExternalCacheDir(),"root");
        LOG_DIR = createDir(ROOT_DIR,"logs");
        HOME_DIR = createDir(ROOT_DIR,"home");
        PUBLIC_DIR = createDir(HOME_DIR,"public");
    }


    private File createDir(File parent,String name){
        File file = new File(parent,
                name);
        if (!file.exists()){
            if (!file.mkdirs())
                Log.e("dir","making failed");
        }
        return file;
    }
}
