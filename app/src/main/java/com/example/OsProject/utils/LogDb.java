package com.example.OsProject.utils;

import com.example.OsProject.activities.App;
import com.example.OsProject.models.Log;

import java.io.*;

public class LogDb {
    private final String userName;
    private File logDbFile;
    public LogDb(String userName) {
        this.userName = userName;
        setLogDbFile();
    }
    private void setLogDbFile(){
        File file = new File(App.LOG_DIR,userName);
        try {
            if (file.exists()) {
                if (file.isFile()) {
                    logDbFile = file;
                    return;
                }
                if (file.delete()) {
                    if (file.createNewFile()) {
                        logDbFile = file;
                        return;
                    }
                    android.util.Log.e("logs", "file not created");
                }
            }
            if (file.createNewFile()) {
                logDbFile = file;
                return;
            }
            android.util.Log.e("logs", "file not created");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addLog(String info){
        Log log = new Log(info);
        try {
            FileWriter writer = new FileWriter(logDbFile,true);
            StringBuilder line = new StringBuilder();
            line.append("\n");
            for (String s:log.getLogData()){
                line.append(s).append(",");
            }
            line.deleteCharAt(line.length()-1);
            writer.append(line.toString());
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String showLog() throws IOException{
        if (!logDbFile.exists()) return "user not found.";
        BufferedReader reader = new BufferedReader(new FileReader(logDbFile));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine())!=null){
            String[] ar = line.split(",");
            for (String s : ar) {
                builder.append(s).append(":\t");
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append("\n");
        }
        return builder.toString();
    }

}
