package iweigh;

import android.annotation.*;
import android.app.*;
import android.bluetooth.*;
import android.bluetooth.le.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.*;
import android.support.v4.app.FragmentManager;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.neovisionaries.bluetooth.ble.advertising.*;

import java.util.*;

import cannyscale.*;
import constantsP.*;
import database.*;
import logger.*;
import model.*;
import test.bpl.com.bplscreens.*;

public class IweighHomeScreenActivityl extends FragmentActivity implements IweighNavigation{

    private   ArrayList<UserModel> UserModellist ;
    RelativeLayout bmiRelativeLayout,metabolismRelativeLayout,visceralFatR;
    RelativeLayout muscleMassR,bodyFatR,bodyAgeR,proteinR,boneMassR,bodyWaterR,lbmR;
    LinearLayout menu_bar;
    String headerBar="";

    private final String TAG=IweighHomeScreenActivityl.class.getSimpleName();
    ImageView settings,record;

    ProgressDialog pd;

    public ArrayAdapter<String> mLeDeviceListAdapter;
    //----
    private BluetoothAdapter mBluetoothAdapter;
    private boolean isDiscovery;
    // Stops scanning after some seconds.
    private static final long SCAN_PERIOD = 7000;



    private TextView readingWeight,bmiTxt;
    private Button btn_save;

    TextView date;
    String mUserName;
    private GlobalClass globalVariable;

    private TextView height,age,metabolicAge,visceralFat,bodyWater,bodyFat,boneMass,muscleMass;


    String sexType;

    int mAge=0,mHeight,mWeight;
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

        height=findViewById(R.id.txtheight_);
        age=findViewById(R.id.txtage_);
        settings=findViewById(R.id.img_settings);
        record=findViewById(R.id.img_records);

        date=findViewById(R.id.date);
        date.setText(DateTime.getCurrentDate());
        btn_save=findViewById(R.id.btn_save);
        btn_save.setText(getString(R.string.scan));

        bmiTxt=findViewById(R.id.bmiTxt);
        metabolicAge=findViewById(R.id.metabolicAge);

        readingWeight=findViewById(R.id.readingWeight);
        visceralFat=findViewById(R.id.visceralFat);
        bodyWater=findViewById(R.id.bodyWater);
        bodyFat=findViewById(R.id.bodyFat);
        boneMass=findViewById(R.id.boneMass);

        muscleMass=findViewById(R.id.muscleMass);
        mUserName= getIntent().getExtras().getString(Constants.USER_NAME);

        Logger.log(Level.DEBUG,TAG,"Get m UserName--"+mUserName);


        globalVariable = (GlobalClass) getApplicationContext();


        if(globalVariable.getUserType().equalsIgnoreCase(Constants.USE_TYPE_HOME)) {
            DatabaseManager.getInstance().openDatabase();

            UserModellist = new ArrayList<>(DatabaseManager.getInstance().getMemberprofilecontent(mUserName));

            if (UserModellist.size() > 0) {
                age.setText(UserModellist.get(0).getAge());
                //   weight.setText(UserModellist.get(0).getWeight());
                height.setText(UserModellist.get(0).getHeight());
                metabolicAge.setText(UserModellist.get(0).getAge());
               sexType= UserModellist.get(0).getSex();
                mAge=Integer.parseInt(UserModellist.get(0).getAge());
                mHeight= Integer.parseInt(UserModellist.get(0).getHeight());

            }
        }else {
            UserModellist = new ArrayList<>(DatabaseManager.getInstance().
                    getAllUserprofilecontent(globalVariable.getUsername(), ""));

            if (UserModellist.size() > 0) {

                age.setText(UserModellist.get(0).getAge());
                height.setText(UserModellist.get(0).getHeight());
                metabolicAge.setText(UserModellist.get(0).getAge());
                sexType= UserModellist.get(0).getSex();
                mAge=Integer.parseInt(UserModellist.get(0).getAge());
                mHeight= Integer.parseInt(UserModellist.get(0).getHeight());
            }
        }

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Constants.BLUETOOTH_REQUEST_CODE);
        }

        final ImageView  mBackKey=  findViewById(R.id.imgBackKey);
        mBackKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mHandler==null){
                    mHandler=new Handler();
                }
                scanLeDevice();
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
        if(isDiscovery)
        {
            unregisterReceiver(mReceiver);
        }
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



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ScanCallback scancallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);


            ScanRecord scanRecord= null;

                scanRecord = result.getScanRecord();

               // Logger.log(Level.DEBUG,TAG,"Scan Record -->"+scanRecord);

                if(result.getDevice().getName()!=null && result.getDevice().getName().contains("ADV"))
                {
                    List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanRecord.getBytes());

                    Log.i(" Advertisement Data", "Length=" +structures.size()+
                            " type= "+structures.get(0).getType()

                            +" 1st byte length="+structures.get(0).getLength()+" 'AND' "+
                            structures.get(0).getData().length+"\n"+" AD Structures "+" " +structures+"\n");




                    Log.i(" Structure 0 Data", "Length=" +structures.get(0).getData());
                    Log.i(" Structure 1 Data", "Length=" +structures.get(2).getData());


                    byte []manufacturerData=  structures.get(2).getData();


                    byte[] mFdata=   Arrays.copyOf(manufacturerData, manufacturerData.length);

                    for(int i=0;i<mFdata.length;i++){
                        byte val=mFdata[i];

                        Log.i("Byte Value of",+i+" is "+val);
                    }

                    byte versionData=mFdata[1];

                    if(versionData==0x20){
                        Log.i(" Version Data Got ", "Length=" +versionData);
                    }


                    if(mFdata[7]==0x01)
                    {

                        int body_weight_scale =  mFdata[7] & 0xFF;
                        Log.i("Wt. Type=","Body Weight Type is "+body_weight_scale);
                    }


                    if(mFdata[7]==0x00)
                    {

                        int body_weight_scale =  mFdata[7] & 0xFF;
                        Log.i("Wt. Type=","Body Weight Type is"+body_weight_scale);
                    }

                    if(mFdata[8]==0x00)
                    {

                        int body_weight_scale =  mFdata[9] & 0xFF;
                        Log.i("Lock Flag=","Weight Data is unlocked or unstable "+body_weight_scale);
                    }

                    if(mFdata[8]==0x01)
                    {


                        Logger.log(Level.DEBUG,TAG,"Byte Val="+HexUtil.byteToHexVal(mFdata));

                        int body_weight_scale =  mFdata[9] & 0xFF;
                        Log.i("Lock Flag=","Weight Data is locked  or LCD display perfect "+body_weight_scale);

                        //int weightData= combineMsbLsb(mFdata[10] & 0xFF,mFdata[11] & 0xFF);
                        float weightData= (HexUtil.byteToInt(mFdata[10])*256.0f+HexUtil.byteToInt(mFdata[11])*1.0f)/10.0f;
                        Log.d("Get Your Wt.=","Canny Scale is "+weightData);
                        readingWeight.setText(String.valueOf(weightData));
                        bmiTxt.setText(CannyAlgorithms.calculate_bmi(weightData,Integer.parseInt(height
                                .getText().toString().trim())));

                        visceralFat.setText(CannyAlgorithms.
                                visceralFat(Float.parseFloat(bmiTxt.getText().toString()),Integer.parseInt(
                                age.getText().toString())));
                        Logger.log(Level.DEBUG,TAG,"mFdata[12] "+HexUtil.byteToHex(mFdata[12])+" "+"mFdata[13]"
                                +HexUtil.byteToHex(mFdata[13]));


                    String impulse= (HexUtil.byteToHex(mFdata[12]))+ HexUtil.byteToHex(mFdata[13]);
                        Log.d("Impulse String","From Canny Scale is"+impulse);
                        int impulseVal;
                        if(impulse.equalsIgnoreCase("0000")){
                            impulseVal=0;
                        }else{
                            impulseVal=Integer.parseInt(impulse,16);
                            Log.d("Get Your impulseVal","From Canny Scale is "+impulseVal);
                        }

                        Log.d("SEX.=","SEX TYPE is "+sexType);

                        Logger.log(Level.DEBUG,TAG,"mFdata[12] "+HexUtil.byteToHex(mFdata[12])+" "+"mFdata[13]"
                                +HexUtil.byteToHex(mFdata[13]));

                        if(sexType.equalsIgnoreCase("Male")){
                            bodyFat.setText(CannyAlgorithms.
                                    bodyFatMale(Float.parseFloat(bmiTxt.getText().toString()),Integer.
                                            parseInt(age.getText().toString()),impulseVal));

                            bodyWater.setText(CannyAlgorithms.bodyWaterMale(Float.parseFloat(bodyFat.getText().toString()),mAge));
                            boneMass.setText(CannyAlgorithms.boneMassMale(Float.parseFloat(bodyFat.getText().toString()),mAge));
                            muscleMass.setText(CannyAlgorithms.muscleMassMale(mHeight,impulseVal,weightData,mAge));
                        }else{
                            bodyFat.setText(CannyAlgorithms.
                                    bodyFatFeMale(Float.parseFloat(bmiTxt.getText().toString()),Integer.
                                            parseInt(age.getText().toString()),impulseVal));

                            bodyWater.setText(CannyAlgorithms.bodyWaterFeMale(Float.parseFloat(bodyFat.getText().toString()),mAge));
                            boneMass.setText(CannyAlgorithms.boneMassFeMale(Float.parseFloat(bodyFat.getText().toString()),mAge));
                            muscleMass.setText(CannyAlgorithms.muscleMassFeMale(mHeight,impulseVal,weightData,mAge));
                        }

                    }

                    int lockSerialNo= mFdata[9] & 0xFF;
                    Log.i("SNo.=","Locked SN is "+lockSerialNo);


                    int a= mFdata[15] & 0xFF;
                    int b= mFdata[16] & 0xFF;
                    int c= mFdata[17] & 0xFF;
                    int d= mFdata[18] & 0xFF;
                    int e= mFdata[19] & 0xFF;
                    int f= mFdata[20] & 0xFF;

                    Log.i("Locked Mac","Getting  Mac Address from bytes is . " +a+":"+b+":"+c+":"+d+":"+e+
                            ";"+f);

                    if (structures instanceof IBeacon)
                    {
                        // An iBeacon was found.
                        IBeacon iBeacon = (IBeacon)structures;

                        Log.i("INFO=","Supported Manufacturer Specific Data is Apple iBeacon");

                    }




                    if (structures instanceof Ucode)
                    {
                        Ucode ucode = (Ucode)structures;
                        Log.i("INFO=","Supported Manufacturer Specific Data is Ucode");

                    }

                }








        }
        private void connect_Bpl_iOxy(String device_address)
        {
            Intent output = new Intent();
            if(pd.isShowing())
            {
                pd.dismiss();
            }
            output.putExtra(Constants.MAC_ADDRESS,device_address);
            setResult(RESULT_OK, output);
            finish();

        }
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Logger.log(Level.DEBUG,TAG,"On Batch Scan Results()");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.i(TAG,"On  Scan Failed()");

        }
    };



    Handler mHandler;
    boolean mScanning;



    private void scanLeDevice() {


        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                mScanning = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBluetoothAdapter.getBluetoothLeScanner().stopScan(scancallback);
                    pd.dismiss();
                }
            }
        }, SCAN_PERIOD);

        mScanning = true;
        pd = new ProgressDialog(IweighHomeScreenActivityl.this);
        pd.setMessage("Searching for Bpl Iweigh  device...");
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        isDiscovery=true;
        scan_bt_device();


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void  scan_bt_device()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
        if(mBluetoothAdapter.isDiscovering())
        {
            mBluetoothAdapter.cancelDiscovery();
        }



            mBluetoothAdapter.getBluetoothLeScanner().startScan(scancallback);
            pd.show();


    }






    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismiss progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found

                BluetoothDevice device =  intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Logger.log(Level.DEBUG,TAG,"---"+device.getName() + "\n" + device.getAddress());


            }
        }
    };

}
