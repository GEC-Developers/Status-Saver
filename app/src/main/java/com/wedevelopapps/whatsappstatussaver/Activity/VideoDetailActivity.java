package com.wedevelopapps.whatsappstatussaver.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class VideoDetailActivity extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;

    ImageView backArrow;
    Uri iri2;
    int time = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_det);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        final String data = bundle.getString("dataKey");
        BottomNavigationView bottomNavigationView = findViewById(R.id.VideoNavigation);
        backArrow = findViewById(R.id.backArrow);
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
                    case R.id.SetStatus:
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
                        break;

                    case R.id.Download:
                        //Download Video
                        File f1, f2;
                        f1 = new File(Uri.parse(data).toString());
                        String fname = f1.getName();
                        f2 = new File(Environment.getExternalStorageDirectory() + "/WhatsAppStatus/Videos/");
                        f2.mkdirs();

                        try {
                            FileUtils.copyFileToDirectory(f1, f2);
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
                            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                            values.put(MediaStore.MediaColumns.DATA, f2.toString() + "/" + fname);
                            getApplicationContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        } finally {
                            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                        }
                        break;

                    //Share
                    case R.id.Share:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("video/mp4");
                        i.putExtra(Intent.EXTRA_STREAM, iri2);
                        startActivity(Intent.createChooser(i, "Share video Using"));
                        break;


                }
                return true;
            }
        });

        videoView = findViewById(R.id.VideoVView);
        mediaController = new MediaController(this);

        iri2 = Uri.parse(data);


        videoView.setVideoURI(Uri.parse(data));
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
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



 /*   @Override
    protected void onStart() {
        super.onStart();
        new MaterialIntroView.Builder(this)
                .enableDotAnimation(true)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.RIGHT)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText("Hi There! \nClick here to download this videos")
                .setShape(ShapeType.CIRCLE)
                .setTarget()
                .setUsageId("intro_card2") //THIS SHOULD BE UNIQUE ID
                .show();
    }  */
}