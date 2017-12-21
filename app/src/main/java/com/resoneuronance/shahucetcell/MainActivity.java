package com.resoneuronance.shahucetcell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import utils.Utility;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String phone;
    private ProgressDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("user", 0);
        phone = sp.getString("userphone", "");
        Log.v("SSSS", phone);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db.collection("students")
                .whereEqualTo("StudentContact", phone)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        proDialog.dismiss();
                        if (task.isSuccessful()) {
                            Log.d("Data", task.getResult().toString());

                            if (task.getResult() == null || task.getResult().size() == 0) {
                                Utility.createAlert(MainActivity.this, "Profile not found for this number " + phone);
                                return;
                            }

                            for (DocumentSnapshot document : task.getResult()) {

                                Log.d("Data", document.getId() + " => " + document.getData());
                                Log.d("Name", document.getId() + " => " + document.getData().get("Name"));


                                SharedPreferences preferences = getSharedPreferences("pref", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("rollno", document.getData().get("RollNo").toString());
                                editor.commit();


                            }


                        }



                    }
                });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new GeneralNotifications());
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = new GeneralNotifications();
        int id = item.getItemId();

       if (id == R.id.nav_general_Notices) {

            fragment = new GeneralNotifications();

        } else if (id == R.id.nav_inbox) {
            fragment = new Student_Inbox();

        } else if (id == R.id.nav_exam) {
            fragment = new Student_ExamResult();

        }else if (id == R.id.nav_progress) {
            fragment = new Student_Progress();

        }
        else if (id == R.id.nav_pofile) {
            fragment = new Student_Profile();

        } else if (id == R.id.nav_logout) {
          /* Intent i =new Intent(MainActivity.this,PhoneAuthActivity.class);
           startActivity(i);*/
            FirebaseAuth.getInstance().signOut();
            Intent i =new Intent(MainActivity.this,PhoneAuthActivity.class);
            startActivity(i);
            finish();
        }
        else{
            fragment = new GeneralNotifications();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
