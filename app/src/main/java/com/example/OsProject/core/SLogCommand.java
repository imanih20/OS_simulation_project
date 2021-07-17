package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;
import com.example.OsProject.utils.LogDb;

public class SLogCommand extends Command{
    private final String CMD = "slog";
    public SLogCommand(User user, FolderUtils utils) {
        super(user, utils);
    }

    @Override
    public void execute() {
        LogDb db = new LogDb(args[1]);
        try {
            setCommandMsg(db.showLog());
            AddToLogDB();
        }catch (Exception e){
            setCommandMsg("an error happened.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean isMatch(String[] command) {
        if (!user.isManager()) return false;
        if (isValid(command,2)){
            args = command;
            return true;
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "shows logs from users." +
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
