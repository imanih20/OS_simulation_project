package com.example.OsProject.core;

import com.example.OsProject.utils.TextUtils;
import java.util.ArrayList;

public class CommandExecutor {
    private final ArrayList<Command> commands;
    private String commandResult;

    public CommandExecutor(ArrayList<Command> commandList){
        this.commands = commandList;
    }

    public void execute(String userCommand){
        setCommandResult("");
        if (userCommand.equals("")){
            return;
        }
        String[] args = TextUtils.getArray(userCommand," ");
        for (Command cmd:commands){
            cmd.setCommandMsg("");
            if (cmd.isMatch(args)){
                cmd.execute();
                setCommandResult(cmd.getCommandMsg());
                return;
            }
            if (cmd.getCommandMsg()!=null && !cmd.getCommandMsg().equals("")) {
                setCommandResult(cmd.getCommandMsg());
                return;
            }
        }
        if (getCommandResult().equals(""))
            setCommandResult(args[0]+": command not found.");
    }
    public String getCommandResult() {
        return commandResult;
    }

    public void setCommandResult(String commandResult) {
        this.commandResult = commandResult;
    }
}
