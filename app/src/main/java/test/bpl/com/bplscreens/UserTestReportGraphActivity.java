package test.bpl.com.bplscreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.*;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import constantsP.Constants;
import logger.Level;
import logger.Logger;
import testreport.GraphTestReportsPagerAdapter;



public class UserTestReportGraphActivity extends FragmentActivity {



    private TextView header_title;
    private ImageView mBackKey;

    String day_time="";
    String spo2_str="";
    String pr_str="";
    String month="";
    String year="";
   public static String total_time="";
     ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_chart);

        header_title=  findViewById(R.id.base_header_title);
      String  userName=getIntent().getExtras().getString(Constants.USER_NAME);
        int density =this.getResources().getDisplayMetrics().densityDpi;

        if(DisplayMetrics.DENSITY_XXHIGH==density)
        {
            header_title.setTextSize(25);
        }else{
            header_title.setTextSize(22);
        }
        if(userName.length()>12)
        {
            String mx= userName.substring(0,10)+"..";
            header_title.setText(new StringBuilder().append(mx).append(" 's ").append(getString(R.string.trend)).toString());

        }else{
            header_title.setText(new StringBuilder().append(userName).append(" 's").append(getString(R.string.trend)).toString());

        }

        TabLayout tabLayout =  findViewById(R.id.tab_layout);

        mBackKey=  findViewById(R.id.imgBackKey);
        mBackKey.setOnClickListener(listner);

         viewPager = findViewById(R.id.pager);


        tabLayout.addTab(tabLayout.newTab().setText("day"));
        tabLayout.addTab(tabLayout.newTab().setText("month"));
        tabLayout.addTab(tabLayout.newTab().setText("year"));
       // tabLayout.addTab(tabLayout.newTab().setText("custom"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        if(getIntent().getExtras()!=null) {
            day_time = getIntent().getExtras().getString(Constants.TESTING_TIME_CONSTANTS, "");
            spo2_str=getIntent().getExtras().getString(Constants.SPO2_CONSTANTS,"");
            pr_str=getIntent().getExtras().getString(Constants.PR_CONSTANTS,"");
            month=getIntent().getExtras().getString(Constants.MONTH,"00");
            year=getIntent().getExtras().getString(Constants.YEAR,"2015");
            total_time = getIntent().getExtras().getString(Constants.DURATION_CONSTANTS, "");
        }

        Logger.log(Level.DEBUG,UserTestReportGraphActivity.this.getClass().getSimpleName(),"Day time="+day_time+"month="+month);

        final GraphTestReportsPagerAdapter adapter = new
                GraphTestReportsPagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount(),day_time,spo2_str,pr_str,month,year);




        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.getTabAt(0).setText("DAY");
        tabLayout.getTabAt(1).setText("MONTH");
        tabLayout.getTabAt(2).setText("YEAR");
      //  tabLayout.getTabAt(3).setText("CUSTOM");

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));

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


    View.OnClickListener listner= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v==mBackKey)
            {
                finish();
            }

        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

}
