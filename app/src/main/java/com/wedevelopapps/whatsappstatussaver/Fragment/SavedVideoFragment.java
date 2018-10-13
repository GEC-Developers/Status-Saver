package com.wedevelopapps.whatsappstatussaver.Fragment;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wedevelopapps.whatsappstatussaver.Activity.VideoDetailActivity;
import com.wedevelopapps.whatsappstatussaver.Activity.showVideoItems;
import com.wedevelopapps.whatsappstatussaver.R;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedVideoFragment extends Fragment {


    RecyclerView recyclerView;
    GridLayoutManager gLay;
    ProgressDialog mProgress;
    TextView tv;
    ImageView cryingEmoji;
    RecyclerView.Adapter mReAdapter;

    public SavedVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saved_video, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.savedVideosRecView);
        recyclerView.setHasFixedSize(true);
        tv = v.findViewById(R.id.statTxt1);
        cryingEmoji = v.findViewById(R.id.cryingEmoji);
        recyclerView.setItemViewCacheSize(60);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        gLay = new GridLayoutManager(getContext(), 2);
        mProgress = new ProgressDialog(getContext(), ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setTitle("Please wait...");
        mProgress.setMessage("Getting things ready..");
        mProgress.setCanceledOnTouchOutside(false);
        recyclerView.setLayoutManager(gLay);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        String data[] = new String[0];
        List<String> muList = new ArrayList<String>();
        try {

            String path = Environment.getExternalStorageDirectory().toString() + "/WhatsAppStatus/Videos/";
            Log.d("test", "onStart: " + path);
            File dir = new File(path);
            File[] files = dir.listFiles();
            Log.d("test", "onStart: " + files.length);
            // String data[] = new String[0];
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

            for (int i = 0; i < files.length; i++) {

                if (files[i].getName().endsWith(".mp4")) {
                    Log.d("test", "onStart:  files " + files[i].getAbsolutePath());
                    muList.add(files[i].getAbsolutePath());

                }


            }


        } catch (Exception ex) {
            //Toast.makeText(getContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
        //Collections.reverse(muList);
        mReAdapter = new SavedVideoFragment.myAdapter((ArrayList<String>) muList, getContext());
        recyclerView.setAdapter(mReAdapter);
        if (muList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
            cryingEmoji.setVisibility(View.GONE);
        }
        Log.d("test2", "onStart: " + muList.size());


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public class myAdapter extends RecyclerView.Adapter<SavedVideoFragment.myAdapter.MyHolder> {

        List<String> muList = new ArrayList<String>();

        public myAdapter(ArrayList<String> mylist, Context context) {
            this.muList = mylist;
        }

        @Override
        public SavedVideoFragment.myAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_template, parent, false);
            SavedVideoFragment.myAdapter.MyHolder holder = new SavedVideoFragment.myAdapter.MyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final SavedVideoFragment.myAdapter.MyHolder holder, int position) {
            final Uri iri = Uri.parse(muList.get(position));
            File f = new File(muList.get(position));


            File thumFile = new File(Environment.getExternalStorageDirectory() + "/WhatsAppStatus/Videos/.Thum/" + f.getName() + ".jpg");
            if (!thumFile.exists()) {
                Bitmap bmap = ThumbnailUtils.createVideoThumbnail(iri.toString(), MediaStore.Video.Thumbnails.MINI_KIND);

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/WhatsAppStatus/Videos/.Thum");
                myDir.mkdirs();
                // Random generator = new Random();
                int n = 10000;
                //  n = generator.nextInt(n);
                String fname = f.getName() + ".jpg";
                File file = new File(myDir, fname);
                if (!file.exists()) {
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                        Log.i("test", "" + file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            Glide.with(getContext()).load(thumFile).into(holder.VidV);

            holder.VidV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), showVideoItems.class);
                    intent.putExtra("dataKey", iri.toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), holder.VidV, "videoTrans1");

                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return muList.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            ImageView VidV;
            ImageButton ImgBtn;

            public MyHolder(View itemView) {
                super(itemView);

                VidV = itemView.findViewById(R.id.videoView);
                ImgBtn = itemView.findViewById(R.id.imgVBtnDown);

            }
        }

    }

}
