package com.wedevelopapps.whatsappstatussaver.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.wedevelopapps.whatsappstatussaver.R;
import com.wedevelopapps.whatsappstatussaver.adapter.CustomSliderAdapter;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowPictureItems extends AppCompatActivity {

    ImageView backArrow;
    List<File> imagesList;
    CustomSliderAdapter myCustomPagerAdapter;
    ViewPager viewPager;
    Uri iri2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pictures_items);

        viewPager = (ViewPager) findViewById(R.id.showItemsviewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.picNavigation);
        backArrow = findViewById(R.id.backArrow);
        imagesList = new ArrayList<>();
        imagesList = fetchImages();
        int pos = 0;
        String posString = getIntent().getStringExtra("pos").toString();
        try {
            pos = Integer.parseInt(posString);
        } catch (Exception e) {
            Log.d("Error", "not parceable Int");
        }



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
                    case R.id.Share:
                        shareImage();
                        break;
                    case R.id.SetStatus:
                        setStatus();
                        break;
                    case R.id.Delete:
                        deleteImage();
                        break;


                }
                return true;
            }
        });

    }


    private void deleteImage() {
        if (imagesList.size() > 0) {
            File file = new File(imagesList.get(viewPager.getCurrentItem()).getAbsolutePath());
            file.delete();
            if (file.exists()) {
                try {
                    file.getCanonicalFile().delete();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {


                    if (file.exists()) {
                        getApplicationContext().deleteFile(file.getName());
                    }

                }

            }
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            update();
        } else {

            previousActivity();
        }

    }


    private void previousActivity() {

        Intent intent = new Intent(this, SavedGallery.class);
        startActivity(intent);

    }


    private void update() {

        int pos = viewPager.getCurrentItem();
        imagesList.remove(pos);
        if (imagesList.size() <= 0) {
            previousActivity();
        }

        myCustomPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(myCustomPagerAdapter);
        if (pos >= imagesList.size()) {
            viewPager.setCurrentItem(pos - 1);
        } else {
            viewPager.setCurrentItem(pos);
        }

    }


    private void shareImage() {
        iri2 = Uri.parse(imagesList.get(viewPager.getCurrentItem()).getAbsolutePath());

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/jpeg");
        i.putExtra(Intent.EXTRA_STREAM, iri2);
        startActivity(Intent.createChooser(i, "Share Image Using"));
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


    List fetchImages() {
        List<File> muList = new ArrayList<File>();
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/WhatsAppStatus/Images/";
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
}
