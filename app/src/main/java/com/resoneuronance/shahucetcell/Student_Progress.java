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
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_progress, container, false);
      //  list=(ListView)rootView.findViewById(R.id.listprogress);
        imageView=(ImageView)rootView.findViewById(R.id.preport);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        proDialog = new ProgressDialog(getContext());
        proDialog.setMessage("please wait....");
        proDialog.setCancelable(false);
        proDialog.show();

        SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String roll=preferences.getString("rollno","");

        final ListenerRegistration docRef = db.collection("Students").document(roll).collection("Exams")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        proDialog.dismiss();
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        exams= new ArrayList<Exam>();
                        for (DocumentSnapshot doc : documentSnapshots) {
                            /*Exam exam = new Exam();
                          exam.setProgressReport(doc.getString("Progressreport"));
                            exams.add(exam);
*/
                            imageUrl=doc.getString("Progressreport");


                            Glide.with(getContext())
                                    .load(imageUrl)

                                    .into(imageView);
                        }
                        /*list.setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, notices));*/
//                        ProgressAdapter adapter = new ProgressAdapter(getActivity(), exams);
//                        list.setAdapter(adapter);

                    }
                });


    }
}
