package com.example.OsProject.core;

import androidx.appcompat.app.AppCompatActivity;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;
import com.example.OsProject.views.EditDialog;

import java.io.File;

public class EditCommand extends Command{
    public static final String CMD = "edit";
    private AppCompatActivity context;
    public EditCommand(User user, FolderUtils utils, AppCompatActivity context){
        super(user,utils);
        setContext(context);
    }

    @Override
    public boolean isMatch(String[] command) {
        if (isValid(command,2)) {
            this.args = command;
            return true;
        }
        return false;
    }

    public void setContext(AppCompatActivity context){
        this.context = context;
    }
    @Override
    public void execute() {
        if (!folderUtils.isOwner(args[1], user.getUsrName())){
            setCommandMsg("you are not allowed to edit this file.");
            return;
        }
        File file = folderUtils.getValidFile(args[1]);
        if (file==null){
            setCommandMsg("file not found");
            return;
        }
        if (!file.exists()){
            setCommandMsg("file not found");
            return;
        }
        if (file.isDirectory()){
            setCommandMsg("please choose a file.");
            return;
        }
        EditDialog dialog = EditDialog.newInstance(args[1],folderUtils);
        dialog.show(context.getSupportFragmentManager(),"dialog");
        dialog.setCancelable(false);
        setCommandMsg("");
    }

    @Override
    public String getHelp() {
        return "edits a file." +
                "\n\tformat:" +
                "\n\t\t"+CMD+" <filePath>";
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
