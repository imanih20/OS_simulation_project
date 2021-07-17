package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;

public class MoreCommand extends Command{
    private final String CMD = "more";
    public MoreCommand(User user, FolderUtils utils) {
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
    public void execute(){
        try {
            if (!folderUtils.isOwner(args[1], user.getUsrName())){
                setCommandMsg("you are not allowed to see content of this folder.");
                return;
            }
            String content = folderUtils.showFileContent(args[1]);
            if (content==null){
                setCommandMsg("The contents of this file could not be displayed.");
                return;
            }
            setCommandMsg(content);
        } catch (Exception e){
            setCommandMsg("some error happened.");
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "displays content of a file" +
                "\n\tformat:" +
                "\n\t\t"+CMD+" <filePath>";
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
