package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;

public class MKDirCommand extends Command{
    private final String CMD = "mkdir";
    public MKDirCommand(User user, FolderUtils utils) {
        super(user, utils);
    }

    @Override
    public boolean isMatch(String[] command) {
        if (isValid(command,2,3)) {
            this.args = command;
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        String path;
        if (args.length == 3){
            if (folderUtils.isValidDirectory(args[2])){
                path = args[2];
            }else {
                setCommandMsg("target folder not found.");
                return;
            }
        }else {
            path = args[1];
        }
        if (!folderUtils.isOwner(path, user.getUsrName())){
            setCommandMsg("you are not allowed to make directory in this folder.");
            return;
        }
        if (folderUtils.isForbidFile(path)){
            setCommandMsg("you don't have permission to make folder in this folder.");
            return;
        }
        if (folderUtils.makeDir(path)){
            setCommandMsg("");
            AddToLogDB();
            return;
        }
        setCommandMsg("some error happened.");
    }

    @Override
    public String getHelp() {
        return "creates a directory." +
                "\n\tformat:" +
                "\n\t\t1."+CMD+" <dirname>" +
                "\n\t\t2."+CMD+" <dirPath>" +
                "\n\t\t3."+CMD+" <fileName> <targetPath>";
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
