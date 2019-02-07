package aicare.net.cn.iweightlibrary.bleprofile;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


import aicare.net.cn.iweightlibrary.utils.AicareBleConfig;
import aicare.net.cn.iweightlibrary.utils.L;
import aicare.net.cn.iweightlibrary.utils.ParseData;
import aicare.net.cn.iweightlibrary.wby.WBYService;

import static android.Manifest.permission.BLUETOOTH_ADMIN;

public abstract class BleProfileServiceReadyActivity<E extends WBYService.WBYBinder> extends FragmentActivity implements WBYService.OnDeviceDataChangeListener, BleProfileService.OnDeviceStateListener {

    private final static String TAG = "BleProfileServiceReadyActivity";

    protected static final int REQUEST_ENABLE_BT = 2;

    private E mService;

    private boolean mIsScanning = false;
    private BluetoothManager bluetoothManager = null;
    private BluetoothAdapter adapter = null;

    private BroadcastReceiver mCommonBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (state) {
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        bluetoothTurningOff();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        bluetoothTurningOn();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        bluetoothStateOff();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        bluetoothStateOn();
                        break;
                }
            }
        }
    };

    /**
     * 蓝牙已关闭
     */
    protected void bluetoothStateOff() {

    }

    /**
     * 蓝牙开启中
     */
    protected void bluetoothTurningOn() {

    }

    /**
     * 蓝牙关闭中
     */
    @RequiresPermission(BLUETOOTH_ADMIN)
    protected void bluetoothTurningOff() {
        if (mService != null) {
            mService.disconnect();
        }
        stopScan();
    }

    /**
     * 蓝牙已开启
     */
    protected void bluetoothStateOn() {
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            final E bleService = mService = (E) service;
            bleService.getService().setOnDeviceStateListener(BleProfileServiceReadyActivity.this);
            bleService.getService().setOnDeviceDataChangeListener(BleProfileServiceReadyActivity.this);

            onServiceBinded(bleService);
            // and notify user if device is connected
            if (bleService.isConnected())
                onStateChanged(bleService.getDeviceAddress(), BleProfileService.STATE_CONNECTED);
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mService = null;
            onServiceUnbinded();
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onInitialize();
        final Intent service = new Intent(this, WBYService.class);
        bindService(service, mServiceConnection, 0);
        getApplication().registerReceiver(mCommonBroadcastReceiver, makeIntentFilter());

    }

    protected void onInitialize() {
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        adapter = bluetoothManager.getAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getApplication().unregisterReceiver(mCommonBroadcastReceiver);
        try {
            unbindService(mServiceConnection);
            mService = null;
            onServiceUnbinded();
        } catch (final IllegalArgumentException e) {
            // do nothing, we were not connected to the sensor
        }
    }

    private static IntentFilter makeIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        return intentFilter;
    }

    protected abstract void onServiceBinded(E binder);

    protected abstract void onServiceUnbinded();

    /**
     * 开始连接设备
     * @param address
     */
    public void startConncet(String address) {
        stopScan();//连接设备时需停止扫描
        final Intent service = new Intent(this, WBYService.class);
        service.putExtra(BleProfileService.EXTRA_DEVICE_ADDRESS, address);
        startService(service);
        bindService(service, mServiceConnection, 0);

        Log.d("BleProfileActivity","Service started and binded()");


    }


    @Override
    public void onStateChanged(String deviceAddress, int state) {
        switch (state) {
            case BleProfileService.STATE_DISCONNECTED:
                try {
                    unbindService(mServiceConnection);
                    mService = null;
                } catch (final IllegalArgumentException e) {
                    // do nothing. This should never happen but does...
                }
                break;
        }
    }

    /**
     * 是否已连接
     * @return true:已连接; false:未连接
     */
    protected boolean isDeviceConnected() {
        return mService != null && mService.isConnected();
    }

    /**
     * 是否支持BLE
     * @return true:支持; false:不支持
     */
    protected boolean ensureBLESupported() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * 蓝牙是否开启
     * @return true:开启; false:未开启
     */
    protected boolean isBLEEnabled() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter adapter = bluetoothManager.getAdapter();
        return adapter != null && adapter.isEnabled();
    }

    /**
     * 显示开启蓝牙提示框
     */
    protected void showBLEDialog() {
        final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    /**
     * 是否正在扫描
     * @return true:正在扫描; false:已停止扫描
     */
    public boolean isScanning() {
        return mIsScanning;
    }

    /**
     * 开始扫描
     */
    protected void startScan() {
        if (isBLEEnabled()) {
            if (!mIsScanning) {
                adapter.startLeScan(mLEScanCallback);
                mIsScanning = true;
            }
        } else {
            showBLEDialog();
        }
    }

    /**
     * 停止扫描
     */
    protected void stopScan() {
        if (mIsScanning) {
            if (adapter != null) {
                adapter.stopLeScan(mLEScanCallback);
            }
            mIsScanning = false;
        }
    }

    private final BluetoothAdapter.LeScanCallback mLEScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            L.e(TAG, "onLeScan");
            if (device != null) {
                L.e(TAG, ParseData.byteArr2Str(scanRecord));
                if (AicareBleConfig.isAicareDevice(scanRecord)) {
                    L.e(TAG, "name: " + device.getName() + "; address: " + device.getAddress());
                    getAicareDevice(device, rssi);
                }
            }
        }
    };

    /**
     * 获取到符合Aicare协议的体脂秤
     * @param device
     * @param rssi
     */
    protected abstract void getAicareDevice(BluetoothDevice device, int rssi);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {

        } else if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {
            finish();
        }

    }
}
