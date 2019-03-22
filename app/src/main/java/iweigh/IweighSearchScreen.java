package iweigh;

import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import pl.bclogic.pulsator4droid.library.*;
import test.bpl.com.bplscreens.*;
import test.bpl.com.bplscreens.R;

public class IweighSearchScreen extends FragmentActivity {

    ImageView arrw,weight,weightPulse;

    Handler mHandler;
    Animation animation;

    PulsatorLayout pulsatorLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iweigh_search);
        arrw=findViewById(R.id.arrow);

        weight=findViewById(R.id.weight);
        pulsatorLayout=findViewById(R.id.pulsator);
        pulsatorLayout.setVisibility(View.GONE);
        weightPulse=findViewById(R.id.weightPulse);

         animation = AnimationUtils.loadAnimation(IweighSearchScreen.this, R.anim.slide_in_out);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(1000);

         arrw.startAnimation(animation);
        pulsatorLayout.start();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(mHandler==null){
            mHandler=new Handler();
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try{
                    arrw.clearAnimation();
                    animation.cancel();
                    arrw.setVisibility(View.GONE);
                  weight.setVisibility(View.GONE);
                    pulsatorLayout.setVisibility(View.VISIBLE);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, 5000);
    }




}
