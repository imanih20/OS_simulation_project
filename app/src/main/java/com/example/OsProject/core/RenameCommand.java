package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;

public class RenameCommand extends Command{

    private final String CMD = "rename";
    public RenameCommand(User user, FolderUtils utils) {
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
        if (!folderUtils.isOwner(args[1], user.getUsrName())){
            setCommandMsg("you are not allowed to rename this file or folder.");
            return;
        }
        if (folderUtils.isForbidFile(args[1])){
            setCommandMsg("you don't have permission to rename this.");
            return;
        }
        if (folderUtils.rename(args[1],args[2])){
            setCommandMsg("");
            return;
        }
        setCommandMsg("file not found.");
    }

    @Override
    public String getHelp() {
        return "renames file or folder." +
                "\n\tformat:" +
                "\n\t\t"+CMD+" <filePath> <newName>";
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
