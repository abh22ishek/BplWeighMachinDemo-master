package biolight;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;


import constantsP.*;
import logger.*;
import test.bpl.com.bplscreens.*;

public class BioLightSettingsActivity extends Activity {

    TextView base_header_title;
    ToggleButton unitConversion;
    SwitchCompat voiceSpeaker;


    private  final String TAG=BioLightSettingsActivity.class.getSimpleName();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biolight_settings);

        base_header_title=findViewById(R.id.base_header_title);
        base_header_title.setText(new StringBuilder().append(getString(R.string.bt))
                .append(" ").append(getString(R.string.settings)).toString());


        unitConversion=findViewById(R.id.unitConversion);
        voiceSpeaker=findViewById(R.id.bioSettingsVoice);


        final ImageView backKey=findViewById(R.id.imgBackKey);
        backKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        unitConversion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b)
                {
                    saveCurrentUnitMeasurement(BioLightSettingsActivity.this,
                            Constants.MM_UNIT_MEASUREMENT_KEY, Constants.MM_HG);

                }else{

                    saveCurrentUnitMeasurement(BioLightSettingsActivity.this,
                            Constants.MM_UNIT_MEASUREMENT_KEY, Constants.KPA);
                }
            }
        });



        voiceSpeaker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    saveSwitchStatePref(BioLightSettingsActivity.this,
                            Constants.BIO_LIGHT_VOICE_STATUS, Constants.SWITCH_ON);
                }else{

                    saveSwitchStatePref(BioLightSettingsActivity.this,
                            Constants.BIO_LIGHT_VOICE_STATUS, Constants.SWITCH_OFF);

                }
            }
        });


        }



    private void saveCurrentUnitMeasurement(Context context, String key, String switchState)
    {
        SharedPreferences togglePref;
        togglePref=context.getSharedPreferences(Constants.BIOLIGHT_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =togglePref.edit();
        editor.putString(key,switchState);
        editor.apply();
        Logger.log(Level.DEBUG, TAG, "// *shared preference toggle state gets stored // *"+switchState);
    }



    private void saveSwitchStatePref(Context context,String key, String switchCompat)
    {
        SharedPreferences switchStatePref;
        switchStatePref=context.getSharedPreferences(Constants.BIOLIGHT_PREF_VOICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =switchStatePref.edit();
        editor.putString(key,switchCompat);
        editor.apply();
        Logger.log(Level.DEBUG, TAG, "// *shared preference switch state gets stored // *"+switchCompat);

    }

}


