package com.wedevelopapps.whatsappstatussaver.Activity;


import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.wedevelopapps.whatsappstatussaver.R;

public class InfoActivity extends AppCompatActivity {

    ShimmerTextView shimmerTextView;
    Shimmer shimmer;
    Typeface tf ;

    @Override
    protected void onResume() {
        super.onResume();
        shimmer.start(shimmerTextView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmer.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        tf = Typeface.createFromAsset(getAssets(),"waltograph.regular.ttf");

        shimmerTextView = findViewById(R.id.textView2);
        shimmerTextView.setTypeface(tf);
        shimmer = new Shimmer();
        shimmer.start(shimmerTextView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        shimmer.cancel();
    }
}
