package com.resoneuronance.shahucetcell;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import model.Notice;
import model.Sprofile;
import utils.Utility;

import static android.content.ContentValues.TAG;


/**
 * Created by Rohit on 11/22/2017.
 */

public class Student_Inbox extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String Roll;
    private ArrayList<Notice> notices;
    private ListView list;
    TextView txtnodata;
    ProgressDialog proDialog;
    private ListenerRegistration docRef;
    private String roll;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_inbox, container, false);
        getActivity().setTitle(Html.fromHtml("<font color='#ffffff'>Inbox </font>"));


        list = (ListView) rootView.findViewById(R.id.listnotice);
        txtnodata=(TextView)rootView.findViewById(R.id.nodata);
        return rootView;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createListener(roll);


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            //createListener(roll);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //createListener(roll);
    }

    private void createListener(String roll) {
        //utility = new Utility();
System.out.println("Oncreate Inbox");
      proDialog = new ProgressDialog(getContext());
        proDialog.setMessage("please wait....");
        proDialog.setCancelable(false);


        SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        roll = preferences.getString("rollno", "");


        System.out.println("################ ROLL NO IN INBOX:" + roll);

        if (roll != null && roll.trim().length() > 0) {
           if (docRef == null && roll != null && roll.trim().length() > 0) {
               try {
                   proDialog.show();
                   docRef = db.collection("students").document(roll).collection("Notices").orderBy("createdDate", Query.Direction.DESCENDING)
                           .addSnapshotListener(new EventListener<QuerySnapshot>() {

                               @Override
                               public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                   System.out.println("Inside snapshot listener!!");
                                proDialog.dismiss();
                                   if (e != null) {
                                       Log.w(TAG, "Listen failed.", e);
                                       //proDialog.dismiss();
                                       return;
                                   }
                                   if (documentSnapshots == null || documentSnapshots.size() == 0) {
                                       //utility.createAlert(getActivity(), "Inbox not found");
                                       Log.d("Both Null","Null Data");
                                       txtnodata.setText("No Notices Found");



                                       return;
                                   }
                                   proDialog.dismiss();
                                   notices = new ArrayList<Notice>();
                                   Notice notice=null;
                                   for (DocumentSnapshot doc : documentSnapshots) {

                                       Log.d("Data", doc.getId() + " => " + doc.getData());
                                       Log.d("Name", doc.getId() + " => " + doc.getData().get("message"));
                                       notice = new Notice();
                                       if(doc.getString("message")!=null)
                                       {
                                           notice.setMessage(doc.getString("message"));
                                       }
                                       if(doc.getDate("createdDate")!=null) {
                                           notice.setDate(doc.getDate("createdDate"));
                                       }
                                       notices.add(notice);
                                       System.out.println("Data with inbox "+notices);
                                   }

                                   if(notices.size() == 0) {
                                       Log.d("Both Null","Notices Empty");
                                       return;
                                   }

                        /*list.setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, notices));*/
                                   CustomAdapter adapter = new CustomAdapter(getActivity(),notices);
                                   list.setAdapter(adapter);
                                   adapter.notifyDataSetChanged();

                               }
                           });

               } catch (Exception e) {
                   System.out.println("### ERROR IN INBOX =>" + e);
                  // proDialog.dismiss();
               }

           } else {
               //utility.createAlert(getContext(), "Inbox data not found");
           }

        } else {
            //utility.createAlert(getContext(), "Inbox data not found");
        }
    }

  

}
