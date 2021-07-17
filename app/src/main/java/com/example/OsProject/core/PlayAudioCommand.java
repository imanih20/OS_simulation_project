package com.example.OsProject.core;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;
import com.example.OsProject.utils.MediaUtils;
import com.example.OsProject.views.AudioPlayerDialog;
import java.io.File;
import java.util.ArrayList;

public class PlayAudioCommand extends Command{
    public static final String CMD = "paudio";
    private  ArrayList<String> audioUris;
    private final AppCompatActivity context;
    public PlayAudioCommand(User user, FolderUtils utils, AppCompatActivity context){
        super(user,utils);
        this.context = context;
    }
    @Override
    public boolean isMatch(String[] command) {
        if (isValid(command,2,3)) {
            this.args = command;
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        File file;
        final String LIST_CMD = "-l";
        if (args.length==2){
            if (args[1].equals(LIST_CMD)){
                MediaUtils mediaUtils = new MediaUtils();
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    audioUris = mediaUtils.getAudioListAPI29(context);
                }else {
                    audioUris = mediaUtils.getMediaList(new MediaUtils.AudioExtensionFilter(),MediaUtils.ROOT_PATH);
                }
                StringBuilder builder = new StringBuilder();
                for (int i = 1 ;i<=audioUris.size();i++){
                    String s = audioUris.get(i-1);
                    builder.append(i).append(". ").append(s.substring(s.lastIndexOf('/')+1)).append("\n");
                }
                setCommandMsg(builder.toString());
                return;
            }else{
                file = folderUtils.getValidFile(args[1]);
            }
        }else {
            if (args[1].equals(LIST_CMD)){
                boolean flag = false;
                String audioUri = "";
                for (String uri: audioUris){
                    if (uri.endsWith(args[2])){
                        audioUri = uri;
                        flag = true;
                        break;
                    }
                }
                if (!flag){
                    setCommandMsg("audio not found.");
                    return;
                }else {
                    file = new File(audioUri);
                }
            }else {
                setCommandMsg("wrong command format");
                return;
            }
        }
        boolean isAudio = false;
        String type = file.getName().substring(file.getName().lastIndexOf('.')+1);
        switch (type){
            case "mp3":
            case "3gp":
                isAudio = true;
                break;
            default:
        }
        if (file.exists()&&file.isFile()) {
            if (isAudio) {
                AudioPlayerDialog dialog = AudioPlayerDialog.newInstance(file);
                dialog.show(context.getSupportFragmentManager(), "dialog");
                setCommandMsg("");
                AddToLogDB();
                return;
            }
            setCommandMsg("this isn't an audio file.");
            return;
        }
        setCommandMsg("file not found.");
    }

    @Override
    public String getHelp() {
        return "plays audio files." +
                "\n\tformat:" +
                "\n\t\t1."+CMD+" <filePath>."+
                "\n\t\t2."+CMD+" -l -> list all audio files." +
                "\n\t\t3."+CMD+" -l <video name from list> -> play audio from the audio list";
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
