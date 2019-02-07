package bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import java.util.List;
import java.util.UUID;

import logger.Level;
import logger.Logger;



public class BLEService extends Service {

    private final static String TAG = BLEService.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "ACTION_DATA_AVAILABLE";
    public final static String ACTION_GATT_WRITE_SUCCESS = "ACTION_GATT_WRITE_SUCCESS";

    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);


    private final IBinder mBinder = new LocalBinder();




    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {


        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Logger.log(Level.DEBUG,TAG, "Connected to GATT server.");
                Logger.log(Level.DEBUG,TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Logger.log(Level.DEBUG,TAG, "Disconnected from GATT server.");
                disconnect_BLE();
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Logger.log(Level.DEBUG, TAG, "**On ServiceDiscovered() called**");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Logger.log(Level.DEBUG,TAG, "**Action GATT service discovered**");
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

            } else {
                Logger.log(Level.DEBUG,TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Logger.log(Level.DEBUG, TAG, "**On Characteristics Read() called**");

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Logger.log(Level.DEBUG,TAG, "********onReadChareactersistices*********** " + status);
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);

            Logger.log(Level.DEBUG, TAG, "**On Characteristics Write() called**");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Logger.log(Level.DEBUG,TAG, "******** onWrite() Chareactersistices*********** " + status);
                Intent intent = new Intent(ACTION_GATT_WRITE_SUCCESS);
                sendBroadcast(intent);
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Logger.log(Level.DEBUG, TAG, "**On Characteristicschanged() called**");
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };




    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        if (SampleGattAttributes.HEART_RATE_MEASUREMENT.equalsIgnoreCase(characteristic.getUuid().toString())) {
            final byte[] data = characteristic.getValue();

            Logger.log(Level.DEBUG,TAG,"**UUId is valid for the device**");

            //   newarr=new byte[data.length];

            Bundle bundle = new Bundle();
            bundle.putByteArray("data",data);
            intent.putExtras(bundle);
            sendBroadcast(intent);
        } else {

        }

    }


    public void connect_to_device(String address)
    {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        Logger.log(Level.DEBUG,TAG, "connecting to BLE device");
        mBluetoothGatt = device.connectGatt(this,false, mGattCallback);
    }


    public class LocalBinder extends Binder {
        public BLEService getService() {
            return BLEService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        disconnect_BLE();
        close_BLE();
        return super.onUnbind(intent);
    }



    public void close_BLE()
    {

        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;

    }



    public void disconnect_BLE() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        Logger.log(Level.DEBUG,TAG, "disconnect()");
        mBluetoothGatt.disconnect();
    }



    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;

        return mBluetoothGatt.getServices();
    }


    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }

        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        Logger.log(Level.DEBUG,TAG, "***characreristic.getUUID" + characteristic.getUuid());
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }
}