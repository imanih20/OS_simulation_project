package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;
import com.example.OsProject.utils.UserDb;

public class ResetPasCommand extends Command{
    private final String CMD = "reset";


    public ResetPasCommand(User user, FolderUtils utils) {
        super(user, utils);
    }

    @Override
    public void execute() {
        UserDb db = new UserDb();
        User user = db.findUser(args[1]);
        if (user == null){
            setCommandMsg("user not found.");
            return;
        }
        user.setPassword("f");
        db.changeUserCredential(user,false);
        AddToLogDB();
        setCommandMsg("");
    }

    @Override
    public boolean isMatch(String[] command) {
        if (isValid(command,2)){
            args = command;
            return true;
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "resets password of user to initial password(id)." +
                "\n\tformat:" +
                "\n\t\t"+CMD+" <userName>";
    }

    @Override
    public String getCommand() {
        return CMD;
    }

    @Override
    public boolean isAdminCommand() {
        return true;
    }
}
