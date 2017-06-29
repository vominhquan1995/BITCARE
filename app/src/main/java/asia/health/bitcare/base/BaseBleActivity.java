package asia.health.bitcare.base;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import asia.health.bitcare.R;
import asia.health.bitcare.ble.DeviceConnection;
import asia.health.bitcare.widget.toast.Boast;

/**
 * Created by Leon on 5/23/2017.
 */

public abstract class BaseBleActivity extends BasePermissionActivity {

    private int REQUEST_ENABLE_BT;

    public DeviceConnection deviceConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRequestCode();

//        deviceConnection = new DeviceConnection(context, Constant.DEFAULT_DEVICE_ADDRESS);
//        deviceConnection.bindService();
    }

    protected void initRequestCode() {

        REQUEST_ENABLE_BT = getEnableBluetoothRequestCode();
    }

    protected abstract int getEnableBluetoothRequestCode();

    @Override
    protected void onResume() {
        super.onResume();

        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Boast.makeText(this, context.getString(R.string.error_bluetooth_not_supported)).show();
            finish();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                requestBlePermissions();
            }
        }

//        deviceConnection.register();
    }

    @Override
    protected void onPause() {
        super.onPause();

//        deviceConnection.unregister();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Contact chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                requestBlePermissions();
            } else {
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void requestBlePermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestRuntimePermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    "The application can't work without this permission",
                    new BasePermissionActivity.OnPermissionsLisnener() {
                        @Override
                        public void onPermissionGranted() {

                        }

                        @Override
                        public void onPermissionDenied() {

                            finish();
                        }
                    });
        }
    }
}
