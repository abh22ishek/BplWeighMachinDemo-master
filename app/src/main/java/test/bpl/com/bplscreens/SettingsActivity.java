package test.bpl.com.bplscreens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import constantsP.Constants;
import constantsP.Utility;
import customviews.customrangebar.RangeBar;
import logger.Level;
import logger.Logger;
import statusbarwindow.StatusBarWindow;




public class SettingsActivity extends Activity {


    private TextView txt_Spo2high,txt_Spo2low,txt_HeartRatehigh,txt_HeartRatelow,txt_PIhigh,txt_PIlow;

    public RangeBar rangeBarSpo2,rangeBarHeartRate,rangeBarPI;

    private String TAG=SettingsActivity.class.getSimpleName();







    SwitchCompat togle_spo2_high;
    private SwitchCompat togle_spo2_low;
    private SwitchCompat togle_heart_rate_high,togle_heart_rate_low;
    private SwitchCompat togle_pi_high,togle_pi_low;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_new);
        StatusBarWindow.set_status_bar_color(SettingsActivity.this);

        final TextView headertitle= findViewById(R.id.base_header_title);
        headertitle.setText(new StringBuilder().append("BPL iOxy ").append(getString(R.string.settings)).toString());

        txt_Spo2high=  findViewById(R.id.txt_SPO2high);
        txt_Spo2low= findViewById(R.id.txt_SPO2Low);
        rangeBarSpo2=  findViewById(R.id.rangebar);


        txt_HeartRatehigh=  findViewById(R.id.txt_HeartRateHigh);
        txt_HeartRatelow=  findViewById(R.id.txt_HeartRateLow);
        txt_PIhigh=  findViewById(R.id.txt_PIHigh);
        txt_PIlow=  findViewById(R.id.txt_PILow);

        rangeBarHeartRate=  findViewById(R.id.rangebar2);
        rangeBarPI=  findViewById(R.id.rangebar3);


        togle_spo2_high=  findViewById(R.id.togle_spo2_high);
        togle_spo2_low=  findViewById(R.id.togle_spo2_low);
        togle_heart_rate_high=  findViewById(R.id.togle_heart_rate_high);
        togle_heart_rate_low=  findViewById(R.id.togle_heart_rate_low);
         togle_pi_high=  findViewById(R.id.togle_PI_high);
        togle_pi_low=  findViewById(R.id.togle_PI_low);


        togle_spo2_high.setChecked(false);
        togle_heart_rate_high.setChecked(false);
        togle_pi_high.setChecked(false);
        rangeBarSpo2.setAlpha(0.2f);
        rangeBarHeartRate.setAlpha(0.2f);
        rangeBarPI.setAlpha(0.2f);


        togle_spo2_high.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                   rangeBarSpo2.setEnabled(true);
                    rangeBarSpo2.setAlpha(1);
                }else{
                    rangeBarSpo2.setEnabled(false);
                    rangeBarSpo2.setAlpha(0.2f);
                }

            }
        });





        togle_heart_rate_high.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rangeBarHeartRate.setEnabled(true);
                    rangeBarHeartRate.setAlpha(1);
                }

                else
                {
                    rangeBarHeartRate.setEnabled(false);
                    rangeBarHeartRate.setAlpha(0.2f);
                }


            }
        });




        togle_pi_high.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    rangeBarPI.setEnabled(true);
                    rangeBarPI.setAlpha(1);
                }

                else{
                    rangeBarPI.setEnabled(false);
                    rangeBarPI.setAlpha(0.2f);
                }

            }
        });





        txt_Spo2high.setText(String.valueOf(100));
        txt_Spo2low.setText(String.valueOf(85));


        txt_HeartRatehigh.setText(new StringBuilder().append(150).toString());
        txt_HeartRatelow.setText(new StringBuilder().append(50).toString());

        txt_PIhigh.setText(new StringBuilder().append(20).toString());
        txt_PIlow.setText(new StringBuilder().append(0).toString());


        final ImageView mBackKey=  findViewById(R.id.imgBackKey);
        mBackKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_limits(SettingsActivity.this);
                finish();
            }
        });



        // No . of possible values=15;


       //int possible_values=( (max-min)/step);

        rangeBarSpo2.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {



                Logger.log(Level.INFO,"LeftThumb="," "+leftThumbIndex);
                Logger.log(Level.INFO,"RightThumb="," "+rightThumbIndex);

                txt_Spo2high.setText(String.valueOf(86+rightThumbIndex));
                if(rightThumbIndex>14)
                {
                    txt_Spo2high.setText(String.valueOf(100));
                }
                txt_Spo2low.setText(String.valueOf(85+leftThumbIndex));


            }
        });


        rangeBarHeartRate.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {

                txt_HeartRatehigh.setText(String.valueOf(51+rightThumbIndex));
                if(rightThumbIndex>100)
                {
                    txt_HeartRatehigh.setText(String.valueOf(150));
                }
                txt_HeartRatelow.setText(String.valueOf(50+leftThumbIndex));
            }
        });


        rangeBarPI.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {

                txt_PIhigh.setText(String.valueOf(1 + rightThumbIndex));

                if(rightThumbIndex>19)
                {
                    txt_PIhigh.setText(String.valueOf(20));
                }

                txt_PIlow.setText(String.valueOf(0+leftThumbIndex));

            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }






    public  void save_limits(Context context)
    {

        SharedPreferences limitprefs;
        limitprefs =context.getSharedPreferences(Utility.LIMIT_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = limitprefs.edit();

        editor.putString(Constants.SPO2_HIGH,txt_Spo2high.getText().toString());
        editor.putString(Constants.SPO2_LOW, txt_Spo2low.getText().toString());

        editor.putString(Constants.HEART_RATE_HIGH, txt_HeartRatehigh.getText().toString());
        editor.putString(Constants.HEART_RATE_LOW, txt_HeartRatelow.getText().toString());


        editor.putString(Constants.PI_HIGH, txt_PIhigh.getText().toString());
        editor.putString(Constants.PI_LOW, txt_PIlow.getText().toString());

        editor.putBoolean(Constants.TOGLE_SPO2_HIGH,togle_spo2_high.isChecked());
        editor.putBoolean(Constants.TOGLE_SPO2_LOW,togle_spo2_low.isChecked());

        editor.putBoolean(Constants.TOGLE_HR_HIGH,togle_heart_rate_high.isChecked());
        editor.putBoolean(Constants.TOGLE_HR_LOW,togle_heart_rate_low.isChecked());

        editor.putBoolean(Constants.TOGLE_PI_HIGH,togle_pi_high.isChecked());
        editor.putBoolean(Constants.TOGLE_PI_LOW,togle_pi_low.isChecked());





        editor.apply();
        Logger.log(Level.DEBUG, context.getClass().getSimpleName(), "togle_spo2_high status="+togle_spo2_high.isChecked());
        Logger.log(Level.DEBUG, context.getClass().getSimpleName(), "togle_hr_high status="+togle_heart_rate_high.isChecked());
        Logger.log(Level.DEBUG, context.getClass().getSimpleName(), "togle_pi_high status="+togle_pi_high.isChecked());

        Logger.log(Level.DEBUG, context.getClass().getSimpleName(), "shared preference s file is saved successfully");

    }


    @Override
    protected void onResume() {
        super.onResume();
        enable_disable_seekbar();
        get_limits();
    }


    public void get_limits()
    {
        SharedPreferences prefs;
        prefs =this.getSharedPreferences(Utility.LIMIT_KEY, Context.MODE_PRIVATE);


        rangeBarSpo2.setThumbIndices(Integer.parseInt(prefs.getString(Constants.SPO2_LOW, "85"))-85,
                Integer.parseInt(prefs.getString(Constants.SPO2_HIGH, "100"))-86 );



        if(Integer.parseInt(prefs.getString(Constants.HEART_RATE_LOW,"50"))<50)
        {
          int px_heart_rate_low=50;
            rangeBarHeartRate.setThumbIndices(px_heart_rate_low-50,Integer.parseInt(prefs.getString(Constants.HEART_RATE_HIGH, "150"))-51);
        }else{
            rangeBarHeartRate.setThumbIndices(Integer.parseInt(prefs.getString(Constants.HEART_RATE_LOW, "50"))-50,
                    Integer.parseInt(prefs.getString(Constants.HEART_RATE_HIGH, "150"))-51);
        }



        if(Integer.parseInt(prefs.getString(Constants.PI_LOW,"50"))<0)
        {
            int px_pi_low=0;
            rangeBarPI.setThumbIndices(px_pi_low-0,
                    Integer.parseInt(prefs.getString(Constants.PI_HIGH, "20"))-1);
        }else{
            rangeBarPI.setThumbIndices(Integer.parseInt(prefs.getString(Constants.PI_LOW, "0"))-0,
                    Integer.parseInt(prefs.getString(Constants.PI_HIGH, "20"))-1);
        }




        togle_spo2_high.setChecked(prefs.getBoolean(Constants.TOGLE_SPO2_HIGH, false));
        togle_spo2_low.setChecked(prefs.getBoolean(Constants.TOGLE_SPO2_LOW,false));

        togle_heart_rate_high.setChecked(prefs.getBoolean(Constants.TOGLE_HR_HIGH,false));
        togle_heart_rate_low.setChecked(prefs.getBoolean(Constants.TOGLE_HR_LOW,false));

        togle_pi_high.setChecked(prefs.getBoolean(Constants.TOGLE_PI_HIGH,false));
        togle_pi_low.setChecked(prefs.getBoolean(Constants.TOGLE_PI_LOW,false));


        if(!prefs.getBoolean(Constants.TOGLE_SPO2_HIGH,false))
        {
            rangeBarSpo2.setAlpha(0.2f);
        }

        if(!prefs.getBoolean(Constants.TOGLE_HR_HIGH,false))
        {
            rangeBarHeartRate.setAlpha(0.2f);
        }

        if(!prefs.getBoolean(Constants.TOGLE_PI_HIGH,false))
        {
            rangeBarPI.setAlpha(0.2f);
        }
        Logger.log(Level.DEBUG, TAG, "get TOGGLE_SPO2_HIGH stored in a shared preference s file *" + prefs.getBoolean(Constants.TOGLE_SPO2_HIGH,false));
        Logger.log(Level.DEBUG, TAG, "get TOGGLE_HR_HIGH stored in a shared preference s file *" + prefs.getBoolean(Constants.TOGLE_HR_HIGH,false));
        Logger.log(Level.DEBUG, TAG, "get TOGGLE_PI_HIGH stored in a shared preference s file *" + prefs.getBoolean(Constants.TOGLE_PI_HIGH,false));

        Logger.log(Level.DEBUG, TAG, "get value stored in a shared preference s file *" + prefs);

    }

    private void enable_disable_seekbar()
    {
        if(togle_spo2_high.isChecked())
            rangeBarSpo2.setEnabled(true);
        else
            rangeBarSpo2.setEnabled(false);

        if(togle_pi_high.isChecked())
            rangeBarPI.setEnabled(true);
        else
            rangeBarPI.setEnabled(false);

        if(togle_heart_rate_high.isChecked())
            rangeBarHeartRate.setEnabled(true);
        else
            rangeBarHeartRate.setEnabled(false);
    }
}
