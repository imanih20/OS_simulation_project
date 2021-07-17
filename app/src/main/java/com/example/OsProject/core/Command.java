package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;
import com.example.OsProject.utils.LogDb;

public abstract class Command {
    private String commandMsg = "";
    protected User user;
    protected FolderUtils folderUtils;
    protected String[] args;

    public Command(User user, FolderUtils utils) {
        this.user = user;
        this.folderUtils = utils;
    }

    protected boolean isValid(String[] command, int...expectLength){
        if (!command[0].equals(getCommand())){
            return false;
        }
        for (int l : expectLength){
            if (command.length == l){
                return true;
            }
        }
        setCommandMsg("wrong command format");
        return false;
    }

    public void setCommandMsg(String msg) {
        this.commandMsg = msg;
    }

    public String getCommandMsg() {
        return commandMsg;
    }
    protected void AddToLogDB(){
        new LogDb(user.getUsrName()).addLog(getCommand());
    }
    public abstract void execute();
    public abstract boolean isMatch(String[] command);
    public abstract String getHelp();
    public abstract String getCommand();
    public abstract boolean isAdminCommand();
}
