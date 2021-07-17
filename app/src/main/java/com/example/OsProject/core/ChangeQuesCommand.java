package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;
import com.example.OsProject.utils.UserDb;

public class ChangeQuesCommand extends Command {
    private final String CMD = "cquest";
    public ChangeQuesCommand(User user, FolderUtils utils) {
        super(user, utils);
    }

    @Override
    public void execute() {
        if (args[1].isEmpty() || args[2].isEmpty()){
            setCommandMsg("wrong question format");
            return;
        }
        user.setBackupQuest(args[1]);
        user.setBackupAnswer(args[2]);
        UserDb db = new UserDb();
        db.changeUserCredential(user,false);
        AddToLogDB();
    }

    @Override
    public boolean isMatch(String[] command) {
        if (isValid(command,3)){
            args = command;
            return true;
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "changes backup question." +
                "\n\tformat:" +
                "\n\t\t"+CMD+" <question> <answer>";
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
