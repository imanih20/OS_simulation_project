package com.example.OsProject.core;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;

import java.util.ArrayList;

public class CommandInitializer {
    private final User user;
    private final FolderUtils utils;
    private AppCompatActivity context;
    private static ArrayList<Command> commands;
    public CommandInitializer(User user){
        this.user = user;
        this.utils = new FolderUtils(user.getUsrName());
    }
    public void init(AppCompatActivity context){
        this.context = context;
        initCommands();
    }
    private void initCommands() {
        commands = new ArrayList<>();
        commands.add(new HelpCommand(user,utils,commands));
        commands.add(new CDirCommand(user,utils));
        commands.add(new ListCommand(user,utils));
        commands.add(new MKDirCommand(user,utils));
        commands.add(new MKFileCommand(user,utils));
        commands.add(new DeleteCommand(user,utils));
        commands.add(new CopyCommand(user,utils));
        commands.add(new MoveCommand(user,utils));
        commands.add(new RenameCommand(user,utils));
        commands.add(new MoreCommand(user,utils));
        commands.add(new EditCommand(user,utils,context));
        commands.add(new SLogCommand(user,utils));
        commands.add(new AddUserCommand(user,utils));
        commands.add(new DLUserCommand(user,utils));
        commands.add(new ChangePassCommand(user,utils));
        commands.add(new ChangeQuesCommand(user,utils));
        commands.add(new InfoCommand(user,utils));
        commands.add(new ResetPasCommand(user,utils));
        ActivityResultLauncher<String> rpLauncher = context.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        commands.add(new PlayAudioCommand(user, utils, context));
                        commands.add(new PlayVideoCommand(user, utils, context));
                    }else {
                        new AlertDialog.Builder(context)
                                .setMessage("you can't access to audio player or video player feature.")
                                .setPositiveButton("continue", (dialog, which) -> dialog.dismiss())
                                .create()
                                .show();
                    }
                });
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            rpLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }else {
            commands.add(new PlayAudioCommand(user, utils, context));
            commands.add(new PlayVideoCommand(user, utils, context));
        }
    }
    public ArrayList<Command> getCommandList(){
        return commands;
    }
    public FolderUtils getUtils(){
        return utils;
    }
}
