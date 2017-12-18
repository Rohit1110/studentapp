package com.resoneuronance.shahucetcell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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

import java.util.ArrayList;
import java.util.List;

import model.Sprofile;
import utils.Utility;


public class MainActivity extends AppCompatActivity {

    private String phone;
    private ProgressDialog proDialog;
    private Utility utility;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //utility = new Utility();
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
                            Log.d("Data",task.getResult().toString());

                            if (task.getResult() == null || task.getResult().size() == 0) {
                                Utility.createAlert(MainActivity.this, "Profile not found for this number "+phone);
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


                       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);

                        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
                        tabLayout.addTab(tabLayout.newTab().setText("College Notices"));
                       /*tabLayout.addTab(tabLayout.newTab().setText("PROFILE"));*/
                        tabLayout.addTab(tabLayout.newTab().setText("Inbox"));
                        tabLayout.addTab(tabLayout.newTab().setText("Exam Section"));
                        tabLayout.addTab(tabLayout.newTab().setText("Progress Report"));
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


                       /* ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

                        // Add Fragments to adapter one by one
                        adapter.addFragment(new GeneralNotifications(), "Notifications");
                        adapter.addFragment(new Student_Inbox(), "Inbox");
                        adapter.addFragment(new Student_ExamResult(), "Results");
                        adapter.addFragment(new Student_Progress(), "Progress Report");
                        viewPager.setAdapter(adapter);

                        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
                        tabLayout.setupWithViewPager(viewPager);
                    }
                });*/
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }


    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

