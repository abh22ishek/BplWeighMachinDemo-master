package test.bpl.com.bplscreens;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.*;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import constantsP.*;
import database.DatabaseManager;
import model.RecordsDetail;
import records.RecordsPagerAdapter;
import statusbarwindow.StatusBarWindow;



public class UsersRecordActivity extends FragmentActivity {


    ImageView mBackKey;
    TextView headertitle;


    GlobalClass globalVariable;
    List<RecordsDetail> mRecordDetailList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.records);

        globalVariable= (GlobalClass) getApplicationContext();
        StatusBarWindow.set_status_bar_color(UsersRecordActivity.this);

        headertitle=  findViewById(R.id.base_header_title);

        mRecordDetailList=new ArrayList<>();

        TabLayout tabLayout =  findViewById(R.id.tab_layout);
        mBackKey=  findViewById(R.id.imgBackKey);
        mBackKey.setOnClickListener(listner);


       final String mUserName= getIntent().getExtras().getString(Constants.USER_NAME);

        int density =this.getResources().getDisplayMetrics().densityDpi;
        if(DisplayMetrics.DENSITY_XXHIGH==density)
        {
            headertitle.setTextSize(22f);
        }else{
            headertitle.setTextSize(21);
        }
        if(mUserName.length()>12)
        {
            String mx= mUserName.substring(0,9)+"..";
            headertitle.setText(new StringBuilder().append(mx).append(" 's ").append("Sp02 ").append(getString(R.string.record)).toString());

        }else{
            headertitle.setText(new StringBuilder().append(mUserName).append(" 's ").append("Sp02 ").append(getString(R.string.record)).toString());

        }
        if(mUserName!=null)
        {
            DatabaseManager.getInstance().openDatabase();
            mRecordDetailList=DatabaseManager.getInstance().get_Records_list(mUserName,globalVariable.getUserType());
        }

        final ViewPager viewPager =  findViewById(R.id.pager);

        final RecordsPagerAdapter adapter = new
                RecordsPagerAdapter(getSupportFragmentManager(),1,mRecordDetailList,mUserName);
        viewPager.setAdapter(adapter);




        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);




    }



    View.OnClickListener listner= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v==mBackKey)
            {
                finish();
            }
        }
    };

}
