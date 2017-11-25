package com.resoneuronance.shahucetcell;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import model.Sprofile;

import static android.content.ContentValues.TAG;


/**
 * Created by Rohit on 11/22/2017.
 */




public class Student_Profile extends Fragment {
    ProgressDialog proDialog;

   EditText name,scontact,category,pcontact,sclass,roll,ppercent;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Sprofile sprofile;
    String phone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_profile, container, false);



        name=(EditText)rootView.findViewById(R.id.sname);
        roll=(EditText)rootView.findViewById(R.id.sroll);
        scontact=(EditText)rootView.findViewById(R.id.scontact);
        category=(EditText)rootView.findViewById(R.id.scatagory);
        pcontact=(EditText)rootView.findViewById(R.id.pcontact);
        sclass=(EditText)rootView.findViewById(R.id.scalss);
        ppercent=(EditText)rootView.findViewById(R.id.spercent);



        return rootView;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        proDialog = new ProgressDialog(getContext());
        proDialog.setMessage("please wait....");
        proDialog.setCancelable(false);
        proDialog.show();

        SharedPreferences sp=this.getActivity().getSharedPreferences("user",0);
        phone=sp.getString("userphone","");
        Log.v("SSSS",phone);



        db.collection("Students")
                .whereEqualTo("StudentContact", phone)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        proDialog.dismiss();
                        if (task.isSuccessful()) {

                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("Data", document.getId() + " => " + document.getData());
                                Log.d("Name", document.getId() + " => " + document.getData().get("Name"));
                                String name=document.getData().get("Name").toString();
                                String category=document.getData().get("Category").toString();
                                String sclass =document.getData().get("Class").toString();
                                String div=document.getData().get("Div").toString();
                                String parentcontact=document.getData().get("ParentContact").toString();
                                String studentcontact=document.getData().get("StudentContact").toString();
                                String rollno=document.getData().get("RollNo").toString();
                                String  PrePercent=document.getData().get("PrePercent").toString();

                                SharedPreferences preferences = getActivity().getSharedPreferences("pref", 0);
                                SharedPreferences.Editor editor=preferences.edit();
                                editor.putString("rollno",document.getData().get("RollNo").toString());
                                editor.commit();

                                sprofile=new  Sprofile(studentcontact,category,PrePercent,parentcontact,sclass,rollno,name,div);





                            }
                            updatetext();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                    private void updatetext() {

                      name.setText(sprofile.getName());
                        roll.setText(sprofile.getRollNo());
                        category.setText(sprofile.getCategory());
                        sclass.setText(sprofile.getStudclass()+" "+sprofile.getDiv());
                        ppercent.setText(sprofile.getPrePercent());
                        pcontact.setText(sprofile.getParentContact());
                        scontact.setText(sprofile.getStudentContact());

                    }
                });
    }



}

