package test.bpl.com.bplscreens;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
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

import bluetooth.BmiListner;
import constantsP.Constants;
import constantsP.DateTime;
import constantsP.GlobalClass;
import customviews.CurvePathView;
import database.BplOximterdbHelper;
import database.DatabaseManager;
import logger.Level;
import logger.Logger;
import model.UserModel;

import static constantsP.Constants.PERMISSION_REQUEST_COARSE_LOCATION;
import static constantsP.DateTime.getDateTime;
import static test.bpl.com.bplscreens.R.id.img_settings;


public class WeighMachineActivity extends FragmentActivity implements BmiListner, View.OnClickListener {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wt_mchine);

        mListner=WeighMachineActivity.this;

        globalVariable = (GlobalClass) getApplicationContext();

        txt_weight=  findViewById(R.id.weight);
        txt_status= findViewById(R.id.status);
        bmi_value=  findViewById(R.id.bmi_value);

        bmi_value.setTextColor(Color.WHITE);

        age=  findViewById(R.id.txtage_);
        height=  findViewById(R.id.txtheight_);
        height.setText("0"); // by default set height to 0

        needle=  findViewById(R.id.needle);
        records=  findViewById(R.id.img_records);
        records.setOnClickListener(this);

        back_key=  findViewById(R.id.imgBackKey);
        back_key.setOnClickListener(this);

        save=  findViewById(R.id.btn_save);
        save.setOnClickListener(this);

        settings= findViewById(img_settings);
        users_help=  findViewById(R.id.img_help);

        measurementType=  findViewById(R.id.measurementType);
        curvePathView=  findViewById(R.id.curvePathView);

        settings.setOnClickListener(this);
        users_help.setOnClickListener(this);



        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if(mBluetoothAdapter!=null && !mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Constants.BLUETOOTH_REQUEST_CODE);
        }




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
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
                Toast.makeText(WeighMachineActivity.this,"Please Enable your Location" +
                        " in your device Settings",Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        Constants.PERMISSION_REQUEST_COARSE_LOCATION);
            }




        }


        final List<UserModel> UserModelList;
        if (globalVariable.getUsername() != null) {
            DatabaseManager.getInstance().openDatabase();
            UserModelList = new ArrayList<>(DatabaseManager.getInstance().
                    getprofilecontent(globalVariable.getUsername()));

            if (UserModelList.size() > 0) {
                age.setText(UserModelList.get(0).getAge());
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
        String dateTime[]=DateTime.getDateTime().split(" ");
        txt_status.setText(dateTime[0]);
        bmi_value.setText("- -");
        save.setVisibility(View.GONE);
        rotate_needle(0);
        scanLeDevice(true);



    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);

    }


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    /**
     * Device scan callback.
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {



                    Logger.log(Level.DEBUG,TAG,"(***)"+bytesToHexString(scanRecord));
                    //Jumper-medical_Scale
                    String bleName = getBleName(scanRecord);
                    if ("Jumper-medical_Scale".equalsIgnoreCase(bleName)) {

                        byte[] resultData = getResultData(scanRecord);
                        Logger.log(Level.DEBUG,TAG,"Result data="+resultData);
                        displayData(resultData);
                    }
                }
            });
        }
    };


    private String getBleName(byte[] scanRecord){
        boolean isFa = true;
        int posotion = 1;
        String name = "";

        while (isFa) {
            if (scanRecord.length < scanRecord[posotion - 1]
                    || posotion >= scanRecord.length || scanRecord[posotion-1] == 0) {
                isFa = false;
            }

            if (scanRecord[posotion] == (byte) 0x09) {
                int length = scanRecord[posotion - 1];
                byte[] nameByte = subByte(scanRecord, posotion + 1, posotion + length);
                if(nameByte!=null)
                {
                    name = new String(nameByte);
                    Logger.log(Level.DEBUG,TAG,"bleName-------->"+name);
                }

                isFa = false;
            } else {
                int length = scanRecord[posotion - 1];
                posotion = length + posotion + 1;
            }
        }
        return name;
    }


    private byte[] getResultData(byte[] scanRecord){
        boolean isFa = true;
        int position = 1;
        byte[] result = new byte[0];

        while (isFa) {
            if (scanRecord.length < scanRecord[position - 1] ||
                    position >= scanRecord.length || scanRecord[position-1] == 0) {
                isFa = false;
            }

            if (scanRecord[position] == (byte) 0xff) {

                int length = scanRecord[position - 1];
                result = subByte(scanRecord, position + 1, position + length);
                Logger.log(Level.DEBUG,TAG,"length="+length+" "+bytesToHexString(result));

                isFa = false;
            } else {
                int length = scanRecord[position - 1];
                position = length + position + 1;
            }
        }
        return result;
    }


    private void displayData(final byte[] result){

        float weight = (((result[6] & 0xFF) << 8) + (result[5] & 0xFF)) / 10.0f;
        float impedance = (((result[8] & 0xFF) << 8) + (result[7] & 0xFF));
        int status = result[3];
        int power = result[4];

        Logger.log(Level.DEBUG,TAG,"**Weight display**"+weight+" kg");

            txt_weight.setText( ""+weight);

        mListner.calculate_bmi(weight,Float.parseFloat(height.getText().toString()));
        curvePathView.invalidate();



        switch (status){
            case 1:
                txt_status.setText("status:   Reading complete");
                Logger.log(Level.DEBUG,TAG,"Reading Complete()");

                if(TAG_SAVE.equalsIgnoreCase("true"))
                {
                    save.setAlpha(1.0f);
                    save.setVisibility(View.VISIBLE);
                }else{
                    save.setAlpha(0.2f);
                }

                break;
            case 2:
                txt_status.setText("status:   waiting for test..");
                rotate_needle(0);
                save.setVisibility(View.GONE);
                break;
            case 3:
                txt_status.setText("status:   Reading from I-Weigh...");
                save.setVisibility(View.GONE);
                break;
            case 4:
                txt_status.setText("status:   The scales Not direct touch with the skin");
                save.setVisibility(View.GONE);
                break;
        }

    }


    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF; // get last 8 bits
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv).append(" ");
        }


        Logger.log(Level.DEBUG,TAG,"Scan device name="+stringBuilder.toString());

        return stringBuilder.toString();
    }


    public static synchronized byte[] subByte(byte[] data, int start, int end) {
        int len = end - start;
        if (end > data.length) {
            return null;
        }
        byte[] subByte = new byte[len];
        System.arraycopy(data, start, subByte, 0, len);
        return subByte;
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



            /*if(Float.parseFloat(s)<16){

                rotate_needle(-90);
            }

            else if(Float.parseFloat(s)>16 && Float.parseFloat(s)<17)
            {
                rotate_needle(-75);
                Logger.log(Level.DEBUG,TAG,"--Underweight--");
            }else if(Float.parseFloat(s)>18.5 && Float.parseFloat(s)<25)
            {
                rotate_needle(-15);
                Logger.log(Level.DEBUG,TAG,"--Underweight--");

            }else if(Float.parseFloat(s)>17 && Float.parseFloat(s)<18)
            {
                rotate_needle(-65);
                Logger.log(Level.DEBUG,TAG,"--Underweight--");

            }else if(Float.parseFloat(s)>18 && Float.parseFloat(s)<18.5)
            {
                rotate_needle(-58);
                Logger.log(Level.DEBUG,TAG,"--Underweight--");

            }
            else if(Float.parseFloat(s)>18.5 && Float.parseFloat(s)<20)
            {
                rotate_needle(-10);
                Logger.log(Level.DEBUG,TAG,"--Normal--");

            }

            else if(Float.parseFloat(s)>20 && Float.parseFloat(s)<21)
            {
                rotate_needle(10);
                Logger.log(Level.DEBUG,TAG,"--Normal--");

            }
            else if(Float.parseFloat(s)>21 && Float.parseFloat(s)<22)
            {
                rotate_needle(20);
                Logger.log(Level.DEBUG,TAG,"--Normal--");

            }

            else if(Float.parseFloat(s)>22 && Float.parseFloat(s)<25)
            {
                rotate_needle(40);
                Logger.log(Level.DEBUG,TAG,"--Normal--");

            }
            else if(Float.parseFloat(s)>25 && Float.parseFloat(s)<30)
            {
                rotate_needle(53);
                Logger.log(Level.DEBUG,TAG,"--Overweight--");

            }

            else if(Float.parseFloat(s)>30 && Float.parseFloat(s)<40){
                rotate_needle(90);
            }

            else{
                rotate_needle(-100);
            }*/


            /*if(flag)
            {
                flag=false;
                rotate_needle(60);

            }else{
                flag=true;
                rotate_needle(120);

            }*/


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

    private void rotate_needle()
    {
        RotateAnimation anim = new RotateAnimation(0,-90,0f,needle.getHeight());
        anim.setInterpolator(new LinearInterpolator());

        anim.setRepeatCount(3);
        anim.setDuration(700);
        anim.setFillAfter(true);
        needle.startAnimation(anim);
    }

    float from_degree=0f;

    @Override
    public void onClick(View view) {
        if (view == records) {
            startActivity(new Intent(WeighMachineActivity.this, WeighMachineRecordActivity.class).
                    setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        else if (view == save) {


            if(TAG_SAVE.equalsIgnoreCase("true"))
            {
                InsertData task=new InsertData(WeighMachineActivity.this);
                task.execute(new String[0]);
                TAG_SAVE="false";
                save.setAlpha(0.2f);
            }else{

            }


        }else if(view==back_key)
        {
            finish();
        }else if(view==users_help)
        {
            Intent intent=new Intent(WeighMachineActivity.this,WeighingMachineUsersguide.class);
            intent.putExtra("weigh machine","weigh machine 1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else if(view==settings)
        {

            Logger.log(Level.INFO,TAG,"Settings Screen coming soon..");
            Intent intent=new Intent(WeighMachineActivity.this,SettingsWeighingMachineActivity.class);
          //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // This doesn't allow to work startActivityForResult
            startActivityForResult(intent,Constants.REQUEST_CODE);
        }
    }


    public ContentValues content_values_weight(String username,String date_time,String bmi,String measured_weight) {
        ContentValues values = new ContentValues();

        values.put(BplOximterdbHelper.USER_NAME, username);
        values.put(BplOximterdbHelper.MEASURED_WEIGHT_DATE,date_time);
        values.put(BplOximterdbHelper.MEASURED_BMI,bmi);
        values.put(BplOximterdbHelper.MEASURED_WEIGHT,measured_weight);
        return values;


    }


    class InsertData extends AsyncTask<String,String,String>
    {

        ProgressDialog progressDialog;
        Context context;
        String isUploaded="false";

        public InsertData(Context context) {
            this.context = context;
        }


        @Override
        protected String doInBackground(String... strings) {

            if (globalVariable.getUsername() != null) {

                try {
                    String username=globalVariable.getUsername();
                    String weight= txt_weight.getText().toString();
                    SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                    database.insert(BplOximterdbHelper.TABLE_NAME_WEIGHT, null,
                            content_values_weight(username, getDateTime(),
                                    bmi_value.getText().toString(),weight
                           ));
                    isUploaded="true";
                }catch (Exception e)
                {
                    isUploaded="false";
                    e.printStackTrace();
                }

            }

            return isUploaded;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog==null)
            {
                progressDialog=new ProgressDialog(WeighMachineActivity.this);
                progressDialog.setMessage("Please wait while saving record into database");

                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

            }


        }



        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog!=null && progressDialog.isShowing())
            {
                progressDialog.dismiss();
                if(isUploaded.equalsIgnoreCase("true"))
                {
                    Toast.makeText(WeighMachineActivity.this,
                            "Weight  Successfully saved", Toast.LENGTH_SHORT).show();
                    scanLeDevice(false);
                    save.setVisibility(View.GONE);

                }

            }
        }


    }

     String KEY_TAG="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Constants.BLUETOOTH_REQUEST_CODE && resultCode==RESULT_OK)
        {
            if(mBluetoothAdapter!=null)
            {
                scanLeDevice(true);
            }



        }else if(requestCode==Constants.BLUETOOTH_REQUEST_CODE && resultCode==RESULT_CANCELED)
        {
            finish();
        }

        else if(requestCode==Constants.REQUEST_CODE && resultCode== RESULT_OK && data!=null)
        {
                KEY_TAG= (String) data.getExtras().get("SETTINGS_TYPE");
                measurementType.setText("Weight in "+KEY_TAG);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==Constants.PERMISSION_REQUEST_COARSE_LOCATION)
        {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

        }
    }
}
