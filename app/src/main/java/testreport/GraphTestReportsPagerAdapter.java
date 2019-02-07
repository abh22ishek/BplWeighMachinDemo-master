package testreport;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import constantsP.Constants;



public class GraphTestReportsPagerAdapter extends FragmentStatePagerAdapter {

     private int mTabs;
    private String day_time,spo2,pr,month,year;


    public GraphTestReportsPagerAdapter(FragmentManager fm, int mTabs, String daytime,String spo2,String pr,String month,String year) {
        super(fm);
        this.mTabs=mTabs;
        this.day_time=daytime;
        this.spo2=spo2;
        this.pr=pr;
        this.month=month;
        this.year=year;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {

            case 0:
                TestReportDayFragment tab1=new TestReportDayFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.TESTING_TIME_CONSTANTS,day_time);
                bundle.putString(Constants.SPO2_CONSTANTS,spo2);
                bundle.putString(Constants.PR_CONSTANTS,pr);
                tab1.setArguments(bundle);
                return tab1;


            case 1:
                TestReportMonthFragment tab2=new TestReportMonthFragment();
                Bundle bundle_ = new Bundle();
                bundle_.putString(Constants.TESTING_TIME_CONSTANTS,day_time);
                bundle_.putString(Constants.SPO2_CONSTANTS,spo2);
                bundle_.putString(Constants.PR_CONSTANTS,pr);
                bundle_.putString(Constants.MONTH,month);
                tab2.setArguments(bundle_);
                return tab2;

            case 2:
                TestReportYearFragment tab3=new TestReportYearFragment();
                Bundle b = new Bundle();
                b.putString(Constants.TESTING_TIME_CONSTANTS,day_time);
                b.putString(Constants.YEAR,year);
                tab3.setArguments(b);
                return tab3;


            case 3:
                TestReportCustomFragment tab4=new TestReportCustomFragment();
                return tab4;

            default:
                return null;
        }
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return mTabs;
    }
}
