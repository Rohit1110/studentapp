package com.resoneuronance.shahucetcell;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import model.Sprofile;
import utils.Utility;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Rohit on 11/22/2017.
 */


public class Student_Profile extends Fragment {
    ProgressDialog proDialog;

    TextView name, scontact, category, pcontact, sclass, roll, ppercent;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Sprofile sprofile;
    String phone;
    private Utility utility;
    ImageView profilep;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_profile__student, container, false);
        getActivity().setTitle("Student Profile");


        name = (TextView)rootView.findViewById(R.id.sname);
        roll = (TextView)rootView.findViewById(R.id.sroll);
        scontact = (TextView)rootView.findViewById(R.id.scontact);
        category = (TextView)rootView.findViewById(R.id.scatagory);
        pcontact = (TextView)rootView.findViewById(R.id.pcontact);
        sclass = (TextView) rootView.findViewById(R.id.scalss);
        ppercent = (TextView)rootView.findViewById(R.id.spercent);
        profilep=(ImageView)rootView.findViewById(R.id.profilephoto);


        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utility = new Utility();
        proDialog = new ProgressDialog(getContext());
        proDialog.setMessage("please wait....");
        proDialog.setCancelable(false);
        proDialog.show();

        SharedPreferences sp = this.getActivity().getSharedPreferences("user", 0);
        phone = sp.getString("userphone", "");
        Log.v("SSSS", phone);


        db.collection("students")
                .whereEqualTo("StudentContact", phone)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      proDialog.dismiss();
                        if (task.isSuccessful()) {

                            if (task.getResult() == null || task.getResult().size() == 0) {
                                //utility.createAlert(getContext(), "data not found");
                                return;
                            }

                            for (DocumentSnapshot document : task.getResult()) {

                                Log.d("Data", document.getId() + " => " + document.getData());
                                Log.d("Name", document.getId() + " => " + document.getData().get("Name"));

                                String rollno = document.getData().get("RollNo").toString();
                                if (rollno == null || rollno.trim().length() == 0) {
                                    //utility.createAlert(getContext(), "data not found");
                                    return;
                                }
                                String name = null, scategory = null, sclass = null, div = null, parentcontact = null, studentcontact = null, PrePercent = null,profilephoto=null;
                                if (document.getData().get("Name") != null) {
                                    name = document.getData().get("Name").toString();
                                }
                                if (document.getData().get("Category") != null) {
                                    scategory = document.getData().get("Category").toString();
                                }
                                if (document.getData().get("Class") != null) {
                                    sclass = document.getData().get("Class").toString();
                                }
                                if (document.getData().get("Div") != null) {
                                    div = document.getData().get("Div").toString();
                                }
                                if (document.getData().get("PrePercent") != null) {
                                    PrePercent = document.getData().get("PrePercent").toString();
                                }
                                if (document.getData().get("StudentContact") != null) {
                                    studentcontact = document.getData().get("StudentContact").toString();
                                }
                                if (document.getData().get("ParentContact") != null) {
                                    parentcontact = document.getData().get("ParentContact").toString();
                                }
                                if (document.getData().get("Image") != null) {
                                    profilephoto = document.getData().get("Image").toString();
                                }

                                SharedPreferences preferences = getActivity().getSharedPreferences("pref", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("rollno", document.getData().get("RollNo").toString());
                                editor.commit();

                                sprofile = new Sprofile(studentcontact, scategory, PrePercent, parentcontact, sclass, rollno, name, div,profilephoto);
                                writeToFile(document);

                            }

                            updatetext();


                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());

                           String read= readFromFile();
                           Toast.makeText(getContext(),read,Toast.LENGTH_LONG).show();
                        }
                    }

                    private void updatetext() {

                        name.setText(sprofile.getName());
                        roll.setText(sprofile.getRollNo());
                        category.setText(sprofile.getCategory());
                        sclass.setText(sprofile.getStudclass() + " " + sprofile.getDiv());
                        ppercent.setText(sprofile.getPrePercent());
                       pcontact.setText(sprofile.getParentContact());
                        scontact.setText(sprofile.getStudentContact());
                        Glide.with(getActivity())
                                .load(sprofile.getProfile())
                                .override(300, 200)
                                .into(profilep);


                    }
                });
    }

    private void writeToFile(DocumentSnapshot data) {

        try {

            FileOutputStream fileout=getContext().openFileOutput(sprofile.getRollNo()+".txt",MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(data.getData().toString());
            outputWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = getContext().openFileInput("profile.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


}

