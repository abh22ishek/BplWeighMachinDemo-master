package biolight;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;

import java.io.*;
import java.util.*;

import constantsP.*;

public class ChartPageAdapter extends FragmentStatePagerAdapter {

    private int nTabs;
    public List<BPMeasurementModel> mRecordDetailList;
    String date;
    String userName,gender,age;
    String globalUserNme;


    public ChartPageAdapter(FragmentManager fm, int mTabs, List<BPMeasurementModel> mRecordDetail,
                            String date, String userN, String age, String gender,String globalUserName) {
        super(fm);
        this.nTabs=mTabs;
        this.mRecordDetailList=mRecordDetail;
        this.date=date;
        this.userName=userN;
        this.age=age;
        this.gender=gender;
        this.globalUserNme=globalUserName;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:


                DaysChartFragment daysChartFragment=new DaysChartFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable(Constants.CHART, (Serializable) mRecordDetailList);

                bundle.putString("user",userName);
                bundle.putString("gender",gender);
                bundle.putString("age",age);
                bundle.putString("global_user",globalUserNme);

                bundle.putString(Constants.DATE,date);
                daysChartFragment.setArguments(bundle);


                return daysChartFragment;




            case 1:


                WeekChartFragment weekChartFragment=new WeekChartFragment();
                Bundle b=new Bundle();
                b.putSerializable(Constants.CHART, (Serializable) mRecordDetailList);
                b.putString(Constants.DATE,date);

                b.putString("user",userName);
                b.putString("gender",gender);
                b.putString("age",age);

                weekChartFragment.setArguments(b);
                return weekChartFragment;


            case 2:

                MonthChartFragment monthChartFragment =new MonthChartFragment();
                Bundle bun=new Bundle();
                bun.putSerializable(Constants.CHART, (Serializable) mRecordDetailList);
                bun.putString(Constants.DATE,date);

                bun.putString("user",userName);
                bun.putString("gender",gender);
                bun.putString("age",age);

                monthChartFragment.setArguments(bun);
                return monthChartFragment;



            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nTabs;
    }
}
