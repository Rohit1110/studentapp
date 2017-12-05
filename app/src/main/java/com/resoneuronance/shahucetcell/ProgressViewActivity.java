package com.resoneuronance.shahucetcell;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import model.Exam;
import utils.Utility;

import static android.content.ContentValues.TAG;

public class ProgressViewActivity extends AppCompatActivity {
    ProgressDialog proDialog;
    private String url;
    String phone;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Exam> exams;
    //private ListView list;
    String imageUrl;
    ImageView imageView;
    WebView web;
    private Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_view);
        web = (WebView) findViewById(R.id.webview);
        Bundle bundle = getIntent().getExtras();//Extract the data…
        if (bundle != null) {
            url = bundle.getString("url");
            //Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();

            utility = new Utility();
            proDialog = new ProgressDialog(getApplicationContext());
            proDialog.setMessage("please wait....");
            proDialog.setCancelable(false);


            SharedPreferences preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
            String roll = preferences.getString("rollno", "");
            System.out.println("################ ROLL NO IN PROGRESS :" + roll);
            if (roll != null && roll.trim().length() > 0) {
                DocumentReference progressRef = db.collection("Students").document(roll);
                if (progressRef == null || progressRef.getId() == null) {
                    utility.createAlert(getApplicationContext(), "Exam result not found");
                    return;
                }
                // proDialog.show();
                final ListenerRegistration docRef = progressRef.collection("Exams")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {


                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                // proDialog.dismiss();
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
                                   // imageUrl = doc.getString("Progressreport");


                               /* Glide.with(getContext())
                                        .load(imageUrl)

                                        .into(imageView);*/
                                    web.getSettings().setBuiltInZoomControls(true);
                                    web.loadUrl(url);
                                }
                        /*list.setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, notices));*/
//                        ProgressAdapter adapter = new ProgressAdapter(getActivity(), exams);
//                        list.setAdapter(adapter);

                            }
                        });
            } else {
                utility.createAlert(getApplicationContext(), "progress not found");
            }


        }

    }
}
