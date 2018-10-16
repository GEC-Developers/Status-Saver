package com.tripleastudio.whatsappstatussaver.Activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;
import com.tripleastudio.whatsappstatussaver.Models.ListenableTabLayout;
import com.tripleastudio.whatsappstatussaver.Models.TabIndicatorFollower;
import com.tripleastudio.whatsappstatussaver.adapter.SelectionsPageAdapter;
import com.wedevelopapps.whatsappstatussaver.R;

import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.ArrayList;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private ViewPager mViewPager;
    private ListenableTabLayout mTabLayout;
    private SelectionsPageAdapter mSectionPageAdapter;
    ArrayList<Integer> imageIDList;
    ArrayList<String> titleList;
    private BoomMenuButton bmb;
    Dialog HelpPopUp, rateUsPopUp;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent mStartActivity = new Intent(MainActivity.this, MainActivity.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(MainActivity.this, mPendingIntentId, mStartActivity,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);
            } else {
                System.exit(0);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final FloatingActionButton fabShare, fabInfo;
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        mToolBar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("WA Status Saver");


        //printHashKey();

        HelpPopUp = new Dialog(this);
        rateUsPopUp = new Dialog(this);

        //PermissifyManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Restart App", Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);


            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }


        //setting up view pager
        mViewPager = findViewById(R.id.viewPager);
        View triangle = findViewById(R.id.triangle);
        mSectionPageAdapter = new SelectionsPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPageAdapter);


        bmb = findViewById(R.id.bmb);
        imageIDList = new ArrayList<>();
        titleList = new ArrayList<>();
        setInitialData();
        assert bmb != null;

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {

            final HamButton.Builder SavedGallery = new HamButton.Builder()
                    .normalImageRes(imageIDList.get(i))
                    .imageRect(new Rect(20, 15, Util.dp2px(50), Util.dp2px(50)))
                    .shadowEffect(true)
                    .normalText(titleList.get(i))
                    .typeface(Typeface.DEFAULT_BOLD)
                    .textSize(22)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if (index == 0) {
                                Intent savedPictures = new Intent(getApplicationContext(), SavedGallery.class);
                                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, bmb, "fabTrans1");

                                startActivity(savedPictures, activityOptionsCompat.toBundle());

                            } else if (index == 1) {
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBody = "Download WhatsApp Status using PLAYSTORE LINK";
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Songs of Zion ");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                            } else if (index == 2) {
                                Intent i = new Intent(getApplicationContext(), InfoActivity.class);
                                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, bmb, "fabTrans");

                                startActivity(i, activityOptionsCompat.toBundle());
                            }
                        }
                    });

            bmb.addBuilder(SavedGallery);
        }

        mTabLayout = findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        TabIndicatorFollower.setupWith(mTabLayout, triangle);



    }

   /* public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    } **/

    private void setInitialData() {
        imageIDList.add(R.drawable.ic_image_black_24dp);
        imageIDList.add(R.drawable.ic_share_black_24dp);
        imageIDList.add(R.drawable.ic_error_outline_black_24dp);

        titleList.add("Saved Gallery");
        titleList.add("Share this App");
        titleList.add("About Us");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.Help) {
            ShowHelpPopUp();
        }

        return true;
    }

    private void ShowHelpPopUp() {
        HelpPopUp.setContentView(R.layout.help_pop_up);
        ImageView ClosePopup = HelpPopUp.findViewById(R.id.ClosePopUp);

        ClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpPopUp.dismiss();
            }
        });
        HelpPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        HelpPopUp.show();
    }

    @Override
    public void onBackPressed() {
        ratePopup();
    }

    private void ratePopup() {
        rateUsPopUp.setContentView(R.layout.rate_us_pop_up);
        Button exitApp = rateUsPopUp.findViewById(R.id.exitApp);
        LinearLayout cancel = rateUsPopUp.findViewById(R.id.cancelButton);

        exitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateUsPopUp.dismiss();
            }
        });

        rateUsPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rateUsPopUp.show();

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
                .setInfoText("Hi There! \nClick here to select videos")
                .setShape(ShapeType.CIRCLE)
                .setTarget(mTabLayout)
                .setUsageId("intro_card1") //THIS SHOULD BE UNIQUE ID
                .show();
    }
}
