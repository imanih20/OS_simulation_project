package com.example.OsProject.core;

import android.content.Context;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;
import com.example.OsProject.utils.ThemeUtils;

public class NightModeCommand extends Command{
    private final Context context ;
    private final String CMD = "swimode";
    public NightModeCommand(User user, FolderUtils utils, Context context) {
        super(user, utils);
        this.context = context;
    }

    @Override
    public void execute() {
        ThemeUtils themeUtils = new ThemeUtils(context);
        themeUtils.switchMode();
        setCommandMsg("");
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
        return "turns night mode on or off" +
                "\n\tformat:" +
                "\n\t\t1."+CMD;
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
