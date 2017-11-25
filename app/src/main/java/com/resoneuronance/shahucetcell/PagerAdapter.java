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

        switch (position) {
            case 0:
                Student_Profile tab1 = new Student_Profile();
                return tab1;
            case 1:
                Student_Inbox tab2 = new Student_Inbox();
                return tab2;

            case 2:
                Student_ExamResult tab4 = new Student_ExamResult();
                return tab4;
            case 3:
                Student_Progress tab3 = new Student_Progress();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
