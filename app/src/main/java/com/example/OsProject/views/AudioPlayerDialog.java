package com.example.OsProject.views;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.DialogFragment;
import com.example.OsProject.R;
import com.example.OsProject.utils.DialogUtils;
import com.example.OsProject.utils.PlayerController;

import java.io.File;

public class AudioPlayerDialog extends DialogFragment {
    private AppCompatSeekBar progressBar;
    private AppCompatImageButton playBtn,pauseBtn;
    private File file;
    private boolean isPlay ;
    private  Handler handler;
    private Runnable progressRun;
    private PlayerController controller;

    public static AudioPlayerDialog newInstance(File file) {
        Bundle args = new Bundle();
        args.putSerializable("file",file);
        AudioPlayerDialog fragment = new AudioPlayerDialog();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_player_layout,container,false);
        progressBar = view.findViewById(R.id.progress_bar);
        playBtn = view.findViewById(R.id.play_btn);
        pauseBtn = view.findViewById(R.id.pause_btn);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DialogUtils.removeShadow(getDialog());
        try {
            if (getDialog()!=null) DialogUtils.transportBackGround(getDialog());
            if (getArguments()!=null){
                file = (File) getArguments().getSerializable("file");
            }
            handler = new Handler(Looper.myLooper());
            controller = new PlayerController(getContext(),Uri.fromFile(file));
            controller.start();
            isPlay = true;
            progressBar.setMax(controller.getDuration());
            playBtn.setOnClickListener(v -> {
                if (!isPlay){
                    controller.start();
                    isPlay = true;
                }
            });
            pauseBtn.setOnClickListener(v -> {
                if (isPlay){
                    controller.pause();
                    isPlay = false;
                }
            });
            controller.setOnCompletionListener(mp -> {
                playBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                isPlay = false;
                controller.seekTo(0);
                progressBar.setProgress(0);
            });
            progressRun = new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(controller.getCurrentPosition());
                    handler.postDelayed(this,10);
                }
            };
            handler.postDelayed(progressRun,10);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(progressRun);
        if (controller!=null)
            controller.release();
    }
}
