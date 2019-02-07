package records;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import constantsP.Constants;
import model.RecordsDetail;



public class RecordsPagerAdapter extends FragmentStatePagerAdapter {

    int mTabs;
    List<RecordsDetail> recordsDetailList;
    String userName;

    public RecordsPagerAdapter(FragmentManager fm, int mTabs, List<RecordsDetail> recordsDetailList,String UserName) {
        super(fm);
        this.mTabs = mTabs;
        this.recordsDetailList = recordsDetailList;
            this.userName=UserName;
    }

    @Override
    public Fragment getItem(int position) {


        switch (position)
        {

            case 0:
                LocalRecordFragment tab1=new LocalRecordFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.RECORDS_DETAIL, (ArrayList<? extends Parcelable>) recordsDetailList);
                bundle.putString(Constants.USER_NAME,userName);
                tab1.setArguments(bundle);
                return tab1;


            case 1:
                CloudRecordFragment tab2=new CloudRecordFragment();
                return tab2;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }


    public static final LocalRecordFragment newInstance(RecordsPagerAdapter instancxe){
        LocalRecordFragment f = new LocalRecordFragment();
        f.recordsDetailList = (List<RecordsDetail>) instancxe;
        return f;
    }
}
