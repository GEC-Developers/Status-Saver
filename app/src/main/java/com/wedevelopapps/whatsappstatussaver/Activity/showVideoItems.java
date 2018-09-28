package com.wedevelopapps.whatsappstatussaver.Activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.wedevelopapps.whatsappstatussaver.R;

public class showVideoItems extends AppCompatActivity {
    FloatingActionButton playpauseFab;

    VideoView videoView;
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
        iri2 = Uri.parse(data);


        videoView.setVideoURI(Uri.parse(data));
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playpauseFab.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            }
        });

        playpauseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    time = videoView.getCurrentPosition();
                    playpauseFab.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } else if (videoView.getCurrentPosition() == videoView.getDuration()) {
                    videoView.start();
                    playpauseFab.setImageResource(R.drawable.ic_pause_black_24dp);
                } else {
                    videoView.seekTo(time);
                    videoView.start();
                    // videoView.resume();
                    playpauseFab.setImageResource(R.drawable.ic_pause_black_24dp);
                }

                Log.d("test", "onClick: video clicked");
            }
        });
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
