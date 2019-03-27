package cannyscale;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;

import java.io.*;
import java.util.*;

import constantsP.*;
import model.*;

public class CannyChartPagerAdapter extends FragmentStatePagerAdapter{

    private int nTabs;
    public List<RecordDetailWeighMachine> mRecordDetailList;
    String date;
    String userName,gender,age;
    String globalUserNme;
    Bundle bundle;

    public CannyChartPagerAdapter(FragmentManager fm, int mTabs, List<RecordDetailWeighMachine> mRecordDetail,
                            String date, String userN, String globalUserName) {
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

                IweighDaysChartFragment daysChartFragment=new IweighDaysChartFragment();
                    bundle=new Bundle();
                    bundle.putSerializable(Constants.CHART, (Serializable) mRecordDetailList);
                    bundle.putString(Constants.USER_NAME,userName);
                    bundle.putString("global_user",globalUserNme);
                    bundle.putString(Constants.DATE,date);

                    daysChartFragment.setArguments(bundle);
                    return daysChartFragment;




            case 1:


                IweighWeekChartFragment weekChartFragment=new IweighWeekChartFragment();
                 bundle=new Bundle();

                 bundle.putSerializable(Constants.CHART, (Serializable) mRecordDetailList);
                bundle.putString(Constants.USER_NAME,userName);
                bundle.putString("global_user",globalUserNme);
                bundle.putString(Constants.DATE,date);
                weekChartFragment.setArguments(bundle);
                return weekChartFragment;


            case 2:

                IweighMonthsFragment monthChartFragment =new IweighMonthsFragment();


                bundle=new Bundle();

                bundle.putSerializable(Constants.CHART, (Serializable) mRecordDetailList);
                bundle.putString(Constants.USER_NAME,userName);
                bundle.putString("global_user",globalUserNme);
                bundle.putString(Constants.DATE,date);

                monthChartFragment.setArguments(bundle);


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
