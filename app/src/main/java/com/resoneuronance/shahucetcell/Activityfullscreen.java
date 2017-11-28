package com.resoneuronance.shahucetcell;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Activityfullscreen extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityfullscreen);
        Bundle bundle = getIntent().getExtras();//Extract the data…
        if (bundle != null) {
            String imgUrl = bundle.getString("url");
            ImageView img = (ImageView) findViewById(R.id.img);
            Glide.with(Activityfullscreen.this)
                    .load(imgUrl)

                    .into(img);


        }

    }



}


