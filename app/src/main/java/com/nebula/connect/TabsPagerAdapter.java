package com.nebula.connect;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Sonam on 24/3/17.
 */
public class TabsPagerAdapter  extends FragmentPagerAdapter {

    public ReportCardFragment reportCardFragment;
    public PainterDbFragment painterDbFragment;
    public MeetingPhotoFragment meetingPhotoFragment;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Movies fragment activity
                if(meetingPhotoFragment == null){
                    meetingPhotoFragment = new MeetingPhotoFragment();
                }
                return meetingPhotoFragment;

               case 1:
                // Games fragment activity
                if(painterDbFragment == null){
                    painterDbFragment = new PainterDbFragment();
                }
                return painterDbFragment;

            case 2:
                // Top Rated fragment activity
                if(reportCardFragment == null){
                    reportCardFragment = new ReportCardFragment();
                }
                return reportCardFragment;

        }

        return null;
    }

    public PainterDbFragment getPainterFragment(){
        return painterDbFragment;
    }

    public MeetingPhotoFragment getMeetingPhotoFragment(){
        return meetingPhotoFragment;
    }

    public ReportCardFragment getReportCardFragment(){
        return reportCardFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence name = "name";
        if(position == 0){
            name = "Meeting";
        }else if(position == 1){
            name = "Attendee Data";
        }else if(position == 2){
            name = "Report Card";
        }
        return name;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
