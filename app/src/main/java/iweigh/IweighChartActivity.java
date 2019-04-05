package iweigh;

import android.graphics.*;
import android.os.*;
import android.support.annotation.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import biolight.*;
import canny.*;
import constantsP.*;
import logger.*;
import model.*;
import test.bpl.com.bplscreens.*;

public class IweighChartActivity extends FragmentActivity {


    ViewPager viewPager;
    CannyChartPagerAdapter chartPagerAdapter;
    TabLayout tabLayout;
    List<RecordDetailWeighMachine> mRecordDetail;
    String date;

    private final String TAG=IweighChartActivity.class.getSimpleName();

    GlobalClass globalClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iweigh_chart);

        setContentView(R.layout.graph_chart);
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);


        final TextView headerText = findViewById(R.id.base_header_title);
        final String mUserName = getIntent().getExtras().getString(Constants.USER_NAME);




        globalClass = (GlobalClass) getApplication();
        // Fetch record data
        int density = this.getResources().getDisplayMetrics().densityDpi;
        if (DisplayMetrics.DENSITY_XXHIGH == density) {
            headerText.setTextSize(22);
        } else {
            headerText.setTextSize(22);
        }
        if (mUserName.length() > 12) {
            String mx = mUserName.substring(0, 10) + "..";
            headerText.setText(new StringBuilder().append(mx).append(" 's ").append("Iweigh").append(" ").
                    append(getString(R.string.trend)).toString());

        } else {
            headerText.setText(new StringBuilder().append(mUserName).append(" 's ").append("Iweigh").append(" ").
                    append(getString(R.string.trend)).toString());

        }



        final ImageView backKey = findViewById(R.id.imgBackKey);
        backKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (getIntent().getExtras().getSerializable(Constants.CHART) != null) {
            mRecordDetail = (List<RecordDetailWeighMachine>) getIntent().getExtras().getSerializable(Constants.CHART);
            Logger.log(Level.WARNING, TAG, "Get serializable list=" + mRecordDetail.size());
        }


        if (getIntent().getExtras().getString(Constants.DATE) != null) {
            date = getIntent().getExtras().getString(Constants.DATE);
        }


        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        chartPagerAdapter = new CannyChartPagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount(), mRecordDetail, date, mUserName, globalClass.getUsername());


        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setAdapter(chartPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        //noinspection ConstantConditions
        tabLayout.getTabAt(0).setText((getString(R.string.day) == null)
                ? "" : getString(R.string.day));

        tabLayout.getTabAt(1).setText((getString(R.string.week) == null) ? "" :
                getString(R.string.week));

        tabLayout.getTabAt(2).
                setText((getString(R.string.month) == null) ? "" : getString(R.string.month));


        tabLayout.setTabTextColors(Color.GRAY, Color.WHITE);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {

                } else if (tab.getPosition() == 1) {

                } else {

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}