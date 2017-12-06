package com.resoneuronance.shahucetcell;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import adapter.ImageWarehouse;

import static utils.Utility.loadBitmap;
import static utils.Utility.saveFile;

public class Activityfullscreen extends AppCompatActivity {
    private Bitmap image;
    private String imgUrl;
    private String imageName;
    private ImageView img;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityfullscreen);
        img = (ImageView) findViewById(R.id.dimage);
        Bundle bundle = getIntent().getExtras();//Extract the data…
        if (bundle != null) {

            imgUrl = bundle.getString("url");
            System.out.println("URL==>>>  "+imgUrl);
            //imageName = bundle.getString("exam");
            // new Bitmapviewer().execute();

            Glide.with(Activityfullscreen.this)
                    .load(imgUrl)

                    .into(img);

        }


       /* FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        StorageReference islandRef = storageRef.child("3.png");

        storageRef.child("3.png").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bitmapdata) {
                // Use the bytes to display the image
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                img.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });*/



// ImageView in your Activity


// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
            /*Glide.with(this *//* context *//*)
                    .load(imagesRef)
                    .into(img);*/


            /*Glide.with(Activityfullscreen.this)
                    .load(imgUrl)
                    .into(img);*/
           /* Picasso
                    .with(getApplicationContext())
                    .load(imgUrl)
                    .fit()
                    .centerCrop()
                    .into(img,
                            new ImageWarehouse(
                                    "imgUrl",
                                    img,
                                    Environment.DIRECTORY_PICTURES,Activityfullscreen.this
                            )
                    );*/




    }


    private class Bitmapviewer extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                URL url = new URL(imgUrl);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                System.out.println(e);
            }
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());

            String mImageName = "shahu" + timeStamp + ".jpg";
            try {
                saveFile(getApplicationContext(), image, imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            getImage();
        }

        private void getImage() {
            Bitmap b = loadBitmap(getApplicationContext(), imageName);
            img.setImageBitmap(b);
        }
    }


}


