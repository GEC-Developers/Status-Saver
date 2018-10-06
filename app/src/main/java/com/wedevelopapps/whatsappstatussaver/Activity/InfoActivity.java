package com.wedevelopapps.whatsappstatussaver.Activity;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.wedevelopapps.whatsappstatussaver.R;

public class InfoActivity extends AppCompatActivity {

    ShimmerTextView shimmerTextView;
    Shimmer shimmer;
    Typeface tf ;
    Button btFeedBack, btLicense;

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
        btFeedBack = findViewById(R.id.btFeedBack);
        btLicense = findViewById(R.id.btLicense);

        shimmerTextView = findViewById(R.id.textView2);
        shimmerTextView.setTypeface(tf);
        shimmer = new Shimmer();
        shimmer.start(shimmerTextView);

        btFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedBack();
            }
        });

        btLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void sendFeedBack() {
        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
        intent.putExtra(Intent.EXTRA_TEXT, " ");
        intent.setData(Uri.parse("mailto:appswedevelop@gmail.com")); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        shimmer.cancel();
    }
}
