package com.example.OsProject.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.core.os.EnvironmentCompat;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class MediaUtils {
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
    private final ArrayList<String> mediaUris;
    public MediaUtils(){
        mediaUris = new ArrayList<>();
    }
    public ArrayList<String> getMediaList(FileNameFilter filter,String homePath){
        File homeFile = new File(homePath);
        if (homeFile.listFiles() == null) return mediaUris;
        for (File file : Objects.requireNonNull(homeFile.listFiles())){
            if (file.isDirectory()){
                getMediaList(filter,file.getPath());
            }else {
                if (filter.accept(file.getName())){
                    mediaUris.add(file.getPath());
                    return mediaUris;
                }
            }
        }
        return mediaUris;
    }
    @RequiresApi(29)
    public ArrayList<String> getAudioListAPI29(Context context){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns._ID,MediaStore.Audio.AudioColumns.DATA};
        Cursor c = context.getContentResolver().query(uri, projection,null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndex(MediaStore.Audio.AudioColumns._ID));
                Uri u = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,id);
                String path  = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
                mediaUris.add(path);
            }
            c.close();
        }
        return mediaUris;
    }
    @RequiresApi(29)
    public ArrayList<String> getVideoListAPI29(Context context){
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns._ID,MediaStore.Video.VideoColumns.DATA};
        Cursor c = context.getContentResolver().query(uri, projection,null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                Uri u = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,id);
                String path  = c.getString(c.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                mediaUris.add(path);
            }
            c.close();
        }
        return mediaUris;
    }
    private interface FileNameFilter{
        boolean accept(String name);
    }
    public static class AudioExtensionFilter implements FileNameFilter {

        @Override
        public boolean accept(String name) {
            return (name.endsWith(".mp3")||name.endsWith(".MP3"));
        }
    }
    public static class VideoExtensionFilter implements FileNameFilter {

        @Override
        public boolean accept(String name) {
            return (name.endsWith(".mp4")||name.endsWith(".MP4")||name.endsWith(".mkv")||name.endsWith(".MKV"));
        }
    }
}
