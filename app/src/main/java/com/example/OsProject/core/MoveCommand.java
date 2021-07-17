package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;


public class MoveCommand extends Command{
    private final String CMD = "move";
    public MoveCommand(User user, FolderUtils utils) {
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
                setCommandMsg("you are not allowed to move this file or folder.");
                return;
            }
            if (folderUtils.isValidTarget(args[2], user.getUsrName())){
                setCommandMsg("you are not allowed to move your file or folder to this folder.");
                return;
            }
            if (folderUtils.move(args[1],args[2])){
                setCommandMsg("");
                return;
            }
            setCommandMsg("failed to move.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "moves a file or folder from source location to target location." +
                "\n\tformat:" +
                "\n\t\t"+CMD+" <source> <location>";
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
