package asia.health.bitcare.ble;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Leon on 9/23/2016.
 */

public class DeviceConnection {

    private final String TAG = this.getClass().getSimpleName();
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BLEService.ACTION_GATT_CONNECTED.equals(action) ||
                    BLEService.ACTION_GATT_DISCONNECTED.equals(action)) {
//                   || BLEService.ACTION_GATT_CONNECTING.equals(action)) {

//                ((MainActivity) context).onConnectionStateChanged(action);//TODO

            } else if (BLEService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
            } else if (BLEService.ACTION_DATA_AVAILABLE.equals(action)) {

            } else if (BLEService.ACTION_BUTTON_A.equals(action) ||
                    BLEService.ACTION_BUTTON_B.equals(action) ||
                    BLEService.ACTION_BUTTON_C.equals(action) ||
                    BLEService.ACTION_BUTTON_D.equals(action)) {

//                ((MainActivity) context).blinkActionButton(action);//TODO
            }
        }
    };
    public BLEService mBleService;
    private Context context;
    private Activity activity;
    private String mDeviceAddress;
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBleService = ((BLEService.LocalBinder) service).getService();
            if (!mBleService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBleService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBleService = null;
        }
    };
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    public DeviceConnection(Context context, String mDeviceAddress) {
        this.context = context;
        this.activity = (Activity) context;
        this.mDeviceAddress = mDeviceAddress;
    }

    public void register() {

        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    public void unregister() {

        context.unregisterReceiver(mGattUpdateReceiver);
    }

    public void bindService() {

        Intent gattServiceIntent = new Intent(context, BLEService.class);
        context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void connect() {

        if (mBleService != null) {
            final boolean result = mBleService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    public void disconnect() {

        context.unbindService(mServiceConnection);
        mBleService.disconnect();
        mBleService = null;
    }

    private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BLEService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BLEService.ACTION_GATT_CONNECTING);
        intentFilter.addAction(BLEService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BLEService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BLEService.ACTION_BUTTON_A);
        intentFilter.addAction(BLEService.ACTION_BUTTON_B);
        intentFilter.addAction(BLEService.ACTION_BUTTON_C);
        intentFilter.addAction(BLEService.ACTION_BUTTON_D);
        return intentFilter;
    }
}
