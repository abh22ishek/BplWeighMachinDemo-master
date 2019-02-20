package biolight;

import android.annotation.*;
import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.database.sqlite.*;
import android.os.*;
import android.speech.tts.*;
import android.support.annotation.*;
import android.support.v7.app.AlertDialog;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.example.bluetoothlibrary.*;
import com.example.bluetoothlibrary.Impl.*;
import com.example.bluetoothlibrary.entity.*;

import java.math.*;
import java.text.*;
import java.util.*;

import constantsP.*;
import database.*;
import logger.*;
import test.bpl.com.bplscreens.R;
import test.bpl.com.bplscreens.*;

import static com.example.bluetoothlibrary.BluetoothLeClass.*;

public class BioLightDeviceHomeScreenActivity extends Activity {


    TextView sysPressure, diaPressure, pulseRateText, textOngoingBP, textError;
    Button save, comment, startTest, stopTest;

    TextView mmHgText, textUnit, bpPressureText;
    private RelativeLayout bpOngoingLayout, bpReadingLayout;

    CustomBPChartBioLight customBPChart;

    public static final int MACRO_CODE_12 = 12;
    public static final int MACRO_CODE_13 = 13;
    public static final int MACRO_CODE_14 = 14;
    public static final int MACRO_CODE_15 = 15;
    public static final int MACRO_CODE_16 = 16;

    public static final int MACRO_CODE_17 = 17;
    public static final int MACRO_CODE_18 = 18;
    public static final int MACRO_CODE_19 = 19;
    public static final int MACRO_CODE_20 = 20;
    public static final int MACRO_CODE_21 = 21;

    public static final int MACRO_CODE_23 = 23;
    public static final int MACRO_CODE_221 = 221;

    final String TAG = BioLightDeviceHomeScreenActivity.class.getSimpleName();

    ResolveWbp resolveWbp;
    ResolveWf100 resolveWf100 = new ResolveWf100();


    Handler mHandler;
    public int j = 2;


    private ImageView img_records,img_settings,img_help,imgBackKey;


    private  boolean mConnected;


    boolean mDeviceIdleStateApp;

    boolean mMeasurementGoingOn;

    boolean mDeviceIdleState;
    boolean mStartPressed;

    private  Set<String> biolightHashSet;
    Timer myTimer;
    MyTimerTask myTimerTask;

    Handler cuffHandler;
    boolean isStopButtonPressed;

    String mUserName,age,gender;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bio_light_wh);

        // wake device


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        bioList = new ArrayList<>();
        resolveWbp = new ResolveWbp();

        biolightHashSet=new HashSet<>();
        mDeviceIdleStateApp=true;


        mSyssSet=new HashSet<>();
        mDiasSet=new HashSet<>();

        bioListduplicateItems=new ArrayList<>();

        cuffHandler=new Handler();

        img_records=findViewById(R.id.img_records);
        img_settings=findViewById(R.id.img_settings);
        img_help=findViewById(R.id.img_help);
        imgBackKey=findViewById(R.id.imgBackKey);

        sysPressure = findViewById(R.id.sysPressure);
        diaPressure = findViewById(R.id.diaPressure);
        pulseRateText = findViewById(R.id.pulseRate);

        textError = findViewById(R.id.textError);
        bpPressureText = findViewById(R.id.bpPressureText);

        save = findViewById(R.id.Save);
        comment = findViewById(R.id.Comment);
        startTest = findViewById(R.id.startTest);
        stopTest = findViewById(R.id.stopTest);

        customBPChart = findViewById(R.id.customBPView);

        bpOngoingLayout = findViewById(R.id.bpOngoingLayout);
        bpReadingLayout = findViewById(R.id.bpReadLayout);
        textOngoingBP = findViewById(R.id.textOngoingBP);
        mmHgText = findViewById(R.id.mmHgText);
        textUnit = findViewById(R.id.textUnit);



        mUserName=getIntent().getExtras().getString(Constants.USER_NAME);
        age=getIntent().getExtras().getString("age");
        gender=getIntent().getExtras().getString("gender");

        imgBackKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mMeasurementGoingOn){
                   disconnectBiolight();
                }else{
                    Toast.makeText(BioLightDeviceHomeScreenActivity.this,"Please Wait while" +
                            "Measurement is going On",Toast.LENGTH_SHORT).show();
                }
            }
        });
        img_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(BioLightDeviceHomeScreenActivity.this,
                        BioLightUserHelpActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });


        img_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(BioLightDeviceHomeScreenActivity.this,
                        BioLightRecordActivity.class).putExtra(Constants.USER_NAME,mUserName).
                        putExtra("gender",gender).putExtra("age",age).
                        putExtra(Constants.MM_UNIT_MEASUREMENT_KEY,textUnit.getText().toString().trim()).
                        setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });


        img_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(BioLightDeviceHomeScreenActivity.this,
                        BioLightSettingsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                if(sysPressure.getText().toString().contains(".") || diaPressure.getText().toString().contains("."))
                {

                   int sys= KpaToMmhg(Float.parseFloat(sysPressure.getText().toString()));
                    int dia= KpaToMmhg(Float.parseFloat(diaPressure.getText().toString()));
                    database.insert(BplOximterdbHelper.TABLE_NAME_BIO_LIGHT,
                            null, Utility.content_values_biolight_bp_monitor(Constants.LOGGED_User_ID,mUserName,
                                    String.valueOf(sys),String.valueOf(dia)
                                    ,pulseRateText.getText().toString(),DateTime.getDateTime(),
                                    Utility.validateTypeBP(sysPressure.getText().toString(),diaPressure.getText().toString()),mComment));
                }else{
                    // Database records insertion with username

                    database.insert(BplOximterdbHelper.TABLE_NAME_BIO_LIGHT,
                            null, Utility.content_values_biolight_bp_monitor(Constants.LOGGED_User_ID,mUserName,
                                    sysPressure.getText().toString(),diaPressure.getText().toString()
                                    ,pulseRateText.getText().toString(),DateTime.getDateTime(),
                                   Utility.validateTypeBP(sysPressure.getText().toString(),diaPressure.getText().toString()),mComment));

                }


                Toast.makeText(BioLightDeviceHomeScreenActivity.this,"Record Saved",Toast.LENGTH_SHORT).show();

                try{

                    database.insert(BplOximterdbHelper.TABLE_NAME_LAST_ACTIVITY_USERS,null,Utility.
                            lastActivityUsers(Constants.LOGGED_User_ID,
                            mUserName,Constants.DEVICE_PARAMETER_IPRESSURE,DateTime.getCurrentDate()));

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                bpReadingLayout.setVisibility(View.VISIBLE);
                bpOngoingLayout.setVisibility(View.GONE);

                textUnit.setVisibility(View.VISIBLE);


                biolightHashSet.clear();

                save.setVisibility(View.GONE);
                if(comment.isEnabled()){
                    comment.setVisibility(View.GONE);
                }


            }
        });


        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                comment_dialog(BioLightDeviceHomeScreenActivity.this);

            }
        });



        stopTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConnected=false;
               mDeviceIdleStateApp=false;

               biolightHashSet.clear();
               bioListduplicateItems.clear();


              mStartPressed=true;

              mDeviceIdleState=false;

              isStopButtonPressed=true;

               bpPressureText.setVisibility(View.GONE);

                resolveWbp.onStopBleCommand(BioLightDeviceScanActivity.mBLE);
               // stopTest.setVisibility(View.GONE);

                bpOngoingLayout.setVisibility(View.VISIBLE);
                bpReadingLayout.setVisibility(View.GONE);


                textUnit.setVisibility(View.GONE);
                //textOngoingBP.setText("- -");
            //    textOngoingBP.setTextSize(35);

               // startTest.setVisibility(View.VISIBLE);
            }
        });

        // update ui layout

        bpPressureText.setText(getString(R.string.measuring));
        bpReadingLayout.setVisibility(View.GONE);
        textError.setVisibility(View.GONE);
        bpOngoingLayout.setVisibility(View.VISIBLE);


        //  textOngoingBP.setText(data);
        stopTest.setVisibility(View.VISIBLE);
        startTest.setVisibility(View.VISIBLE);

        comment.setVisibility(View.GONE);
        save.setVisibility(View.GONE);

         myTimer = new Timer();
         myTimerTask=new MyTimerTask();





        //    Logger.log(Level.DEBUG,TAG,"** On Going Bp condition gets called");


        //   BioLightDeviceScanActivity.mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
        BioLightDeviceScanActivity.mBLE.setOnDataAvailableListener(mOnDataAvailable);
        BioLightDeviceScanActivity.mBLE.setOnDisconnectListener(mOndisconnectListener);
        BioLightDeviceScanActivity.mBLE.setOnStopBioLight(mStopBioLight);


        resolveWbp.setOnWBPDataListener(onWBPDataListener);


        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Logger.log(Level.DEBUG,TAG,"Start BioLight Test");


                mStartPressed=true;
                mDeviceIdleState=true;

                mComment="";
                mConnected=true;
                mDeviceIdleStateApp=false;

                 mSyssSet.clear();
                 mDiasSet.clear();

                biolightHashSet.clear();
                bioListduplicateItems.clear();

                isStopButtonPressed=false;

                bpPressureText.setVisibility(View.VISIBLE);
                bpOngoingLayout.setVisibility(View.VISIBLE);
                bpReadingLayout.setVisibility(View.GONE);


               // startTest.setVisibility(View.GONE);
              //  stopTest.setVisibility(View.VISIBLE);



                if(save.getVisibility()==View.VISIBLE){
                    save.setVisibility(View.GONE);
                }

                if(comment.getVisibility()==View.VISIBLE){
                    comment.setVisibility(View.GONE);
                }



                textUnit.setVisibility(View.VISIBLE);
           //     textOngoingBP.setTextSize(60);


                customBPChart.set_XY_points(0,0);
                customBPChart.invalidate();


                resolveWbp.onSingleCommand(BioLightDeviceScanActivity.mBLE);


            }
        });


        initHandler();









        textToSpeech=new TextToSpeech(BioLightDeviceHomeScreenActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.US);

                    }
            }
        });


        textToSpeech.setPitch(1f);


        textOngoingBP.addTextChangedListener(new TextWatcher() {
                                                 @Override
                                                 public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                 }

                                                 @Override
                                                 public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                     Logger.log(Level.DEBUG,TAG,"(--yo escribo code---) --OnText CHanged");




                                                 }

                                                 @Override
                                                 public void afterTextChanged(Editable editable) {


                                                     startTest.setVisibility(View.GONE);
                                                     stopTest.setVisibility(View.VISIBLE);

                                                     mMeasurementGoingOn=true;

                                                         Logger.log(Level.DEBUG,TAG,"(--yo soy un hombre---) --AfterText CHanged");


                                                     try{
                                                         myTimer.cancel();
                                                         myTimer=new Timer();
                                                         myTimerTask=new MyTimerTask();
                                                         myTimer.schedule(myTimerTask,1000,4000);
                                                     }catch (Exception e){
                                                         e.printStackTrace();
                                                     }


                                                 }
                                             }
        );




    }


    TextToSpeech textToSpeech;

    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener() {
        /**
         * all data comes from here
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic) {

            Log.d(TAG, "onCharRead " + gatt.getDevice().getName() + " read " + characteristic.getUuid().toString() + " -> "
                    + Utils.bytesToHexString(characteristic.getValue()));


            mConnected=true;

            Logger.log(Level.DEBUG,TAG,"--------");
            if (GetCharacteristicID.equals(characteristic.getUuid())) {

                final byte[] datas = characteristic.getValue();
                Logger.log(Level.INFO, TAG, "Read Data Array[] from CharacterUId" + datas);



                if (BioLightDeviceScanActivity.config.getConnectPreipheralOpsition().getModel().equals(Constant.WT2)) {
                    // resolveWt2.calculateData_WT2(datas,mBLE,BioLightDeviceHomeScreenActivity.this);//to  resolve the data from wt2
                } else if (BioLightDeviceScanActivity.config.getConnectPreipheralOpsition().getModel().equals(Constant.M70C)) {
                    final byte[] data = characteristic.getValue();
                    if (data != null && data.length > 0) {
                        final StringBuilder stringBuilder = new StringBuilder(data.length);
                        for (byte byteChar : data)
                            stringBuilder.append(String.format("%02X ", byteChar));
                        String s = stringBuilder.toString();
                        // resolveM70c.calculateData_M70c(s);//resolve data from m70c
                    }
                } else {
                    //  resolvewt1.calculateData_WT1(datas,mBLE,BioLightDeviceHomeScreenActivity.this);//resolve data from wt1
                }


            } else {


                final byte[] data = characteristic.getValue();
                if (BioLightDeviceScanActivity.config.getConnectPreipheralOpsition().getBluetooth().equals(Constant.BLT_WBP)) {

                    resolveWbp.resolveBPData2(data, BioLightDeviceScanActivity.mBLE, BioLightDeviceHomeScreenActivity.this);//resolve data from wep
                } else if (BioLightDeviceScanActivity.config.getConnectPreipheralOpsition().getBluetooth().equals(Constant.AL_WBP)) {
                    resolveWbp.resolveALiBPData(data, getApplicationContext());//this is to resolve data from alibaba'device
                } else {

                    if (data != null && data.length > 0) {
                        final StringBuilder stringBuilder = new StringBuilder(
                                data.length);
                        for (byte byteChar : data)
                            stringBuilder.append(String.format("%02X ", byteChar));
                        String s = stringBuilder.toString();
                        s = s.replace(" ", "");
                        Log.e("s", "this is what" + "" + s);
                        resolveWf100.resolveBPData_wf(s);// this is to resolve data from wf100
                    }
                }
            }
        }

        /**
         * get the callback from write
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {


            Logger.log(Level.INFO,TAG,"----------Check for Stopping of BioLight--------------------");

            Log.e(TAG, "onCharWrite " + gatt.getDevice().getName()
                    + " write "
                    + characteristic.getUuid().toString()
                    + " -> "
                    + characteristic.getValue().toString());
        }
    };


    List<SycnBp> bioList;
    List<Integer> bioListduplicateItems;
    Set<Integer> mSyssSet;
    Set<Integer> mDiasSet;

    int mPrevious=0;






    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mHandler = new Handler() {

            @SuppressLint("DefaultLocale")
            @Override
            public void dispatchMessage(Message msg) {


                switch (msg.what) {


                    case MACRO_CODE_12://Cuff pressure

                        // mMeasurementGoingOn=true;

                        if(bpReadingLayout.getVisibility()==View.VISIBLE){
                            bpReadingLayout.setVisibility(View.GONE);
                        }




                        if(save.getVisibility()==View.VISIBLE){
                            save.setVisibility(View.GONE);
                        }


                        if(comment.getVisibility()==View.VISIBLE){
                            comment.setVisibility(View.GONE);
                        }

                      //  textOngoingBP.setTextSize(60);
                        bpPressureText.setVisibility(View.VISIBLE);
                        textUnit.setVisibility(View.VISIBLE);

                        Logger.log(Level.DEBUG, TAG, "((***((****CUFF Pressure**))**" + " " + msg.arg1);


                        bioListduplicateItems.add(msg.arg1);



                        if(bioListduplicateItems.size()>3){
                            if(bioListduplicateItems.get(0).equals(0) && bioListduplicateItems.get(1).equals(0)

                                    && bioListduplicateItems.get(2).equals(0)){

                                Logger.log(Level.WARNING,TAG,"(--Cuff Pressure is In IDLE STATE----)");
                                startTest.setVisibility(View.VISIBLE);
                                stopTest.setVisibility(View.GONE);
                                bioListduplicateItems.clear();
                                return;

                            }
                        }


                        if(bioListduplicateItems.size()>4){
                            Logger.log(Level.WARNING,TAG,"(--Cuff Pressure is In WORKING STATE----)");
                          //  mHandler.sendEmptyMessage(1000);

                        }



                      //  if(!mStartPressed){
                           // stopTest.setVisibility(View.VISIBLE);
                            // startTest.setVisibility(View.GONE);
                       // }


                        if(!mMeasurementGoingOn){
                            Logger.log(Level.WARNING,TAG,
                                    "((****))--Checking Measurement Reading is stopped--(***)");
                        }



                        /*



                        Logger.log(Level.DEBUG, TAG, "****CUFF Pressure****" + " " + msg.arg1 +"mConnected="+mConnected);


                            if(!mConnected){
                                textOngoingBP.setText("0");
                                bpPressureText.setVisibility(View.GONE);
                                bpOngoingLayout.setVisibility(View.GONE);
                                return;
                            }
*/



                            Logger.log(Level.DEBUG,TAG,"-----get Current unit while measuring Cuff Pressure()-----"
                                    +BioLightDeviceHomeScreenActivity.this.getCurrentUnit
                                    (Constants.MM_UNIT_MEASUREMENT_KEY));

                        if(BioLightDeviceHomeScreenActivity.this.getCurrentUnit
                                (Constants.MM_UNIT_MEASUREMENT_KEY).equalsIgnoreCase(Constants.MM_HG)){
                            textOngoingBP.setText(new StringBuilder().append(msg.arg1).toString());
                        }else{
                            textOngoingBP.setText(mmHgTokPa(msg.arg1));
                        }



                        break;


                    case MACRO_CODE_13://web the final result

                        Logger.log(Level.DEBUG, TAG, "***Get Final BP Result***"+
                                msg.arg1+"-Another Parameter-"+msg.arg2);

                        mSyssSet.add(msg.arg1);
                        mDiasSet.add(msg.arg2);
                        bpOngoingLayout.setVisibility(View.VISIBLE);





                        Logger.log(Level.DEBUG,TAG,"SYS  DIA Set size-->"+mSyssSet.size()+" SYS  DIA Set size-->"+mDiasSet.size());

                        if (mSyssSet.size()>1 || mDiasSet.size()> 1) {

                            bpPressureText.setVisibility(View.VISIBLE);
                            bpPressureText.setText(getString(R.string.blood_press));
                            Logger.log(Level.DEBUG, TAG, "***Get Final BP Result is Visible***");

                            mMeasurementGoingOn=false;

                            bpReadingLayout.setVisibility(View.VISIBLE);
                            bpOngoingLayout.setVisibility(View.GONE);


                            save.setVisibility(View.VISIBLE);
                            startTest.setVisibility(View.VISIBLE);
                            comment.setVisibility(View.VISIBLE);

                            mStartPressed=false;

                            pointX=msg.arg1;
                            pointY=msg.arg2;
                            // enable voice on phone
                            if(getSWitchVoiceState(Constants.BIO_LIGHT_VOICE_STATUS)!=null)
                            {
                                if(getSWitchVoiceState(Constants.BIO_LIGHT_VOICE_STATUS).
                                        equalsIgnoreCase(Constants.SWITCH_ON))
                                {
                                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        // only for lollipop and newer versions
                                        textToSpeech.speak(Utility.validatePressureForVoice(pointX,pointY,BioLightDeviceHomeScreenActivity.this),
                                                TextToSpeech.QUEUE_FLUSH,null,"uk");

                                    }
                                    Logger.log(Level.INFO,TAG,"GET Shared Pref for Sound="+getSWitchVoiceState( Constants.BIO_LIGHT_VOICE_STATUS));
                                    Logger.log(Level.INFO,TAG,"Text to voice is  working");
                                }
                            }


                            try {
                                myTimer.cancel();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            // put marker on map




                            customBPChart.set_XY_points(pointY,pointX);
                            customBPChart.invalidate();

                            if(BioLightDeviceHomeScreenActivity.this.getCurrentUnit
                                    (Constants.MM_UNIT_MEASUREMENT_KEY).equalsIgnoreCase(Constants.MM_HG)){
                                sysPressure.setText(String.format("%d", msg.arg1));
                                diaPressure.setText(MessageFormat.format("{0}", msg.arg2));
                                pulseRateText.setText( msg.obj.toString());
                            }else{
                                sysPressure.setText(mmHgTokPa( msg.arg1));
                                diaPressure.setText(mmHgTokPa( msg.arg2));
                                pulseRateText.setText(mmHgTokPa(Integer.parseInt(msg.obj.toString())));
                            }


                            if(stopTest.getVisibility()==View.VISIBLE){
                                stopTest.setVisibility(View.GONE);
                            }

                            textUnit.setVisibility(View.VISIBLE);
                            mSyssSet.clear();
                            mDiasSet.clear();


                        }


                        String str2 = msg.getData().getString("is_guest_mode");
                        //    isguestmode.setText(""+str2);
                        break;


                    case MACRO_CODE_14://the state and version of the wbp
                        // bettray.setText(""+msg.arg1);

                        String str_version = msg.getData().getString("version");
                        //   version_show.setText(str_version);
                        String str_bleState = msg.getData().getString("bleState");

                        Log.e("str_bleState", str_bleState);

                        Logger.log(Level.DEBUG,TAG,"(((----BioLight Work Mode----)))="
                                +str_bleState);




                        biolightHashSet.add(str_bleState);




                        Logger.log(Level.DEBUG,TAG,"---mStartPressed---="+mStartPressed);


                        // direct connect



                        // idle connect

                      /*  if(mDeviceIdleState && mStartPressed && str_bleState.equals("0")){

                            mPxCount++;
                            Logger.log(Level.DEBUG,TAG,"---count mPX value"+mPxCount);

                            if(mPxCount==2){
                                stopTest.setVisibility(View.GONE);
                                startTest.setVisibility(View.VISIBLE);
                                mPxCount=0;
                                mDeviceIdleState=false;

                            }



                        }


                        if(!mStartPressed){
                            if(str_bleState.equals("0") ){   // STOP
                                mDeviceIdleState=true; // this only works from device side
                                stopTest.setVisibility(View.GONE);
                                startTest.setVisibility(View.VISIBLE);

                            }else                    // START
                            {
                                stopTest.setVisibility(View.VISIBLE);
                                startTest.setVisibility(View.GONE);
                                mDeviceIdleState=true;

                            }

                            if(bpReadingLayout.getVisibility()==View.VISIBLE){
                                stopTest.setVisibility(View.GONE);
                                startTest.setVisibility(View.VISIBLE);
                            }
                        }else{

                            Logger.log(Level.DEBUG,TAG,"--YO SOY UN HOMBRE---");
                            if(isStopButtonPressed){
                                if(stopTest.getVisibility()==View.VISIBLE){
                                    stopTest.setVisibility(View.GONE);

                                }

                                if(startTest.getVisibility()==View.GONE){
                                    startTest.setVisibility(View.VISIBLE);

                                }


                                mXcount++;
                                Logger.log(Level.DEBUG,TAG,"---count MX value"+mXcount);

                                if(mXcount==2){
                                    isStopButtonPressed=false;
                                    mXcount=0;
                                }
                            }




                        }

*/





                        //    wbpmode.setText(str_bleState);
                        String str_devState = msg.getData().getString("devState");
                        Logger.log(Level.DEBUG,TAG,"(((----BioLight Device  Mode----)))="+str_devState);



                       /* if(!mDeviceIdleState ){                       // START
                            startTest.setVisibility(View.GONE);
                            stopTest.setVisibility(View.VISIBLE);
                            textOngoingBP.setTextSize(60);





                        }
                        else{
                            startTest.setVisibility(View.VISIBLE);    // STOP
                            stopTest.setVisibility(View.GONE);
                            bpPressureText.setVisibility(View.GONE);

                            textUnit.setVisibility(View.GONE);
                            textOngoingBP.setText("Waiting for Measurement ..");
                            textOngoingBP.setTextSize(35);
                        }*/


                        //    work_mode.setText(str_devState);
                        break;


                    case MACRO_CODE_15://error
                        Logger.log(Level.WARNING,TAG,"--ERROR MACRO_CODE_15--"+msg.obj);

                        if((msg.obj)instanceof String && msg.obj.toString()!="0"){
                            Logger.log(Level.DEBUG,TAG,"Instance of string");
                            bpPressureText.setVisibility(View.GONE);
                            bpOngoingLayout.setVisibility(View.GONE);

                            showDialog(BioLightDeviceHomeScreenActivity.this,msg.obj.toString());


                        }


                    case MACRO_CODE_16://sync total datas

                        Logger.log(Level.WARNING, TAG, "Get =" + msg.obj);
                        /*SycnBp syncBp= (SycnBp) msg.obj;
                        bioList.add(syncBp);

                        sysPressure.setText(bioList.get(0).getSys());
                        diaPressure.setText(bioList.get(0).getDia());
                        pulseRateText.setText(bioList.get(0).getHr());*/

                        break;
                    case MACRO_CODE_17://softversion

                        Log.e("softversion", "" + msg.obj);
                        break;



                        case MACRO_CODE_18://BTtime
                            Log.e("time", "" + msg.obj);
                            break;


                    case MACRO_CODE_19://battery
                        Log.e("battery", "" + msg.obj);
                        break;

                        case MACRO_CODE_20://fetal wf100
                            Log.e("fetal", "" + msg.arg1);
                            break;


                    case MACRO_CODE_21://voice wf100
                        Log.e("voice", "" + msg.obj);

                        break;

                    case MACRO_CODE_221://the user information/wbp

                        Logger.log(Level.WARNING, TAG, "Bio Light got Disconnected");

                        Intent intent = new Intent(BioLightDeviceHomeScreenActivity.this, SelectParameterActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;



                        case 1200:

                        Logger.log(Level.DEBUG,TAG,"--------------1200------------is executed----------------");


                        stopTest.setVisibility(View.GONE);
                        startTest.setVisibility(View.VISIBLE);



                        break;





                    default:

                        break;
                }
            }
        };
        BioLightDeviceScanActivity.config.setMyFragmentHandler(mHandler);
    }




    boolean pxCounter;



    class MyTimerTask extends TimerTask {
        public void run() {
            // check whether flag is true or not

            pxCounter=false;
            mMeasurementGoingOn=false;
            Logger.log(Level.WARNING,TAG,"making pxCounter flag false in 1200 ms");
            Logger.log(Level.DEBUG,TAG,"--check boolean flag pxCounter--"+pxCounter);

            mHandler.sendEmptyMessage(1200);

        }
    }





    public ResolveWbp.OnWBPDataListener onWBPDataListener = new ResolveWbp.OnWBPDataListener() {



        @Override
        public void onMeasurementBp(int temp) {
            Log.e("temp.", "" + temp);
            Message msg = new Message();
            msg.what = MACRO_CODE_12;
            msg.arg1 = temp;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onMeasurementfin(final int SYS, final int DIA, final int PR, final Boolean isguestmode) {

            Logger.log(Level.DEBUG, TAG, "SYS...." + SYS);
            Logger.log(Level.DEBUG, TAG, "DIA...." + DIA);
            Logger.log(Level.DEBUG, TAG, "PR...." + PR);

            Logger.log(Level.DEBUG, TAG, "Is Guestmode " + isguestmode);


            Message msg = new Message();
            msg.what = MACRO_CODE_13;
            msg.arg1 = SYS;
            msg.arg2 = DIA;
            msg.obj = PR;



            Bundle bundle = new Bundle();
            bundle.putString("isguestmode", " " + isguestmode);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }

        /**
         * when measure failed
         * @param
         */
        @Override
        public void onErroer(Object obj) {
            Log.e("error", "" + obj);

            Message message = new Message();
            message.what = MACRO_CODE_15;
            message.obj = obj;
            mHandler.sendMessage(message);
        }


        @Override
        public void onState(int btbattey, String bleState, String version, String devState) {


            Log.e("bleState.............", "" + bleState);
            Log.e("btbattey.............", "" + btbattey);


            Log.e("devState.............", "" + devState);
            Log.e("version.............", "" + version);


            Message msg = new Message();
            msg.what = MACRO_CODE_14;
            msg.arg1 = btbattey;
            Bundle bundle = new Bundle();


            bundle.putString("bleState", "" + bleState);
            bundle.putString("version", "" + version);
            bundle.putString("devState", "" + devState);

            msg.setData(bundle);
            mHandler.sendMessage(msg);


        }


        @Override
        public void onSycnBp(ArrayList<SycnBp> sycnBps) {

            for (SycnBp sycnBp : sycnBps) {
                Log.e("the sync data ", "sys" + sycnBp.getSys() +
                        "dia" + sycnBp.getDia() + "pr" + sycnBp.getHr() + "time" + sycnBp.getTime());
            }

            //wbpDatalistAdapter=new WbpDatalistAdapter(DeviceScanActivity.this,sycnBps);

            Message message = new Message();
            message.arg1 = sycnBps.size();
            message.what = MACRO_CODE_16;
            mHandler.obtainMessage(message.what, sycnBps).sendToTarget();

            //  mHandler.sendMessage(message);

        }

        @Override
        public void onTime(String wbp_time) {

            Message message = new Message();
            message.obj = wbp_time;
            message.what = MACRO_CODE_18;
            mHandler.sendMessage(message);

        }

        /**
         *
         * @param user
         */
        @Override
        public void onUser(int user) {
            Log.e("user::", "......" + user);
            Message message = new Message();
            message.arg1 = user;
            message.what = MACRO_CODE_23;
            mHandler.sendMessage(message);

        }
    };


    BluetoothLeClass.OnDisconnectListener mOndisconnectListener = new BluetoothLeClass.OnDisconnectListener() {
        @Override
        public void onDisconnect(BluetoothGatt gatt) {

            Message msg = new Message();
            msg.what = MACRO_CODE_221;
            msg.arg1 = 1;

            BioLightDeviceScanActivity.config.getMyFragmentHandler().sendMessage(msg);


        }
    };


    BluetoothLeClass.OnStopBioLightListner mStopBioLight=new BluetoothLeClass.OnStopBioLightListner() {
        @Override
        public void stopioLight() {

            Logger.log(Level.DEBUG,TAG,"<<--Stop BioLight-->>>");
            textOngoingBP.setText("$$");
        }
    };


    boolean exit;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.log(Level.INFO,TAG,"On Destroy() -- gets Called");

        try{
            myTimer.cancel();
        }catch (Exception e){
            e.printStackTrace();
        }


       disconnectBiolight();

        if(textToSpeech!=null)
        {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }



    }

    @Override
    public void onBackPressed() {

        if (exit) {
            super.onBackPressed();
            disconnectBiolight();

        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }


    private void disconnectBiolight()
    {
        if (BioLightDeviceScanActivity.mBLE!=null && BioLightDeviceScanActivity.mBLE.isBLEConnected()) {
            resolveWbp.onStopBleCommand(BioLightDeviceScanActivity.mBLE);
            BioLightDeviceScanActivity.mBLE.disconnect();
            BioLightDeviceScanActivity.mBLE.close();

        }


        Intent intent = new Intent(BioLightDeviceHomeScreenActivity.this, SelectParameterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }

    boolean mKpa;
    int pointX=0;
    int pointY=0;


    @Override
    protected void onResume() {
        super.onResume();

        if(BioLightDeviceHomeScreenActivity.this.getCurrentUnit
                (Constants.MM_UNIT_MEASUREMENT_KEY).equalsIgnoreCase(Constants.MM_HG)){

            textUnit.setText(getString(R.string.mm_hg));
            mmHgText.setText(getString(R.string.mm_hg));
            mKpa=false;
        }

        else{
            textUnit.setText(getString(R.string.kpa));
            mmHgText.setText(getString(R.string.kpa));
            mKpa=true;
        }


    }


    // get shared pref file for current Unit Measurement
    public String getCurrentUnit(String keyName)
    {
        SharedPreferences toggleStatePref;
        toggleStatePref = BioLightDeviceHomeScreenActivity.this.
                getSharedPreferences(Constants.BIOLIGHT_PREF, Context.MODE_PRIVATE);

        final String toggleUnitMeasurementState=toggleStatePref.getString(keyName, Constants.MM_HG);
        Logger.log(Level.INFO, TAG, "((get current unit state from shared pref**=))"
                + toggleUnitMeasurementState);
        return toggleUnitMeasurementState;

    }


    // convert mmHg to kPa
    @SuppressLint("DefaultLocale")
    public String mmHgTokPa(int mmHg)
    {
        float kPaValue;
        kPaValue = (float) (mmHg *0.133322);
        return String.format("%.1f",kPaValue);
    }


    public  int KpaToMmhg(float kpa) {
        float tempUnit = 7.5006168f;
        float tempmmhg = tempUnit * kpa;
        BigDecimal b = new BigDecimal(tempmmhg);
        int mmhg = b.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        return mmhg;
    }

    private String getSWitchVoiceState(String key_name)
    {
        SharedPreferences switchStatePref;
        switchStatePref = BioLightDeviceHomeScreenActivity.this.
                getSharedPreferences(Constants.BIOLIGHT_PREF_VOICE, Context.MODE_PRIVATE);

        final String switchState=switchStatePref.getString(key_name, Constants.SWITCH_ON);
        Logger.log(Level.INFO, TAG, "((get switch state from shared pref**=))" + switchState);
        return switchState;

    }


    // show dialog box

    AlertDialog alert;
    int mDialogCount=0;

    private void showDialog(Context context,String title)
    {
        mDialogCount++;
        if(mDialogCount==1){
            android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(R.string.bio_light_err_cuff)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if(alert.isShowing()){
                                alert.dismiss();
                               Intent intent = new Intent(BioLightDeviceHomeScreenActivity.this, SelectParameterActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                mDialogCount=0;

                            }

                        }
                    });



            alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            logger.Logger.log(logger.Level.DEBUG, TAG, "Alert dialog box gets called");
            if(!alert.isShowing()){
                alert.show();
            }

        }

    }

    String mComment="";

    private void comment_dialog(Context context)
    {
     final Dialog  dialog = new Dialog(context);

        if(dialog.getWindow()!=null){
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogBoxAnimation;
            dialog.setContentView(R.layout.dialog_comment);
        }

        final EditText content= dialog.findViewById(R.id.content);
        final TextView header=  dialog.findViewById(R.id.header);
        final Button save=  dialog.findViewById(R.id.save);
        final Button cancel=  dialog.findViewById(R.id.cancel);

        cancel.setVisibility(View.VISIBLE);

        header.setText(getResources().getString(R.string.comment));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mComment=content.getText().toString();
                comment.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
















}