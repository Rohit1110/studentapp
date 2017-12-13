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


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.resoneuronance.shahucetcell.R;

import java.util.ArrayList;

import adapter.CustomAdapter;
import adapter.ExamAdapter;
import model.Exam;
import model.Notice;
import model.Sprofile;
import utils.Utility;

import static android.content.ContentValues.TAG;

/**
 * Created by Rohit on 11/22/2017.
 */

public class Student_ExamResult extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Exam> exams;
    private ListView list;
    ProgressDialog proDialog;
    private Utility utility;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_examresult, container, false);
        list = (ListView) rootView.findViewById(R.id.listexam);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){

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


        if (roll != null && roll.trim().length() > 0) {
            DocumentReference studentsRef = db.collection("students").document(roll);

            if (studentsRef == null || studentsRef.getId() == null) {
                utility.createAlert(getContext(), "Exam  not found");
                return;
            }
            proDialog.show();
            if (studentsRef == null || studentsRef.getId() != null) {
                final ListenerRegistration docRef = studentsRef.collection("Exams").orderBy("dateAdded", Query.Direction.DESCENDING)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {

                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                 proDialog.dismiss();
                                if (e != null) {
                                    Log.w(TAG, "Listen failed.", e);
                                    proDialog.dismiss();
                                    return;
                                }
                                if (documentSnapshots == null || documentSnapshots.size() == 0) {
                                    //utility.createAlert(getActivity(), "Exam result not found");
                                    return;
                                }
                                exams = new ArrayList<Exam>();
                                for (DocumentSnapshot doc : documentSnapshots) {
                                    Log.d("Data", doc.getId() + " => " + doc.getData());
                                    Exam exam = new Exam();
                                    exam.setTestName(doc.getString("TestName"));
                                    exam.setOMR(doc.getString("OMR"));
                                    //exam.setPSolution(doc.getString("PSolution"));
                                    exam.setProgressReport(doc.getString("Progressreport"));
                                    exam.setAnalysis(doc.getString("Analysis"));
                                    //exam.setCorrectkey(doc.getString("correctedAnswerFileUrl"));
                                    exam.setExamid(doc.getString("examID"));
                                    exams.add(exam);

                                }
                        /*list.setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, notices));*/
                                ExamAdapter adapter = new ExamAdapter(getActivity(), exams);
                                list.setAdapter(adapter);
                                proDialog.dismiss();
                            }
                        });
            }


        }else{
            //utility.createAlert(getContext(),"Exam not found");
        }

    }
}
