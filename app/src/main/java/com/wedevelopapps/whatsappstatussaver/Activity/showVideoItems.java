package com.wedevelopapps.whatsappstatussaver.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.wedevelopapps.whatsappstatussaver.R;

import java.io.File;

public class showVideoItems extends AppCompatActivity {
    FloatingActionButton playpauseFab;

    VideoView videoView;
    ImageView backArrow;
    MediaController mediaController;
    Uri iri2;
    int time = 0;
    InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video_items);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        MobileAds.initialize(this, "ca-app-pub-3781654628202166~6266376014");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3781654628202166/9503322561");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        String data = bundle.getString("dataKey");
        videoView = findViewById(R.id.showVideoView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.picNavigation);
        backArrow = findViewById(R.id.backArrow);
        mediaController = new MediaController(this);
        iri2 = Uri.parse(data);


        videoView.setVideoURI(Uri.parse(data));
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Share:
                        showInterstitial();
                        shareImage();
                        break;
                    case R.id.SetStatus:
                        showInterstitial();
                        setStatus();
                        break;
                    case R.id.Delete:
                        //TODO delete Video
                        deleteVideo();
                        break;


                }
                return true;
            }
        });

    }

    private void deleteVideo() {
        try {
            File videofiles = new File(String.valueOf(iri2));
            if (videofiles.exists()) {
                videofiles.delete();
                previousActivity();
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "You don't have video to delete", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void previousActivity() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }


    private void shareImage() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("video/mp4");
        i.putExtra(Intent.EXTRA_STREAM, iri2);
        startActivity(Intent.createChooser(i, "Share video Using"));
    }

    private void setStatus() {
        //Set Status
        Intent setStatus = new Intent(Intent.ACTION_SEND);
        setStatus.setPackage("com.whatsapp");

        setStatus.setType("video/mp4");
        setStatus.putExtra(Intent.EXTRA_STREAM, iri2);
        setStatus.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(setStatus);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();

        }
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
