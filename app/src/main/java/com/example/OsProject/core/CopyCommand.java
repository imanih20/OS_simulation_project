package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;

public class CopyCommand extends Command{
    private final String CMD = "copy";
    public CopyCommand(User user, FolderUtils utils) {
        super(user, utils);
    }

    @Override
    public boolean isMatch(String[] command) {
        if (isValid(command,3)) {
            this.args = command;
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        try {
            if (!folderUtils.isOwner(args[1], user.getUsrName())){
                setCommandMsg("you are not allowed to copy this file or folder.");
                return;
            }
            if (folderUtils.isValidTarget(args[2], user.getUsrName())){
                setCommandMsg("you are not allowed to copy your file or folder to this folder.");
                return;
            }
            if (folderUtils.copy(args[1],args[2])){
                setCommandMsg("");
                return;
            }
            setCommandMsg("failed to copy.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "copy source location to target location." +
                "\n\tformat:" +
                "\n\t\t"+CMD+" <source> <target>";
    }

    @Override
    public String getCommand() {
        return CMD;
    }

    @Override
    public boolean isAdminCommand() {
        return false;
    }
}
