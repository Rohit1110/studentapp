package com.resoneuronance.shahucetcell;


import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Date;

import adapter.CustomAdapter;
import model.Notice;
import model.Sprofile;

import static android.content.ContentValues.TAG;


/**
 * Created by Rohit on 11/22/2017.
 */

public class Student_Inbox extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String Roll;
    private ArrayList<Notice> notices;
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_inbox, container, false);
        list=(ListView)rootView.findViewById(R.id.listnotice);
        return rootView;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sprofile sprofile=new Sprofile();




        final ListenerRegistration docRef = db.collection("Students").document("1601007").collection("Notices")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                         notices = new ArrayList<Notice>();
                        for (DocumentSnapshot doc : documentSnapshots) {
                            Notice notice = new Notice();
                            notice.setMessage(doc.getString("message"));
                            notice.setDate(doc.getDate("createdDate"));
                            notices.add(notice);
                        }
                        /*list.setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, notices));*/
                        CustomAdapter adapter = new CustomAdapter(getActivity(), notices);
                        list.setAdapter(adapter);
                    }
                });


    }


}
