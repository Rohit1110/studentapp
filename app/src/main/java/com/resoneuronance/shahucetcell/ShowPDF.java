package com.resoneuronance.shahucetcell;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ShowPDF extends AppCompatActivity {
    String url;
    WebView wb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);
        wb=(WebView)findViewById(R.id.webview);
        Bundle bundle = getIntent().getExtras();//Extract the data…
        if (bundle != null) {
            url = bundle.getString("url");
            System.out.println("url in Showpdf"+url);
            //wb.getSettings().setJavaScriptEnabled(true);
            //wb.getSettings().setBuiltInZoomControls(true);
            //wb.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
            //wb.loadUrl(url);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);

        }
    }

}
