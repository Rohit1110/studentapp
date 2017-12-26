package com.resoneuronance.shahucetcell;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rohit on 12/22/2017.
 */

public class Student_Feedback extends Fragment implements View.OnClickListener {
    Button feedback;
    EditText feedbackedit;
    String rollNo;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_feedback, container, false);
        getActivity().setTitle(Html.fromHtml("<font color='#ffffff'>Feedback </font>"));
        feedback=(Button)rootView.findViewById(R.id.feedback);
        feedbackedit=(EditText)rootView.findViewById(R.id.editfeedback);
        feedback.setOnClickListener(this);

        return  rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
         rollNo = preferences.getString("rollno", "");

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.feedback) {
            if (!feedbackedit.getText().toString().equals("") || feedbackedit.getText().toString().length() > 0) {
                 progressDialog=new ProgressDialog(getActivity());
                progressDialog.setMessage("please wait....");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Date d = new Date();
                Map<String, Object> feedback = new HashMap<>();
                feedback.put("message",feedbackedit.getText().toString() );
                feedback.put("rollNo", rollNo);
                feedback.put("createdDate",d);
                db.collection("feedbacks").document()
                        .set(feedback)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Log.d("Write Data", "DocumentSnapshot successfully written!");
                                Toast.makeText(getActivity(),"Feedback submitted",Toast.LENGTH_LONG).show();
                                feedbackedit.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Log.w("Write Data", "Error writing document", e);
                            }
                        });


            } else {
                Toast.makeText(getActivity(), "Wrire your feedback", Toast.LENGTH_LONG).show();
            }
        }

    }
}
