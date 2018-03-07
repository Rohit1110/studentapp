package com.resoneuronance.shahucetcell;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import utils.PermissionUtil;
import utils.Utility;

import static android.content.ContentValues.TAG;

public class TimeTableview extends AppCompatActivity  implements ActivityCompat.OnRequestPermissionsResultCallback {
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
            /*Log.i(TAG, "Contacts permissions were NOT granted.");
            Snackbar.make(mLayout, "permissions were NOT granted",
                    Snackbar.LENGTH_SHORT)
                    .show();*/
            //Toast.makeText(PDfViewer.this,"Permission not Grant",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_pdf_viewer);
        // img = (ImageView) findViewById(R.id.temp);
        // Here, thisActivity is the current activity
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.stausbar));
        }

        isStoragePermissionGranted();

        Bundle bundle = getIntent().getExtras();//Extract the data…
        /*if (bundle != null) {

           *//* examId = bundle.getString("examid");
            examname = bundle.getString("testname");
            System.out.println("Exam Id" + examId + "  " + examname);*//*



        }*/
        proDialog = new ProgressDialog(TimeTableview.this);
        proDialog.setMessage("please wait .....");
        proDialog.setCancelable(false);
        proDialog.show();
        if (Utility.isInternetOn(TimeTableview.this)) {
            // requeststoragepermission();


            docRef = db.collection("Timetable").document("sFrBYuIYXl7X8DDsYmMC")

                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {

                        @Override
                        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                            //proDialog.dismiss();

                            try {
                                if (e != null) {
                                    Log.w(TAG, "Listen failed.", e);
                                    return;
                                }

                                System.out.println("Data For PDF +>>" + documentSnapshot.getData());
                                Url = documentSnapshot.getString("examtimetable");
                                System.out.println("Url paper ==>> " + Url);
                                //Toast.makeText(PDfViewer.this, "Url Pdf " + Url, Toast.LENGTH_SHORT).show();

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

                                final File pdfFile = new File(logFolder, "timetable.pdf");
                                //final File pdfFile=xmlFile;
                                System.out.println("File PDF===>>>" + pdfFile);
                                //Toast.makeText(PDfViewer.this, "File Path " + pdfFile, Toast.LENGTH_LONG).show();
                                httpsReference.getFile(pdfFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {


                                        // create new Intent
                                        Intent intent = new Intent(Intent.ACTION_VIEW);

// set flag to give temporary permission to external app to use your FileProvider
                                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

// generate URI, I defined authority as the application ID in the Manifest, the last param is file I want to open


// I am opening a PDF file so I give it a valid MIME type
                                        intent.setDataAndType(FileProvider.getUriForFile(TimeTableview.this, BuildConfig.APPLICATION_ID+".provider", pdfFile), "application/pdf");

// validate that the device can open your File!
                                        PackageManager pm = getPackageManager();
                                        if (intent.resolveActivity(pm) != null) {
                                            startActivity(intent);
                                            finish();

                                        }

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
            // proDialog.dismiss();
        } else {
            proDialog.dismiss();
            final File logFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Exams");

            final File pdfFile = new File(logFolder,  "timetable.pdf");

            System.out.println("File PDF===>>>" + pdfFile);

            // create new Intent
            Intent intent = new Intent(Intent.ACTION_VIEW);

// set flag to give temporary permission to external app to use your FileProvider
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

// generate URI, I defined authority as the application ID in the Manifest, the last param is file I want to open


// I am opening a PDF file so I give it a valid MIME type
            intent.setDataAndType(FileProvider.getUriForFile(TimeTableview.this, BuildConfig.APPLICATION_ID+".provider", pdfFile), "application/pdf");

// validate that the device can open your File!
            PackageManager pm = getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                startActivity(intent);
                finish();

            }

        }
    }



    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED|| checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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
