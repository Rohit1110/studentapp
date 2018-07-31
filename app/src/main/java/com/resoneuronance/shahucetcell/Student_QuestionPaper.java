package com.resoneuronance.shahucetcell;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


import adapter.QuestionPaperAdapter;
import model.QuetionPaper;
import utils.Utility;

import static android.content.ContentValues.TAG;

/**
 * Created by Rohit on 4/9/2018.
 */

public class Student_QuestionPaper extends Fragment {
    private ListView list;
    private TextView txtnodata;
    private Utility utility;
    ProgressDialog proDialog;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //StorageReference storageRef = storage.getReference();
    StorageReference httpsReference;
    private ListenerRegistration docRef;
    private ArrayList<QuetionPaper> qpaper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.question_paper_layout, container, false);
        getActivity().setTitle(Html.fromHtml("<font color='#ffffff'>Download Section</font>"));
        list = (ListView) rootView.findViewById(R.id.exam_name_list);
        txtnodata=(TextView)rootView.findViewById(R.id.papernodata);
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
        String stud_class = preferences.getString("sclass", "");
        String roll = preferences.getString("rollno", "");
        System.out.println("classss....."+stud_class + roll);

        //if (Utility.isInternetOn(getActivity())) {
            proDialog.show();
            System.out.println("size documentSnapshots");
            final ListenerRegistration docRef = db.collection("downloadSectionDocs").whereEqualTo("documentClass",stud_class).orderBy("timestamp", Query.Direction.DESCENDING)
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
                                txtnodata.setText("No Downloads Found");
                                return;
                            }
                            qpaper=new ArrayList<QuetionPaper>();
                            QuetionPaper quetionPaper=null;
                            for (DocumentSnapshot doc : documentSnapshots) {

                                quetionPaper=new QuetionPaper();


                                    Log.d("Data", doc.getId() + " => " + doc.getData());

                                    if (doc.getString("documentName") != null)
                                        quetionPaper.setDocumentName(doc.getString("documentName"));
                                    if (doc.getString("documentClass") != null)
                                        quetionPaper.setDocumentClass(doc.getString("documentClass"));
                                    //exam.setPSolution(doc.getString("PSolution"));
                                    if(doc.getString("downloadPdfDocFileUrl")!=null)
                                        quetionPaper.setDownloadPdfDocFileUrl(doc.getString("downloadPdfDocFileUrl"));
                                if(doc.getDate("timestamp")!=null)
                                    quetionPaper.setTimestamp(doc.getDate("timestamp"));

                                    qpaper.add(quetionPaper);
                                }


                                     proDialog.dismiss();
                            QuestionPaperAdapter adapter = new QuestionPaperAdapter(getActivity(), qpaper);
                            list.setAdapter(adapter);
                            //adapter.notifyDataSetChanged();
                            proDialog.dismiss();


                        }
                    });






       // }


    }
}
