package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;

import java.io.IOException;

public class MKFileCommand extends Command{
    private final String CMD = "mkfile";
    public MKFileCommand(User user, FolderUtils utils) {
        super(user, utils);
    }

    @Override
    public boolean isMatch(String[] command) {
        if (isValid(command,2,3)) {
            args = command;
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        String path;
        if (args.length == 3){
            if (folderUtils.isValidDirectory(args[2])){
                path = args[2]+"/"+args[1];
            }else {
                setCommandMsg("target folder not found.");
                return;
            }
        }else {
            path = args[1];
        }
        if (!folderUtils.isOwner(path, user.getUsrName())){
            setCommandMsg("you are not allowed to make file in this folder.");
            return;
        }
        if (folderUtils.isForbidFile(path)){
            setCommandMsg("you don't have permission to make file in this folder.");
            return;
        }
        try {
            if (folderUtils.makeFile(path)){
                setCommandMsg("");
                AddToLogDB();
                return;
            }
            setCommandMsg("file not created.");
        }catch (IOException e){
            e.printStackTrace();
            setCommandMsg("some error happened.");
        }
    }

    @Override
    public String getHelp() {
        return "creates a file." +
                "\n\tformat:" +
                "\n\t\t1."+CMD+" <fileName>" +
                "\n\t\t2."+CMD+" <filePath>" +
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
