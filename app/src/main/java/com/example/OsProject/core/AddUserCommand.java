package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;
import com.example.OsProject.utils.UserDb;

public class AddUserCommand extends Command{
    private final String CMD = "adduser";
    public AddUserCommand(User user, FolderUtils utils) {
        super(user, utils);
    }

    @Override
    public void execute() {
        try {
            UserDb db = new UserDb();
            User user = db.findUser(args[1]);
            if (user!=null){
                setCommandMsg("A user with this user name already exists. choose a different user name.");
                return;
            }
            db.addUser(args[1],args[2]);
            AddToLogDB();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isMatch(String[] command) {
        if (!user.isManager()){
            return false;
        }
        if (isValid(command, 3)){
            args = command;
            return true;
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "adds new user to program" +
                "\n\tformat:" +
                "\n\t\t"+CMD+" <userName> <password>";
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
