package bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import java.lang.ref.WeakReference;

import constantsP.GlobalClass;
import test.bpl.com.bplscreens.HomeScreenActivity;





public class IncomingCallsReceiver extends BroadcastReceiver {
   
   
    @Override
    public void onReceive(Context context, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);                         // 3
        //String msg = "Phone state changed to " + state;
        HomeScreenActivity activity = null;

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {                                   // 4
            GlobalClass globalVariable = (GlobalClass) context.getApplicationContext();
           
            if(globalVariable.activityVisible)
            {
                 WeakReference<HomeScreenActivity> mActivityRef= new WeakReference<>(activity);
                if(mActivityRef.get().mSpaeaker) {
                    //mActivityRef.get().img_speaker.setImageResource(R.mipmap.speaker_mute);
                    mActivityRef.get().mSpaeaker=false;

               }

            }

            }





    }
}
