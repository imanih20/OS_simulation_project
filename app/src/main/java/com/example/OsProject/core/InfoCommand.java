package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;

public class InfoCommand extends Command {
    private final String CMD = "info";
    public InfoCommand(User user, FolderUtils utils) {
        super(user, utils);
    }

    @Override
    public void execute() {
        setCommandMsg(user.toString());
    }

    @Override
    public boolean isMatch(String[] command) {
        if (isValid(command,1)){
            args = command;
            return true;
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "shows user information in program." +
                "\n\tformat:" +
                "\n\t\t"+CMD;
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
