/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package asia.health.bitcare.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BasePermissionActivity;
import asia.health.bitcare.widget.toast.Boast;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity2 extends BasePermissionActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private ListView lvDevices;
    private Button btnStopScanning, btnRescan, btnWithoutConnect;
    private ProgressBar progressBar;
    private View lnlScanComplete;
    private TextView tvNoDevices;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private DeviceConnection deviceConnection;
    private Boolean isScan;
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

//                            if (device.getName() != null && device.getName().contains("Remocon")) {

                            mLeDeviceListAdapter.addDevice(device);
                            mLeDeviceListAdapter.notifyDataSetChanged();
//                            }
                        }
                    });
                }
            };

    @Override
    public int getView() {
        return R.layout.activity_device_scan;
    }

    @Override
    public void initView() {
        super.initView();

        tvNoDevices = (TextView) findViewById(R.id.tvNoDevices);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        lvDevices = (ListView) findViewById(R.id.lvDevices);
        btnStopScanning = (Button) findViewById(R.id.btnStopScanning);
        btnRescan = (Button) findViewById(R.id.btnRescan);
        btnWithoutConnect = (Button) findViewById(R.id.btnWithoutConnect);
        lnlScanComplete = findViewById(R.id.lnlScanComplete);
        lnlScanComplete.setVisibility(View.GONE);
        toolbar.setTitle(R.string.activity_device_scan_title);
        toolbar.setNavigationIcon(null);

        btnStopScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanLeDevice(false);
            }
        });
        btnRescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanLeDevice(true);
            }
        });
        btnWithoutConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(context, MainActivity.class));
            }
        });
        lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isScan) {
                    final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
                    if (device == null) return;
                    Log.d("name", String.valueOf(device.getAddress()));
                    deviceConnection = new DeviceConnection(context, device.getAddress());
                    deviceConnection.bindService();
                    deviceConnection.register();
                    deviceConnection.connect();
                }
//                final Intent intent = new Intent(context, DeviceControlActivity.class);
//                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
//                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
//                stopScan();
//                startActivity(intent);

//                BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
//                sharedPreferences.edit()
//                        .putString(Prefs.DEVICE_ADDRESS, device.getAddress())
//                        .apply();
//                startMainActitivy(device.getAddress());
            }
        });
    }

    @Override
    public void initValue() {

    }

    @Override
    public void initAction() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestRuntimePermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    "Can't scan ble devices without this permission",
                    new OnPermissionsLisnener() {
                        @Override
                        public void onPermissionGranted() {

                        }

                        @Override
                        public void onPermissionDenied() {

                            finish();
                        }
                    });
        }

        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Boast.makeText(this, context.getString(R.string.ble_not_supported)).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Boast.makeText(this, context.getString(R.string.error_bluetooth_not_supported)).show();
            finish();
        }

        //TODO enable auto connect
//        if(mBluetoothAdapter.isEnabled()){
//            String deviceAddress = sharedPreferences.getString(Prefs.DEVICE_ADDRESS, "");
//            if (!deviceAddress.equals("")) {
//                startMainActitivy(deviceAddress);
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        lvDevices.setAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    private void startMainActitivy(String deviceAdress) {

//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra(MainActivity.EXTRAS_DEVICE_ADDRESS, deviceAdress);
//        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Contact chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
//            finish();
            //TODO enable na
//            String deviceAddress = sharedPreferences.getString(Prefs.DEVICE_ADDRESS, "");
//            if (!deviceAddress.equals("")) {
//                startMainActitivy(deviceAddress);
//            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    private void scanLeDevice(final boolean enable) {
        isScan=enable;
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isScan=false;
                    stopScan();
                }
            }, SCAN_PERIOD);
            startScan();
        } else {
            stopScan();
        }
        invalidateOptionsMenu();
    }

    private void startScan() {
        mLeDeviceListAdapter.clear();
        mLeDeviceListAdapter.notifyDataSetChanged();
        mBluetoothAdapter.startLeScan(mLeScanCallback);
        lnlScanComplete.setVisibility(View.GONE);
        tvNoDevices.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        btnStopScanning.setVisibility(View.VISIBLE);
    }

    private void stopScan() {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        lnlScanComplete.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        btnStopScanning.setVisibility(View.GONE);
        if (mLeDeviceListAdapter.getCount() == 0) {

//            new AlertDialog.Builder(context)
//                    .setTitle(getString(R.string.dialog_title_not_found_device))
//                    .setMessage(getString(R.string.dialog_message_not_found_device))
//                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    })
//                    .setPositiveButton(getString(R.string.positive_btn_try_again), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            startScan();
//                        }
//                    })
//                    .show();

            tvNoDevices.setVisibility(View.VISIBLE);
        }
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = DeviceScanActivity2.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.list_item_bluetooth_devices, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }

//    @Override
//    public void onBackPressed() {
////        LockScreenEvent.isAppVisible = false;
////        if (null != lockScreenEvent) {
////            lockScreenEvent.stop();
////        }
//      //  moveTaskToBack(true);
//     //   finish();
//    }
}