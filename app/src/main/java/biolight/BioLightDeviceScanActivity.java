package biolight;

import android.*;
import android.annotation.*;
import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.content.pm.*;
import android.database.sqlite.*;
import android.location.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.example.bluetoothlibrary.*;
import com.example.bluetoothlibrary.Config;
import com.example.bluetoothlibrary.entity.*;

import java.util.*;

import constantsP.*;
import database.*;
import logger.*;
import test.bpl.com.bplscreens.R;


/**
 * Created by abhishek.raj on 09-04-2018.
 */


public class BioLightDeviceScanActivity extends Activity {


    private static final String TAG =BioLightDeviceScanActivity.class.getSimpleName() ;
    BluetoothAdapter mBluetoothAdapter;
    public static ArrayList<Peripheral> preipheralCopy ;
    public static int WBPMODE = -1;//

    BioLightAdapter bioLightAdapter;
    public static Config config;
    ListView blueToothListView;

    public static final int MACRO_CODE_1 = 1;
    public static final int MACRO_CODE_2 = 2;
    public static final int MACRO_CODE_22= 22;


    private final static String UUID_KEY_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private final static String UUID_KEY_DATA_WF = "0000fff4-0000-1000-8000-00805f9b34fb";

    private boolean mScanning;
    boolean mActivitySeriesPause;

    public static  BioLightListner bioLightListner;
    ProgressDialog progressDialog;

    String mUserName,mAge,mGender;

    String biolighArry[]={"88:4A:EA:84:B7:74",
            "F045DA1120BE",
            "F045DA111AE2",
            "F045DA118FF9",
            "F045DA1120F2",
            "F045DA10F4A6",
            "94E36DA61876",
            "F045DA10F2C6",
            "94E36DA4E1D3",
            "F045DA116479",
            "F045DA10DA30",
            "94E36DA60B69",
            "F045DA11641C",
            "F045DA111ADA",
            "F045DA111DE5",
            "F045DA111A9C",
            "F045DA10DB27",
            "F045DA10E8FE",
            "94E36DAA978F",
            "F045DA11177A",
            "94E36DA4F327",
            "F045DA10F11A",
            "94E36DA60339",
            "94E36DA6036F",
            "F045DA111DB5",
            "94E36DAA6C4D",
            "F045DA10DD5A",
            "94E36DA61D33",
            "F045DA11D90F",
            "F045DA111D8A",
            "94E36DA6187C",
            "94E36DAA7492",
            "F045DA11D766",
            "94E36DAA978C",
            "F045DA111ACD",
            "F045DA10DD3A",
            "94E36DA4FDDC",
            "94E36DA612CA",
            "F045DA116764",
            "F045DA10DB36",
            "94E36DAA9781",
            "F045DA10F494",
            "F045DA10DA4F",
            "F045DA111AC3",
            "F045DA116768",
            "F045DA10DB09",
            "F045DA10DD46",
            "F045DA11DB2D",
            "F045DA12173C",
            "94E36DAA9786",
            "F045DA1120D7",
            "F045DA11174D",
            "F045DA111DAC",
            "F045DA11E36F",
            "94E36DA60F29",
            "F045DA111AFE",
            "94E36DAA978A",
            "94E36DAA6FB9",
            "F045DA111DD7",
            "F045DA111709",
            "94E36DA61D15",
            "F045DA111DCD",
            "F045DA11626D",
            "94E36DA6184A",
            "94E36DA4F809",
            "C8DF842BE11F",
            "94E36DAA6C72",
            "F045DA111DFA",
            "F045DA10DA19",
            "F045DA111701",
            "94E36DAA6C75",
            "F045DA11208A",
            "F045DA11D931",
            "F045DA111DCB",
            "F045DA11DB69",
            "C8DF842BD8F4",
            "F045DA11176D",
            "94E36DAA6C7E",
            "F045DA10DC30",
            "F045DA116A27",
            "94E36DAA6C23",
            "F045DA10DB34",
            "94E36DA4F358",
            "F045DA11E361",
            "F045DA11E55E",
            "F045DA111752",
            "F045DA10F10A",
            "F045DA11174F",
            "94E36DAA6FEF",
            "94E36DA61D7D",
            "94E36DAA7491",
            "F045DA111DB3",
            "F045DA116A28",
            "F045DA111AE6",
            "C8DF842BDDDB",
            "94E36DA4F85B",
            "F045DA10EA3F",
            "F045DA1120A2",
            "F045DA1191FE",
            "F045DA111DE7",
            "F045DA11DB08",
            "F045DA10DB59",
            "94E36DAA6C77",
            "F045DA10DD09",
            "94E36DA61D58",
            "F045DA111734",
            "F045DA11DB4A",
            "F045DA1120E7",
            "F045DA112092",
            "F045DA111AA5",
            "F045DA10F287",
            "F045DA112098",
            "F045DA10DA43",
            "F045DA116452",
            "F045DA10DA66",
            "F045DA1120AB",
            "94E36DA6031F",
            "F045DA10DC36",
            "F045DA116222",
            "C8DF842BC7F6",
            "F045DA111779",
            "94E36DAA93E3",
            "F045DA111D9A",
            "F045DA10DB1A",
            "F045DA10DD1D",
            "F045DA11D775",
            "94E36DAA9793",
            "F045DA11D919",
            "94E36DA4F31A",
            "94E36DA612AB",
            "94E36DA6213E",
            "94E36DAA9783",
            "F045DA10DB3D",
            "94E36DAA93EE",
            "94E36DAA724D",
            "94E36DA4FDD2",
            "F045DA10EF87",
            "F045DA10DB29",
            "F045DA10F105",
            "94E36DAA6FE7",
            "94E36DAA6C34",
            "F045DA10DB32",
            "94E36DAA6C30",
            "F045DA118FE7",
            "F045DA10DA61",
            "F045DA11624C",
            "F045DA111777",
            "94E36DA4F834",
            "F045DA111A85",
            "94E36DAA97A2",
            "94E36DAA6FC2",
            "F045DA10EBB0",
            "F045DA1114E5",
            "94E36DA4F36D",
            "F045DA10F2C7",
            "F045DA10DA79",
            "C8DF842BE14D",
            "94E36DAA7217",
            "94E36DA4FDBA",
            "F045DA10DD4A",
            "94E36DAA6C73",
            "94E36DAA6FDD",
            "F045DA111A99",
            "94E36DAA93EB",
            "F045DA10DC00",
            "94E36DA4FDA2",
            "F045DA116247",
            "94E36DAA74F7",
            "F045DA10DB66",
            "94E36DA60B0C",
            "F045DA11DB11",
            "F045DA10F486",
            "F045DA10DB46",
            "F045DA116736",
            "94E36DAA725E",
            "F045DA11176A",
            "F045DA10F28B",
            "F045DA111DBF",
            "C8DF842BCD96",
            "94E36DA62138",
            "F045DA10F104",
            "94E36DAA6FE2",
            "94E36DAA74D1",
            "F045DA11DB0D",
            "F045DA116476",
            "F045DA11DB19",
            "F045DA10DD39",
            "C8DF842BC7BB",
            "94E36DAA74CF",
            "94E36DAA74BE",
            "F045DA1114FA",
            "F045DA111AF7",
            "94E36DAA6C64",
            "F045DA10F2A5",
            "F045DA121731",
            "F045DA11DB03",
            "F045DA1114DE",
            "F045DA111DAD",
            "F045DA1120F1",
            "94E36DA612DE",
            "F045DA111DDD"};



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.biolight);
        final ArrayList<String> macList= new ArrayList<String>(Arrays.asList(biolighArry));
        final   Button  btnFindDevice= findViewById(R.id.btnFindDevice);
        blueToothListView = findViewById(R.id.listDevices);
        config=new Config();
        mScanning=true;

        preipheralCopy=new ArrayList<>();

       // bioLightListner=BioLightDeviceScanActivity.this;


        mUserName=getIntent().getExtras().getString(Constants.USER_NAME);
        mAge=getIntent().getExtras().getString("age");
        mGender=getIntent().getExtras().getString("gender");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(BioLightDeviceScanActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(BioLightDeviceScanActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                } else {
                    ActivityCompat.requestPermissions(BioLightDeviceScanActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                           Constants. PERMISSION_REQUEST_COARSE_LOCATION);
                }
            }


        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final LocationManager manager = (LocationManager)this.getSystemService( Context.LOCATION_SERVICE );

            if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                Toast.makeText(BioLightDeviceScanActivity.this,"Please Enable your Location" +
                        " in your device Settings",Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(BioLightDeviceScanActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        Constants.PERMISSION_REQUEST_COARSE_LOCATION);
            }

        }


        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        //  Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else  {
            if (!mBluetoothAdapter.isEnabled()) {
                mScanning=false;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivityForResult(enableBtIntent, Constants.BLUETOOTH_REQUEST_CODE);

            }

        }



        //----

        mBLE = new BluetoothLeClass(BioLightDeviceScanActivity.this);
        mBLE.setBluetoothGattCallback();



        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
            finish();
        }

        if (mBluetoothAdapter.isEnabled())
        {
            mBLE.scanLeDevice(true);//start to scan
        //    initializeProgressDialog();
        }


        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
        mBLE.setOnsetDevicePreipheral(mOnSetDevicePreipheral);
        mBLE.setOnConnectListener(mOnConnectlistener);
        mBLE.setOnDisconnectListener(mOndisconnectListener);

        initHandler();

        btnFindDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });




        blueToothListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final Peripheral device = bioLightAdapter.getDevice(position);
                if (device == null)
                {
                    return;
                }
                else
                {

                    if(macList.contains(device.getPreipheralMAC()))
                        {
                            mBLE.setBLEService(device.getPreipheralMAC());
                            config.setConnectPreipheralOpsition(device);//set to be current device

                            Log.e(" the current device", "" + config.getConnectPreipheralOpsition().getPreipheralMAC() + "" + config.getConnectPreipheralOpsition().getBluetooth());
                            Log.e(" version of  device", "" + device.getModel());
                        }else{
                            Toast.makeText(BioLightDeviceScanActivity.this,"Mac Id not Registered",Toast.LENGTH_SHORT).show();
                        }




                }
            }
        });



    }

    public static BluetoothLeClass mBLE;



    @Override
    protected void onDestroy() {
        super.onDestroy();


        Logger.log(Level.INFO,TAG,"On Destroy () gets called");
        if(mBLE!=null){
            mBLE.scanLeDevice(false);
            mBLE.disconnect();
            mBLE.close();

        }
    }

    @Override
    protected void onStop() {
//        mBLE.Unregister();
        super.onStop();

        }




    @Override
    protected void onResume() {
        super.onResume();

        if(!mScanning){
            mBLE.scanLeDevice(true);
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Constants.BLUETOOTH_REQUEST_CODE && resultCode==RESULT_OK)
        {
            if(mBluetoothAdapter!=null)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    mBLE = new BluetoothLeClass(this);
                    mBLE.setBluetoothGattCallback();
                    if (!mBLE.initialize()) {
                        Log.e(TAG, "Unable to initialize Bluetooth");
                        finish();
                    }

                    if (mBluetoothAdapter.isEnabled())
                    {
                        mBLE.scanLeDevice(true);//start to scan
                    }
                }
            }

        }else if(requestCode==Constants.BLUETOOTH_REQUEST_CODE && resultCode==RESULT_CANCELED)
        {
            finish();
        }

    }


        // scanner the list


    BluetoothLeClass.OnsetDevicePreipheral mOnSetDevicePreipheral=new BluetoothLeClass.OnsetDevicePreipheral() {
        @Override
        public void setDevicePreipheral(BluetoothDevice device, int model, String SN, float protocolVer) {

            /*if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }*/
            Peripheral preipheral = new Peripheral();
            preipheral.setBluetooth(device.getName());
            preipheral.setPreipheralMAC(device.getAddress());



            switch (model) {
                case 1:
                    preipheral.setModel("WT1");
                    break;
                case 2:
                    preipheral.setModel("WT2");
                    break;
                case 3:
                    preipheral.setModel("WT3");
                    break;
                case 48:
                    preipheral.setModel("M70C");
                    break;
                case 51:
                    preipheral.setModel("WBP202");
                    WBPMODE = 1;
                    break;
                case 57:
                    preipheral.setModel("WBP204");
                    WBPMODE = 1;
                    break;
                case 70:
                    preipheral.setModel("WF100");
                    break;
                case 71:
                    preipheral.setModel("WF200");
                    break;
                default:
                    break;
            }
            //it is just for wbp
            if (WBPMODE!=-1)
            {
                preipheral.setWebMode(WBPMODE);
            }
            preipheral.setPreipheralSN(SN);
            preipheral.setName("Smart thermometer");
            preipheral.setBrand("Wearcare");
            preipheral.setManufacturer("blt");
            preipheral.setIsActivation(0);
            preipheral.setProtocolVer(protocolVer);
            preipheral.setRemark("");


            synchronized (preipheralCopy) {

                Logger.log(Level.DEBUG,TAG,"---Peripheral Copy ------");



                if (preipheralCopy.size() == 0) {
                    preipheralCopy.add(preipheral);
                    bioLightAdapter=new BioLightAdapter(BioLightDeviceScanActivity.this,preipheralCopy);
                   sendMsg(BioLightDeviceScanActivity.MACRO_CODE_1,handler,null);
                } else {
                    boolean isTrue = false;//
                    for (int i = 0; i < preipheralCopy.size(); i++) {
                        Peripheral preipheral3 = preipheralCopy.get(i);
                        if (preipheral3.getPreipheralSN().equals(SN)) {
                            isTrue = true;//
                        }
                    }
                    //
                    if (!isTrue) {
                        preipheralCopy.add(preipheral);
                        bioLightAdapter=new BioLightAdapter(BioLightDeviceScanActivity.this,preipheralCopy);
                        sendMsg(BioLightDeviceScanActivity.MACRO_CODE_2,handler,null);
                    }
                }
                Log.e("the connecting device", preipheral.toString());
            }
        }
    };




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        if(requestCode== Constants.PERMISSION_REQUEST_COARSE_LOCATION)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

        }
    }

    Handler handler;

    @SuppressLint("HandlerLeak")
    private void initHandler()   {
        handler = new Handler() {

            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case MACRO_CODE_1://show the device list
                        blueToothListView.setAdapter(bioLightAdapter);
                        if (bioLightAdapter != null) {
                            bioLightAdapter.notifyDataSetChanged();
                        }
                        break;
                    case MACRO_CODE_2:
                        blueToothListView.setAdapter(bioLightAdapter);
                        if (bioLightAdapter != null) {
                            bioLightAdapter.notifyDataSetChanged();
                        }
                        break;
                    case MACRO_CODE_22://the state of Biolight  0:spare 1:working

                        ActivityOptions options;
                        Logger.log(Level.DEBUG,TAG," Connection State is 1 and is working ..BioLight  got working ");

                       if (msg.arg1==0)
                        {
                            //blt_state.setText("1");
                            mActivitySeriesPause=true;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                            {
                                options = ActivityOptions.makeSceneTransitionAnimation(BioLightDeviceScanActivity.this);

                                try{

                                    Intent intent=new Intent(BioLightDeviceScanActivity.this,
                                            BioLightDeviceHomeScreenActivity.class);
                                    intent.putExtra(Constants.USER_NAME,mUserName);

                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent,options.toBundle());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }else{
                                Intent intent=new Intent(BioLightDeviceScanActivity.this,
                                        BioLightDeviceHomeScreenActivity.class);
                                intent.putExtra(Constants.USER_NAME,mUserName);

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }


                        }else {
                           Toast.makeText(BioLightDeviceScanActivity.this,"Device got Disconnected",
                                   Toast.LENGTH_SHORT).show();
                           finish();
                        }


                        break;

                    default:

                        break;
                }
            }
        };
        config.setMyFragmentHandler(handler);
    }




    public static void sendMsg(int flag, Handler handler, Object object) {
        Message msg = new Message();
        msg.what = flag;
        msg.obj = object;
        if (handler!=null) {
            handler.sendMessage(msg);
        }
    }



    BluetoothLeClass.OnConnectListener mOnConnectlistener=new BluetoothLeClass.OnConnectListener() {
        @Override
        public void onConnect(BluetoothGatt gatt) {


            if (config.getConnectPreipheralOpsition().getModel().equals(Constant.M70C))
            {

            }

            Message msg = new Message();
            msg.what = MACRO_CODE_22;
            msg.arg1=0;
            config.getMyFragmentHandler().sendMessage(msg);

//            isConnecting=true;
        }


    };

    /**
     * the bluetoth is disconnected
     *
     */
    BluetoothLeClass.OnDisconnectListener mOndisconnectListener=new BluetoothLeClass.OnDisconnectListener() {
        @Override
        public void onDisconnect(BluetoothGatt gatt) {

            Logger.log(Level.WARNING,TAG,"-- Disconnected from GATT Server---");

            Message msg = new Message();
            msg.what = MACRO_CODE_22;
            msg.arg1=1;

            config.getMyFragmentHandler().sendMessage(msg);


        }
    };


    ConnectBleServiceInfo connectServiceInfo;


    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener() {


        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {

            Logger.log(Level.DEBUG,TAG,"--Service Discover Found----");
            connectServiceInfo = new ConnectBleServiceInfo();

            String  connectingDevice=BioLightDeviceScanActivity.config.getConnectPreipheralOpsition().getBluetooth();

            if (connectingDevice.equals(Constant.BLT_WBP)||connectingDevice.equals(Constant.AL_WBP)) {

                connectServiceInfo.setDeviceName(connectingDevice);
                connectServiceInfo.setServiceUUID(SampleGattAttributes.SeviceIDfbb0);

                connectServiceInfo.setCharateUUID(SampleGattAttributes.GetCharacteristicIDfbb2);
                connectServiceInfo.setCharateReadUUID(SampleGattAttributes.GetCharacteristicIDfbb1);

                connectServiceInfo.setConectModel(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

                if (connectingDevice.equals(Constant.BLT_WBP) || connectingDevice.equals(Constant.AL_WBP)) {
                    displayGattServices(BioLightDeviceScanActivity.mBLE.getSupportedGattServices(), connectServiceInfo);
                    Logger.log(Level.DEBUG,TAG,"Display Gatt Services()");
                }

                else if (connectingDevice.equals(Constant.AL_WBP)) {

                }

                else if ((connectingDevice.equals(Constant.BLT_WF1))) {
                    // displayGattServices_WF(mBLE.getSupportedGattServices());
                }


                else {
                    //   displayGattServices(mBLE.getSupportedGattServices());
                }

            }
        }
    };


// To Display BioLight data

    private void displayGattServices(List<BluetoothGattService> gattServices, ConnectBleServiceInfo serviceInfo) {
        if (gattServices == null)
            return;
        String uuid = null;

        Logger.log(Level.DEBUG,TAG,"Display GattServices "+serviceInfo.toString());
        for (BluetoothGattService gattService : gattServices) {

            uuid = gattService.getUuid().toString();

            Logger.log(Level.DEBUG,TAG,"UUID-->"+gattService.getUuid().toString());

            if (serviceInfo.getServiceUUID().equals(uuid)) {
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

                for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {


                    uuid = gattCharacteristic.getUuid().toString();
                    Logger.log(Level.INFO,TAG,"Display Gatt Service()->Gatt Characacter.getUuid"+
                            gattCharacteristic.getUuid().toString());


                    if (uuid.equals(serviceInfo.getCharateReadUUID())) {

                        Logger.log(Level.INFO, TAG, "*****GattCharacterUuid*****CharacterSize" +
                                gattCharacteristics.size());


                       mBLE.setCharacteristicNotification(gattCharacteristic, true, serviceInfo);
                       mBLE.readCharacteristic(gattCharacteristic);
                       Logger.log(Level.INFO, TAG, "displayGattServices()  gatt service read data");

                        return;


                    }

                    if (uuid.equals(serviceInfo.getCharateUUID())) {

                        Logger.log(Level.INFO, TAG, "(--displayGattServices()  gatt service character Notification--)");
                         mBLE.setCharacteristicNotification(gattCharacteristic, true, serviceInfo);
                        return;
                    }
                }
            }

        }

    }




/*
    @Override
    public void disconnectBiolight() {

    Logger.log(Level.WARNING,TAG,"On disconnect Biolight() -- Disconnecting BLE And  closing Activity--");
         //   resolveWbp.onStopBleCommand(BioLightDeviceScanActivity.mBLE);
            mBLE.disconnect();
            mBLE.close();

    }*/








    int SysArray[]={90,112,120,130,140,130,180,190,150,100,114,189,167,153,95};
    int DiabArray[]={40,65,80,90,100,90,80,100,110,90,60,69,97,80,60};

    int pulseArray[]={40,65,80,90,100,120,80,100,110,90,60,69,97,80,60};


    String dates[]={"01-12-2018 10:12:34","31-12-2018 10:20:34",};


    private void insert_data()
    {

        String x="00";
        List<String> datesList=Utility.getDaysBetweenDates(dates[0],dates[1]);
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        if(database!=null){

            for(int i=0;i<15;i++)
            {
                database.insert(BplOximterdbHelper.TABLE_NAME_BIO_LIGHT,
                        null, Utility.content_values_biolight_bp_monitor(Constants.LOGGED_User_ID,mUserName,
                                String.valueOf(SysArray[i]),String.valueOf(DiabArray[i])
                                , String.valueOf(pulseArray[i]),datesList.get(i)+"\n"+"10:12:34",
                                Utility.validateTypeBP(String.valueOf(SysArray[i]),String.valueOf(DiabArray[i])),""));
            }
        }


    }


}
