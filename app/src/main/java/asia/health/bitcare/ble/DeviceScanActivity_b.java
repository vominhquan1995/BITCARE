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
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import asia.health.bitcare.base.BaseDiscoverableBTActivity;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity_b extends BaseDiscoverableBTActivity {

    @Override
    protected int getEnableBluetoothRequestCode() {
        return 1111;
    }

    @Override
    protected int getDicoverableBTRequestCode() {
        return 2222;
    }

    @Override
    public int getView() {
        return R.layout.activity_device_scan;
    }

    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;
    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    // Member fields
    private ListView lvDevices;
    private ProgressBar progressBar;
    private Button btnScan;
    private TextView tvNoDevices;


    @Override
    public void initView() {
        super.initView();

        toolbar.setTitle("Device Scan");

        findViewById(R.id.btnStopScanning).setVisibility(View.GONE);
        tvNoDevices = (TextView) findViewById(R.id.tvNoDevices);
        tvNoDevices.setVisibility(View.GONE);
        lvDevices = (ListView) findViewById(R.id.lvDevices);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        btnScan = (Button) findViewById(R.id.btnRescan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                doDiscovery();
            }
        });
    }

    private BluetoothAdapter mBtAdapter;
    private LeDeviceListAdapter mLeDeviceListAdapter;

    @Override
    public void initValue() {

        setResult(Activity.RESULT_CANCELED);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        mLeDeviceListAdapter = new LeDeviceListAdapter();
        lvDevices.setAdapter(mLeDeviceListAdapter);
        lvDevices.setOnItemClickListener(mDeviceClickListener);
    }

    @Override
    public void initAction() {

        doDiscovery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup the window
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//        setContentView(R.layout.device_list);
        // Set result CANCELED incase the user backs out





//        // Initialize array adapters. One for already paired devices and
//        // one for newly discovered devices
//        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
//        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
//        // Find and set up the ListView for paired devices
//        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
//        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
//        pairedListView.setOnItemClickListener(mDeviceClickListener);
//        // Find and set up the ListView for newly discovered devices
//        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
//        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
//        newDevicesListView.setOnItemClickListener(mDeviceClickListener);
//
//        // Get a set of currently paired devices
//        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
//        // If there are paired devices, add each one to the ArrayAdapter
//        if (pairedDevices.size() > 0) {
//            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
//            for (BluetoothDevice device : pairedDevices) {
//                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//            }
//        } else {
//            String noDevices = "none_paired";
//            mPairedDevicesArrayAdapter.add(noDevices);
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");
        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle("scanning");
        // Turn on sub-title for new devices
//        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter

        requestDiscoverable(new RequestDiscoverableCallback() {
            @Override
            public void onAllow() {

                mLeDeviceListAdapter.clear();
                mLeDeviceListAdapter.notifyDataSetChanged();

                tvNoDevices.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                btnScan.setVisibility(View.GONE);

                mBtAdapter.startDiscovery();
            }

            @Override
            public void onDeny() {

                finish();
            }
        });
    }

    // The on-click listener for all devices in the ListViews
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int position, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();
            // Get the device MAC address, which is the last 17 chars in the View
//            String info = ((TextView) v).getText().toString();
//            String address = info.substring(info.length() - 17);
            String address = mLeDeviceListAdapter.getDevice(position).getAddress();
            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
////                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//                    mLeDeviceListAdapter.addDevice(device);
//                    mLeDeviceListAdapter.notifyDataSetChanged();
//                }
                mLeDeviceListAdapter.addDevice(device);
                mLeDeviceListAdapter.notifyDataSetChanged();
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle("select_device");
//                if (mNewDevicesArrayAdapter.getCount() == 0) {
//                    String noDevices = "none_found";
//                    mNewDevicesArrayAdapter.add(noDevices);
//                }
                if (mLeDeviceListAdapter.getCount() == 0) {
                    tvNoDevices.setVisibility(View.VISIBLE);
                }
                btnScan.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
    };


    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = getLayoutInflater();
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
            DeviceScanActivity2.ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.list_item_bluetooth_devices, viewGroup, false);
                viewHolder = new DeviceScanActivity2.ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (DeviceScanActivity2.ViewHolder) view.getTag();
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