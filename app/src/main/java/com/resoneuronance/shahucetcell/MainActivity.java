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
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

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
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("user", 0);
        phone = sp.getString("userphone", "");
        Log.v("SSSS", phone);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.stausbar));
        }
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
                                editor.putString("sclass",document.getData().get("Class").toString());
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
        } else if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(getBaseContext(), "Press once again to exit!",
                        Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
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

        }else if (id == R.id.nav_feedback) {
           fragment = new Student_Feedback();

       }
       else if (id == R.id.nav_timetable) {
           Intent i =new Intent(MainActivity.this,TimeTableview.class);
           startActivity(i);

       }
       else if (id == R.id.nav_quepaper) {
           fragment = new Student_QuestionPaper();

       }
        else if (id == R.id.nav_pofile) {
            fragment = new Student_Profile();

        }else if (id == R.id.nav_test_result) {
           fragment = new TestResult();

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
