package com.resoneuronance.shahucetcell;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

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
import utils.PermissionUtil;
import utils.Utility;

import static android.content.ContentValues.TAG;

public class PDfViewer extends AppCompatActivity  implements ActivityCompat.OnRequestPermissionsResultCallback {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //StorageReference storageRef = storage.getReference();
    StorageReference httpsReference;
    private ListenerRegistration docRef;
    ProgressDialog proDialog;
    String examId, Url;
    //ImageView img;
    String examname;
    private View mLayout;

    private static final int REQUEST_CAMERA = 0;
    private static String[] PERMISSIONS_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_pdf_viewer);
       // img = (ImageView) findViewById(R.id.temp);
        // Here, thisActivity is the current activity

        isStoragePermissionGranted();

        Bundle bundle = getIntent().getExtras();//Extract the data…
        if (bundle != null) {

            examId = bundle.getString("examid");
            examname=bundle.getString("testname");
            System.out.println("Exam Id" + examId+"  "+examname);
            proDialog = new ProgressDialog(PDfViewer.this);
            proDialog.setMessage("please wait....");
            proDialog.setCancelable(false);
            proDialog.show();

        }
       if(Utility.isInternetOn(PDfViewer.this)) {
           // requeststoragepermission();


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
                                System.out.println("Url paper ==>> " + Url);


                                httpsReference = storage.getReferenceFromUrl(Url);

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

                                final File pdfFile = new File(logFolder, examname+".pdf");
                              //final File pdfFile=xmlFile;
                                System.out.println("File PDF===>>>"+ pdfFile);
                                httpsReference.getFile(pdfFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        proDialog.hide();

                                       // System.out.println("OnSuccess=>> " + xmlFile.getAbsolutePath());
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();

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
           proDialog.hide();
     }else {
           proDialog.hide();
           final File logFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Exams");

            final File pdfFile = new File(logFolder, examname+".pdf");

            System.out.println("File PDF===>>>"+ pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}
