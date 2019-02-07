package bluetooth;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import constantsP.Constants;
import constantsP.Utility;
import logger.Level;
import logger.Logger;
import test.bpl.com.bplscreens.R;




public class BLEdevicelistActivity extends FragmentActivity {


    ProgressDialog pd;
    ListView list_devices;
    public ArrayAdapter<String> mLeDeviceListAdapter;
    //----
    private BluetoothAdapter mBluetoothAdapter;

    // Stops scanning after some seconds.
    private static final long SCAN_PERIOD= 5000;
    List<String> mDevicelist;
    Set<String> mUniqueSet;

    ImageView mBack_key;

    TextView headertitle;

    private final String TAG = BluetoothdevicelistActivity.class.getSimpleName();

    Button mSearch;
    private Handler mHandler;

    BluetoothLeScanner scanner;

    private boolean mValid_Device;


    String device_list_arr[]={"84EB186B0B5E"
            ,"CC79CFF92B0A"
            ,"CC79CFF92ABF"
            ,"CC79CFF92B08"
            ,"CC79CFF92AB3"
            ,"CC79CFF92C52"
            ,"CC79CFF92A75"
            ,"CC79CFF92B09"
            ,"CC79CFF92B47"
            ,"CC79CFF92C00"
            ,"CC79CFF92AFE"
            ,"CC79CFF92BC0"
            ,"CC79CFF92AFB"
            ,"CC79CFF92BDE"
            ,"CC79CFF92C23"
            ,"CC79CFF92B25"
            ,"CC79CFF92C04"
            ,"CC79CFF92C62"
            ,"CC79CFF92B22"
            ,"CC79CFF92BC2"
            ,"CC79CFF92AE1"
            ,"CC79CFF92BD0"
            ,"CC79CFF92BB1"
            ,"CC79CFF92AE6"
            ,"CC79CFF92BD3"
            ,"CC79CFF92A7A"

            ,"CC79CFF92B32"
            ,"CC79CFF92BCA"
            ,"CC79CFF92B2F"
            ,"CC79CFF92BEE"
            ,"CC79CFF92B6B"
            ,"CC79CFF92B93"
            ,"CC79CFF92BB6"
            ,"CC79CFF92AB6"
            ,"CC79CFF92AD0"
            ,"CC79CFF92AE7"
            ,"CC79CFF92A96"
            ,"CC79CFF92ADE"
            ,"CC79CFF92C06"
            ,"CC79CFF92C0A"
            ,"CC79CFF92C57"
            ,"CC79CFF92C2F"
            ,"CC79CFF92BEF"
            ,"CC79CFF92AB0"
            ,"CC79CFF92AEC"
            ,"CC79CFF92BD7"
            ,"CC79CFF92B2B"
            ,"CC79CFF92B18"
            ,"CC79CFF92BCF"
            ,"CC79CFF92B30"
            ,"CC79CFF92B5E"

            ,"CC79CFF92BC0"
            ,"CC79CFF92B81"
            ,"CC79CFF92C41"
            ,"CC79CFF92BB0"
            ,"CC79CFF92B69"
            ,"CC79CFF92AC9"
            ,"CC79CFF92B11"
            ,"CC79CFF92AA7"
            ,"CC79CFF92BB3"
            ,"CC79CFF92B71"
            ,"CC79CFF92B29"
            ,"CC79CFF92ACF"
            ,"CC79CFF92BC4"

            ,"CC79CFF92C4A"
            ,"CC79CFF92AED"
            ,"CC79CFF92B45"
            ,"CC79CFF92BD9"
            ,"CC79CFF92B01"
            ,"CC79CFF92C46"
            ,"CC79CFF92AA1"
            ,"CC79CFF92BB6"
            ,"CC79CFF92BB9"
            ,"CC79CFF92B0E"
            ,"CC79CFF92A8D"
            ,"CC79CFF92BC9"

            ,"CC79CFF92B27"
            ,"CC79CFF92AEF"
            ,"CC79CFF92B0D"
            ,"CC79CFF92ACD"
            ,"CC79CFF92C63"
            ,"CC79CFF92BE3"
            ,"CC79CFF92BA3"
            ,"CC79CFF92AE9"
            ,"CC79CFF92B12"
            ,"CC79CFF92B35"
            ,"CC79CFF92AEE"
            ,"CC79CFF92B2D"
            ,"CC79CFF92BCE"
            ,"CC79CFF92B33"
            ,"CC79CFF92BC6"
            ,"CC79CFF92B4A"
            ,"CC79CFF92AEB"
            ,"CC79CFF92AB1"
            ,"CC79CFF92BB0"
            ,"CC79CFF92C54"
            ,"CC79CFF92AF8"
            ,"CC79CFF92AE8"
            ,"CC79CFF92B20"
            ,"CC79CFF92BCB"
            ,"CC79CFF92A86"

            ,"CC79CFF92AA0"
            ,"CC79CFF92B7E"
            ,"CC79CFF92B6F"
            ,"CC79CFF92B8E"
            ,"CC79CFF92AC8"
            ,"CC79CFF92B60"
            ,"CC79CFF92AE0"
            ,"CC79CFF92ADF"
            ,"CC79CFF92A9D"
            ,"CC79CFF92AFF"
            ,"CC79CFF92AD0"
            ,"CC79CFF92B19"
            ,"CC79CFF92B21"
            ,"CC79CFF92ADA"
            ,"CC79CFF92B3D"
            ,"CC79CFF92A76"
            ,"CC79CFF92B17"
            ,"CC79CFF92BEB"
            ,"CC79CFF92BDD"
            ,"CC79CFF92A9F"
            ,"CC79CFF92C2D"
            ,"CC79CFF92B70"
            ,"CC79CFF92BDB"
            ,"CC79CFF92AEA"
            ,"CC79CFF92A7C"
            ,"CC79CFF92C47"
            ,"CC79CFF92B4C"
            ,"CC79CFF92B0C"
            ,"CC79CFF92A74"
            ,"CC79CFF92A73"
            ,"CC79CFF92AF0"
            ,"CC79CFF92C4D"
            ,"CC79CFF92A97"
            ,"CC79CFF92BFC"
            ,"CC79CFF92BC3"
            ,"CC79CFF92B1B"
            ,"CC79CFF92B40"
            ,"CC79CFF92AB2"
            ,"CC79CFF92A85"
            ,"CC79CFF92B91"
            ,"CC79CFF92B26"
            ,"CC79CFF92BB4"
            ,"CC79CFF92BA1"
            ,"CC79CFF92B2A"
            ,"CC79CFF92BB2"
            ,"CC79CFF92BA8"
            ,"CC79CFF92B9E"
            ,"CC79CFF92BF8"
            ,"CC79CFF92B59"
            ,"CC79CFF92ABB"


            ,"CC79CFF92AC0"
            ,"CC79CFF92C35"
            ,"CC79CFF92BEC"
            ,"CC79CFF92AB5"
            ,"CC79CFF92B0B"
            ,"CC79CFF92B89"
            ,"CC79CFF92AD7"
            ,"CC79CFF92C65"
            ,"CC79CFF92AD9"
            ,"CC79CFF92B3B"
            ,"CC79CFF92B07"
            ,"CC79CFF92B57"
            ,"CC79CFF92ADB"
            ,"CC79CFF92BD4"
            ,"CC79CFF92A93"
            ,"CC79CFF92B7F"
            ,"CC79CFF92B31"
            ,"CC79CFF92AAA"
            ,"CC79CFF92C05"
            ,"CC79CFF92BB6"
            ,"CC79CFF92BED"
            ,"CC79CFF92B23"
            ,"CC79CFF92C14"
            ,"CC79CFF92C3E"
            ,"CC79CFF92AD1"

            ,"CC79CFF92A84"
            ,"CC79CFF92A8F"
            ,"CC79CFF92B61"
            ,"CC79CFF92A8C"
            ,"CC79CFF92A90"
            ,"CC79CFF92BDC"
            ,"CC79CFF92B2C"
            ,"CC79CFF92BF7"
            ,"CC79CFF92BA2"
            ,"CC79CFF92BA5"
            ,"CC79CFF92B7A"
            ,"CC79CFF92BF5"
            ,"CC79CFF92B1A"
            ,"CC79CFF92BF0"
            ,"CC79CFF92B66"
            ,"CC79CFF92B28"
            ,"CC79CFF92C16"
            ,"CC79CFF92B0F"
            ,"CC79CFF92C44"
            ,"CC79CFF92ADD"
            ,"CC79CFF92B02"
            ,"CC79CFF92BC1"
            ,"CC79CFF92B5F"
            ,"CC79CFF92A80"


            ,"CC79CFF92BD1"



            ,"CC79CFF92C2B"
            ,"CC79CFF92C32"
            ,"CC79CFF92C20"
            ,"CC79CFF92B48"
            ,"CC79CFF92B24"
            ,"CC79CFF92AB0"
            ,"CC79CFF92AD5"
            ,"CC79CFF92B49"
            ,"CC79CFF92AAF"
            ,"CC79CFF92AF2"
            ,"CC79CFF92ACB"
            ,"CC79CFF92B3C"
            ,"CC79CFF92B82"
            ,"CC79CFF92B3E"
            ,"CC79CFF92A79"
            ,"CC79CFF92A9C"
            ,"CC79CFF92A78"
            ,"CC79CFF92AF3"
            ,"CC79CFF92C58"
            ,"CC79CFF92A94"
            ,"CC79CFF92B8B"
            ,"CC79CFF92B38"
            ,"CC79CFF92BFE"
            ,"CC79CFF92C39"
            ,"CC79CFF92C0B"


            ,"CC79CFF92B8A"
            ,"CC79CFF92C64"
            ,"CC79CFF92B76"
            ,"CC79CFF92C3F"
            ,"CC79CFF92BE6"
            ,"CC79CFF92C34"
            ,"CC79CFF92B96"
            ,"CC79CFF92C01"
            ,"CC79CFF92C38"
            ,"CC79CFF92B73"
            ,"CC79CFF92C4C"
            ,"CC79CFF92C3B"
            ,"CC79CFF92C29"
            ,"CC79CFF92B51"
            ,"CC79CFF92B03"
            ,"CC79CFF92BF6"
            ,"CC79CFF92AFA"
            ,"CC79CFF92AC3"
            ,"CC79CFF92B62"
            ,"CC79CFF92BF4"
            ,"CC79CFF92B6D"
            ,"CC79CFF92A95"
            ,"CC79CFF92B16"
            ,"CC79CFF92BEA"
            ,"CC79CFF92AE2"


            ,"CC79CFF92BE0"
            ,"CC79CFF92C51"
            ,"CC79CFF92B43"
            ,"CC79CFF92BD2"
            ,"CC79CFF92AD4"
            ,"CC79CFF92AA9"
            ,"CC79CFF92B56"
            ,"CC79CFF92B53"
            ,"CC79CFF92B88"
            ,"CC79CFF92B2E"
            ,"CC79CFF92B54"
            ,"CC79CFF92A98"
            ,"CC79CFF92C13"
            ,"CC79CFF92B15"
            ,"CC79CFF92B63"
            ,"CC79CFF92A77"
            ,"CC79CFF92B77"
            ,"CC79CFF92B83"
            ,"CC79CFF92C50"
            ,"CC79CFF92C19"
            ,"CC79CFF92ABE"
            ,"CC79CFF92B74"
            ,"CC79CFF92BAE"
            ,"CC79CFF92B50"
            ,"CC79CFF92BC7"

            ,"CC79CFF92C15"
            ,"CC79CFF92AA5"
            ,"CC79CFF92C59"
            ,"CC79CFF92B86"
            ,"CC79CFF92BFB"
            ,"CC79CFF92C1C"
            ,"CC79CFF92B05"
            ,"CC79CFF92C0E"
            ,"CC79CFF92C1A"
            ,"CC79CFF92C1B"
            ,"CC79CFF92AE3"
            ,"CC79CFF92AFC"
            ,"CC79CFF92AAD"
            ,"CC79CFF92AAO"
            ,"CC79CFF92C5A"
            ,"CC79CFF92C18"
            ,"CC79CFF92BE8"
            ,"CC79CFF92B14"
            ,"CC79CFF92C5E"
            ,"CC79CFF92B36"
            ,"CC79CFF92C58"
            ,"CC79CFF92FBB"
            ,"CC79CFF92FBB"
            ,"CC79CFF92FBB"
            ,"CC79CFF92FBB"
            ,"CC79CFF92C30"
            ,"CC79CFF92C2A"
            ,"CC79CFF92BBD"
            ,"CC79CFF92AF9"
            ,"CC79CFF92ABA"
            ,"CC79CFF92A8E"
            ,"CC79CFF92AB9"
            ,"CC79CFF92B8F"
            ,"CC79CFF92AD2"
            ,"CC79CFF92A82"
            ,"CC79CFF92B95"
            ,"CC79CFF92C28"
            ,"CC79CFF92AB4"
            ,"CC79CFF92AF4"
            ,"CC79CFF92C02"
            ,"CC79CFF92B9F"
            ,"CC79CFF92AC6"
            ,"CC79CFF92BA9"
            ,"CC79CFF92B00"
            ,"CC79CFF92B6C"
            ,"CC79CFF92A89"
            ,"CC79CFF92BAF"
            ,"CC79CFF92AAB"
            ,"CC79CFF92AF7"
            ,"CC79CFF92BAB"
            ,"CC79CFF92B99"
            ,"CC79CFF92C03"
            ,"CC79CFF92ACE"
            ,"CC79CFF92AA4"
            ,"CC79CFF92B97"
            ,"CC79CFF92BCD"
            ,"CC79CFF92BDF"
            ,"CC79CFF92C3D"
            ,"CC79CFF92AD8"
            ,"CC79CFF92BBB"
            ,"CC79CFF92BA0"
            ,"CC79CFF92B39"
            ,"CC79CFF92B79"
            ,"CC79CFF92A88"
            ,"CC79CFF92BBA"
            ,"CC79CFF92C68"
            ,"CC79CFF92C26"
            ,"CC79CFF92BBF"
            ,"CC79CFF92B94"
            ,"CC79CFF92ACA"
            ,"CC79CFF92A7F"
            ,"CC79CFF92B75"
            ,"CC79CFF92B9C"
            ,"CC79CFF92ABD"
            ,"CC79CFF92C0C"
            ,"CC79CFF92B42"
            ,"CC79CFF92A83"
            ,"CC79CFF92A81"
            ,"CC79CFF92C31"
            ,"CC79CFF92BC8"
            ,"CC79CFF92C37"
            ,"CC79CFF92B8C"
            ,"CC79CFF92A7E"
            ,"CC79CFF92A7B"
            ,"CC79CFF92B41"
            ,"CC79CFF92C43"
            ,"CC79CFF92C4F"
            ,"CC79CFF92B7C"
            ,"CC79CFF92C36"
            ,"CC79CFF92B68"
            ,"CC79CFF92BFA"
            ,"CC79CFF92B64"
            ,"CC79CFF92B5D"
            ,"CC79CFF92C11"
            ,"CC79CFF92BD6"
            ,"CC79CFF92B72"
            ,"CC79CFF92AF6"
            ,"CC79CFF92B3F"
            ,"CC79CFF92A8B"
            ,"CC79CFF92B04"
            ,"CC79CFF92B44"
            ,"CC79CFF92C45"
            ,"CC79CFF92C24"
            ,"CC79CFF92A9E"

            ,"CC79CFF92BF2"
            ,"CC79CFF92AB8"
            ,"CC79CFF92B65"
            ,"CC79CFF92BE1"
            ,"CC79CFF92B67"
            ,"CC79CFF92B58"
            ,"CC79CFF92C1F"
            ,"CC79CFF92B90"
            ,"CC79CFF92BE7"
            ,"CC79CFF92AF5"
            ,"CC79CFF92C27"
            ,"CC79CFF92C33"
            ,"CC79CFF92AC0"
            ,"CC79CFF92AC4"
            ,"CC79CFF92BF1"
            ,"CC79CFF92BE9"
            ,"CC79CFF92BB7"
            ,"CC79CFF92B1F"
            ,"CC79CFF92C48"
            ,"CC79CFF92B7B"
            ,"CC79CFF92B3A"
            ,"CC79CFF92AB7"
            ,"CC79CFF92A9B"
            ,"CC79CFF92B06"
            ,"CC79CFF92C0D"
            ,"CC79CFF92B55"
            ,"CC79CFF92BB8"
            ,"CC79CFF92BA4"
            ,"CC79CFF92C21"
            ,"CC79CFF92BF3"
            ,"CC79CFF92B9B"
            ,"CC79CFF92AC1"
            ,"CC79CFF92C2E"
            ,"CC79CFF92C10"
            ,"CC79CFF92B98"
            ,"CC79CFF92C1D"
            ,"CC79CFF92B13"
            ,"CC79CFF92A99"
            ,"CC79CFF92B1C"
            ,"CC79CFF92AF1"
            ,"CC79CFF92B52"
            ,"CC79CFF92BA7"
            ,"CC79CFF92B1E"
            ,"CC79CFF92C66"
            ,"CC79CFF92B92"
            ,"CC79CFF92AA3"
            ,"CC79CFF92B87"
            ,"CC79CFF92C5D"
            ,"CC79CFF92C25"
            ,"CC79CFF92AC2"
            ,"CC79CFF92AFD"
            ,"CC79CFF92B5B"
            ,"CC79CFF92AE5"
            ,"CC79CFF92AD6"
            ,"CC79CFF92AA8"
            ,"CC79CFF92AE4"
            ,"CC79CFF92B80"
            ,"CC79CFF92C5F"
            ,"CC79CFF92B85"
            ,"CC79CFF92B84"
            ,"CC79CFF92AA6"
            ,"CC79CFF92C4E"
            ,"CC79CFF92B46"
            ,"CC79CFF92A7D"
            ,"CC79CFF92A91"
            ,"CC79CFF92AC7"
            ,"CC79CFF92C61"
            ,"CC79CFF92C17"
            ,"CC79CFF92B6E"
            ,"CC79CFF92B7D"
            ,"CC79CFF92C5C"
            ,"CC79CFF92B78"
            ,"CC79CFF92AAE"
            ,"CC79CFF92C07"
            ,"CC79CFF92AD3"
            ,"CC79CFF92C49"
            ,"CC79CFF92B6A"
            ,"CC79CFF92B4D"
            ,"CC79CFF92C56"
            ,"CC79CFF92B4B"
            ,"CC79CFF92BFF"
            ,"CC79CFF92C60"
            ,"CC79CFF92B5C"
            ,"CC79CFF92C22"
            ,"CC79CFF92C08"
            ,"CC79CFF92B10"
            ,"CC79CFF92B4F"
            ,"CC79CFF92BFD"
            ,"CC79CFF92C53"
            ,"CC79CFF92A87"
            ,"CC79CFF92C09"
            ,"CC79CFF92BE5"
            ,"CC79CFF92BAA"
            ,"CC79CFF92C3C"
            ,"CC79CFF92C40"
            ,"CC79CFF92BD8"

            ,"CC79CFF92B4E"
            ,"CC79CFF92B9D"
            ,"CC79CFF92BC5"
            ,"CC79CFF92BF9"
            ,"CC79CFF92B8D"
            ,"CC79CFF92C3A"
            ,"CC79CFF92BE4"
            ,"CC79CFF92C42"
            ,"CC79CFF92B5A"
            ,"CC79CFF92B9A"
            ,"CC79CFF92C0F"
            ,"CC79CFF92A9A"
            ,"CC79CFF92BA6"
            ,"CC79CFF92BDA"
            ,"CC79CFF92B37"
            ,"CC79CFF92AC5"
            ,"CC79CFF92AA2"
            ,"CC79CFF92C12"
            ,"CC79CFF92B34"
            ,"CC79CFF92C55"
            ,"CC79CFF92BAD"
            ,"CC79CFF92BD5"
            ,"CC79CFF92BA0"
            ,"CC79CFF92C1E"
            ,"CC79CFF92C2C"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.log(Level.DEBUG,"On create () gets called", "" );
        setContentView(R.layout.bluetooth_device_list);

        mBack_key = findViewById(R.id.imgBackKey);
        list_devices = findViewById(R.id.listView);
        mSearch =  findViewById(R.id.search);

        mHandler = new Handler();
        mDevicelist = new ArrayList<>();
        mUniqueSet = new HashSet<>();
        mBack_key.setOnClickListener(listner);
        mSearch.setOnClickListener(listner);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

            if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                Toast.makeText(BLEdevicelistActivity.this,"Please Enable your Location in your " +
                        "device Settings",Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        Constants.PERMISSION_REQUEST_COARSE_LOCATION);
            }


        }
        headertitle = findViewById(R.id.base_header_title);
        mSearch.setTextColor(Utility.getColorWrapper(BLEdevicelistActivity.this, R.color.black));
        headertitle.setText(getString(R.string.bluetooth));

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();


        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Constants.BLUETOOTH_REQUEST_CODE);
        }



        list_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent output = new Intent();
                String device_name=parent.getItemAtPosition(position).toString().substring(0, 7);

                if(parent.getItemAtPosition(position).toString().substring(0,7).
                        equalsIgnoreCase("Medical"))
                {
                    device_name = parent.getItemAtPosition(position).toString().substring(0,7);
                            mValid_Device=true;

                }

                else if(parent.getItemAtPosition(position).toString().substring(0,8).equalsIgnoreCase("BPL iOxy"))
                {
                    device_name = parent.getItemAtPosition(position).toString().substring(0,8);

                    for(String s:device_list_arr)
                    {

                        if(get_mac_id(s).equalsIgnoreCase(parent.getItemAtPosition(position).toString().substring(9,26)))
                        {
                            mValid_Device=true;
                            break;
                        }
                    }



                }
                else{
                    mValid_Device=false;
                }

               // output.putExtra(ConstantsP.MAC_ADDRESS,"B4:99:4C:4D:49:2C");
                //setResult(RESULT_OK, output);
                //finish();

                if(mValid_Device)
                {


                    if(device_name.equalsIgnoreCase("Medical"))
                    {
                        output.putExtra(Constants.MAC_ADDRESS, parent.getItemAtPosition(position).toString().substring(8, 25));
                    }else{
                        output.putExtra(Constants.MAC_ADDRESS, parent.getItemAtPosition(position).toString().substring(9, 26));
                    }
                    setResult(RESULT_OK, output);
                    finish();
                }else{
                    Toast.makeText(BLEdevicelistActivity.this, "Device not compatible", Toast.LENGTH_SHORT).show();
                }

            }
        });
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
    protected void onResume() {
        super.onResume();
        mSearch.setText(getString(R.string.search));
        if(mBluetoothAdapter!=null)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    scanner = mBluetoothAdapter.getBluetoothLeScanner();
                }

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scanBLE21()
    {
        final ScanCallback mScan=new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);

                BluetoothDevice device = result.getDevice();

                Logger.log(Level.DEBUG,"Getting BLE devices=", "" + device.getAddress() + "  " + device.getName());

                mDevicelist.add(device.getName() + "\n" + device.getAddress());
                mUniqueSet.addAll(mDevicelist);
                mDevicelist.clear();
                mDevicelist.addAll(mUniqueSet);
                mLeDeviceListAdapter = new ArrayAdapter<>(BLEdevicelistActivity.this, android.R.layout.simple_list_item_1, mDevicelist);
                list_devices.setAdapter(mLeDeviceListAdapter);

                Logger.log(Level.DEBUG,TAG,"Get device ="+device.getName());

                if(device.getName()!=null) {

                    if (device.getName().equalsIgnoreCase("Medical") || device.getName().equalsIgnoreCase("Bpl Ioxy")) {

                        connect_Bpl_iOxy(device.getAddress());
                    } else {
                        if(pd.isShowing())
                        {
                            pd.dismiss();
                        }

                    }

                    if (mDevicelist.size() > 0 && mDevicelist.contains("Medical")) {
                        mSearch.setText(getString(R.string.select));
                    }

                }



            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Logger.log(Level.DEBUG, TAG, "**Scan Results  BLE**=" + results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);

                Logger.log(Level.DEBUG,TAG,"**Error code BLE**="+errorCode);
            }
        };



        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scanner.stopScan(mScan);

                if(pd.isShowing())
                {
                    pd.dismiss();
                }

            }
        }, SCAN_PERIOD);

            scanner.startScan(mScan);
    }






    @SuppressWarnings("deprecation")
    private void scanLE18()
    {
         final BluetoothAdapter.LeScanCallback mLeScanCallback =  new BluetoothAdapter.LeScanCallback() {


            @Override
            public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("Getting BLE devices=", "" + device.getAddress() + "  " + device.getName());

                        mDevicelist.add(device.getName() + "\n" + device.getAddress());
                        mUniqueSet.addAll(mDevicelist);
                        mDevicelist.clear();
                        mDevicelist.addAll(mUniqueSet);
                        mLeDeviceListAdapter = new ArrayAdapter<>(BLEdevicelistActivity.this, android.R.layout.simple_list_item_1, mDevicelist);
                        list_devices.setAdapter(mLeDeviceListAdapter);

                        if(device.getName().equalsIgnoreCase("Medical")|| device.getName().equalsIgnoreCase("Bpl Ioxy"))
                        {
                            connect_Bpl_iOxy(device.getAddress());
                        }

                    }
                });
            }
        };


        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                if(pd.isShowing())
                {
                    pd.dismiss();
                }

            }
        }, SCAN_PERIOD);

        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }






    View.OnClickListener listner= new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if(v==mBack_key)
            {
                finish();
            }else if(v==mSearch)
            {
                if(!mBluetoothAdapter.isEnabled())
                {
                    Toast.makeText(BLEdevicelistActivity.this, "Please Turn On your Bluetooth", Toast.LENGTH_SHORT).show();
                    return;
                }
                pd = new ProgressDialog(BLEdevicelistActivity.this);
                pd.setMessage(getResources().getString(R.string.search_bpl_dev));
                pd.setIndeterminate(true);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    scanBLE21();
                }else{
                    scanLE18();
                }


            }
        }
    };


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




    private String get_mac_id(String address)
    {
        String mac_address;

        StringBuilder sb=new StringBuilder(address);
        sb.insert(2,":");
        sb.insert(5,":");
        sb.insert(8,":");
        sb.insert(11,":");
        sb.insert(14,":");


        mac_address= String.valueOf(sb);
        Logger.log(Level.DEBUG,TAG,mac_address);
        return mac_address;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Constants.BLUETOOTH_REQUEST_CODE && resultCode==RESULT_OK)
        {
            if(mBluetoothAdapter!=null)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        scanner = mBluetoothAdapter.getBluetoothLeScanner();
                    }
                }

        }else if(requestCode==Constants.BLUETOOTH_REQUEST_CODE && resultCode==RESULT_CANCELED)
        {
            finish();
        }

    }
}