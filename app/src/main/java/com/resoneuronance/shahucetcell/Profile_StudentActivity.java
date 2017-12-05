package com.resoneuronance.shahucetcell;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import model.Sprofile;
import utils.Utility;

public class Profile_StudentActivity extends AppCompatActivity {
    ProgressDialog proDialog;

    EditText name, scontact, category, pcontact, sclass, roll, ppercent;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Sprofile sprofile;
    String phone;
    private Utility utility;
    ImageView profilep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__student);
           utility = new Utility();
            /*proDialog = new ProgressDialog(Profile_StudentActivity.this);
            proDialog.setMessage("please wait....");
            proDialog.setCancelable(false);
            proDialog.show();*/
        name = (EditText)findViewById(R.id.sname);
        roll = (EditText)findViewById(R.id.sroll);
        scontact = (EditText)findViewById(R.id.scontact);
        category = (EditText)findViewById(R.id.scatagory);
        pcontact = (EditText)findViewById(R.id.pcontact);
        sclass = (EditText) findViewById(R.id.scalss);
        ppercent = (EditText)findViewById(R.id.spercent);
        profilep=(ImageView)findViewById(R.id.profilephoto);

            SharedPreferences sp = getSharedPreferences("user", 0);
            phone = sp.getString("userphone", "");
            Log.v("SSSS", phone);


            db.collection("Students")
                    .whereEqualTo("StudentContact", phone)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            //proDialog.dismiss();
                            if (task.isSuccessful()) {

                                if (task.getResult() == null || task.getResult().size() == 0) {
                                    utility.createAlert(Profile_StudentActivity.this, "data not found");
                                    return;
                                }

                                for (DocumentSnapshot document : task.getResult()) {

                                    Log.d("Data", document.getId() + " => " + document.getData());
                                    Log.d("Name", document.getId() + " => " + document.getData().get("Name"));

                                    String rollno = document.getData().get("RollNo").toString();
                                    if (rollno == null || rollno.trim().length() == 0) {
                                        utility.createAlert(Profile_StudentActivity.this, "data not found");
                                        return;
                                    }
                                    String name = null, scategory = null, sclass = null, div = null, parentcontact = null, studentcontact = null, PrePercent = null, profilephoto = null;
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
                                        parentcontact = document.getData().get("PrePercent").toString();
                                    }
                                    if (document.getData().get("StudentContact") != null) {
                                        studentcontact = document.getData().get("StudentContact").toString();
                                    }
                                    if (document.getData().get("ParentContact") != null) {
                                        PrePercent = document.getData().get("ParentContact").toString();
                                    }
                                    if (document.getData().get("Image") != null) {
                                        profilephoto = document.getData().get("Image").toString();
                                    }

                                    SharedPreferences preferences = getSharedPreferences("pref", 0);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("rollno", document.getData().get("RollNo").toString());
                                    editor.commit();

                                    sprofile = new Sprofile(studentcontact, scategory, PrePercent, parentcontact, sclass, rollno, name, div, profilephoto);
                                    // writeToFile(document);

                                }

                                updatetext();


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
                            Glide.with(Profile_StudentActivity.this)
                                    .load(sprofile.getProfile())
                                    .override(300, 200)
                                    .into(profilep);


                        }
                    });
        }

}
