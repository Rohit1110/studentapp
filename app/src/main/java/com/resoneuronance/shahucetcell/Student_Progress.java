package com.resoneuronance.shahucetcell;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
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
import java.util.ArrayList;

import adapter.ExamAdapter;
import adapter.ProgressAdapter;
import model.Exam;
import utils.Utility;

import static android.content.ContentValues.TAG;

/**
 * Created by Rohit on 11/22/2017.
 */

public class Student_Progress extends Fragment {
    ProgressDialog proDialog;
    String phone;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    StorageReference httpsReference;
    private ArrayList<Exam> exams;
    //private ListView list;
    String imageUrl;
    ImageView imageView;
    WebView web;
    private Utility utility;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_progress, container, false);
        //  list=(ListView)rootView.findViewById(R.id.listprogress);
        // imageView = (ImageView) rootView.findViewById(R.id.preport);
        web = (WebView) rootView.findViewById(R.id.webview);
        return rootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            /*utility = new Utility();
            proDialog = new ProgressDialog(getContext());
            proDialog.setMessage("please wait....");
            proDialog.setCancelable(false);


            SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
            String roll = preferences.getString("rollno", "");
            System.out.println("################ ROLL NO IN PROGRESS :" + roll);
            if (roll != null && roll.trim().length() > 0) {
                DocumentReference progressRef = db.collection("students").document(roll);
                if (progressRef == null || progressRef.getId() == null) {
                    utility.createAlert(getContext(), "Progress result not found");
                    return;
                }
                // proDialog.show();
                final ListenerRegistration docRef = progressRef.collection("ProgressReport")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {


                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                // proDialog.dismiss();
                                if (e != null) {
                                    Log.w(TAG, "Listen failed.", e);
                                    return;
                                }
                                if (documentSnapshots == null || documentSnapshots.size() == 0) {
                                    utility.createAlert(getActivity(), "Progress not found");
                                    return;
                                }

                                // exams = new ArrayList<Exam>();
                                for (DocumentSnapshot doc : documentSnapshots) {
                            *//*Exam exam = new Exam();
                          exam.setProgressReport(doc.getString("Progressreport"));
                            exams.add(exam);
*//*
                                    imageUrl = doc.getString("Progressreport");


                               *//* Glide.with(getContext())
                                        .load(imageUrl)

                                        .into(imageView);*//*
                                    web.getSettings().setBuiltInZoomControls(true);
                                    web.loadUrl(imageUrl);
                                }
                        *//*list.setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, notices));*//*
//                        ProgressAdapter adapter = new ProgressAdapter(getActivity(), exams);
//                        list.setAdapter(adapter);

                            }
                        });
            } else {
                utility.createAlert(getContext(), "progress not found");
            }*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        utility = new Utility();
        proDialog = new ProgressDialog(getContext());
        proDialog.setMessage("please wait....");
        proDialog.setCancelable(false);


        SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String roll = preferences.getString("rollno", "");

        System.out.println("################ ROLL NO IN PROGRESS :" + roll);
        if (Utility.isInternetOn(getActivity())) {
            if (roll != null && roll.trim().length() > 0) {
                DocumentReference progressRef = db.collection("students").document(roll);
                if (progressRef == null || progressRef.getId() == null) {
                    utility.createAlert(getContext(), "Progress result not found");
                    return;
                }
                // proDialog.show();
                final ListenerRegistration docRef = progressRef.collection("ProgressReport")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {


                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                // proDialog.dismiss();
                                if (e != null) {
                                    Log.w(TAG, "Listen failed.", e);
                                    return;
                                }
                                if (documentSnapshots == null || documentSnapshots.size() == 0) {
                                    //utility.createAlert(getActivity(), "Progress not found");
                                    return;
                                }
                                // exams = new ArrayList<Exam>();
                                for (DocumentSnapshot doc : documentSnapshots) {
                                    imageUrl = doc.getString("Progressreport");
                                }
                                httpsReference = storage.getReferenceFromUrl(imageUrl);

                                System.out.println("Reference is created !!" + httpsReference);

                                //File dir = new File(Environment.getDataDirectory() + "/ShahuApp/");
                                final File logFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Exams");
                                if (!logFolder.exists()) {
                                    logFolder.mkdirs();
                                    System.out.println("Directory created ...");
                                }

                                final File pdfFile = new File(logFolder, "progressReport.html");

                                httpsReference.getFile(pdfFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        web.getSettings().setBuiltInZoomControls(true);
                                        web.loadUrl(Uri.fromFile(pdfFile).toString());

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors

                                        System.out.println("Exception====> " + exception);
                                        exception.printStackTrace();
                                    }
                                });


                            }
                        });
            }

        } else {
            final File logFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Exams");

            final File pdfFile = new File(logFolder, "progressReport.html");
            web.getSettings().setBuiltInZoomControls(true);
            web.loadUrl(Uri.fromFile(pdfFile).toString());
        }


    }
}

