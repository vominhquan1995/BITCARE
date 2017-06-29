/*
 * Copyright (C) 2009 The Android Open Source Project
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

      Derived from BluetoothChat Android example.

 */

package asia.health.bitcare.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import asia.health.bitcare.activity.MainActivity;


/**
 * This is the main Activity that displays the current chat session.
 */
public class ANDManager {

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    static final String HEXES = "0123456789ABCDEF";
    //39121440
    private static final String TAG = "KKC";
    private static final boolean D = true;
    private static final int REQUEST_ENABLE_BT = 1;
    //change value to test
    public static String BPSys = "110";
    public static String BPMin = "90";
    public static String BPPulse = "90";
    public static boolean BMPSTYPE_D = false;
    //    public static double mGlucose = 0;
    ByteArrayOutputStream mPacket = new ByteArrayOutputStream(128);
    boolean mPDU_valid = true;
    private String mConnectedDeviceName = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothBPService mBluetoothService = null;
    //    private BloodPressureInputView bloodPressureInputView;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothBPService.STATE_CONNECTED:
                            if (D) Log.d(TAG, "mConnectedDeviceName = " + mConnectedDeviceName);
                            mPDU_valid = true;
                            break;
                        case BluetoothBPService.STATE_CONNECTING:
                            break;
                        case BluetoothBPService.STATE_LISTEN:
                        case BluetoothBPService.STATE_NONE:
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    if (mPDU_valid)
                        processInput(msg.arg1, (byte[]) msg.obj);
                    break;
                case MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    if (D) Log.d(TAG, "Connected to " + mConnectedDeviceName);
                    break;
                case MESSAGE_TOAST:
//                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    if (D) Log.d(TAG, "MESSAGE_TOAST = " + msg.getData().getString(TOAST));
                    break;
            }
        }
    };
    private Activity activity;

    public ANDManager(Activity activity) {
        this.activity = activity;
//        int numberListener = 1;
//        int timeDelay = 1000;
//        while (numberListener > 0) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    MainActivity.onReceiveDataListener.onReceiveData();
//                }
//            }, timeDelay);
//            numberListener--;
//            timeDelay += 2000;
//        }
//        this.bloodPressureInputView = bloodPressureInputView;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            if (D) Log.d(TAG, "Bluetooth is not available");
            activity.finish();
            return;
        }
    }

    public static String getHex(byte[] raw, int len) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(3 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)))
                    .append(" ");
            if (--len == 0)
                break;
        }
        return hex.toString();
    }

    public void onStart() {
        if (D) Log.e(TAG, "++ ON START ++");
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (mBluetoothService == null) setupTrace();
        }
    }

    public synchronized void onResume() {
        if (D) Log.e(TAG, "+ ON RESUME +");
        if (mBluetoothService != null) {
            if (mBluetoothService.getState() == BluetoothBPService.STATE_NONE) {
                mBluetoothService.start();
            }
        }
    }

    private void setupTrace() {
        mBluetoothService = new BluetoothBPService(activity, mHandler);

    }

    public synchronized void onPause() {
        if (D) Log.e(TAG, "- ON PAUSE -");
    }

    public void onStop() {
        if (D) Log.e(TAG, "-- ON STOP --");
    }

    public void onDestroy() {
        if (mBluetoothService != null) mBluetoothService.stop();
        if (D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void sendMessage(String message) {
        if (mBluetoothService.getState() != BluetoothBPService.STATE_CONNECTED) {
//            Boast.makeText(this, message).show();
            Log.d("KKC", "sendMessage = " + message);
            return;
        }

        if (message.length() > 0) {
            byte[] send = message.getBytes();
            mBluetoothService.write(send);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    setupTrace();
                } else {
                    Log.d(TAG, "BT not enabled");
                    activity.finish();
                }
        }
    }

    private void processInput(int len, byte[] rxpacket) {
        mPacket.write(rxpacket, 0, len);
        Log.d(TAG, "< " + getHex(rxpacket, len));

        boolean ok = true;
        boolean done = false;
        int pktLen = 0;

        ByteArrayInputStream pdu = new ByteArrayInputStream(mPacket.toByteArray());
        int pktType = pdu.read() + (pdu.read() << 8);

        if (pktType != 2) {
            Log.e(TAG, "< Incorrect packet type");
            ok = false;
        }
        if (ok) {
            pktLen = pdu.read() + (pdu.read() << 8) + (pdu.read() << 16) + (pdu.read() << 24);

            if ((pktLen + 60) == mPacket.size()) {
                done = true;
            } else if ((pktLen + 60) < mPacket.size()) {
                Log.d(TAG, "< Packet too big...pktLen=" + pktLen);
                ok = false;
            } else {
                Log.d(TAG, "< Incomplete...");
            }
        }
        if (ok) {
            if (done) {
                Log.d(TAG, "< type=" + pktType);
                Log.d(TAG, "< length=" + pktLen);

                byte reading[] = new byte[16];
                String dev = "";

                int devType = pdu.read() + (pdu.read() << 8);
                pdu.skip(1); // Skip the flag for now
                pdu.skip(7); // Skip the timestamp for now
                pdu.skip(7); // Skip the timestamp for now
                pdu.skip(6); // Skip the bdaddr for now
                pdu.skip(6); // Skip the bdaddr for now

                byte sn[] = new byte[16];
                pdu.read(sn, 0, 12); // Serial number

                pdu.skip(10); // Skip reserved
                pdu.skip(1); // Skip the battery status for now
                pdu.skip(1); // Skip reserved
                pdu.skip(1); // Skip firmware rev for now

                String strReading = null;
                String strSystolic = null;
                String strDiastolic = null;
                String strPulse_rate = null;

                if (devType == 767) { // BP
                    dev = "UA-767PBT";
                    pdu.read(reading, 0, 10);
                    strReading = new String(reading, 0, 9);
                    strSystolic = new String(reading, 2, 2);
                    strDiastolic = new String(reading, 4, 2);
                    strPulse_rate = new String(reading, 6, 2);

                    long n = Long.parseLong(strSystolic, 16);
                    long m = Long.parseLong(strDiastolic, 16);
                    long y = Long.parseLong(strPulse_rate, 16);

                    BPSys = Long.toString(n + m);
                    BPMin = Long.toString(m);
                    BPPulse = Long.toString(y);
                    //set type input is device
                    BMPSTYPE_D = true;

                    Log.d("KKC", "BPSys = " + BPSys);
                    Log.d("KKC", "BPMin = " + BPMin);
                    Log.d("KKC", "BPPulse = " + BPPulse);
                    //move to page input bloodpressure
                    MainActivity.onReceiveDataListener.onReceiveData();
                } else {
                    dev = "UC-321PBT";
                    pdu.read(reading, 0, 14);
                    strReading = new String(reading, 0, 13);
                }

                String strSN = new String(sn, 0, 11);

                Log.i(TAG, dev + " sn: " + strSN + " Reading: " + strReading);

                sendMessage("PWA1");
//                mGlucose = 1;
//                bloodPressureInputView.onAddBloodPressureSuccess("Success");
                mPacket.reset();
            }
        } else {
            mPDU_valid = false;
            mPacket.reset();
            Log.i(TAG, "Invalid PDU!!!");
            sendMessage("PWA0");
        }

        try {
            pdu.close();
        } catch (IOException e) {
            Log.e(TAG, "Problem closing input stream.");
        }
    }


}