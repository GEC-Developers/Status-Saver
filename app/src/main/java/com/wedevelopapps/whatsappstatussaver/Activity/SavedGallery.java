package com.wedevelopapps.whatsappstatussaver.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.wedevelopapps.whatsappstatussaver.R;
import com.wedevelopapps.whatsappstatussaver.SelectionsPageAdapter;
import com.wedevelopapps.whatsappstatussaver.adapter.SavedGalleryAdapter;

public class SavedGallery extends AppCompatActivity {

    private Toolbar mToolBar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SavedGalleryAdapter mSavedGalleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_gallery);

        mToolBar = (Toolbar) findViewById(R.id.saved_gallery_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Saved Gallery");


        //setting up view pager
        mViewPager = findViewById(R.id.saved_gallery_viewPager);
        mSavedGalleryAdapter = new SavedGalleryAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSavedGalleryAdapter);

        mTabLayout = findViewById(R.id.saved_gallery_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }
}
