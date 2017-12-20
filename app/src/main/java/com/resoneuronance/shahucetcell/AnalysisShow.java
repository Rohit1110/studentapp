package com.resoneuronance.shahucetcell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import utils.Utility;

public class AnalysisShow extends AppCompatActivity {

    private String url,examname;
    WebView webView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference httpsReference;
    ProgressDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_show);
        webView = (WebView) findViewById(R.id.analysiswebview);
        proDialog = new ProgressDialog(AnalysisShow.this);
        proDialog.setMessage("please wait....");
        //proDialog.setCancelable(false);
        proDialog.show();
       proDialog.dismiss();
        Bundle bundle = getIntent().getExtras();//Extract the data…
        if (bundle != null) {
            url = bundle.getString("url");
            examname = bundle.getString("testname");

            System.out.println("URL==>>> " + url);
            // webView.loadUrl(url);

        }

        if (Utility.isInternetOn(AnalysisShow.this)) {

            httpsReference = storage.getReferenceFromUrl(url);

            System.out.println("Reference is created !!" + httpsReference);

            //File dir = new File(Environment.getDataDirectory() + "/ShahuApp/");
            final File logFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Exams");
            if (!logFolder.exists()) {
                logFolder.mkdirs();
                System.out.println("Directory created ...");
            }
            //proDialog.dismiss();
            final File pdfFile = new File(logFolder, examname + ".html");

            httpsReference.getFile(pdfFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    webView.getSettings().setBuiltInZoomControls(true);
                    webView.loadUrl(Uri.fromFile(pdfFile).toString());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                    System.out.println("Exception====> " + exception);
                    exception.printStackTrace();
                }
            });


        } else {
            final File logFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Exams");

            final File pdfFile = new File(logFolder, examname + ".html");
            webView.getSettings().setBuiltInZoomControls(true);
            webView.loadUrl(Uri.fromFile(pdfFile).toString());


        }
    }



}
