package asia.health.bitcare.ble;

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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

import rx.Observable;

/**
 * Created by Administrator on 21.09.2015.
 */
public class BLEService extends Service {
    public static final String ACTION_BUTTON_A = "ACTION_BUTTON_A";
    public static final String ACTION_BUTTON_B = "ACTION_BUTTON_B";
    public static final String ACTION_BUTTON_C = "ACTION_BUTTON_C";
    public static final String ACTION_BUTTON_D = "ACTION_BUTTON_D";
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public final static String ACTION_GATT_CONNECTED =
            "bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String ACTION_GATT_CONNECTING =
            "bluetooth.le.ACTION_GATT_CONNECTING";
    public final static String ACTION_SMS_RECEIVED = "ACTION_SMS_RECEIVED";
    public final static String ACTION_INCOMING_CALL = "ACTION_INCOMING_CALL";
    private final String TAG = this.getClass().getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    public BluetoothAdapter mBluetoothAdapter;
    public int mConnectionState = STATE_DISCONNECTED;
//    public final static String EXTRA_DATA =
//            "bluetooth.le.EXTRA_DATA";
//    public static final String ACTION_GET_SETTING_VALUE =
//            "bluetooth.le.ACTION_GET_SETTING_VALUE";
//    public static final String ACTION_GET_WRONG_FORMAT =
//            "bluetooth.le.ACTION_GET_WRONG_FORMAT";
//    public static final String ACTION_GET_VERSION_VALUE =
//            "bluetooth.le.ACTION_GET_VERSION_VALUE";
//    public static final String ACTION_UPLOAD_FILE_START =
//            "bluetooth.le.ACTION_UPLOAD_START";
//    public static final String ACTION_UPLOADING
//            = "bluetooth.le.ACTION_UPLOADING";
//    public static final String ACTION_UPLOADED
//            = "bluetooth.le.ACTION_UPLOADED";
//
//    //Development Action
//    public static final String ACTION_GET_DEVELOPMENT_DATA_1
//            = "bluetooth.le.ACTION_GET_DEVELOPMENT_DATA_1";
//    public static final String ACTION_GET_DEVELOPMENT_DATA_2
//            = "bluetooth.le.ACTION_GET_DEVELOPMENT_DATA_2";
//
//    //Install Action
//    public static final String ACTION_GET_INSTALL_DATA = "bluetooth.le.ACTION_GET_INSTALL_DATA";
//
//    //Power
//    public static final String ACTION_POWER_OFF = "bluetooth.le.ACTION_POWER_OFF";
    int retryCount;
    boolean isConnected;
    private BluetoothManager mBluetoothManager;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                isConnected = true;
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                isConnected = false;
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                for (BluetoothGattService service : gatt.getServices()) {
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        gatt.setCharacteristicNotification(characteristic, true);
                        for (BluetoothGattDescriptor bluetoothGattDescriptor : characteristic.getDescriptors()) {
                            bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        }
                    }
                }
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS && characteristic.getValue().length == 10) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {

            //TODO
//            int signal = characteristic.getValue()[0];
//            switch (signal) {
//                case DevicePrefs.SIGNAL_A:
//                    broadcastUpdate(ACTION_BUTTON_A);
//                    sendToDevice(DevicePrefs.SIGNAL_a);
//                    break;
//                case DevicePrefs.SIGNAL_B:
//                    broadcastUpdate(ACTION_BUTTON_B);
//                    sendToDevice(DevicePrefs.SIGNAL_a);
//                    break;
//                case DevicePrefs.SIGNAL_C:
//                    broadcastUpdate(ACTION_BUTTON_C);
//                    sendToDevice(DevicePrefs.SIGNAL_a);
//                    break;
//                case DevicePrefs.SIGNAL_D:
//                    broadcastUpdate(ACTION_BUTTON_D);
//                    sendToDevice(DevicePrefs.SIGNAL_a);
//                    break;
//            }
//            SmsSender.send(BLEService.this, signal);

//            if (Utils.isNotificaValue(characteristic.getValue()))
//                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
//            else if (Utils.isSettingValue(characteristic.getValue()))
//                broadcastUpdate(ACTION_GET_SETTING_VALUE, characteristic);
//            else if (Utils.isVersionValue(characteristic.getValue()))
//                broadcastUpdate(ACTION_GET_VERSION_VALUE, characteristic);
//            else if (Utils.isUploadStart(characteristic.getValue()))
//                broadcastUpdate(ACTION_UPLOAD_FILE_START);
//            else if (Utils.isUploading(characteristic.getValue()))
//                broadcastUpdate(ACTION_UPLOADING);
//            else if (Utils.isUploaded(characteristic.getValue()))
//                broadcastUpdate(ACTION_UPLOADED);
//            else if (Utils.isDevelopment1(characteristic.getValue()))
//                broadcastUpdate(ACTION_GET_DEVELOPMENT_DATA_1, characteristic);
//            else if (Utils.isDevelopment2(characteristic.getValue()))
//                broadcastUpdate(ACTION_GET_DEVELOPMENT_DATA_2, characteristic);
//            else if (Utils.isInstallValue(characteristic.getValue()))
//                broadcastUpdate(ACTION_GET_INSTALL_DATA, characteristic);
//            else if (Utils.isPowerOff(characteristic.getValue()))
//                broadcastUpdate(ACTION_POWER_OFF,characteristic);
//            else broadcastUpdate(ACTION_GET_WRONG_FORMAT, characteristic);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);

        }
    };
    private BroadcastReceiver bleServiceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//            sendToDevice(DevicePrefs.SIGNAL_a);
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
//        String a = Utils.toBinary(characteristic.getValue());
//        if (a.length() >= 32) {
//            intent.putExtra(EXTRA_DATA, a);
//            sendBroadcast(intent);
//        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        registerReceiver(bleServiceBroadcastReceiver, makeBleIntentFilter());
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
//        close();
        unregisterReceiver(bleServiceBroadcastReceiver);
        return super.onUnbind(intent);
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to countDownBackGAP.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {

                broadcastUpdate(ACTION_GATT_CONNECTING);

                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;

        broadcastUpdate(ACTION_GATT_CONNECTING);

        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    public Observable<Boolean> writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return Observable.just(false);
        }
        return Observable.just(mBluetoothGatt.writeCharacteristic(characteristic));
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }

    public void sendToDevice(int value) {

        try {
            BluetoothGattCharacteristic bluetoothGattCharacteristic =
                    this.getSupportedGattServices().get(2).getCharacteristics().get(1);
            bluetoothGattCharacteristic.setValue(new byte[]{(byte) value});
            this.writeCharacteristic(bluetoothGattCharacteristic);
        } catch (NullPointerException e) {
            Log.e("CheckVersion: ", e.toString());
        }
    }

    private IntentFilter makeBleIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SMS_RECEIVED);
        intentFilter.addAction(ACTION_INCOMING_CALL);
        return intentFilter;
    }

    public class LocalBinder extends Binder {
        public BLEService getService() {
            return BLEService.this;
        }
    }
}
