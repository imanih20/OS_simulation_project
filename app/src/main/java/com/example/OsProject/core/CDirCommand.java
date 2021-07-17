package com.example.OsProject.core;

import com.example.OsProject.activities.App;
import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;


public class CDirCommand extends Command{
    public static final String CMD = "cd";
    public CDirCommand(User user, FolderUtils utils) {
        super(user,utils);
    }

    @Override
    public boolean isMatch(String[] command) {
        if (isValid(command,2)) {
            this.args = command;
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        if (args.length==1){
            setCommandMsg("wrong command format");
            return;
        }
        if (args[1].equals(".")){
            setCommandMsg("");
            return;
        }
        if (args[1].equals("..")){
            if (folderUtils.getCurrentDir().equals(App.HOME_DIR.getPath())&&!user.isManager()){
                setCommandMsg("");
                return;
            }
            if (folderUtils.getCurrentDir().equals(App.ROOT_DIR.getPath())){
                setCommandMsg("");
                return;
            }
            folderUtils.changeToParent();
            setCommandMsg("");
            return;
        }
        if (args[1].equals("/")&&user.isManager()){
            folderUtils.changeDirTo("/");
            setCommandMsg("");
            return;
        }
        if (args[1].equals("~")){
            folderUtils.changeDirTo(App.HOME_DIR.getName()+"/"+user.getUsrName());
            setCommandMsg("");
            return;
        }
        if (!folderUtils.isOwner(args[1], user.getUsrName())){
            setCommandMsg("you are not allowed to change directory to this folder.");
            return;
        }
        if (folderUtils.changeDirTo(args[1])){
            setCommandMsg("");
            return;
        }
        setCommandMsg("folder not found.");
    }

    @Override
    public String getHelp() {
        return "changes the current directory." +
                "\n\tspecial Params:" +
                "\n\t\t1.<.> shows the current directory." +
                "\n\t\t2.<..> specifies that you want to change to parent directory." +
                "\n\t\t3.</> goes to the root directory (only for admin user)." +
                "\n\t\t3.<~> goes to the user home directory." +
                "\n\tformat:" +
                "\n\t\t"+CMD+" <path>";
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
