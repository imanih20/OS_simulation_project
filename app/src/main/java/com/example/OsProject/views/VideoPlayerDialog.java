package com.example.OsProject.views;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.DialogFragment;

import com.example.OsProject.R;
import com.example.OsProject.utils.DialogUtils;
import com.example.OsProject.utils.PlayerController;
import com.example.OsProject.utils.TextUtils;

import java.io.File;

public class VideoPlayerDialog extends DialogFragment {
    public static VideoPlayerDialog newInstance(File file) {
        Bundle args = new Bundle();
        args.putSerializable("file",file);
        VideoPlayerDialog fragment = new VideoPlayerDialog();
        fragment.setArguments(args);
        return fragment;
    }
    private File file;
    private AppCompatImageButton pauseBtn;
    private  VideoView videoView;
    private AppCompatImageButton playBtn;
    private AppCompatTextView elapsedTime;
    private LinearLayoutCompat btnLayout;
    private Runnable timer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_dialog_layout,container,false);
        videoView = view.findViewById(R.id.video_view);
        pauseBtn = view.findViewById(R.id.pause_btn);
        playBtn = view.findViewById(R.id.play_btn);
        elapsedTime = view.findViewById(R.id.elapsed_time);
        btnLayout = view.findViewById(R.id.btn_layout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler(Looper.myLooper()).postDelayed(() -> btnLayout.setVisibility(View.INVISIBLE),5000);
        DialogUtils.transportBackGround(getDialog());
        DialogUtils.removeShadow(getDialog());
        try {
            if (getArguments()!=null){
                file = (File)getArguments().getSerializable("file");
            }
            if (getView()!=null){
                getView().setOnClickListener(v -> {
                    btnLayout.setVisibility(View.VISIBLE);
                    new Handler(Looper.myLooper()).postDelayed(() -> btnLayout.setVisibility(View.INVISIBLE),5000);
                });
            }
            elapsedTime.bringToFront();
            Uri uri = Uri.fromFile(file);
            MediaController controller = new MediaController(getContext());
            controller.setAnchorView(videoView);
            controller.setMediaPlayer(new PlayerController(getContext(),Uri.fromFile(file)));
//            videoView.setMediaController(controller);
            videoView.setVideoURI(uri);
            videoView.start();
            Handler handler;
            handler = new Handler(Looper.myLooper());
            pauseBtn.setOnClickListener(v -> {
                if (videoView.isPlaying()){
                    videoView.pause();
                }
            });

            playBtn.setOnClickListener(v -> {
                if (!videoView.isPlaying()){
                    videoView.start();
                    handler.postDelayed(timer,1000);
                }
            });
            timer = () -> {
                if (videoView.isPlaying()) {
                    elapsedTime.setText(TextUtils.milliSecToTextTime(videoView.getCurrentPosition()));
                    handler.postDelayed(timer,1000);
                }
            };
            handler.postDelayed(timer,1000);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
