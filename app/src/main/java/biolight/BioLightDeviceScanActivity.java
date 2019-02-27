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

import java.lang.*;
import java.lang.Process;
import java.util.*;

import constantsP.*;
import database.*;
import logger.*;
import test.bpl.com.bplscreens.R;


/**
 * Created by abhishek.raj on 09-04-2018.
 */


public class BioLightDeviceScanActivity extends Activity implements BioLightListner{


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
            "F0:45:DA:11:20:BE",
            "F0:45:DA:11:1A:E2",
            "F0:45:DA:11:8F:F9",
            "F0:45:DA:11:20:F2",
            "F0:45:DA:10:F4:A6",
            "94:E3:6D:A6:18:76",
            "F0:45:DA:10:F2:C6",
            "94:E3:6D:A4:E1:D3",
            "F0:45:DA:11:64:79",
            "F0:45:DA:10:DA:30",
            "94:E3:6D:A6:0B:69",
            "F0:45:DA:11:64:1C",
            "F0:45:DA:11:1A:DA",
            "F0:45:DA:11:1D:E5",
            "F0:45:DA:11:1A:9C",
            "F0:45:DA:10:DB:27",
            "F0:45:DA:10:E8:FE",
            "94:E3:6D:AA:97:8F",
            "F0:45:DA:11:17:7A",
            "94:E3:6D:A4:F3:27",
            "F0:45:DA:10:F1:1A",
            "94:E3:6D:A6:03:39",
            "94:E3:6D:A6:03:6F",
            "F0:45:DA:11:1D:B5",
            "94:E3:6D:AA:6C:4D",
            "F0:45:DA:10:DD:5A",
            "94:E3:6D:A6:1D:33",
            "F0:45:DA:11:D9:0F",
            "F0:45:DA:11:1D:8A",
            "94:E3:6D:A6:18:7C",
            "94:E3:6D:AA:74:92",
            "F0:45:DA:11:D7:66",
            "94:E3:6D:AA:97:8C",
            "F0:45:DA:11:1A:CD",
            "F0:45:DA:10:DD:3A",
            "94:E3:6D:A4:FD:DC",
            "94:E3:6D:A6:12:CA",
            "F0:45:DA:11:67:64",
            "F0:45:DA:10:DB:36",
            "94:E3:6D:AA:97:81",
            "F0:45:DA:10:F4:94",
            "F0:45:DA:10:DA:4F",
            "F0:45:DA:11:1A:C3",
            "F0:45:DA:11:67:68",
            "F0:45:DA:10:DB:09",
            "F0:45:DA:10:DD:46",
            "F0:45:DA:11:DB:2D",
            "F0:45:DA:12:17:3C",
            "94:E3:6D:AA:97:86",
            "F0:45:DA:11:20:D7",
            "F0:45:DA:11:17:4D",
            "F0:45:DA:11:1D:AC",
            "F0:45:DA:11:E3:6F",
            "94:E3:6D:A6:0F:29",
            "F0:45:DA:11:1A:FE",
            "94:E3:6D:AA:97:8A",
            "94:E3:6D:AA:6F:B9",
            "F0:45:DA:11:1D:D7",
            "F0:45:DA:11:17:09",
            "94:E3:6D:A6:1D:15",
            "F0:45:DA:11:1D:CD",
            "F0:45:DA:11:62:6D",
            "94:E3:6D:A6:18:4A",
            "94:E3:6D:A4:F8:09",
            "C8:DF:84:2B:E1:1F",
            "94:E3:6D:AA:6C:72",
            "F0:45:DA:11:1D:FA",
            "F0:45:DA:10:DA:19",
            "F0:45:DA:11:17:01",
            "94:E3:6D:AA:6C:75",
            "F0:45:DA:11:20:8A",
            "F0:45:DA:11:D9:31",
            "F0:45:DA:11:1D:CB",
            "F0:45:DA:11:DB:69",
            "C8:DF:84:2B:D8:F4",
            "F0:45:DA:11:17:6D",
            "94:E3:6D:AA:6C:7E",
            "F0:45:DA:10:DC:30",
            "F0:45:DA:11:6A:27",
            "94:E3:6D:AA:6C:23",
            "F0:45:DA:10:DB:34",
            "94:E3:6D:A4:F3:58",
            "F0:45:DA:11:E3:61",
            "F0:45:DA:11:E5:5E",
            "F0:45:DA:11:17:52",
            "F0:45:DA:10:F1:0A",
            "F0:45:DA:11:17:4F",
            "94:E3:6D:AA:6F:EF",
            "94:E3:6D:A6:1D:7D",
            "94:E3:6D:AA:74:91",
            "F0:45:DA:11:1D:B3",
            "F0:45:DA:11:6A:28",
            "F0:45:DA:11:1A:E6",
            "C8:DF:84:2B:DD:DB",
            "94:E3:6D:A4:F8:5B",
            "F0:45:DA:10:EA:3F",
            "F0:45:DA:11:20:A2",
            "F0:45:DA:11:91:FE",
            "F0:45:DA:11:1D:E7",
            "F0:45:DA:11:DB:08",
            "F0:45:DA:10:DB:59",
            "94:E3:6D:AA:6C:77",
            "F0:45:DA:10:DD:09",
            "94:E3:6D:A6:1D:58",
            "F0:45:DA:11:17:34",
            "F0:45:DA:11:DB:4A",
            "F0:45:DA:11:20:E7",
            "F0:45:DA:11:20:92",
            "F0:45:DA:11:1A:A5",
            "F0:45:DA:10:F2:87",
            "F0:45:DA:11:20:98",
            "F0:45:DA:10:DA:43",
            "F0:45:DA:11:64:52",
            "F0:45:DA:10:DA:66",
            "F0:45:DA:11:20:AB",
            "94:E3:6D:A6:03:1F",
            "F0:45:DA:10:DC:36",
            "F0:45:DA:11:62:22",
            "C8:DF:84:2B:C7:F6",
            "F0:45:DA:11:17:79",
            "94:E3:6D:AA:93:E3",
            "F0:45:DA:11:1D:9A",
            "F0:45:DA:10:DB:1A",
            "F0:45:DA:10:DD:1D",
            "F0:45:DA:11:D7:75",
            "94:E3:6D:AA:97:93",
            "F0:45:DA:11:D9:19",
            "94:E3:6D:A4:F3:1A",
            "94:E3:6D:A6:12:AB",
            "94:E3:6D:A6:21:3E",
            "94:E3:6D:AA:97:83",
            "F0:45:DA:10:DB:3D",
            "94:E3:6D:AA:93:EE",
            "94:E3:6D:AA:72:4D",
            "94:E3:6D:A4:FD:D2",
            "F0:45:DA:10:EF:87",
            "F0:45:DA:10:DB:29",
            "F0:45:DA:10:F1:05",
            "94:E3:6D:AA:6F:E7",
            "94:E3:6D:AA:6C:34",
            "F0:45:DA:10:DB:32",
            "94:E3:6D:AA:6C:30",
            "F0:45:DA:11:8F:E7",
            "F0:45:DA:10:DA:61",
            "F0:45:DA:11:62:4C",
            "F0:45:DA:11:17:77",
            "94:E3:6D:A4:F8:34",
            "F0:45:DA:11:1A:85",
            "94:E3:6D:AA:97:A2",
            "94:E3:6D:AA:6F:C2",
            "F0:45:DA:10:EB:B0",
            "F0:45:DA:11:14:E5",
            "94:E3:6D:A4:F3:6D",
            "F0:45:DA:10:F2:C7",
            "F0:45:DA:10:DA:79",
            "C8:DF:84:2B:E1:4D",
            "94:E3:6D:AA:72:17",
            "94:E3:6D:A4:FD:BA",
            "F0:45:DA:10:DD:4A",
            "94:E3:6D:AA:6C:73",
            "94:E3:6D:AA:6F:DD",
            "F0:45:DA:11:1A:99",
            "94:E3:6D:AA:93:EB",
            "F0:45:DA:10:DC:00",
            "94:E3:6D:A4:FD:A2",
            "F0:45:DA:11:62:47",
            "94:E3:6D:AA:74:F7",
            "F0:45:DA:10:DB:66",
            "94:E3:6D:A6:0B:0C",
            "F0:45:DA:11:DB:11",
            "F0:45:DA:10:F4:86",
            "F0:45:DA:10:DB:46",
            "F0:45:DA:11:67:36",
            "94:E3:6D:AA:72:5E",
            "F0:45:DA:11:17:6A",
            "F0:45:DA:10:F2:8B",
            "F0:45:DA:11:1D:BF",
            "C8:DF:84:2B:CD:96",
            "94:E3:6D:A6:21:38",
            "F0:45:DA:10:F1:04",
            "94:E3:6D:AA:6F:E2",
            "94:E3:6D:AA:74:D1",
            "F0:45:DA:11:DB:0D",
            "F0:45:DA:11:64:76",
            "F0:45:DA:11:DB:19",
            "F0:45:DA:10:DD:39",
            "C8:DF:84:2B:C7:BB",
            "94:E3:6D:AA:74:CF",
            "94:E3:6D:AA:74:BE",
            "F0:45:DA:11:14:FA",
            "F0:45:DA:11:1A:F7",
            "94:E3:6D:AA:6C:64",
            "F0:45:DA:10:F2:A5",
            "F0:45:DA:12:17:31",
            "F0:45:DA:11:DB:03",
            "F0:45:DA:11:14:DE",
            "F0:45:DA:11:1D:AD",
            "F0:45:DA:11:20:F1",
            "94:E3:6D:A6:12:DE",
            "F0:45:DA:11:1D:DD"};

       Button  btnFindDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biolight);
        final ArrayList<String> macList= new ArrayList<String>(Arrays.asList(biolighArry));
          btnFindDevice= findViewById(R.id.btnFindDevice);
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

                        Log.e(" the current device", "" + config.
                                getConnectPreipheralOpsition().
                                getPreipheralMAC() + "" + config.getConnectPreipheralOpsition().getBluetooth());
                        Log.e(" version of  device", "" + device.getModel());

                       Log.w("Row Selected=","Mac Id "+config.getConnectPreipheralOpsition().getPreipheralMAC()
                               +" Serial No. "+config.getConnectPreipheralOpsition().getPreipheralSN());

                       Constants.SELECTED_MAC_ID_BT02=config.getConnectPreipheralOpsition().getPreipheralMAC();
                       Constants.SELECTED_SERIAL_NO_ID_BT02=config.getConnectPreipheralOpsition().getPreipheralSN();


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
        if(mBLE!=null){
            mBLE.scanLeDevice(false);
        }

    }




    @Override
    protected void onResume() {
        super.onResume();

        if(mBLE!=null){
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
                            btnFindDevice.setText(R.string.tap_bt);

                        }
                        break;
                    case MACRO_CODE_2:
                        blueToothListView.setAdapter(bioLightAdapter);
                        if (bioLightAdapter != null) {
                            bioLightAdapter.notifyDataSetChanged();
                            btnFindDevice.setText(R.string.tap_bt);

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





    @Override
    public void biolightItemRecived(Object object) {

    }
}
