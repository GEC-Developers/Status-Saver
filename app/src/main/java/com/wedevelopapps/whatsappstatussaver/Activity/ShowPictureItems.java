package com.wedevelopapps.whatsappstatussaver.Activity;

import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.wedevelopapps.whatsappstatussaver.R;
import com.wedevelopapps.whatsappstatussaver.adapter.CustomSliderAdapter;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowPictureItems extends AppCompatActivity {


    List<File> imagesList;
    CustomSliderAdapter myCustomPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gallery_items);

        viewPager = (ViewPager) findViewById(R.id.showItemsviewPager);
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
