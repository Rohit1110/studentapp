package com.resoneuronance.shahucetcell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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

import utils.Utility;

import static android.content.ContentValues.TAG;

public class CorrectkeyShow extends AppCompatActivity {

    private String imgUrl, examId;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //StorageReference storageRef = storage.getReference();
    StorageReference httpsReference;
    private ListenerRegistration docRef;
    ProgressDialog proDialog;
    String examname;
     WebView web;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correctkey_show);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.stausbar));
        }
        proDialog = new ProgressDialog(CorrectkeyShow.this);
        proDialog.setMessage("please wait....");
        //proDialog.setCancelable(false);
        proDialog.show();
        Bundle bundle = getIntent().getExtras();//Extract the data…
        if (bundle != null) {

            examId = bundle.getString("examid");
            System.out.println("Exam id = "+examId);
            examname=bundle.getString("testname");

            img = (ImageView) findViewById(R.id.correctkey);
            web=(WebView)findViewById(R.id.correctke);
        }
            docRef = db.collection("exams").document(examId)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                          proDialog.dismiss();
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }


                            imgUrl = documentSnapshot.getString("correctedAnswerFileUrl");
                            System.out.println("Exam url = "+imgUrl);
                            if(Utility.isInternetOn(CorrectkeyShow.this)) {

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

                                final File pdfFile = new File(logFolder, examname + "qanalysis.html");
                                //final File pdfFile=xmlFile;
                                System.out.println("File PDF===>>>" + pdfFile);
                                httpsReference.getFile(pdfFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {


                                        // System.out.println("OnSuccess=>> " + xmlFile.getAbsolutePath());

                    /*Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //proDialog.dismiss();
                    finish();
            */


                                        /*Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(pdfFile), "image*//*");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();*/
                                        web.getSettings().setBuiltInZoomControls(true);
                                        web.loadUrl(Uri.fromFile(pdfFile).toString());


                                    }




                                });
                            }else {
                                //proDialog.dismiss();
                                final File logFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Exams");

                                final File pdfFile = new File(logFolder, examname+"qanalysis.html");

                               /* Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(pdfFile), "image*//*");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();*/
                                web.getSettings().setBuiltInZoomControls(true);
                                web.loadUrl(Uri.fromFile(pdfFile).toString());
                            }
                        }
                    });
        }
    }

