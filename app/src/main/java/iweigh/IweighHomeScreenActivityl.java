package iweigh;

import android.annotation.*;
import android.app.*;
import android.bluetooth.*;
import android.bluetooth.le.*;
import android.content.*;
import android.database.sqlite.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.*;
import android.support.v4.app.FragmentManager;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.canny.xue.bialib.BodyFat;
import com.neovisionaries.bluetooth.ble.advertising.*;

import java.math.*;
import java.util.*;

import canny.*;
import constantsP.*;
import database.*;
import logger.*;
import model.*;
import pl.bclogic.pulsator4droid.library.*;
import test.bpl.com.bplscreens.*;
import test.bpl.com.bplscreens.R;

import static constantsP.DateTime.getDateTime;

public class IweighHomeScreenActivityl extends FragmentActivity implements IweighNavigation{

    private ArrayList<UserModel> UserModellist ;
    RelativeLayout bmiRelativeLayout,metabolismRelativeLayout,visceralFatR;
    RelativeLayout muscleMassR,bodyFatR,bodyAgeR,proteinR,boneMassR,bodyWaterR,lbmR;
    LinearLayout menu_bar;
    String headerBar="";

    private final String TAG=IweighHomeScreenActivityl.class.getSimpleName();
    ImageView settings,record,help;

    ProgressDialog pd;

    public ArrayAdapter<String> mLeDeviceListAdapter;
    //----
    private BluetoothAdapter mBluetoothAdapter;
    private boolean isDiscovery;
    // Stops scanning after some seconds.
    private static final long SCAN_PERIOD = 10000;



    private TextView readingWeight,bmiTxt;
    private Button btn_save;

    TextView date;
    String mUserName;
    private GlobalClass globalVariable;

    private TextView height,age,metabolicAge,visceralFat,bodyWater,bodyFat,boneMass,muscleMass,metabolismKcal,protein,lbm,obesity;


    String sexType;

    int mAge=0,mHeight;

    String mBMI,mWeight, mMetabolism="NA",mbodyWater,mBodyFat,
            mBoneMass,mProtein="NA",mVisceralFat,mBodyAge="NA",mMuscleMass,mLBM="NA",mObesity="NA";


    Animation animation;
     Dialog dialog;
    private void SearchScreenDialog(Context context)
    {
         dialog = new Dialog(context);
        if(dialog.getWindow()!=null)
        {
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogBoxAnimation;
            dialog.setContentView(R.layout.iweigh_search);
        }


    /*    Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);*/

       //dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        final ImageView arrw,weight,weightPulse;
        final PulsatorLayout pulsatorLayout;

        final TextView text;


            arrw=dialog.findViewById(R.id.arrow);

            weight=dialog.findViewById(R.id.weightT);
            pulsatorLayout=dialog.findViewById(R.id.pulsator);
            pulsatorLayout.setVisibility(View.GONE);

            text=dialog.findViewById(R.id.text);

        animation = AnimationUtils.loadAnimation(IweighHomeScreenActivityl.this, R.anim.slide_in_out);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(1000);

        arrw.startAnimation(animation);
        text.setVisibility(View.GONE);
         pulsatorLayout.start();




        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try{
                    arrw.clearAnimation();
                   // animation.cancel();
                    arrw.setVisibility(View.GONE);
                    weight.setVisibility(View.GONE);
                    pulsatorLayout.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                     scanLeDevice();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, 5000);


        dialog.show();
    }




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
        metabolismKcal=findViewById(R.id.metabolismKcal);
        height=findViewById(R.id.txtheight_);
        age=findViewById(R.id.txtage_);
        settings=findViewById(R.id.img_settings);
        record=findViewById(R.id.img_records);
        protein=findViewById(R.id.protein);
        lbm=findViewById(R.id.lbm);
        obesity=findViewById(R.id.obesity);
        date=findViewById(R.id.date);
        date.setText(DateTime.getCurrentDate());
        btn_save=findViewById(R.id.btn_save);
        btn_save.setText(getString(R.string.scan));

        bmiTxt=findViewById(R.id.bmiTxt);
        metabolicAge=findViewById(R.id.metabolicAge);




        //--------
        help=findViewById(R.id.img_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(IweighHomeScreenActivityl.this);

                    Intent intent=new Intent(IweighHomeScreenActivityl.this,
                            WeighingMachineUsersguide.class);
                    intent.putExtra("weigh machine","weigh machine 2");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent,options.toBundle());
                }else{

                    Intent intent=new Intent(IweighHomeScreenActivityl.this, WeighingMachineUsersguide.class);
                    intent.putExtra("weigh machine","weigh machine 2");
                    intent. setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
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

                SearchScreenDialog(IweighHomeScreenActivityl.this);
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

                    startActivity(new Intent(IweighHomeScreenActivityl.this, IweighRecordsActivity.class).
                            putExtra(Constants.USER_NAME,mUserName)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), options.toBundle());
                } else {
                    startActivity(new Intent(IweighHomeScreenActivityl.this, IweighRecordsActivity.class)
                            .  putExtra(Constants.USER_NAME,mUserName)
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
                    if(dialog.isShowing())
                        dialog.dismiss();

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

                        Log.d("SEX.=","SEX TYPE is "+sexType+" age is "+mAge +"  "+"Height is "+mHeight);

                        Logger.log(Level.DEBUG,TAG,"mFdata[12] "+HexUtil.byteToHex(mFdata[12])+" "+"mFdata[13]"
                                +HexUtil.byteToHex(mFdata[13]));

                        if(sexType.equalsIgnoreCase("Male")){

                            //---------------------

                            float bestweight=BodyFat.getBestWeight(false,mAge,mHeight,weightData,impulseVal/10,0);
                            float bmi=BodyFat.getBMI(false,mAge,mHeight,weightData,impulseVal/10,0);
                            float fat=BodyFat.getFat(false,mAge,mHeight,weightData,impulseVal/10,0);
                            float tbw=BodyFat.getTbw(false,mAge,mHeight,weightData,impulseVal/10,0);
                            float mus=BodyFat.getMus(false,mAge,mHeight,weightData,impulseVal/10,0);
                            float bone=BodyFat.getBone(false,mAge,mHeight,weightData,impulseVal/10,0);
                            float kcal=BodyFat.getKcal(false,mAge,mHeight,weightData,impulseVal/10,0);
                            float vfat=BodyFat.getVfat(false,mAge,mHeight,weightData,impulseVal/10,0);
                            float bodyage=BodyFat.getBage(false,mAge,mHeight,weightData,impulseVal/10,0);
                            float proteinX=BodyFat.getProtein(false,mAge,mHeight,weightData,impulseVal/10,0);
                            float  lbmX=BodyFat.getWeightWithoutFat(false,mAge,mHeight,weightData,impulseVal/10,0);
                            float  obaserate=BodyFat.getObeseRate(false,mAge,mHeight,weightData,impulseVal/10,0);
                            float   score=BodyFat.getScore(false,mAge,mHeight,weightData,impulseVal/10,0);
                            float    bodyshape=BodyFat.getBodyShape(false,mAge,mHeight,weightData,impulseVal/10,0);


                            //---------------------


                            bmi=TwoReservations(bmi);
                            fat=TwoReservations(fat);
                            tbw=TwoReservations(tbw);

                            mus=TwoReservations(mus);
                            bone=TwoReservations(bone);

                            kcal=TwoReservations(kcal);
                            vfat=TwoReservations(vfat);

                            bodyage=TwoReservations(bodyage);
                            proteinX=TwoReservations(proteinX);

                            lbmX=TwoReservations(lbmX);
                            obaserate=TwoReservations(obaserate);




                            bmiTxt.setText(String.valueOf(bmi));
                            bodyWater.setText(String.valueOf(tbw));
                            bodyFat.setText(String.valueOf(fat));
                            boneMass.setText(String.valueOf(bone));
                            muscleMass.setText(String.valueOf(mus));
                            metabolismKcal.setText(String.valueOf(kcal));
                            protein.setText(String.valueOf(proteinX));
                            visceralFat.setText(String.valueOf(vfat));
                            metabolicAge.setText(String.valueOf(bodyage));
                            lbm.setText(String.valueOf(lbmX));
                            obesity.setText(String.valueOf(obaserate));

                            mVisceralFat=visceralFat.getText().toString().trim();
                            mWeight=readingWeight.getText().toString().trim();
                            mBMI=bmiTxt.getText().toString().trim();

                            mBodyFat=bodyFat.getText().toString().trim();
                            mbodyWater=bodyWater.getText().toString().trim();
                            mBoneMass=boneMass.getText().toString().trim();
                            mMuscleMass=muscleMass.getText().toString().trim();



                        }else{

                            float bestweight=BodyFat.getBestWeight(true,mAge,mHeight,weightData,impulseVal/10,0);
                            float bmi=BodyFat.getBMI(true,mAge,mHeight,weightData,impulseVal/10,0);
                            float fat=BodyFat.getFat(true,mAge,mHeight,weightData,impulseVal/10,0);
                            float tbw=BodyFat.getTbw(true,mAge,mHeight,weightData,impulseVal/10,0);
                            float mus=BodyFat.getMus(true,mAge,mHeight,weightData,impulseVal/10,0);
                            float bone=BodyFat.getBone(true,mAge,mHeight,weightData,impulseVal/10,0);
                            float kcal=BodyFat.getKcal(true,mAge,mHeight,weightData,impulseVal/10,0);
                            float vfat=BodyFat.getVfat(true,mAge,mHeight,weightData,impulseVal/10,0);
                            float bodyage=BodyFat.getBage(true,mAge,mHeight,weightData,impulseVal/10,0);
                            float proteinX=BodyFat.getProtein(true,mAge,mHeight,weightData,impulseVal/10,0);
                            float  lbmX=BodyFat.getWeightWithoutFat(true,mAge,mHeight,weightData,impulseVal/10,0);
                            float  obaserate=BodyFat.getObeseRate(true,mAge,mHeight,weightData,impulseVal/10,0);
                            float   score=BodyFat.getScore(true,mAge,mHeight,weightData,impulseVal/10,0);
                            float    bodyshape=BodyFat.getBodyShape(true,mAge,mHeight,weightData,impulseVal/10,0);


                            bmi=TwoReservations(bmi);
                            fat=TwoReservations(fat);
                            tbw=TwoReservations(tbw);

                            mus=TwoReservations(mus);
                            bone=TwoReservations(bone);

                            kcal=TwoReservations(kcal);
                            vfat=TwoReservations(vfat);

                            bodyage=TwoReservations(bodyage);
                            proteinX=TwoReservations(proteinX);

                            lbmX=TwoReservations(lbmX);
                            obaserate=TwoReservations(obaserate);



                            //---------------------

                            bmiTxt.setText(String.valueOf(bmi));
                            bodyWater.setText(String.valueOf(tbw));
                            bodyFat.setText(String.valueOf(fat));
                            boneMass.setText(String.valueOf(bone));
                            muscleMass.setText(String.valueOf(mus));
                            metabolismKcal.setText(String.valueOf(kcal));
                            protein.setText(String.valueOf(proteinX));
                            visceralFat.setText(String.valueOf(vfat));
                            metabolicAge.setText(String.valueOf(bodyage));
                            lbm.setText(String.valueOf(lbmX));
                            obesity.setText(String.valueOf(obaserate));

                            mVisceralFat=visceralFat.getText().toString().trim();
                            mWeight=readingWeight.getText().toString().trim();
                            mBMI=bmiTxt.getText().toString().trim();

                            mBodyFat=bodyFat.getText().toString().trim();
                            mbodyWater=bodyWater.getText().toString().trim();
                            mBoneMass=boneMass.getText().toString().trim();
                            mMuscleMass=muscleMass.getText().toString().trim();

                        }

                        SaveWeightData();
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

                    if(dialog.isShowing())
                        dialog.dismiss();
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
          //  pd.show();


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




    private void SaveWeightData()
    {
        String username="";
        if(globalVariable.getUsername()!=null){
            username=globalVariable.getUsername();
        }


        try{
            SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
            database.insert(BplOximterdbHelper.TABLE_NAME_WEIGHT_MACHINE_B, null,
                    content_values_weight(username, mUserName,getDateTime(),
                            mBMI,mWeight,mMetabolism,mbodyWater,mBodyFat,mBoneMass,
                            mProtein,mVisceralFat,mBodyAge,mMuscleMass,mLBM,mObesity));

            database.insert(BplOximterdbHelper.TABLE_NAME_LAST_ACTIVITY_USERS,null, Utility.
                    lastActivityUsers(username,
                            mUserName,Constants.DEVICE_PARAMETER_IWEIGH,DateTime.getCurrentDate(),
                            globalVariable.getUserType()));

            Logger.log(Level.DEBUG,TAG,"---Weight Successfully Saved into Database--");

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public ContentValues content_values_weight(String username, String userSelected,String date_time,
                                               String bmi,
                                               String measured_weight,String metabol,
                                               String bdy_water,String bdy_fat,String bone_mass,
                                               String protein,String viscFat,String BdyAge,String muscMass,String lbm,String obesity) {
        ContentValues values = new ContentValues();

        values.put(BplOximterdbHelper.USER_NAME, username);
        values.put(BplOximterdbHelper.USER_NAME_, userSelected);
        values.put(BplOximterdbHelper.MEASURED_WEIGHT_DATE,date_time);
        values.put(BplOximterdbHelper.MEASURED_BMI,bmi);
        values.put(BplOximterdbHelper.MEASURED_WEIGHT,measured_weight);

        values.put(BplOximterdbHelper.METABOLISM,metabol);
        values.put(BplOximterdbHelper.BODY_WATER,bdy_water);
        values.put(BplOximterdbHelper.BODY_FAT,bdy_fat);
        values.put(BplOximterdbHelper.BONE_MASS,bone_mass);
        values.put(BplOximterdbHelper.PROTEIN,protein);

        values.put(BplOximterdbHelper.VISCERAL_FAT,viscFat);
        values.put(BplOximterdbHelper.BODY_AGE,BdyAge);
        values.put(BplOximterdbHelper.MUSCLE_MASS,muscMass);
        values.put(BplOximterdbHelper.LBM,lbm);
        values.put(BplOximterdbHelper.OBESITY,obesity);

        return values;

    }
    public static float TwoReservations(float f) {
        BigDecimal bg = new BigDecimal(f);
        float f1 = (float) bg.setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        return f1;
    }
}
