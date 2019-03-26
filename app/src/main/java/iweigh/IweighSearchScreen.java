package iweigh;

import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import pl.bclogic.pulsator4droid.library.*;
import test.bpl.com.bplscreens.R;

public class IweighSearchScreen extends DialogFragment {

    ImageView arrw,weight,weightPulse;
    PulsatorLayout pulsatorLayout;
    Handler mHandler;
    Animation animation;



    static IweighSearchScreen newInstance() {
        return new IweighSearchScreen();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.iweigh_search,container);
        arrw=view.findViewById(R.id.arrow);

        weight=view.findViewById(R.id.weightT);
     //   pulsatorLayout=view.findViewById(R.id.pulsator);
     //   pulsatorLayout.setVisibility(View.GONE);
      //  weightPulse=view.findViewById(R.id.weightPulse);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      /*  animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_out);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(1000);

        arrw.startAnimation(animation);*/
      //  pulsatorLayout.start();


        if(mHandler==null){
            mHandler=new Handler();
        }
/*
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try{
                    arrw.clearAnimation();
                   // animation.cancel();
                    arrw.setVisibility(View.GONE);
                    weight.setVisibility(View.GONE);
                    pulsatorLayout.setVisibility(View.VISIBLE);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, 5000);*/
    }





}
