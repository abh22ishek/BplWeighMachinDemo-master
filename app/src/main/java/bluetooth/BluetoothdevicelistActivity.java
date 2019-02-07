package bluetooth;

import android.annotation.*;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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
import test.bpl.com.bplscreens.R;



/**
 * Created by admin on 16-03-2016.
 */
public class BluetoothdevicelistActivity extends FragmentActivity{

    ProgressDialog pd;
    ListView list_devices;
    public ArrayAdapter<String> mLeDeviceListAdapter;
    //----
    private BluetoothAdapter mBluetoothAdapter;

    // Stops scanning after some seconds.
    private static final long SCAN_PERIOD = 12000;
    List<String> mDevicelist;
    Set<String> mUniqueSet;

    ImageView mBack_key;

    TextView headertitle;

    private String TAG=BluetoothdevicelistActivity.class.getSimpleName();

    Button mSearch;
    private Handler mHandler;
    private boolean mScanning;
    private boolean isDiscovery;

    @Override
    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_device_list);

        mBack_key= findViewById(R.id.imgBackKey);
        list_devices=  findViewById(R.id.listView);
        mSearch= findViewById(R.id.search);


        mHandler=new Handler();
        mDevicelist = new ArrayList<>();
        mUniqueSet=new HashSet<>();
        mBack_key.setOnClickListener(listner);
        mSearch.setOnClickListener(listner);


        headertitle= findViewById(R.id.base_header_title);
        mSearch.setTextColor(Utility.getColorWrapper(BluetoothdevicelistActivity.this,R.color.black));
        headertitle.setText(R.string.blu);

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
                String item=parent.getItemAtPosition(position).toString().substring(0,7);
                if (!parent.getItemAtPosition(position).toString().substring(0,7).equalsIgnoreCase("Medical"))

                {
                    Toast.makeText(BluetoothdevicelistActivity.this,"Device not compatible",Toast.LENGTH_SHORT).show();
                    return;
                }
                output.putExtra(Constants.MAC_ADDRESS,parent.getItemAtPosition(position).toString().substring(8,25));
                setResult(RESULT_OK, output);
                finish();



            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        mSearch.setText(R.string.searc);

    }



    private void scanLeDevice() {

        isDiscovery=true;
        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                mScanning = false;
                // mBluetoothAdapter.stopLeScan(mLeScanCallback);
                mSearch.setText(R.string.sear);
                pd.dismiss();

            }
        }, SCAN_PERIOD);

        mSearch.setText(R.string.sear_);
        mScanning = true;
       pd = new ProgressDialog(BluetoothdevicelistActivity.this);
    pd.setMessage("Searching for Bpl ioxy device...");
    pd.setIndeterminate(true);
    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    pd.setCancelable(false);
    pd.setCanceledOnTouchOutside(false);
        scan_bt_device();


    }
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {


        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  //
                    Log.i("Getting BLE devices=", "" + device.getAddress() + "  " + device.getName());

                    mDevicelist.add(device.getName() + "\n" + device.getAddress());
                    mUniqueSet.addAll(mDevicelist);
                    mDevicelist.clear();
                    mDevicelist.addAll(mUniqueSet);
                    mLeDeviceListAdapter = new ArrayAdapter<>(BluetoothdevicelistActivity.this, android.R.layout.simple_list_item_1, mDevicelist);
                    list_devices.setAdapter(mLeDeviceListAdapter);
                    if(mDevicelist.size()>0)
                    {

                       // mSearch.setText("Select");
                    }



                }
            });
        }
    };




    View.OnClickListener listner= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v==mBack_key)
            {
                finish();
            }else if(v==mSearch)
            {

                scanLeDevice();// start scanning

            }
        }
    };









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


    mBluetoothAdapter.startDiscovery();
    pd.show();
}


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found

                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDevicelist.add(device.getName() + "\n" + device.getAddress());
                mUniqueSet.addAll(mDevicelist);
                mDevicelist.clear();
                mDevicelist.addAll(mUniqueSet);
                mLeDeviceListAdapter = new ArrayAdapter<String>(BluetoothdevicelistActivity.this, android.R.layout.simple_list_item_1, mDevicelist);
                list_devices.setAdapter(mLeDeviceListAdapter);
            }
        }
    };


    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(isDiscovery)
        {
            unregisterReceiver(mReceiver);
        }
    }
}
