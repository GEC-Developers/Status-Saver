package com.wedevelopapps.whatsappstatussaver.Fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
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

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.wedevelopapps.whatsappstatussaver.Activity.PicDetail;
import com.wedevelopapps.whatsappstatussaver.Activity.ShowPictureItems;
import com.wedevelopapps.whatsappstatussaver.R;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class SavedPictureFragment extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.Adapter mReAdapter;
    GridLayoutManager gLay;
    TextView tv;

    public SavedPictureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saved_picture, container, false);
        recyclerView = v.findViewById(R.id.savedPicturesRecView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        gLay = new GridLayoutManager(getContext(), 2);
        tv = v.findViewById(R.id.statTxt);
        recyclerView.setLayoutManager(gLay);

        List<String> muList = new ArrayList<String>();
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/WhatsAppStatus/Images/";
            Log.d("test", "onStart: " + path);
            File dir = new File(path);
            File[] files = dir.listFiles();

            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            if (files.length > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                tv.setVisibility(View.GONE);
            }
            for (int i = 0; i < files.length; i++) {

                if (files[i].getName().endsWith(".jpg") || files[i].getName().endsWith(".png")) {

                    muList.add(files[i].getAbsolutePath());

                }

            }


        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

        mReAdapter = new myAdapter((ArrayList<String>) muList, getContext());
        recyclerView.setAdapter(mReAdapter);

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public class myAdapter extends RecyclerView.Adapter<myAdapter.MyPictureHolder> {

        List<String> muList = new ArrayList<String>();

        public myAdapter(ArrayList<String> mylist, Context context) {
            this.muList = mylist;
        }

        @Override
        public myAdapter.MyPictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pictures_template, parent, false);
            MyPictureHolder holder = new MyPictureHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final myAdapter.MyPictureHolder holder, int position) {
            final Uri iri = Uri.parse(muList.get(position));
            File f = new File(muList.get(position));

            Picasso.with(getContext()).load(f).centerCrop().networkPolicy(NetworkPolicy.OFFLINE).fit().into(holder.imgV, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Log.d("test", "onError: ");
                }
            });

        }

        @Override
        public int getItemCount() {
            return muList.size();
        }

        class MyPictureHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView imgV;
            ImageButton imgB;

            public MyPictureHolder(View itemView) {
                super(itemView);

                imgV = itemView.findViewById(R.id.imageView);
                imgB = itemView.findViewById(R.id.imgBdon);
                imgV.setOnClickListener(this);

            }

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ShowPictureItems.class);
                intent.putExtra("pos", "" + getAdapterPosition());
                intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(getActivity()), imgV, "imageViewTrans1");
                Objects.requireNonNull(getContext()).startActivity(intent, activityOptionsCompat.toBundle());
            }


        }

    }
}

