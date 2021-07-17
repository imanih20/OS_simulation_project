package com.example.OsProject.core;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import com.example.OsProject.models.User;
import com.example.OsProject.utils.FolderUtils;
import com.example.OsProject.utils.MediaUtils;
import com.example.OsProject.views.VideoPlayerDialog;
import java.io.File;
import java.util.ArrayList;

public class PlayVideoCommand extends Command{
    private final String CMD = "pvideo";
    private ArrayList<String> videoUris;
    private AppCompatActivity context;
    public PlayVideoCommand(User user, FolderUtils utils, AppCompatActivity context){
        super(user,utils);
        setContext(context);
    }
    @Override
    public boolean isMatch(String[] command) {
        if (isValid(command,2,3)) {
            this.args = command;
            return true;
        }
        return false;
    }
    public void setContext(AppCompatActivity context){
        this.context= context;
    }
    @Override
    public void execute() {
        File file;
        final String LIST_CMD = "-l";
        if (args.length==2){
            if (args[1].equals(LIST_CMD)){
                MediaUtils mediaUtils = new MediaUtils();
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    videoUris = mediaUtils.getVideoListAPI29(context);
                }else {
                    videoUris = mediaUtils.getMediaList(new MediaUtils.VideoExtensionFilter(),MediaUtils.ROOT_PATH);
                }
                StringBuilder builder = new StringBuilder();
                for (int i = 1 ;i<=videoUris.size();i++){
                    String s = videoUris.get(i-1);
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
                String videoUri = "";
                for (String uri: videoUris){
                    if (uri.endsWith(args[2])){
                        videoUri = uri;
                        flag = true;
                        break;
                    }
                }
                if (!flag){
                    setCommandMsg("video not found.");
                    return;
                }else {
                    file = new File(videoUri);
                }
            }else {
                setCommandMsg("wrong command format");
                return;
            }
        }
        boolean isVideo = false;
        String type = file.getName().substring(file.getName().lastIndexOf('.')+1);
        switch (type) {
            case "mkv":
            case "mp4":
                isVideo = true;
                break;
            default:
        }
        if (file.exists()&&file.isFile()) {
            if (isVideo) {
                VideoPlayerDialog dialog = VideoPlayerDialog.newInstance(file);
                dialog.show(context.getSupportFragmentManager(), "dialog");
                setCommandMsg("");
                AddToLogDB();
                return;
            }
            setCommandMsg("this isn't a video file.");
            return;
        }
        setCommandMsg("file not found.");
    }

    @Override
    public String getHelp() {
        return "plays video file" +
                "\n\tformat:" +
                "\n\t\t1."+CMD+" <filePath>" +
                "\n\t\t2."+CMD+" -l -> list all video files." +
                "\n\t\t3."+CMD+" -l <video name from list> -> play video from the video list";
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
