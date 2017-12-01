package com.resoneuronance.shahucetcell;

/**
 * Created by Rohit on 11/23/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        System.out.println("Position of fragment" + position);

        Fragment tab = null;

        switch (position) {
            case 0:
                tab = new GeneralNotifications();
                break;
            case 1:
                tab = new Student_Profile();
                break;
            case 2:
                tab = new Student_Inbox();
                break;

            case 3:
                tab = new Student_ExamResult();
                break;
            case 4:
                tab = new Student_Progress();
                break;

            default:
                tab = new Student_Profile();
                break;
        }
        return tab;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
