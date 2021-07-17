package com.example.OsProject.core;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;

import java.util.List;

public class HelpCommand extends Command{
    public static final String CMD = "help";
    private List<Command> commandList;
    public HelpCommand(User user, FolderUtils utils,List<Command> commandList) {
        super(user, utils);
        setCommandList(commandList);
    }

    @Override
    public boolean isMatch(String[] command) {
        if (isValid(command,1,2)) {
            this.args = command;
            return true;
        }
        return false;
    }
    public void setCommandList(List<Command> list) {
        this.commandList = list;
    }

    @Override
    public void execute(){
        if (args.length==1){
            StringBuilder builder = new StringBuilder();
            int i = 1;
            for (Command cmd : commandList){
                if(cmd.isAdminCommand()&&!user.isManager()) continue;
                builder.append("\n").append(i++).append(". ").append(cmd.getCommand()).append(":\n");
                builder.append("\t").append(cmd.getHelp()).append("\n\n");
            }
            setCommandMsg(builder.toString());
            return;
        }
        for (Command cmd : commandList){
            if (args[1].equals(cmd.getCommand())){
                if (cmd.isAdminCommand()&&!user.isManager()) break;
                setCommandMsg(cmd.getHelp());
                return;
            }
        }
        setCommandMsg("help information not found for this command.");
    }

    @Override
    public String getHelp() {
        return "provides help information for commands." +
                "\n\tformat:" +
                "\n\t\t"+CMD+" <commandName>";
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
