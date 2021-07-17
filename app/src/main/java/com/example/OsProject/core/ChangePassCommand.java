package com.example.OsProject.core;


import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;
import com.example.OsProject.utils.TextUtils;
import com.example.OsProject.utils.UserDb;

public class ChangePassCommand extends Command {
    private final String CMD = "cpass";

    public ChangePassCommand(User user, FolderUtils utils) {
        super(user, utils);

    }

    @Override
    public void execute() {
        if (!user.getPassword().equals(args[1])){
            setCommandMsg("password is not correct.");
            return;
        }
        if (!TextUtils.isValidPass(args[2])){
            setCommandMsg("new password is not valid.");
            return;
        }
        UserDb db = new UserDb();
        user.setPassword(args[2]);
        db.changeUserCredential(user,false);
        AddToLogDB();
        setCommandMsg("");
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
        return "changes user password." +
                "\n\tformat:" +
                "\n\t\t"+CMD+" <UserPassword> <newPassword>.";
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
