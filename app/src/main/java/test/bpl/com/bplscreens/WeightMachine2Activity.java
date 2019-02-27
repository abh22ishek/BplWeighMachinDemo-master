package test.bpl.com.bplscreens;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aicare.net.cn.iweightlibrary.bleprofile.BleProfileService;
import aicare.net.cn.iweightlibrary.bleprofile.BleProfileServiceReadyActivity;
import aicare.net.cn.iweightlibrary.entity.BodyFatData;
import aicare.net.cn.iweightlibrary.entity.WeightData;
import aicare.net.cn.iweightlibrary.utils.AicareBleConfig;
import aicare.net.cn.iweightlibrary.utils.L;
import aicare.net.cn.iweightlibrary.utils.ParseData;
import aicare.net.cn.iweightlibrary.wby.WBYService;
import bluetooth.BmiListner;
import constantsP.*;
import customviews.CurvePathView;
import database.BplOximterdbHelper;
import database.DatabaseManager;
import logger.Level;
import logger.Logger;
import model.UserModel;

import static constantsP.Constants.PERMISSION_REQUEST_COARSE_LOCATION;
import static constantsP.DateTime.getDateTime;
import static test.bpl.com.bplscreens.R.id.img_settings;

/**
 * Created by abhishek.raj on 29-01-2018.
 */

public class WeightMachine2Activity extends BleProfileServiceReadyActivity implements BmiListner,DeviceDialog.OnDeviceScanListener, View.OnClickListener {

    private TextView txt_weight,bmi_value;
    private TextView txt_status;
    private ImageView needle;

    private TextView age,height,measurementType;


    private BluetoothAdapter mBluetoothAdapter;
    private static final String TAG=WeighMachineActivity.class.getSimpleName();

    float bmi=0;
    BmiListner mListner;
    private ImageView records,back_key,settings,users_help;
    Button save;
    private GlobalClass globalVariable;
    BluetoothLeScanner scanner;
    CurvePathView curvePathView;

    String TAG_SAVE="true";

    private WBYService.WBYBinder binder;
    private DeviceDialog devicesDialog;
    private byte unit = AicareBleConfig.UNIT_KG;
    String mUserName="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wt_mchine);

        mListner=WeightMachine2Activity.this;

        Logger.log(Level.WARNING,TAG,"Weigh Machine 2 ()");
        globalVariable = (GlobalClass) getApplicationContext();

        txt_weight=findViewById(R.id.weight);
        txt_status= findViewById(R.id.status);
        bmi_value=  findViewById(R.id.bmi_value);

        bmi_value.setTextColor(Color.WHITE);

        age=findViewById(R.id.txtage_);
        height= findViewById(R.id.txtheight_);
        height.setText("0"); // by default set height to 0

        needle=findViewById(R.id.needle);
        records= findViewById(R.id.img_records);
        records.setOnClickListener(this);

        back_key=findViewById(R.id.imgBackKey);
        back_key.setOnClickListener(this);

        save=findViewById(R.id.btn_save);
        save.setOnClickListener(this);

        settings=findViewById(img_settings);
        users_help= findViewById(R.id.img_help);

        measurementType=  findViewById(R.id.measurementType);
        curvePathView= findViewById(R.id.curvePathView);

        settings.setOnClickListener(this);
        users_help.setOnClickListener(this);

        mUserName = getIntent().getExtras().getString(Constants.USER_NAME);




        devicesDialog=new DeviceDialog(this,this);
        final BluetoothManager bluetoothManager = (BluetoothManager)
                getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = bluetoothManager.getAdapter();

       /* if(mBluetoothAdapter!=null && !mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ConstantsP.BLUETOOTH_REQUEST_CODE);
        }
*/



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_REQUEST_COARSE_LOCATION);
                }
            }


        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

            if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                Toast.makeText(WeightMachine2Activity.this,"Please Enable your Location" +
                        " in your device Settings",Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        Constants.PERMISSION_REQUEST_COARSE_LOCATION);
            }


        }


        if (isDeviceConnected()) {
            binder.disconnect();
        } else {
            devicesDialog.show();
            devicesDialog.startScan();

        }


        final List<UserModel> UserModelList;
        if (globalVariable.getUsername() != null) {
            DatabaseManager.getInstance().openDatabase();
            UserModelList = new ArrayList<>(DatabaseManager.getInstance().getAllUserprofilecontent(mUserName,""));

            if (UserModelList.size() > 0) {
                age.setText(UserModelList.get(0).getSex());
                if(UserModelList.get(0).getHeight()!=null)
                {
                    height.setText(UserModelList.get(0).getHeight());
                }else
                    height.setText("0");
            }

        }


    }




    @Override
    protected void onResume() {
        super.onResume();
        TAG_SAVE="true";
        txt_weight.setText("- -");
        String dateTime[]= DateTime.getDateTime().split(" ");
        txt_status.setText(dateTime[0]);
        bmi_value.setText("- -");
        rotate_needle(0);





    }

    @Override
    protected void onPause() {
        super.onPause();
      //  scanLeDevice(false);
    }


    @Override
    public void calculate_bmi(float weight, float height) {
        bmi=(weight)/((height/100)*(height/100));



        try{
            String s = String.format("%.1f",bmi);
            bmi_value.setText(s);
            if(Float.parseFloat(s)>=25f){
                bmi_value.setTextColor(Color.RED);
            }else if(Float.parseFloat(s)>18.5f && Float.parseFloat(s)<25){
                bmi_value.setTextColor(Color.parseColor("#7CBC50"));
            }else{
                bmi_value.setTextColor(Color.WHITE);
            }

            int rotationValue =Math.round(Float.parseFloat(s)); // 3
            if(rotationValue<5)
                rotate_needle(-90);
            else if(rotationValue>40)
                rotate_needle(90);
            else
                rotate_needle(Constants.ROTATION_DEGREES.get(rotationValue));


        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public void rotate_needle(float to_degree) {

        RotateAnimation anim = new RotateAnimation(from_degree,to_degree,0f,needle.getHeight());
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(500);
        anim.setFillAfter(true);
        needle.startAnimation(anim);
        from_degree=to_degree;
    }


    private float kgToPound(float weightKG)
    {
        float pounds=weightKG*2.20f;
        return pounds;

    }

    private float poundToKg(float weightPound)
    {
        float Kg=weightPound/2.20f;
        return Kg;

    }



    float from_degree=0f;

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.img_records) {
            startActivity(new Intent(WeightMachine2Activity.this, WeightMachine2RecordActivity.class).
                    putExtra(Constants.USER_NAME,mUserName).
                    setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        else if (view.getId() == R.id.btn_save) {
            /* if(!save.getText().toString().equalsIgnoreCase("SAVE"))
            {
                if (!isBLEEnabled()) {
                    showBLEDialog();
                } else {
                    if (isDeviceConnected()) {
                        binder.disconnect();
                    } else {
                        devicesDialog.show();
                        devicesDialog.startScan();
                    }
                }

                return;
            }*/


            saveData(WeightMachine2Activity.this);



        }else if(view.getId()==R.id.imgBackKey)
        {
            finish();
        }else if(view.getId()==R.id.img_help)
        {
            Intent intent=new Intent(WeightMachine2Activity.this,WeighingMachineUsersguide.class);
            intent.putExtra("weigh machine","weigh machine 2");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else if(view.getId()==R.id.img_settings)
        {

            Logger.log(Level.INFO,TAG,"Settings Screen coming soon..");
            Intent intent=new Intent(WeightMachine2Activity.this,SettingsWeighingMachineActivity.class);
            //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This doesn't allow to work startActivityForResult
            startActivityForResult(intent,Constants.REQUEST_CODE);
        }
    }


    public ContentValues content_values_weight(String username, String userSelected,String date_time, String bmi, String measured_weight) {
        ContentValues values = new ContentValues();

        values.put(BplOximterdbHelper.USER_NAME, username);
        values.put(BplOximterdbHelper.USER_NAME_, userSelected);
        values.put(BplOximterdbHelper.MEASURED_WEIGHT_DATE,date_time);
        values.put(BplOximterdbHelper.MEASURED_BMI,bmi);
        values.put(BplOximterdbHelper.MEASURED_WEIGHT,measured_weight);
        return values;

    }


boolean isUpload;

    private void saveData(Context context){
        if (globalVariable.getUsername() != null) {

            try {
               final String username=globalVariable.getUsername();
                 String weight= txt_weight.getText().toString();

                if(!KEY_TAG.equals("") && !KEY_TAG.equals("KG"))
                {
                   float kgP= poundToKg(Float.parseFloat(weight));
                    weight = String.format("%.1f",kgP);
                }

               if(weight.equals("--") || weight.equals("- -")){
                   isUpload=false;
                   return;
               }

                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                database.insert(BplOximterdbHelper.TABLE_NAME_WEIGHT_MACHINE_B, null,
                        content_values_weight(username, mUserName,getDateTime(),
                                bmi_value.getText().toString(),weight));
                try{

                    database.insert(BplOximterdbHelper.TABLE_NAME_LAST_ACTIVITY_USERS,null, Utility.
                            lastActivityUsers(username,
                                    mUserName,Constants.DEVICE_PARAMETER_IWEIGH,DateTime.getCurrentDate(),globalVariable.getUserType()));

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                isUpload=true;
            }catch (Exception e)
            {
                isUpload=false;
                e.printStackTrace();
            }

            finally {
                if(isUpload)
                Toast.makeText(WeightMachine2Activity.this,
                        "Weight  Successfully saved", Toast.LENGTH_SHORT).show();
                bmi_value.setText("- -");
                rotate_needle(0);
                txt_weight.setText("- -");
            }

        }
    }


    String KEY_TAG="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Constants.BLUETOOTH_REQUEST_CODE && resultCode==RESULT_OK)
        {

        }else if(requestCode==Constants.BLUETOOTH_REQUEST_CODE && resultCode==RESULT_CANCELED)
        {
            finish();
        }

        else if(requestCode==Constants.REQUEST_CODE && resultCode== RESULT_OK && data!=null)
        {
            KEY_TAG= (String) data.getExtras().get("SETTINGS_TYPE");
            measurementType.setText("Weight in "+KEY_TAG);

            // Handle in LBS or Weight in KG

            if(KEY_TAG.equalsIgnoreCase("KG"))
            {
                unit = AicareBleConfig.UNIT_KG;
                binder.syncUnit(AicareBleConfig.UNIT_KG);
            }else{
                // for LBS
                unit = AicareBleConfig.UNIT_LB;
                binder.syncUnit(AicareBleConfig.UNIT_LB);
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==Constants.PERMISSION_REQUEST_COARSE_LOCATION)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }
    }


    //------ Add weigh2 data


    // Add I weigh 2 interface


    @Override
    public void onGetWeightData(final WeightData weightData) {
        L.e(TAG, weightData.toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (unit) {
                    case AicareBleConfig.UNIT_KG:
                        setWeighDataText(String.valueOf(weightData.getWeight()),"KG");
                        break;
                    case AicareBleConfig.UNIT_LB:
                        setWeighDataText(String.valueOf(ParseData.kg2lb(weightData.getWeight())),"LBS");
                        // convert lbs to kg for bmi
                        break;
                    case AicareBleConfig.UNIT_ST:
                        setWeighDataText(ParseData.kg2st(weightData.getWeight()),"KG");
                        break;
                    case AicareBleConfig.UNIT_JIN:
                        setWeighDataText(String.valueOf(ParseData.kg2jin(weightData.getWeight())),"KG");
                        break;
                }
                if (weightData.getTemp() != Double.MAX_VALUE) {
                    //tv_temp.setText(getString(R.string.temp, String.valueOf(weightData.getTemp())));
                }
            }
        });
    }

    // this function gets called multiple times

    private void setWeighDataText(String weight,String TAG) {
        txt_weight.setText(weight);
        if(TAG.equalsIgnoreCase("KG"))
        {
            mListner.calculate_bmi(Float.parseFloat(weight),Float.parseFloat(height.getText().toString()));
        }else{
            // weight is in lBS so convert to kg for BMI
            mListner.calculate_bmi( poundToKg(Float.parseFloat(weight)),Float.parseFloat(height.getText().toString()));

        }

        curvePathView.invalidate();
    }




    @Override
    public void onGetSettingStatus(AicareBleConfig.SettingStatus status) {
        L.e(TAG, "SettingStatus = " + status);
        switch (status) {
            case NORMAL:
              //  showInfo(getString(R.string.settings_status, getString(R.string.normal)));
                break;
            case LOW_POWER:
              //  showInfo(getString(R.string.settings_status, getString(R.string.low_power)));
                break;
            case LOW_VOLTAGE:
              //  showInfo(getString(R.string.settings_status, getString(R.string.low_voltage)));
                break;
            case ERROR:
              //  showInfo(getString(R.string.settings_status, getString(R.string.error)));
                break;
            case TIME_OUT:
               // showInfo(getString(R.string.settings_status, getString(R.string.time_out)));
                break;
            case UNSTABLE:
              //  showInfo(getString(R.string.settings_status, getString(R.string.unstable)));
                break;
            case SET_UNIT_SUCCESS:
              //  showInfo(getString(R.string.settings_status, getString(R.string.set_unit_success)));
                break;
            case SET_UNIT_FAILED:
              //  showInfo(getString(R.string.settings_status, getString(R.string.set_unit_failed)));
                break;
            case SET_TIME_SUCCESS:
              //  showInfo(getString(R.string.settings_status, getString(R.string.set_time_success)));
                break;
            case SET_TIME_FAILED:
               // showInfo(getString(R.string.settings_status, getString(R.string.set_time_failed)));
                break;
            case SET_USER_SUCCESS:
              //  showInfo(getString(R.string.settings_status, getString(R.string.set_user_success)));
                break;
            case SET_USER_FAILED:
              //  showInfo(getString(R.string.settings_status, getString(R.string.set_user_failed)));
                break;
            case UPDATE_USER_LIST_SUCCESS:
              //  showInfo(getString(R.string.settings_status, getString(R.string.update_user_list_success)));
                break;
            case UPDATE_USER_LIST_FAILED:
              //  showInfo(getString(R.string.settings_status, getString(R.string.update_user_list_failed)));
                break;
            case UPDATE_USER_SUCCESS:
              //  showInfo(getString(R.string.settings_status, getString(R.string.update_user_success)));
                break;
            case UPDATE_USER_FAILED:
              //  showInfo(getString(R.string.settings_status, getString(R.string.update_user_failed)));
                break;
            case NO_HISTORY:
              //  showInfo(getString(R.string.settings_status, getString(R.string.no_history)));
                break;
            case HISTORY_START_SEND:
               // showInfo(getString(R.string.settings_status, getString(R.string.history_start_send)));
                break;
            case HISTORY_SEND_OVER:
              //  showInfo(getString(R.string.settings_status, getString(R.string.history_send_over)));
                break;
            case NO_MATCH_USER:
              //  showInfo(getString(R.string.settings_status, getString(R.string.no_match_user)));
                break;
            case ADC_MEASURED_ING:
              //  showInfo(getString(R.string.settings_status, getString(R.string.adc_measured_ind)));
                break;
            case ADC_ERROR:
               // showInfo(getString(R.string.settings_status, getString(R.string.adc_error)));
                break;
            case UNKNOWN:
             //   showInfo(getString(R.string.settings_status, getString(R.string.unknown)));
                break;
        }
    }

    @Override
    public void onGetResult(int index, String result) {
        Logger.log(Level.DEBUG,TAG,"index = " + index + "; result = " + result);

        switch (index) {
            case WBYService.BLE_VERSION:
              //  showInfo("BLE Version " +result);
                Logger.log(Level.DEBUG,TAG,"BLE VERSION : result = " + result);
                break;
            case WBYService.USER_ID:
               // showInfo("USER ID "+result);
                break;
            case WBYService.MCU_DATE:
              //  showInfo("MCU DATE " +result);
                break;
            case WBYService.MCU_TIME:
               // showInfo("MCU_Time" +result);
                break;
            case WBYService.ADC:
               // showInfo("ADC" +result);
                break;
        }
    }

    @Override
    public void onGetFatData(boolean isHistory, BodyFatData bodyFatData) {
       /* L.e(TAG, "isHistory = " + isHistory + "; BodyFatData = " + bodyFatData.toString());
        if (isHistory) {
            showInfo(getString(R.string.history_data, bodyFatData.toString()));
        } else {
            showInfo(getString(R.string.body_fat_data, bodyFatData.toString()));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            if (isDeviceConnected()) {
                // binder.updateUser(user);
                // No need to update any user data
            }
        }*/
    }

    @Override
    public void onError(String msg, int errorCode) {

    }


    /*private void showInfo(String str) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ParseData.getCurrentTime());
        stringBuilder.append("\n\n");
        stringBuilder.append(str);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // dataList.add(stringBuilder.toString());
               // listAdapter.notifyDataSetChanged();
               // lv_data.setSelection(dataList.size() - 1);
            }
        });
    }*/




    @Override
    protected void onServiceBinded(WBYService.WBYBinder binder) {
        this.binder = binder;
    }

    @Override
    protected void onServiceUnbinded() {
        this.binder = null;
    }

    @Override
    protected void getAicareDevice(final BluetoothDevice device, final int rssi)
    {
        L.e(TAG, "name: " + device.getName() + "; address: " + device.getAddress());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (devicesDialog.isShowing()) {
                    devicesDialog.setDevice(new ExtendedBluetoothDevice(device, device.getName(), rssi));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        stopScan();
        if (isDeviceConnected()) {
            this.binder.disconnect();
        }
        super.onDestroy();
    }

    @Override
    public void scan() {
        startScan();
        devicesDialog.setScanning(isScanning());
    }

    @Override
    public void stop() {
        stopScan();
        devicesDialog.setScanning(isScanning());
    }

    @Override
    public void connect(BluetoothDevice device) {
        startConncet(device.getAddress());
    }

    @Override
    public void onStateChanged(String deviceAddress, int state) {
        super.onStateChanged(deviceAddress, state);
        switch (state) {
            case BleProfileService.STATE_CONNECTED:
                Logger.log(Level.DEBUG,TAG,"*** STATE CONNECTED ***");
                setStateTitle(deviceAddress, state);
                break;
            case BleProfileService.STATE_DISCONNECTED:
                Logger.log(Level.DEBUG,TAG,"*** STATE DISCONNECTED ***");
                setStateTitle(deviceAddress, state);
                break;
            case BleProfileService.STATE_SERVICES_DISCOVERED:
                Logger.log(Level.DEBUG,TAG,"*** STATE DISCOVERED() ***");
                break;
            case BleProfileService.STATE_INDICATION_SUCCESS:
                Logger.log(Level.DEBUG,TAG,"*** STATE INDICATION SUCCESS ***");
                if (this.binder != null) {
                    this.binder.queryBleVersion();
                }
                break;
        }
    }

    private void setStateTitle(final String deviceAddress, final int state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (state) {
                    case BleProfileService.STATE_CONNECTED:
                     Toast.makeText(WeightMachine2Activity.this,"Device is Connected",Toast.LENGTH_SHORT).show();
                        break;
                    case BleProfileService.STATE_DISCONNECTED:
                        Toast.makeText(WeightMachine2Activity.this,"Device is DisConnected",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }
        });
    }

}
