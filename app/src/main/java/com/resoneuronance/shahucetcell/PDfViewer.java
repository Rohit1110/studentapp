package com.resoneuronance.shahucetcell;

import android.app.ProgressDialog;
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
import java.io.IOException;
import java.util.ArrayList;

import adapter.CustomAdapter;
import model.Notice;

import static android.content.ContentValues.TAG;

public class PDfViewer extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //StorageReference storageRef = storage.getReference();
    StorageReference httpsReference;
    private ListenerRegistration docRef;
    ProgressDialog proDialog;
    String examId, Url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        Bundle bundle = getIntent().getExtras();//Extract the data…
        if (bundle != null) {

            examId = bundle.getString("examid");
            System.out.println("Exam Id" + examId);

            docRef = db.collection("exams").document(examId)

                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }

                            System.out.println("Data For PDF +>>" + documentSnapshot.getData());
                            Url = documentSnapshot.getString("paperSolutionExamFileUrl");
                            System.out.println("Url ==>> " + Url);


                            httpsReference = storage.getReferenceFromUrl(Url);

                            /*httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                            });*/

                            File localFile = null;
                            try {
                                File dir = new File(Environment.getDataDirectory() + "/ShahuApp");
                                if (!dir.exists()) {
                                    dir.mkdirs();

                                }

                                localFile = File.createTempFile("Exams", "pdf", dir);
                                System.out.println("File U:" + localFile);


                            } catch (IOException ioe) {
                                System.out.println("Exception in creating file =>" + ioe);
                                ioe.printStackTrace();
                            }
                            final File pdffile = localFile;
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

                                        Log.v(e + "", "PDF Reader application is not installed in your device");
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
                            });


                        }
                    });



















            /**/


        }
    }
}
