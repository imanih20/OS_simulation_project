package com.example.OsProject.core;

import com.example.OsProject.activities.App;
import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;
import com.example.OsProject.utils.UserDb;

public class DLUserCommand extends Command{
    private final String CMD = "dluser";

    public DLUserCommand(User user, FolderUtils utils) {
        super(user, utils);
    }

    @Override
    public void execute() {
        if (user.getUsrName().equals(args[1])){
            setCommandMsg("you can't delete your account.");
            return;
        }
        UserDb db = new UserDb();
        User user = new User();
        user.setUsrName(args[1]);
        db.changeUserCredential(user,true);
        folderUtils.delete(App.HOME_DIR.getPath()+"/"+user.getUsrName());
        AddToLogDB();
        setCommandMsg("");
    }

    @Override
    public boolean isMatch(String[] command) {
        if (!user.isManager()){
            return false;
        }
        if (isValid(command,2)){
            args = command;
            return true;
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "deletes user account." +
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
