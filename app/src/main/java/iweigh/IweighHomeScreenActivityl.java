package iweigh;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.*;
import android.widget.*;

import constantsP.*;
import logger.*;
import test.bpl.com.bplscreens.*;

public class IweighHomeScreenActivityl extends FragmentActivity implements IweighNavigation{

    RelativeLayout bmiRelativeLayout,metabolismRelativeLayout,visceralFatR;
    RelativeLayout muscleMassR,bodyFatR,bodyAgeR,proteinR,boneMassR,bodyWaterR,lbmR;
    LinearLayout menu_bar;
    String headerBar="";
    ImageView settings,record;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iweigh_home);

        bmiRelativeLayout=findViewById(R.id.bmiRelativeLayout);
        menu_bar=findViewById(R.id.menu_bar);
        muscleMassR=findViewById(R.id.muscleMassR);
        bodyFatR=findViewById(R.id.bodyFatR);
        bodyAgeR=findViewById(R.id.bodyAgeR);
        proteinR=findViewById(R.id.proteinR);
        boneMassR=findViewById(R.id.boneMassR);
        bodyWaterR=findViewById(R.id.bodyWaterR);
        lbmR=findViewById(R.id.LBMR);

        settings=findViewById(R.id.img_settings);
        record=findViewById(R.id.img_records);


        final ImageView  mBackKey=  findViewById(R.id.imgBackKey);
        mBackKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityOptions options;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(IweighHomeScreenActivityl.this);

                    startActivity(new Intent(IweighHomeScreenActivityl.this, IweighSettingsScreenActivity.class)

                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), options.toBundle());
                } else {
                    startActivity(new Intent(IweighHomeScreenActivityl.this, IweighSettingsScreenActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }

            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(IweighHomeScreenActivityl.this);

                    startActivity(new Intent(IweighHomeScreenActivityl.this, IweighRecordsActivity.class)

                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), options.toBundle());
                } else {
                    startActivity(new Intent(IweighHomeScreenActivityl.this, IweighRecordsActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        });

        visceralFatR=findViewById(R.id.visceralFatR);

        visceralFatR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VisceralScreenFragment visceralScreenFragment=new VisceralScreenFragment();
                callFragments(visceralScreenFragment,"visceral fat");
            }
        });

        lbmR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LBMScreenFragment lbmScreenFragment=new LBMScreenFragment();
                callFragments(lbmScreenFragment,"lbm");
            }
        });
        muscleMassR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MuscleMassScreenFragment muscleMassScreenFragment=new MuscleMassScreenFragment();
                callFragments(muscleMassScreenFragment,"muscle mass");
            }
        });


        bodyFatR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BodyFatScreenFragment bodyFatScreenFragment=new BodyFatScreenFragment();
                callFragments(bodyFatScreenFragment,"body fat");
            }
        });



        bodyAgeR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BodyAgeScreenFragment bodyAgeScreenFragment=new BodyAgeScreenFragment();
                callFragments(bodyAgeScreenFragment,"body age");

            }


        });


        proteinR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProteinScreenFragments proteinScreenFragments=new ProteinScreenFragments();
                callFragments(proteinScreenFragments,"protein");

            }
        });


        boneMassR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BoneMassScreenFragment boneMassScreenFragment=new BoneMassScreenFragment();
                callFragments(boneMassScreenFragment,"bone mass");

            }
        });




        bmiRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BMIScreenFragment bmiScreenFragment=new BMIScreenFragment();
                callFragments(bmiScreenFragment,"bmi");

            }
        });


        bodyWaterR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BodyWaterScreenFragment bodyWaterScreenFragment=new BodyWaterScreenFragment();
                callFragments(bodyWaterScreenFragment,"body water");
            }
        });

        metabolismRelativeLayout=findViewById(R.id.metabolism);
        metabolismRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetabolismScreenFragment metabolismScreenFragment=new MetabolismScreenFragment();
                callFragments(metabolismScreenFragment,"metabolism");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Logger.log(Level.DEBUG,"On Resume()","called");
        menu_bar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.log(Level.DEBUG,"On Restart()","called");

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void navigationPass(String tag) {
        if(tag.equalsIgnoreCase("I-weigh"))
        {
            menu_bar.setVisibility(View.GONE);
            headerBar=tag;
        }
    }


    @Override
    public void onBackPressed() {


        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        Logger.log(Level.DEBUG, "I-weigh", "((--count no. fo Fragments-----))=" + count);

        if(!headerBar.equals(""))
        {
            menu_bar.setVisibility(View.VISIBLE);
        }

        fragmentManager.popBackStackImmediate();

        }




        public void callFragments(Fragment fragment,String Tag)
        {
            android.support.v4.app.FragmentManager fragmentManager;
            android.support.v4.app.FragmentTransaction fragmentTransaction;
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.conatiner, fragment,Tag);
            fragmentTransaction.addToBackStack(Tag);
            fragmentTransaction.commit();
        }

}
