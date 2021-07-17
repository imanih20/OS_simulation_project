package com.example.OsProject.core;
import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;

import java.io.File;

public class ListCommand extends Command {
    public static final String CMD = "ls";
    public ListCommand(User user, FolderUtils utils){
        super(user,utils);
    }
    @Override
    public boolean isMatch(String[] command) {
        if (isValid(command,1,2)) {
            this.args = command;
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        File[] list;
        if (args.length==1){
            list = folderUtils.getCurFolderContents();
        }else {
            if (!folderUtils.isOwner(args[1], user.getUsrName())){
                setCommandMsg("you are not allowed to see content of this folder.");
                return;
            }
            list = folderUtils.getFolderContents(args[1]);
        }
        if (args.length>1 && list==null){
            setCommandMsg("folder not found");
            return;
        }
        StringBuilder msg = new StringBuilder();
        String DIR_TAG = "<DIR>    ";
        msg.append(DIR_TAG).append(".").append("\n");
        msg.append(DIR_TAG).append("..").append("\n");
        if (list!=null && list.length>0) {
            for (File file : list) {
                String FILE_TAG = "<FILE>   ";
                msg.append(file.isDirectory() ? DIR_TAG : FILE_TAG).append(file.getName()).append("\n");
            }
        }
        setCommandMsg(msg.toString());
    }

    @Override
    public String getHelp() {
        return "shows content of directory." +
                "\n\tformat:" +
                "\n\t\t1."+CMD+" -> without any params for showing content of current directory." +
                "\n\t\t2."+CMD+" <path> -> for showing content of defined directory.";
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