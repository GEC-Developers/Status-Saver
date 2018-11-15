package com.tripleastudio.whatsappstatussaver.Fragment;


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
import com.tripleastudio.whatsappstatussaver.Activity.VideoDetailActivity;
import com.tripleastudio.whatsappstatussaver.Models.DataModel;
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


public class VideosFragment extends android.support.v4.app.Fragment {


    public VideosFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    GridLayoutManager gLay;

    ProgressDialog mProgress;
    TextView tv;
    ImageView cryingEmoji;
    RecyclerView.Adapter mReAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_videos, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.VideosRecView);
        recyclerView.setHasFixedSize(true);
        tv = v.findViewById(R.id.statTxt1);
        cryingEmoji = v.findViewById(R.id.cryingEmoji);
        recyclerView.setItemViewCacheSize(60);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        gLay = new GridLayoutManager(getContext(),2);
        mProgress = new ProgressDialog(getContext(),ProgressDialog.STYLE_HORIZONTAL);
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
        //TODO remove muList as mylist is used now

        List<String> muList = new ArrayList<String>();
        ArrayList<DataModel> myList= new ArrayList<>();
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses";
            Log.d("test", "onStart: " + path);
            File dir = new File(path);
            File[] files = dir.listFiles();
            Log.d("test", "onStart: " + files.length);
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

            for (File file: files) {
                if(file.getName().endsWith(".mp4")){
                    Log.d("test", "onStart:  files " + file.getAbsolutePath());
                   // muList.add(file.getAbsolutePath());
                    myList.add(new DataModel(file.getAbsolutePath(),file.getName()));
                }


            }


        }catch (Exception ex){
            // Toast.makeText(getContext(),ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
        //Collections.reverse(muList);
        mReAdapter = new VideosFragment.myAdapter(myList);
        recyclerView.setAdapter(mReAdapter);
        if(myList.size()>0) {
            recyclerView.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
            cryingEmoji.setVisibility(View.GONE);
        }
        Log.d("test2", "onStart: "+muList.size());



    }

    private ArrayList<String> findUnusedThumb(ArrayList<DataModel> mylist) {

        ArrayList<String> allItems = new ArrayList<String>();
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/WhatsAppStatus/Videos/.Thum";
            Log.d("test", "onStart: " + path);
            File dir = new File(path);
            File[] files = dir.listFiles();
            Log.d("test", "onStart: " + files.length);
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (File file: files) {
                if(file.getName().endsWith(".jpg")){
                    Log.d("test", "onStart:  files " + file.getAbsolutePath());
                    allItems.add(file.getName());
                }
            }
        }catch (Exception ex){
            // Toast.makeText(getContext(),ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
        if (allItems.size() > 0) {
            for(DataModel data:mylist){
                allItems.remove(data.getFileName());
            }
        }
        return allItems;
    }

    private void delete(ArrayList<String> list){

        ArrayList<String> allItems = new ArrayList<String>();
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/WhatsAppStatus/Videos/.Thum";
            Log.d("test", "onStart: " + path);
            File dir = new File(path);
            File[] files = dir.listFiles();
            Log.d("test", "onStart: " + files.length);
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (File file: files) {
                if(list.contains(file.getName())){

                    file.delete();

                    if (file.exists()) {
                        try {
                            file.getCanonicalFile().delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (file.exists()) {
                                getContext().deleteFile(file.getName());
                            }

                        }

                    }
                }
            }

        }catch (Exception ex){
            // Toast.makeText(getContext(),ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            Log.d("delete status","NOTHING To DELETE");
        }



    }



    private void deleteUnusedThumbs(ArrayList<DataModel> mylist){

        delete(findUnusedThumb(mylist));

    }


    public class myAdapter extends RecyclerView.Adapter<VideosFragment.myAdapter.MyHolder>{

        ArrayList<DataModel> myList;

        @Override
        public VideosFragment.myAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_template,parent,false);
            VideosFragment.myAdapter.MyHolder holder = new VideosFragment.myAdapter.MyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyHolder holder, int position) {
            final Uri iri = Uri.parse(myList.get(position).getPath());
            File f = new File(myList.get(position).getPath());

            deleteUnusedThumbs(myList);

            File thumFile = new File(Environment.getExternalStorageDirectory() + "/WhatsAppStatus/Videos/.Thum/" + f.getName() + ".jpg");
            if(!thumFile.exists()){
                Bitmap bmap = ThumbnailUtils.createVideoThumbnail(iri.toString(), MediaStore.Video.Thumbnails.MINI_KIND);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/WhatsAppStatus/Videos/.Thum");
                myDir.mkdirs();
                // Random generator = new Random();
                int n = 10000;
                //  n = generator.nextInt(n);
                String fname = f.getName()+".jpg";
                File file = new File(myDir, fname);
                if (!file.exists()) {
                    //file.delete();
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
            //Picasso.with(getContext()).load(u).networkPolicy(NetworkPolicy.OFFLINE).fit().into(holder.VidV);
            Glide.with(getContext()).load(thumFile).into(holder.VidV);

            holder.ImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File f1,f2;
                    f1 = new File(iri.toString());
                    String fname = f1.getName();
                    f2 = new File(Environment.getExternalStorageDirectory() + "/WhatsAppStatus/Videos/");
                    f2.mkdirs();
                    try {
                        //FileUtils.copyFile(f1,f2);
                        FileUtils.copyFileToDirectory(f1,f2);
                        ContentValues values =new ContentValues();
                        values.put(MediaStore.Video.Media.DATE_TAKEN,System.currentTimeMillis());
                        values.put(MediaStore.Video.Media.MIME_TYPE,"video/mp4");
                        values.put(MediaStore.MediaColumns.DATA,f2.toString()+"/"+fname);
                        getContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,values);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }finally {
                        Toast.makeText(getContext(),"Saved",Toast.LENGTH_LONG).show();
                    }


                }
            });

            holder.VidV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(),VideoDetailActivity.class);
                    intent.putExtra("dataKey",iri.toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),holder.VidV,"videoTrans1");
                    startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return myList.size();
        }

        public myAdapter(ArrayList<DataModel> mylist) {
            this.myList = mylist;

        }

        class MyHolder extends RecyclerView.ViewHolder{
            ImageView VidV;
            ImageButton ImgBtn;
            public MyHolder(View itemView) {
                super(itemView);

                VidV = itemView.findViewById(R.id.videoView);
                ImgBtn = itemView.findViewById(R.id.imgVBtnDown);

            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


}