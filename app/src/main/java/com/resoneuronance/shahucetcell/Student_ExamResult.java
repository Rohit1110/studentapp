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
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.resoneuronance.shahucetcell.R;

import java.util.ArrayList;

import adapter.CustomAdapter;
import adapter.ExamAdapter;
import model.Exam;
import model.Notice;
import model.Sprofile;

import static android.content.ContentValues.TAG;

/**
 * Created by Rohit on 11/22/2017.
 */

public class Student_ExamResult extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Exam> exams;
    private ListView list;
    ProgressDialog proDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_examresult, container, false);
        list=(ListView)rootView.findViewById(R.id.listexam);
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
                            Exam exam = new Exam();
                            exam.setTestName(doc.getString("TestName"));
                            exam.setOMR("OMR");
                            exam.setOMR("PSolution");
                            exams.add(exam);

                        }
                        /*list.setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, notices));*/
                        ExamAdapter adapter = new ExamAdapter(getActivity(), exams);
                        list.setAdapter(adapter);
                    }
                });


    }
}
