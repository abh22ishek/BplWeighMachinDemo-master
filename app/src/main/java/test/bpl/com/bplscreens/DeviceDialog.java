package test.bpl.com.bplscreens;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import logger.Level;
import logger.Logger;


public class DeviceDialog extends Dialog {

    private DeviceListAdapter adapter;
    private Button scanButton;
    private Context context;
    private OnDeviceScanListener listener;
    private boolean scanning;
    private List<ExtendedBluetoothDevice> listValues;
    private ListView listView;
    private final ExtendedBluetoothDevice.AddressComparator comparator = new ExtendedBluetoothDevice.AddressComparator();

    public void setScanning(boolean scanning) {
        this.scanning = scanning;
    }

    public interface OnDeviceScanListener {
        void scan();
        void stop();
        void connect(BluetoothDevice device);
    }




    String device_list_arr[]={"03:B3:EC:14:04:C7","03:B3:EC:14:04:C8"};


    public DeviceDialog(Context context, OnDeviceScanListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_device);
        setTitle("Select the device");
        listValues = new ArrayList<>();
        initViews();
        initEvents();

    }

    private void initEvents() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

               // Automatically connection for SWAN

               stopScanDevice();
                dismiss();
                final ExtendedBluetoothDevice d = (ExtendedBluetoothDevice) adapter.getItem(position);
                Logger.log(Level.DEBUG,"Device Dialog ",
                        "ExtendedBluetoothDevice "+d.name+" "+d.rssi+" " +
                        d.device+" ");
                listener.connect(d.device);
            }
        });


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.action_cancel) {
                    if (scanning) {
                        stopScanDevice();
                        dismiss();
                    } else {
                        startScan();
                    }
                }
            }
        });
    }

    private void initViews() {
        listView = findViewById(android.R.id.list);
        adapter = new DeviceListAdapter(context, listValues);
        listView.setAdapter(adapter);
        scanButton = findViewById(R.id.action_cancel);
    }

    private final static int SCAN_DURATION = 5000;

    private Handler handler = new Handler();

    public void startScan() {
        clearDevices();
        TextView textView = findViewById(android.R.id.empty);
        if (listValues.isEmpty()) {
            listView.setEmptyView(textView);
        }
        scanButton.setText("Exit");
        if (!scanning) {
            listener.scan();
            handler.postDelayed(stopScanRunnable, SCAN_DURATION);
        }
    }

    private Runnable stopScanRunnable = new Runnable() {
        @Override
        public void run() {
            stopScanDevice();
        }
    };

    private void stopScanDevice() {
        if (scanning) {
            scanButton.setText("Search");
            listener.stop();
        }
    }

    public void setDevice(ExtendedBluetoothDevice device) {
        comparator.address = device.device.getAddress();
        final int index = listValues.indexOf(comparator);
        if (index >= 0) {
            ExtendedBluetoothDevice previousDevice = listValues.get(index);
            previousDevice.rssi = device.rssi;
            adapter.notifyDataSetChanged();
            return;
        }
        listValues.add(device);
        adapter.notifyDataSetChanged();

    }

    private void clearDevices() {
        listValues.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        handler.removeCallbacks(stopScanRunnable);
        stopScanDevice();
    }
}
