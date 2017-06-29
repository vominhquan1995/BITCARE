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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
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

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity3 extends BasePermissionActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private static final String TAG = "Scan device";
    private ListView lvDevices;
    private Button btnStopScanning, btnRescan, btnWithoutConnect;
    private ProgressBar progressBar;
    private View lnlScanComplete;
    private TextView tvNoDevices;
    private LeDeviceListAdapter listDevice;
    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "Device: " + device.getName() + ", " + device);
                listDevice.addDevice(device);
            } else {
                if (BluetoothDevice.ACTION_UUID.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                    if (uuidExtra != null) {
                        for (int i = 0; i < uuidExtra.length; i++) {
                            listDevice.addDevice(device);
                            Log.d(TAG, "\n  Device: " + device.getName() + ", " + device + ", Service: " + uuidExtra[i].toString());
                        }
                    }
                } else {
                    if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                        Log.d(TAG, "Discovery Started...");
                    } else {
                        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                            Iterator<BluetoothDevice> itr = listDevice.iterator();
//                            while (itr.hasNext()) {
//                                // Get Services for paired devices
//                                BluetoothDevice device = itr.next();
//                                Log.d(TAG, "Getting Services for " + device.getName() + ", " + device);
//                                if (!device.fetchUuidsWithSdp()) {
//                                    Log.d(TAG, "SDP Failed for " + device.getName());
//                                }
//
//                            }
                        }
                    }
                }
            }
            listDevice.notifyDataSetChanged();
        }
    };
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private DeviceConnection deviceConnection;
    private Boolean isScan;

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
        lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isScan) {
                    final BluetoothDevice device = listDevice.getDevice(position);
                    if (device == null) return;
                    Log.d("name", String.valueOf(device.getAddress()));
                    deviceConnection = new DeviceConnection(context, device.getAddress());
                    deviceConnection.bindService();
                    deviceConnection.register();
                    deviceConnection.connect();
                }
            }
        });
    }

    @Override
    public void initValue() {

    }

    @Override
    public void initAction() {
        register();
        // Getting the Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBTState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(ActionFoundReceiver);
    }

    private void CheckBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // If it isn't request to turn it on
        // List paired devices
        // Emulator doesn't support Bluetooth and will return null
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "Bluetooth NOT supported. Aborting.");
            return;
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                Log.d(TAG, "Bluetooth is enabled...");
                // Starting the device discovery
                mBluetoothAdapter.startDiscovery();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestRuntimePermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            "The application can't work without this permission",
                            new OnPermissionsLisnener() {
                                @Override
                                public void onPermissionGranted() {
                                    mBluetoothAdapter.startDiscovery();
                                }

                                @Override
                                public void onPermissionDenied() {
                                    finish();
                                }
                            });
                }

            } else {
                Intent enableBtIntent = new Intent(mBluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
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
        listDevice = new LeDeviceListAdapter();
        lvDevices.setAdapter(listDevice);
        scanLeDevice(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        listDevice.clear();
    }

    private void scanLeDevice(final boolean enable) {
        isScan = enable;
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isScan = false;
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
        listDevice.clear();
        listDevice.notifyDataSetChanged();
        register();
        CheckBTState();
        lnlScanComplete.setVisibility(View.GONE);
        tvNoDevices.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        btnStopScanning.setVisibility(View.VISIBLE);
    }

    private void stopScan() {
        lnlScanComplete.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        btnStopScanning.setVisibility(View.GONE);
        if (listDevice.getCount() == 0) {
            tvNoDevices.setVisibility(View.VISIBLE);
        }
    }

    public void register() {
        mHandler = new Handler();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(ActionFoundReceiver, filter); // Don't forget to unregister during onDestroy

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
            mInflator = DeviceScanActivity3.this.getLayoutInflater();
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

}