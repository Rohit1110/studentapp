package com.resoneuronance.shahucetcell;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
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
import utils.PermissionUtil;
import utils.Utility;

import static android.content.ContentValues.TAG;
import static utils.Utility.loadBitmap;
import static utils.Utility.saveFile;

public class Activityfullscreen extends AppCompatActivity  implements ActivityCompat.OnRequestPermissionsResultCallback {
    private Bitmap image;
    private String imgUrl;
    private String imageName;
    private ImageView img;
    ProgressDialog proDialog;
    StorageReference httpsReference;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private View mLayout;
    String examname;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtil.verifyPermissions(grantResults)) {
            // All required permissions have been granted, display contacts fragment.
        /*    Snackbar.make(mLayout, "permission granted",
                    Snackbar.LENGTH_SHORT)
                    .show();*/
            //Toast.makeText(PDfViewer.this,"Permission Grant",Toast.LENGTH_LONG).show();
        } else {
            Log.i(TAG, "Contacts permissions were NOT granted.");
            Snackbar.make(mLayout, "permissions were NOT granted",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityfullscreen);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.stausbar));
        }
        proDialog = new ProgressDialog(Activityfullscreen.this);
           isStoragePermissionGranted();

        proDialog.setMessage("please wait....");
        //proDialog.setCancelable(false);
        proDialog.show();


        img = (ImageView) findViewById(R.id.dimage);
        Bundle bundle = getIntent().getExtras();//Extract the data…
        if (bundle != null) {

            imgUrl = bundle.getString("url");
            examname = bundle.getString("testname");
            System.out.println("URL==>>>  " + imgUrl);
            //imageName = bundle.getString("exam");
            // new Bitmapviewer().execute();
        }
        if(Utility.isInternetOn(Activityfullscreen.this)) {

            httpsReference = storage.getReferenceFromUrl(imgUrl);

            System.out.println("Reference is created !!" + httpsReference);

            //File dir = new File(Environment.getDataDirectory() + "/ShahuApp/");
            final File logFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Exams");
                               /* java.io.File xmlFile = new java.io.File((PDfViewer.this
                                        .getApplicationContext().getFileStreamPath("CW-09.pdf")
                                        .getPath()));
                                System.out.println("File path online: "+xmlFile);*/

            if (!logFolder.exists()) {
                logFolder.mkdirs();
                System.out.println("Directory created ...");
            }

            final File pdfFile = new File(logFolder, examname + "Omr.jpg");
            //final File pdfFile=xmlFile;
            System.out.println("File PDF===>>>" + pdfFile);
            httpsReference.getFile(pdfFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri uri=FileProvider.getUriForFile(Activityfullscreen.this,BuildConfig.APPLICATION_ID + ".provider",pdfFile);
                    intent.setDataAndType(uri, MimeTypeMap.getSingleton().getMimeTypeFromExtension("image*//*"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                     finish();



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


            });
        }else {
            //proDialog.dismiss();
            final File logFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Exams");

            final File pdfFile = new File(logFolder, examname+"Omr.jpg");
/*
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(pdfFile), "image*//*");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();*/

         /*   // create new Intent
            Intent intent = new Intent(Intent.ACTION_VIEW);

// set flag to give temporary permission to external app to use your FileProvider
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

// generate URI, I defined authority as the application ID in the Manifest, the last param is file I want to open


// I am opening a PDF file so I give it a valid MIME type
            intent.setDataAndType(FileProvider.getUriForFile(Activityfullscreen.this, BuildConfig.APPLICATION_ID+".provider", pdfFile), "image*//**//*");

// validate that the device can open your File!
            PackageManager pm = getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                startActivity(intent);
                finish();

            }*/


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri=FileProvider.getUriForFile(Activityfullscreen.this,BuildConfig.APPLICATION_ID + ".provider",pdfFile);
            intent.setDataAndType(uri, MimeTypeMap.getSingleton().getMimeTypeFromExtension("image*//*"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
            finish();
        }
    }



    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED|| checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }


}


