package com.wedevelopapps.whatsappstatussaver.Activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.wedevelopapps.whatsappstatussaver.R;

public class showVideoItems extends AppCompatActivity {
    FloatingActionButton playpauseFab;

    VideoView videoView;
    MediaController mediaController;
    Uri iri2;
    int time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video_items);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        final String data = bundle.getString("dataKey");
        playpauseFab = findViewById(R.id.playPauseFab);
        videoView = findViewById(R.id.showVideoView);
        mediaController = new MediaController(this);
        iri2 = Uri.parse(data);


        videoView.setVideoURI(Uri.parse(data));
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();

    }
}
