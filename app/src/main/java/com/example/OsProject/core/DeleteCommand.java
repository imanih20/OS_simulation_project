package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;

public class DeleteCommand extends Command{
    private final String CMD = "dl";
    public DeleteCommand(User user, FolderUtils utils) {
        super(user, utils);
    }

    @Override
    public boolean isMatch(String[] command) {
        if (isValid(command,2)) {
            this.args = command;
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        if (!folderUtils.isOwner(args[1], user.getUsrName())){
            setCommandMsg("you are not allowed to delete this file or folder.");
            return;
        }
        if (folderUtils.isForbidFile(args[1])){
            setCommandMsg("you don't have permission to delete this file or folder.");
            return;
        }
        if (folderUtils.delete(args[1])){
            AddToLogDB();
            setCommandMsg("");
            return;
        }
        setCommandMsg("file not found.");
    }


    @Override
    public String getHelp() {
        return "deletes a file or folder." +
                "\n\tformat:" +
                "\n\t\t"+CMD+" <path>";
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
