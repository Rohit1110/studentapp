package com.resoneuronance.shahucetcell;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import adapter.CustomAdapter;
import model.Notice;
import utils.Utility;

import static android.content.ContentValues.TAG;

public class PDfViewer extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //StorageReference storageRef = storage.getReference();
    StorageReference httpsReference;
    private ListenerRegistration docRef;
    ProgressDialog proDialog;
    String examId, Url;
    ImageView img;
    String examname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        img = (ImageView) findViewById(R.id.temp);
        Bundle bundle = getIntent().getExtras();//Extract the data…
        if (bundle != null) {

            examId = bundle.getString("examid");
            examname=bundle.getString("testname");
            System.out.println("Exam Id" + examId+"  "+examname);

        }
        if(Utility.isInternetOn(PDfViewer.this)) {

            docRef = db.collection("exams").document(examId)

                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                            try {
                                if (e != null) {
                                    Log.w(TAG, "Listen failed.", e);
                                    return;
                                }

                                System.out.println("Data For PDF +>>" + documentSnapshot.getData());
                                Url = documentSnapshot.getString("paperSolutionExamFileUrl");
                                System.out.println("Url ==>> " + Url);


                                httpsReference = storage.getReferenceFromUrl(Url);

                                System.out.println("Reference is created !!" + httpsReference);

                                //File dir = new File(Environment.getDataDirectory() + "/ShahuApp/");
                                final File logFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Exams");
                                if (!logFolder.exists()) {
                                    logFolder.mkdirs();
                                    System.out.println("Directory created ...");
                                }

                                final File pdfFile = new File(logFolder, examname+".pdf");

                                httpsReference.getFile(pdfFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                        System.out.println("OnSuccess=>> " + pdfFile.getAbsolutePath());
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors

                                        System.out.println("Exception====> " + exception);
                                        exception.printStackTrace();
                                    }
                                });
                            } catch (Exception error) {
                                System.out.println("Exception in HTTPS Reference =>" + error);
                                error.printStackTrace();
                            }


                        }
                    });
        }else {
            final File logFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Exams");

            final File pdfFile = new File(logFolder, examname+".pdf");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


    }
}
