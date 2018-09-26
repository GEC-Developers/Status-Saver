package com.wedevelopapps.whatsappstatussaver.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
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

    FloatingActionButton downloadFab, shareFab;
    Bitmap bmap;
    Uri iri2;
    CustomSliderAdapter myCustomPagerAdapter;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        String posString = getIntent().getStringExtra("pos").toString();
        try {
            int pos = Integer.parseInt(posString);
        } catch (Exception e) {
            Log.d("Error", "not parceable Inet");
        }
        downloadFab = findViewById(R.id.DownloadFab);
        shareFab = findViewById(R.id.shareFab);


        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);



        myCustomPagerAdapter = new CustomSliderAdapter(this, fetchImages());
        viewPager.setAdapter(myCustomPagerAdapter);

    }



    List fetchImages(){
        List<File> muList = new ArrayList<File>();
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses";
            Log.d("test", "onStart: " + path);
            File dir = new File(path);
            File[] files = dir.listFiles();

            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (int i = 0; i < files.length; i++) {

                if(files[i].getName().endsWith(".jpg")||files[i].getName().endsWith(".png")){

                    muList.add(files[i]);

                }

            }



        }catch (Exception ex){
            Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }

        return muList;
    }




    @Override
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
    }
}