package com.wedevelopapps.whatsappstatussaver.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.wedevelopapps.whatsappstatussaver.R;
import com.wedevelopapps.whatsappstatussaver.adapter.CustomSliderAdapter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class PicDetail extends AppCompatActivity {

    ImageView backArrow;
    Bitmap bmap;
    Uri iri2;
    List<File> imagesList;
    CustomSliderAdapter myCustomPagerAdapter;
    ViewPager viewPager;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        imagesList = new ArrayList<>();
        imagesList = fetchImages();
        int pos = 0;
        String posString = getIntent().getStringExtra("pos").toString();
        try {
            pos = Integer.parseInt(posString);
        } catch (Exception e) {
            Log.d("Error", "not parceable Int");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.picNavigation);
        backArrow = findViewById(R.id.backArrow);

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

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        myCustomPagerAdapter = new CustomSliderAdapter(this, imagesList);
        viewPager.setAdapter(myCustomPagerAdapter);
        viewPager.setCurrentItem(pos);

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
                        setStatus();
                        showInterstitial();
                        break;
                    case R.id.Download:
                        downloadImage();
                        showInterstitial();
                        break;
                    case R.id.Share:
                        shareImage();

                        break;


                }
                return true;
            }
        });

    }

    private void setStatus() {
        Uri imageUri = Uri.parse(imagesList.get(viewPager.getCurrentItem()).getAbsolutePath());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //Target whatsapp:
        shareIntent.setPackage("com.whatsapp");
        //Add text and then Image URI
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }


    }




    /*


    sharing Images

     */

    private void shareImage() {

        iri2 = Uri.parse(imagesList.get(viewPager.getCurrentItem()).getAbsolutePath());

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/jpeg");
        i.putExtra(Intent.EXTRA_STREAM, iri2);
        startActivity(Intent.createChooser(i, "Share Image Using"));


    }


    /*

    downloading images

     */
    private void downloadImage() {


        File f1, f2;
        f1 = imagesList.get(viewPager.getCurrentItem());
        String fname = f1.getName();
        f2 = new File(Environment.getExternalStorageDirectory() + "/WhatsAppStatus/Images/");
        f2.mkdirs();

        try {
            FileUtils.copyFileToDirectory(f1, f2);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA, f2.toString() + "/" + fname);
            getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
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


    List fetchImages() {
        List<File> muList = new ArrayList<File>();
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses";
            Log.d("test", "onStart: " + path);
            File dir = new File(path);
            File[] files = dir.listFiles();

            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (int i = 0; i < files.length; i++) {

                if (files[i].getName().endsWith(".jpg") || files[i].getName().endsWith(".png")) {

                    muList.add(files[i]);

                }

            }


        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

        return muList;
    }


  /*  @Override
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
                .setInfoText("Hi There! \nClick here to share this picture")
                .setShape(ShapeType.CIRCLE)
                .setTarget(shareFab)
                .setUsageId("intro_card3") //THIS SHOULD BE UNIQUE ID
                .show();
    } */
}