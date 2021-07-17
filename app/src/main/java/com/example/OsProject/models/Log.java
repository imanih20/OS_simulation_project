package com.example.OsProject.models;

import java.util.Date;

public class Log {
    private String command;
    private String date;
    public Log( String command){
        setDate();
        setCommand(command);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDate() {
        return date;
    }

    public void setDate() {
        this.date = new Date().toString();
    }

    public String[] getLogData(){
        String[] data = new String[2];
        data[1] = getDate();
        data[0] = getCommand();
        return data;
    }
}
