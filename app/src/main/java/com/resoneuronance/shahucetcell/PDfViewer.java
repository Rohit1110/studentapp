package com.resoneuronance.shahucetcell;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class PDfViewer extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //StorageReference storageRef = storage.getReference();
    StorageReference httpsReference;
    String Url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        Bundle bundle = getIntent().getExtras();//Extract the data…
        if (bundle != null) {

            Url = bundle.getString("examid");
            System.out.println("URi File"+ Url);


            httpsReference = storage.getReferenceFromUrl(Url);

            /*File localFile = null;
            try {
                File dir = new File(Environment.getDataDirectory()+ "/ShahuApp/");
                if(dir.exists()) {
                    dir.mkdirs();
                }
                localFile = new File(dir + "Exams.pdf");
                localFile.createNewFile();
                //localFile = File.createTempFile("Exams", ".pdf", dir);
                System.out.println("File:" + localFile);
            } catch (IOException e) {
                System.out.println("Exception in creating file =>" + e);
                e.printStackTrace();
            }
            final File pdffile= localFile;
            System.out.println("File URI ==>" + Uri.fromFile(pdffile));
            httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(pdffile), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    } catch (ActivityNotFoundException e) {

                        Log.v(e+"","PDF Reader application is not installed in your device");
                    } catch (Exception e) {
                        System.out.println("Exception in file access =>");
                        e.printStackTrace();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });*/

            httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }
}
