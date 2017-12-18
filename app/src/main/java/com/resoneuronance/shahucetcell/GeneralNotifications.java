package com.resoneuronance.shahucetcell;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Date;

import adapter.CustomAdapter;
import adapter.GeneralNoticeAdapter;
import model.GeneralNotice;
import model.Notice;
import model.Sprofile;
import utils.Utility;

import static android.content.ContentValues.TAG;


/**
 * Created by Rohit on 11/22/2017.
 */

public class GeneralNotifications extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String Roll;
    private ArrayList<GeneralNotice> notices;
    private ListView list;
    ProgressDialog proDialog;
    private ListenerRegistration docRef;
    private String roll;
    private Utility utility;
    TextView txtnodata;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.general_notification, container, false);
        list = (ListView) rootView.findViewById(R.id.listgeneral);
        txtnodata=(TextView)rootView.findViewById(R.id.generalNodata);

        return rootView;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createListener();



    }

    @Override
    public void onStart() {
        super.onStart();

       // createListener();
    }

    @Override
    public void onResume() {
        super.onResume();
      createListener();
    }

    private void createListener() {

        utility = new Utility();

        proDialog = new ProgressDialog(getContext());
        proDialog.setMessage("please wait....");
        proDialog.setCancelable(false);



        SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        roll = preferences.getString("rollno", "");


        System.out.println("################ ROLL NO IN INBOX:" + roll);

        if (roll != null && roll.trim().length() > 0) {

            if (docRef == null && roll != null && roll.trim().length() > 0) {
               // proDialog.show();
                try {

                    docRef = db.collection("GeneralNotices").orderBy("createdDate", Query.Direction.DESCENDING)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {

                                @Override
                                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                    //proDialog.dismiss();
                                    if (e != null) {
                                        Log.w(TAG, "Listen failed.", e);
                                        return;
                                    }
                                    if (documentSnapshots == null || documentSnapshots.size() == 0) {
                                        //utility.createAlert(getActivity(), "Inbox not found");
                                        Log.d("Both Null","Null Data");
                                        txtnodata.setText("No notices");



                                        return;
                                    }

                                    notices = new ArrayList<GeneralNotice>();
                                    for (DocumentSnapshot doc : documentSnapshots) {
                                        Log.d("Data", doc.getId() + " => " + doc.getData());
                                        Log.d("Name", doc.getId() + " => " + doc.getData().get("message"));
                                       GeneralNotice notice = new GeneralNotice();
                                        notice.setMessage(doc.getString("message"));

                                        notice.setDate(doc.getDate("createdDate"));
                                        //Toast.makeText(getContext(),doc.getDate("createdDate").toString(),Toast.LENGTH_LONG).show();
                                        notices.add(notice);
                                    }
                        /*list.setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, notices));*/
                                    GeneralNoticeAdapter adapter = new GeneralNoticeAdapter(getActivity(), notices);
                                    list.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

                                }
                            });

                } catch (Exception e) {
                    System.out.println("### ERROR IN INBOX =>" + e);
                    //proDialog.dismiss();
                }

            } else {
                // utility.createAlert(getContext(), "notification not found");
                //proDialog.dismiss();
            }

        } else {
           proDialog.dismiss();
        }
    }



}
