package com.resoneuronance.shahucetcell;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        web=(WebView)rootView.findViewById(R.id.webview);
        return rootView;
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
        if (roll != null && roll.trim().length() > 0) {
            DocumentReference progressRef = db.collection("Students").document(roll);
            if (progressRef == null || progressRef.getId() == null) {
                utility.createAlert(getContext(), "Exam result not found");
                return;
            }
            proDialog.show();
            final ListenerRegistration docRef = progressRef.collection("Exams")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {


                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            proDialog.dismiss();
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }

                            exams = new ArrayList<Exam>();
                            for (DocumentSnapshot doc : documentSnapshots) {
                            /*Exam exam = new Exam();
                          exam.setProgressReport(doc.getString("Progressreport"));
                            exams.add(exam);
*/
                                imageUrl = doc.getString("Progressreport");


                               /* Glide.with(getContext())
                                        .load(imageUrl)

                                        .into(imageView);*/
                               web.getSettings().setBuiltInZoomControls(true);
                               web.loadUrl(imageUrl);
                            }
                        /*list.setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, notices));*/
//                        ProgressAdapter adapter = new ProgressAdapter(getActivity(), exams);
//                        list.setAdapter(adapter);

                        }
                    });
        } else {
            utility.createAlert(getContext(), "progress not found");
        }


    }
}
