package com.example.OsProject.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.MediaController;


public class PlayerController implements MediaController.MediaPlayerControl {
    private final MediaPlayer player;

    public PlayerController(Context context,Uri uri){
        player = MediaPlayer.create(context,uri);
    }
    @Override
    public void start() {
        player.start();
    }

    public void release(){
        player.release();
    }
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        player.setOnCompletionListener(listener);
    }
    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return player.getAudioSessionId();
    }

}
