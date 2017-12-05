package com.resoneuronance.shahucetcell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import model.Sprofile;
import utils.Utility;


public class MainActivity extends AppCompatActivity {

    private String phone;
    private ProgressDialog proDialog;
    private Utility utility;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        utility = new Utility();
        proDialog = new ProgressDialog(MainActivity.this);
        proDialog.setMessage("please wait....");
        proDialog.setCancelable(false);
        proDialog.show();

        SharedPreferences sp = getSharedPreferences("user", 0);
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
                                utility.createAlert(getApplicationContext(), "data not found");
                                return;
                            }

                            for (DocumentSnapshot document : task.getResult()) {

                                Log.d("Data", document.getId() + " => " + document.getData());
                                Log.d("Name", document.getId() + " => " + document.getData().get("Name"));

                                String rollno = document.getData().get("RollNo").toString();
                                if (rollno == null || rollno.trim().length() == 0) {
                                    utility.createAlert(getApplicationContext(), "data not found");
                                    return;
                                }

                                SharedPreferences preferences = getSharedPreferences("pref", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("rollno", document.getData().get("RollNo").toString());
                                editor.commit();


                            }


                        }


                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);

                        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
                        tabLayout.addTab(tabLayout.newTab().setText("Notifications"));
                        tabLayout.addTab(tabLayout.newTab().setText("PROFILE"));
                        tabLayout.addTab(tabLayout.newTab().setText("INBOX"));
                        tabLayout.addTab(tabLayout.newTab().setText("EXAM RESULT"));
                        tabLayout.addTab(tabLayout.newTab().setText("PROGRESS"));
                        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


                        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                        final PagerAdapter adapter = new PagerAdapter
                                (getSupportFragmentManager(), tabLayout.getTabCount());

                        viewPager.setAdapter(adapter);
                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                viewPager.setCurrentItem(tab.getPosition());
                            }

                            @Override
                            public void onTabUnselected(TabLayout.Tab tab) {

                            }

                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {

                            }
                        });


                    }





                });
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.student_Profile:
                //Toast.makeText(this, "You have selected profile Menu", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,Profile_StudentActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}

