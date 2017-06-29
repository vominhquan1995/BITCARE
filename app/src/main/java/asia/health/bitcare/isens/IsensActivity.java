package asia.health.bitcare.isens;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import asia.health.bitcare.R;

/**
 * Created by farid on 2017-06-13.
 */

public class IsensActivity extends AppCompatActivity {
    public final static double GLUCOSE_CONV = 18.016;   // conversion factor between mg/dL and mmol/L (mg/dL = mmol/L * 18.016)
    public final static int PERMISSION_REQUEST_CODE = 100;
    private static final boolean DEVICE_IS_BONDED = true;
    private static final boolean DEVICE_NOT_BONDED = false;
    private static final int REQUEST_ENABLE_BT = 2;
    private final static int OP_CODE_REPORT_STORED_RECORDS = 1;
    private final static int OP_CODE_DELETE_STORED_RECORDS = 2;
    private final static int OP_CODE_REPORT_NUMBER_OF_RECORDS = 4;
    private final static int OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE = 5;
    private final static int OP_CODE_RESPONSE_CODE = 6;
    private final static int COMPLETE_RESULT_FROM_METER = 192;
    private final static int OPERATOR_ALL_RECORDS = 1;
    private final static int OPERATOR_GREATER_OR_EQUAL_RECORDS = 3;
    private final static int OPERATOR_LATEST_RECORDS = 6;
    private final static int FILTER_TYPE_SEQUENCE_NUMBER = 1;
    private final static int RESPONSE_SUCCESS = 1;
    private final static int RESPONSE_OP_CODE_NOT_SUPPORTED = 2;
    private final static int RESPONSE_NO_RECORDS_FOUND = 6;
    private final static int RESPONSE_ABORT_UNSUCCESSFUL = 7;
    private final static int RESPONSE_PROCEDURE_NOT_COMPLETED = 8;
    private final static int SOFTWARE_REVISION_BASE = 1, SOFTWARE_REVISION_1 = 1, SOFTWARE_REVISION_2 = 0; //base: custom profile version
    private final SparseArray<GlucoseRecord> mRecords = new SparseArray<GlucoseRecord>();
    private TextView _txt_serial_num, _txt_device_name, _txt_software_ver, _title_listview, _result, _txt_total_count;
    private ListView _listview;
    private DeviceAdapter _adapter;
    private LinearLayout _deviceInfo, _buttonView;
    private EditText _sequenceNumber;
    private int _number;
    private Button _btnBack, _btnStartScan, _btnStopScan, _btnDownloadAll, _btnDownloadAfter, _btnSynchronizeTime, _btnDisconnect;
    private CheckBox _autoConnect;
    private RadioButton _radioButton_mgdl, _radioButton_mmoll;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mGlucoseMeasurementCharacteristic;
    private BluetoothGattCharacteristic mGlucoseMeasurementContextCharacteristic;
    private BluetoothGattCharacteristic mRACPCharacteristic;
    private BluetoothGattCharacteristic mDeviceSerialCharacteristic;
    private BluetoothGattCharacteristic mDeviceSoftwareRevisionCharacteristic;
    private BluetoothGattCharacteristic mCustomTimeCharacteristic;
    private String _serial_text, _software_ver;
    private int _version_1;
    private Handler mHandler;
    private double mGlucoseData;
    private ProgressBar mProgressBar;

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            if ((status == 0 || status == 19 || status == 8) && newState == BluetoothProfile.STATE_DISCONNECTED) {
                close();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Pumpkin", "onConnectionStateChange() ====");
                        _btnStartScan.setEnabled(true);
                        _btnBack.setEnabled(false);
                        _btnDownloadAll.setEnabled(false);
                        _btnDownloadAfter.setEnabled(false);
                        _btnSynchronizeTime.setEnabled(false);
                        _btnDisconnect.setEnabled(false);
                        Log.d("Pumpkin", "onConnectionStateChange() - finish()");

                        mProgressBar.setVisibility(View.GONE);
                        Intent intent = new Intent();
                        intent.putExtra("glucose", mGlucoseData);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
            }

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        _btnStartScan.setEnabled(false);
                        _btnStopScan.setEnabled(false);
                    }
                });
                mBluetoothGatt = gatt;
                broadcastUpdate(Const.INTENT_BLE_DEVICECONNECTED, "");
                gatt.discoverServices();

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("Pumpkin", "onConnectionStateChange() -- STATE_DISCONNECTED");
                broadcastUpdate(Const.INTENT_BLE_DEVICEDISCONNECTED, "");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                initCharacteristics();
                broadcastUpdate(Const.INTENT_BLE_SERVICEDISCOVERED, "");
                for (BluetoothGattService service : gatt.getServices()) {
                    if (Const.BLE_SERVICE_GLUCOSE.equals(service.getUuid())) { //1808
                        mGlucoseMeasurementCharacteristic = service.getCharacteristic(Const.BLE_CHAR_GLUCOSE_MEASUREMENT); //2A18
                        mGlucoseMeasurementContextCharacteristic = service.getCharacteristic(Const.BLE_CHAR_GLUCOSE_CONTEXT); //2A34
                        mRACPCharacteristic = service.getCharacteristic(Const.BLE_CHAR_GLUCOSE_RACP);//2A52
                    } else if (Const.BLE_SERVICE_DEVICE_INFO.equals(service.getUuid())) { //180A
                        mDeviceSerialCharacteristic = service.getCharacteristic(Const.BLE_CHAR_GLUCOSE_SERIALNUM);//2A25
                        mDeviceSoftwareRevisionCharacteristic = service.getCharacteristic(Const.BLE_CHAR_SOFTWARE_REVISION); //2A28
                    } else if (Const.BLE_SERVICE_CUSTOM.equals(service.getUuid())) {//FFF0
                        mCustomTimeCharacteristic = service.getCharacteristic(Const.BLE_CHAR_CUSTOM_TIME);//FFF1
                        if (mCustomTimeCharacteristic != null)
                            gatt.setCharacteristicNotification(mCustomTimeCharacteristic, true);
                    }
                }
                // Validate the device for required characteristics
                if (mGlucoseMeasurementCharacteristic == null || mRACPCharacteristic == null) {
                    broadcastUpdate(Const.INTENT_BLE_DEVICENOTSUPPORTED, "");
                    return;
                }

                if (mDeviceSoftwareRevisionCharacteristic != null) {
                    readDeviceSoftwareRevision(gatt);
                }

            } else {
                broadcastUpdate(Const.INTENT_BLE_ERROR, getResources().getString(R.string.ERROR_DISCOVERY_SERVICE) + " (" + status + ")");
            }
        }

        ;

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (Const.BLE_CHAR_SOFTWARE_REVISION.equals(characteristic.getUuid())) { // 2A28
                    String[] revisions = characteristic.getStringValue(0).split("\\.");
                    _version_1 = Integer.parseInt(revisions[0]);
                    _software_ver = characteristic.getStringValue(0);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (_version_1 == SOFTWARE_REVISION_1) {
                                _txt_software_ver.setTextColor(Color.BLACK);
                            } else {
                                _txt_software_ver.setTextColor(Color.RED);
                            }
                            _txt_software_ver.setText(_software_ver);
                        }
                    });

                    // in case of valid version & supported version, do something what we want.
                    if (_version_1 >= SOFTWARE_REVISION_BASE && _version_1 == SOFTWARE_REVISION_1) {
                        if (mCustomTimeCharacteristic == null) {
                            //  'custom' must be present. (OR exit)
                            broadcastUpdate(Const.INTENT_BLE_READ_SOFTWARE_REV, characteristic.getStringValue(0));
                            Log.d("-- disconnect", " -- custom null");
                            gatt.disconnect();
                            return;
                        } else {
                            // continue
                        }
                    } else {
                        // in case of un-supported version, do exception handling and exit.
                        broadcastUpdate(Const.INTENT_BLE_READ_SOFTWARE_REV, "");
                    }

                    if (mDeviceSerialCharacteristic != null) {
                        readDeviceSerial(gatt);
                    }
                } else if (Const.BLE_CHAR_GLUCOSE_SERIALNUM.equals(characteristic.getUuid())) { //2A25
                    _serial_text = characteristic.getStringValue(0);
                    final int last_seq = getPreference(_serial_text);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _txt_serial_num.setText(_serial_text);
                            _sequenceNumber.setText(String.valueOf(last_seq + 1));
                            _sequenceNumber.setSelection(_sequenceNumber.length());
                        }
                    });
                    if (_version_1 > SOFTWARE_REVISION_1 || _version_1 < SOFTWARE_REVISION_1) {
                        Log.d("-- disconnect", " -- revision");
                        gatt.disconnect();
                        return;
                    }
                    enableRecordAccessControlPointIndication(gatt);
                }
            }
        }

        ;

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (Const.BLE_CHAR_GLUCOSE_MEASUREMENT.equals(descriptor.getCharacteristic().getUuid())) { //2A18
                    enableGlucoseContextNotification(gatt);
                }
                if (Const.BLE_CHAR_GLUCOSE_CONTEXT.equals(descriptor.getCharacteristic().getUuid())) { //2A34
                    enableTimeSyncIndication(gatt);
                }
                if (Const.BLE_CHAR_GLUCOSE_RACP.equals(descriptor.getCharacteristic().getUuid())) { //2A52
                    enableGlucoseMeasurementNotification(gatt);
                }
                if (Const.BLE_CHAR_CUSTOM_TIME.equals(descriptor.getCharacteristic().getUuid())) { //FFF1
                    broadcastUpdate(Const.INTENT_BLE_REQUEST_COUNT, "");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _title_listview.setVisibility(View.GONE);
                            _listview.setVisibility(View.GONE);
                            //_deviceInfo.setVisibility(View.VISIBLE);
                            //_buttonView.setVisibility(View.VISIBLE);


                            Log.d("Pumpkin", "onDescriptorWrite() ----------------");
                            _result.setMovementMethod(new ScrollingMovementMethod());
                            _result.scrollTo(0, 0);
                            _result.setText("");
                            //_btnBack.setVisibility(View.VISIBLE);
                            //_buttonView.setVisibility(View.GONE);
                            broadcastUpdate(Const.INTENT_BLE_SEQUENCECOMPLETED, Integer.toString(_number));
                        }
                    });
                }
            } else if (status == BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION) {
                if (gatt.getDevice().getBondState() != BluetoothDevice.BOND_NONE) {
                    broadcastUpdate(Const.INTENT_BLE_ERROR, getResources().getString(R.string.ERROR_AUTH_ERROR_WHILE_BONDED) + " (" + status + ")");
                }
            }
        }

        ;

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            final UUID uuid = characteristic.getUuid();

            Log.d("--  char", characteristic.getUuid().toString());
            Log.d("--  value", characteristic.getValue().toString());
            if (Const.BLE_CHAR_CUSTOM_TIME.equals(uuid)) { //FFF1
                int offset = 0;
                final int opCode = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset);
                offset += 2; // skip the operator

                if (opCode == OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE) { // 05: time result
                    //broadcastUpdate(Const.INTENT_BLE_REQUEST_COUNT, "");
                }
            } else if (Const.BLE_CHAR_GLUCOSE_MEASUREMENT.equals(uuid)) { //2A18
                int offset = 0;
                final int flags = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset);
                offset += 1;

                final boolean timeOffsetPresent = (flags & 0x01) > 0;
                final boolean typeAndLocationPresent = (flags & 0x02) > 0;
                final boolean sensorStatusAnnunciationPresent = (flags & 0x08) > 0;
                final boolean contextInfoFollows = (flags & 0x10) > 0;

                int sequenceNumber = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
                boolean isSavedData = true;
                GlucoseRecord record = mRecords.get(sequenceNumber);
                if (record == null) {
                    record = new GlucoseRecord();
                    isSavedData = false;
                }
                record.sequenceNumber = sequenceNumber;
                record.flag_context = 0;
                offset += 2;

                final int year = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset + 0);
                final int month = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset + 2);
                final int day = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset + 3);
                final int hours = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset + 4);
                final int minutes = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset + 5);
                final int seconds = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset + 6);
                offset += 7;

                final Calendar calendar = Calendar.getInstance();
                calendar.set(year, month - 1, day, hours, minutes, seconds);
                record.time = calendar.getTimeInMillis() / 1000;

                if (timeOffsetPresent) {
                    record.timeoffset = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16, offset);
                    record.time = record.time + (record.timeoffset * 60);
                    offset += 2;
                }

                if (typeAndLocationPresent) {
                    byte[] value = characteristic.getValue();
                    int glucoseValue = (int) bytesToFloat(value[offset], value[offset + 1]);
                    if(_radioButton_mgdl.isChecked()) {
                        record.glucoseData = glucoseValue;
                    } else{
                        record.glucoseData = Double.parseDouble(String.valueOf(Math.round(10 * (double) glucoseValue / GLUCOSE_CONV) / 10.0));
                    }
                    final int typeAndLocation = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset + 2);
                    int type = (typeAndLocation & 0xF0) >> 4;
                    record.flag_cs = type == 10 ? 1 : 0;
                    offset += 3;
                }

                if (sensorStatusAnnunciationPresent) {
                    int hilow = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
                    if (hilow == 64) record.flag_hilow = -1;//lo
                    if (hilow == 32) record.flag_hilow = 1;//hi

                    offset += 2;
                }

                if (contextInfoFollows == false) {
                    record.flag_context = 1;
                }

                try {
                    if (isSavedData == false)
                        mRecords.put(record.sequenceNumber, record);
                } catch (Exception e) {
                }

                final GlucoseRecord glucoseRecord = record;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String str_hilow = "-";
                        if (glucoseRecord.flag_hilow == -1) str_hilow = "Lo";
                        else if (glucoseRecord.flag_hilow == 1) str_hilow = "Hi";

                        mRecords.put(glucoseRecord.sequenceNumber, glucoseRecord);
                        if (!contextInfoFollows) {
                            broadcastUpdate(Const.INTENT_BLE_DATASETCHANGED, "");
                            _result.append("### sequence: " + glucoseRecord.sequenceNumber + "," + " glucose: " + glucoseRecord.glucoseData +
                                    "," + " date: " + getDate(glucoseRecord.time) + "," + " timeoffset: " + glucoseRecord.timeoffset +
                                    "," + " hilo: " + str_hilow + "BBB \n\n");
                            _result.setVisibility(View.GONE);
                            mGlucoseData = glucoseRecord.glucoseData;
                        }
                        _sequenceNumber.setText(String.valueOf(glucoseRecord.sequenceNumber + 1));
                        _sequenceNumber.setSelection(_sequenceNumber.length());
                    }
                });
                setPreference(_serial_text, record.sequenceNumber);
            } else if (Const.BLE_CHAR_GLUCOSE_CONTEXT.equals(uuid)) { //2A34
                int offset = 0;
                final int flags = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset);
                offset += 1;

                final boolean carbohydratePresent = (flags & 0x01) > 0;
                final boolean mealPresent = (flags & 0x02) > 0;
                final boolean moreFlagsPresent = (flags & 0x80) > 0;

                final int sequenceNumber = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
                offset += 2;

                if (moreFlagsPresent) offset += 1;

                if (carbohydratePresent) offset += 3;

                final int meal = mealPresent == true ? characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset) : -1;

                // data set modifications must be done in UI thread
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        boolean isSavedData = true;
                        GlucoseRecord record = mRecords.get(sequenceNumber);
                        if (record == null) {
                            record = new GlucoseRecord();
                            isSavedData = false;
                        }
                        if (record == null || mealPresent == false)
                            return;

                        record.sequenceNumber = sequenceNumber;
                        record.flag_context = 1;

                        switch (meal) {
                            case 0:
                                if (record.flag_cs == 0)
                                    record.flag_nomark = 1;
                                break;
                            case 1:
                                record.flag_meal = -1;
                                break;
                            case 2:
                                record.flag_meal = 1;
                                break;
                            case 3:
                                record.flag_fasting = 1;
                                break;
                            case 6:
                                record.flag_ketone = 1;
                                break;
                        }
                        try {
                            String str_hilow = "-";
                            if (record.flag_hilow == -1) str_hilow = "Lo";
                            else if (record.flag_hilow == 1) str_hilow = "Hi";

                            if (isSavedData == false)
                                mRecords.put(record.sequenceNumber, record);

                            _result.append("### sequence: " + record.sequenceNumber + "," + " glucose: " + record.glucoseData +
                                    "," + " date: " + getDate(record.time) + "," + " timeoffset: " + record.timeoffset +
                                    "," + " hilo: " + str_hilow + "AAA\n\n");
                            _result.setVisibility(View.GONE);
                            mGlucoseData = record.glucoseData;
                        } catch(Exception e){}
                        broadcastUpdate(Const.INTENT_BLE_DATASETCHANGED, "");
                    }
                });
            } else if (Const.BLE_CHAR_GLUCOSE_RACP.equals(uuid)) { // Record Access Control Point characteristic 2A52
                int offset = 0;
                final int opCode = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset);
                offset += 2; // skip the operator

                if (opCode == COMPLETE_RESULT_FROM_METER) { //C0
                    final int requestedOpCode = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset - 1);
                    switch (requestedOpCode) {
                        case RESPONSE_SUCCESS: //01
                            broadcastUpdate(Const.INTENT_BLE_READCOMPLETED, "");
                            //mBluetoothGatt.writeCharacteristic(characteristic);
                            break;
                        case RESPONSE_OP_CODE_NOT_SUPPORTED: //02
                            break;
                    }
                } else if (opCode == OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE) { // 05
                    if (mBluetoothGatt == null || mRACPCharacteristic == null) {
                        broadcastUpdate(Const.INTENT_BLE_ERROR, getResources().getString(R.string.ERROR_CONNECTION_STATE_CHANGE));
                        return;
                    }

                    clear();
                    broadcastUpdate(Const.INTENT_BLE_OPERATESTARTED, "");

                    _number = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
                    offset += 2;

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _txt_total_count.setText(String.valueOf(_number));
                        }
                    });

                } else if (opCode == OP_CODE_RESPONSE_CODE) { // 06
                    final int subResponseCode = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset);
                    final int responseCode = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset + 1);
                    offset += 2;

                    switch (responseCode) {
                        case RESPONSE_SUCCESS:
                            break;
                        case RESPONSE_NO_RECORDS_FOUND: //06000106
                            // android 6.0.1 issue - app disconnect send
                            //mBluetoothGatt.writeCharacteristic(characteristic);
                            broadcastUpdate(Const.INTENT_BLE_READCOMPLETED, "");

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                try {
                                    Thread.sleep(100);
                                    //mBluetoothGatt.disconnect();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    Thread.sleep(100);
                                    //mBluetoothGatt.writeCharacteristic(characteristic);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case RESPONSE_OP_CODE_NOT_SUPPORTED:
                            broadcastUpdate(Const.INTENT_BLE_OPERATENOTSUPPORTED, "");
                            break;
                        case RESPONSE_PROCEDURE_NOT_COMPLETED:
                        case RESPONSE_ABORT_UNSUCCESSFUL:
                        default:
//                            broadcastUpdate(Const.INTENT_BLE_OPERATEFAILED, "");
                            break;
                    }
                }
            }
        }

        ;

        private void readDeviceSoftwareRevision(final BluetoothGatt gatt) {
            gatt.readCharacteristic(mDeviceSoftwareRevisionCharacteristic);
        }

        private void readDeviceSerial(final BluetoothGatt gatt) {
            gatt.readCharacteristic(mDeviceSerialCharacteristic);
        }
    };
    private boolean _isScanning = false;
    private ScanCallback scanCallback;
    private BroadcastReceiver mBondingBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            final int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);

            if (device == null || mBluetoothGatt == null) return;

            // skip other devices
            if (!device.getAddress().equals(mBluetoothGatt.getDevice().getAddress())) {
                return;
            }

            if (mBluetoothGatt == null) return;

            if (bondState == BluetoothDevice.BOND_BONDING) {
            } else if (bondState == BluetoothDevice.BOND_BONDED) {
                broadcastUpdate(Const.INTENT_BLE_BONDED, device.getAddress());
                //[2016.06.10][leenain] After bonded, discover services.
                mBluetoothGatt.discoverServices();
            } else if (bondState == BluetoothDevice.BOND_NONE) {
                broadcastUpdate(Const.INTENT_BLE_BOND_NONE, "");
                Log.d("-- close", " -- non bond");
                close();
            }
        }
    };
    private BluetoothAdapter.LeScanCallback mLEScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (device != null) {
                try {
                    if (ScannerServiceParser.decodeDeviceAdvData(scanRecord)) {
                        // On some devices device.getName() is always null. We have to parse the name manually :(
                        // This bug has been found on Sony Xperia Z1 (C6903) with Android 4.3.
                        // https://devzone.nordicsemi.com/index.php/cannot-see-device-name-in-sony-z1
                        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                            if (_autoConnect.isChecked() == true) {
                                broadcastUpdate(Const.INTENT_BLE_CONNECT_DEVICE, device.toString());
                                _txt_device_name.setText(device.getName());
                            } else addScannedDevice(device, rssi, DEVICE_IS_BONDED);
                        } else {
                            addScannedDevice(device, rssi, DEVICE_NOT_BONDED);
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
//					DebugLogger.e(TAG, "Invalid data in Advertisement packet " + e.toString());
                }
            }
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            final String extraData = intent.getStringExtra(Const.INTENT_BLE_EXTRA_DATA);

            switch (action) {
                case Const.INTENT_BLE_CONNECT_DEVICE:
                    if (extraData != "") {
                        connect(extraData);
                    }
                    break;
                case Const.INTENT_BLE_BOND_NONE:
                    disconnect();
                    _buttonView.setVisibility(View.GONE);
                    _btnStartScan.setEnabled(true);
                    _btnStopScan.setEnabled(false);
                    showToast(R.string.bond_none);
                    break;
                case Const.INTENT_BLE_DEVICECONNECTED:
                    break;
                case Const.INTENT_BLE_DEVICEDISCONNECTED:
                    break;
                case Const.INTENT_BLE_SERVICEDISCOVERED:
                    clear();
                    break;
                case Const.INTENT_BLE_ERROR:
                    Toast.makeText(IsensActivity.this, extraData, Toast.LENGTH_SHORT).show();
                    break;
                case Const.INTENT_BLE_DEVICENOTSUPPORTED:
                    showToast(R.string.not_supported);
                    break;
                case Const.INTENT_BLE_OPERATESTARTED:
                case Const.INTENT_BLE_OPERATECOMPLETED:
                    break;
                case Const.INTENT_BLE_OPERATEFAILED:
                    showToast(R.string.gls_operation_failed);
                    break;
                case Const.INTENT_BLE_OPERATENOTSUPPORTED:
                    showToast(R.string.gls_operation_not_supported);
                    break;
                case Const.INTENT_BLE_DATASETCHANGED:
                    break;
                case Const.INTENT_BLE_READ_MANUFACTURER:
                    break;
                case Const.INTENT_BLE_READ_SOFTWARE_REV:
                    showToast(R.string.error_software_revision);
                    break;
                case Const.INTENT_BLE_READ_SERIALNUMBER:
                    break;
                case Const.INTENT_BLE_RACPINDICATIONENABLED:
                    if (getCustomTimeSync() == false) {
                        try {
                            Thread.sleep(200);
                            getCustomTimeSync();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Date date = new Date(System.currentTimeMillis());
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strNow = sdfNow.format(date);
                        _result.setText("");
                        _result.append(strNow + "\n");
                    }
                    break;
                case Const.INTENT_BLE_REQUEST_COUNT:
                    if (getSequenceNumber() == false) {
                        try {
                            Thread.sleep(200);
                            getSequenceNumber();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Const.INTENT_BLE_SEQUENCECOMPLETED:
                    if (getSerialNumber() == null) return;
                    requestBleAll();
                    break;
                case Const.INTENT_BLE_READAFTER:
                    if (getSerialNumber() == null) return;
                    requestBleAfter();
                    break;
                case Const.INTENT_BLE_READCOMPLETED:
                    break;
            }
        }
    };

    private static boolean runningOnKitkatOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Const.INTENT_BLE_EXTRA_DATA);
        intentFilter.addAction(Const.INTENT_BLE_CONNECT_DEVICE);
        intentFilter.addAction(Const.INTENT_BLE_BOND_NONE);
        intentFilter.addAction(Const.INTENT_BLE_BONDED);
        intentFilter.addAction(Const.INTENT_BLE_DEVICECONNECTED);
        intentFilter.addAction(Const.INTENT_BLE_DEVICEDISCONNECTED);
        intentFilter.addAction(Const.INTENT_BLE_SERVICEDISCOVERED);
        intentFilter.addAction(Const.INTENT_BLE_ERROR);
        intentFilter.addAction(Const.INTENT_BLE_DEVICENOTSUPPORTED);
        intentFilter.addAction(Const.INTENT_BLE_OPERATESTARTED);
        intentFilter.addAction(Const.INTENT_BLE_OPERATECOMPLETED);
        intentFilter.addAction(Const.INTENT_BLE_OPERATEFAILED);
        intentFilter.addAction(Const.INTENT_BLE_OPERATENOTSUPPORTED);
        intentFilter.addAction(Const.INTENT_BLE_DATASETCHANGED);
        intentFilter.addAction(Const.INTENT_BLE_RACPINDICATIONENABLED);
        intentFilter.addAction(Const.INTENT_BLE_READ_MANUFACTURER);
        intentFilter.addAction(Const.INTENT_BLE_READ_SOFTWARE_REV);
        intentFilter.addAction(Const.INTENT_BLE_READ_SERIALNUMBER);
        intentFilter.addAction(Const.INTENT_BLE_READCOMPLETED);
        intentFilter.addAction(Const.INTENT_BLE_SEQUENCECOMPLETED);
        intentFilter.addAction(Const.INTENT_BLE_REQUEST_COUNT);
        intentFilter.addAction(Const.INTENT_BLE_READAFTER);
        return intentFilter;
    }

    private void initCharacteristics() {
        mGlucoseMeasurementCharacteristic = null;
        mGlucoseMeasurementContextCharacteristic = null;
        mRACPCharacteristic = null;
        mDeviceSerialCharacteristic = null;
        mDeviceSoftwareRevisionCharacteristic = null;
        mCustomTimeCharacteristic = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isens);

        initView();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        boolean isBleAvailable = getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) ? true : false;
        if (isBleAvailable && runningOnKitkatOrHigher()) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(this, R.string.ValidationWarningPopup_31, Toast.LENGTH_SHORT).show();
            }
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        }
        clearView();
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean isBleAvailable = getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) ? true : false;
        if (isBleAvailable && runningOnKitkatOrHigher() && mBluetoothAdapter.isEnabled() == true) {
            final Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : devices) {
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    //startScan();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initCallbackLollipop() {
        if (scanCallback != null) return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.scanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    if (result != null) {
                        try {
                            if (ScannerServiceParser.decodeDeviceAdvData(result.getScanRecord().getBytes())) {
                                if (result.getDevice().getBondState() == BluetoothDevice.BOND_BONDED) {
                                    if (_autoConnect.isChecked() == true) {
                                        broadcastUpdate(Const.INTENT_BLE_CONNECT_DEVICE, result.getDevice().toString());
                                        _txt_device_name.setText(result.getDevice().getName());
                                    } else
                                        addScannedDevice(result.getDevice(), result.getRssi(), DEVICE_IS_BONDED);
                                } else {
                                    addScannedDevice(result.getDevice(), result.getRssi(), DEVICE_NOT_BONDED);
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    super.onBatchScanResults(results);
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                }
            };
        }
    }

    private void addScannedDevice(final BluetoothDevice device, final int rssi, final boolean isBonded) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _adapter.addDevice(new ExtendedDevice(device, rssi, isBonded));
                }
            });
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
    }

    public void startScan() {
        mProgressBar.setVisibility(View.VISIBLE);

        _adapter.clearDevices();
        if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (scanCallback == null)
                    initCallbackLollipop();

                List<ScanFilter> filters = new ArrayList<ScanFilter>();

                ScanSettings settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // or BALANCED previously
                        .setReportDelay(0)
                        .build();

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mBluetoothAdapter.getBluetoothLeScanner().flushPendingScanResults(scanCallback);
                    mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
                    mBluetoothAdapter.getBluetoothLeScanner().startScan(filters, settings, scanCallback);
                }

            } else {
                // Samsung Note II with Android 4.3 build JSS15J.N7100XXUEMK9 is not filtering by UUID at all. We have to disable it
                mBluetoothAdapter.startLeScan(mLEScanCallback);
            }
        }
        _isScanning = true;
    }

    public void stopScan() {
        if (_isScanning) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mBluetoothAdapter != null) {
                    mBluetoothAdapter.getBluetoothLeScanner().flushPendingScanResults(scanCallback);
                    mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
                } else {
                    mBluetoothAdapter.stopLeScan(mLEScanCallback);
                }
            } catch (NullPointerException e) {
            } catch (Exception e) {
            }
            _isScanning = false;
            close();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGattUpdateReceiver); //ble
        Log.d("-- close", " -- destroy");
        close();
    }

    private int getPreference(String key) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    private void setPreference(String key, int value) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void onDisconnectClick(View v) {
        disconnect();
    }

    public void onStartScanClick(View v) {
        setScanningView();

        String[] permission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (isBLEEnabled() && checkPermission(permission)) {
            startScan();
        } else if (isBLEEnabled() == false) {
            showBLEDialog();
        } else if (checkPermission(permission) == false) {
            requestPermissions(permission);
        }
    }

    public boolean checkPermission(String[] permission) {
        for (int i = 0; i < permission.length; i++) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission[i]) < 0) {
                return false;
            }
        }
        return true;
    }

    public void requestPermissions(String[] permission) {
        for (int i = 0; i < permission.length; i++) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(IsensActivity.this, permission[i]) == false) {
                ActivityCompat.requestPermissions(IsensActivity.this, permission, PERMISSION_REQUEST_CODE);
                return;
            }
        }
        Toast.makeText(IsensActivity.this, "Call camera allows us to access.", Toast.LENGTH_SHORT).show();
    }

    public void initView() {
        _radioButton_mgdl = (RadioButton) findViewById(R.id.radio_mgdl);
        _radioButton_mmoll = (RadioButton) findViewById(R.id.radio_mmoll);
        _autoConnect = (CheckBox) findViewById(R.id.checkbox_autoConnect);
        _btnDownloadAll = (Button) findViewById(R.id.downloadAllButton);
        _btnDownloadAfter = (Button) findViewById(R.id.downloadAfterButton);
        _btnSynchronizeTime = (Button) findViewById(R.id.synchronizeTimeButton);
        _btnDisconnect = (Button) findViewById(R.id.disconnectButton);
        _btnStartScan = (Button) findViewById(R.id.startScanButton);
        _btnStopScan = (Button) findViewById(R.id.stopScanButton);
        _btnStopScan.setEnabled(false);
        _btnBack = (Button) findViewById(R.id.backButton);
        _result = (TextView) findViewById(R.id.resultView);
        _sequenceNumber = (EditText) findViewById(R.id.sequence_num);
        _buttonView = (LinearLayout) findViewById(R.id.buttonView);
        _deviceInfo = (LinearLayout) findViewById(R.id.deviceInfo);
        _title_listview = (TextView) findViewById(R.id.title_listview);
        _txt_serial_num = (TextView) findViewById(R.id.serial_num);
        _txt_device_name = (TextView) findViewById(R.id.device_name);
        _txt_software_ver = (TextView) findViewById(R.id.software_version);
        _txt_total_count = (TextView) findViewById(R.id.total_count);
        _adapter = new DeviceAdapter(this);
        _listview = (ListView) findViewById(R.id.listview);
        _listview.setAdapter(_adapter);
        _listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //_deviceInfo.setVisibility(View.VISIBLE);
                _title_listview.setVisibility(View.GONE);
                _listview.setVisibility(View.GONE);
                _txt_device_name.setText(((ExtendedDevice) _adapter.getItem(i)).device.getName());
                broadcastUpdate(Const.INTENT_BLE_CONNECT_DEVICE, ((ExtendedDevice) _adapter.getItem(i)).device.toString());
            }
        });

        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
            ((TextView) findViewById(R.id.version)).setText("v"+info.versionName+"a");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setScanningView() {
        _btnDownloadAll.setEnabled(true);
        _btnDownloadAfter.setEnabled(true);
        _btnSynchronizeTime.setEnabled(true);
        _btnDisconnect.setEnabled(true);
        _btnStartScan.setEnabled(false);
        _btnStopScan.setEnabled(true);
        _result.setVisibility(View.GONE);
        _deviceInfo.setVisibility(View.GONE);
        _buttonView.setVisibility(View.GONE);
        _btnBack.setVisibility(View.GONE);
        _btnBack.setEnabled(true);
        _title_listview.setVisibility(View.VISIBLE);
        _listview.setVisibility(View.VISIBLE);
        _txt_device_name.setText("");
        _txt_serial_num.setText("");
        _txt_software_ver.setText("");
        _txt_total_count.setText("");
    }

    public void clearView() {
        _result.setVisibility(View.GONE);
        _deviceInfo.setVisibility(View.GONE);
        _title_listview.setVisibility(View.GONE);
        _listview.setVisibility(View.GONE);
        _buttonView.setVisibility(View.GONE);
        _btnBack.setVisibility(View.GONE);
    }

    public void onBackClick(View v) {
        _result.setVisibility(View.GONE);
        //_buttonView.setVisibility(View.VISIBLE);
        _btnBack.setVisibility(View.GONE);
    }

    public void onStopScanClick(View v) {
        mProgressBar.setVisibility(View.GONE);
        _btnStartScan.setEnabled(true);
        _btnStopScan.setEnabled(false);
        stopScan();
        clearView();
    }

    public void onDownloadAllClick(View v) {
        _result.setMovementMethod(new ScrollingMovementMethod());
        _result.scrollTo(0, 0);
        _result.setText("");
        _btnBack.setVisibility(View.VISIBLE);
        _buttonView.setVisibility(View.GONE);
        broadcastUpdate(Const.INTENT_BLE_SEQUENCECOMPLETED, Integer.toString(_number));
    }

    public void onDownloadAfterClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_sequenceNumber.getWindowToken(), 0);

        if (_sequenceNumber.getText().toString().trim().length() <= 0) {
            Toast.makeText(getApplicationContext(), "Enter sequence number", Toast.LENGTH_SHORT).show();
            return;
        }
        _result.setMovementMethod(new ScrollingMovementMethod());
        _result.scrollTo(0, 0);
        _result.setText("");
        _btnBack.setVisibility(View.VISIBLE);
        _buttonView.setVisibility(View.GONE);
        broadcastUpdate(Const.INTENT_BLE_READAFTER, Integer.toString(_number));
    }

    public void onSynchronizeTimeClick(View v) {
        _result.setMovementMethod(new ScrollingMovementMethod());
        _result.scrollTo(0, 0);
        _result.setText("");
        _btnBack.setVisibility(View.VISIBLE);
        _buttonView.setVisibility(View.GONE);
        _result.append("Time synchronizing..\n");
        _result.setVisibility(View.VISIBLE);
        broadcastUpdate(Const.INTENT_BLE_RACPINDICATIONENABLED, "Custom_Flag");
    }

    private boolean isBLEEnabled() {
        final BluetoothAdapter adapter = mBluetoothManager.getAdapter();
        return adapter != null && adapter.isEnabled();
    }

    private void showBLEDialog() {
        final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.disconnect();
    }

    public String getSerialNumber() {
        return _serial_text;
    }

    //[2016.06.10][leenain] clear gatt cache
    private boolean refreshDeviceCache(BluetoothGatt gatt) {
        try {
            BluetoothGatt localBluetoothGatt = gatt;
            Method localMethod = localBluetoothGatt.getClass().getMethod("refresh", new Class[0]);
            if (localMethod != null) {
                boolean bool = ((Boolean) localMethod.invoke(localBluetoothGatt, new Object[0])).booleanValue();
                return bool;
            }
        } catch (Exception localException) {
            Log.d("exception", "refreshing device");
        }
        return false;
    }

    public boolean connect(final String address) {
        stopScan();

        if (mBluetoothAdapter == null || address == null) {
            return false;
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            return false;
        }

        if (mBluetoothManager != null && mBluetoothManager.getConnectionState(device, BluetoothProfile.GATT) == BluetoothProfile.STATE_CONNECTED)
            return false;

        Log.d("---connect", address);
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
            if (mBluetoothGatt.connect()) {
                return true;
            } else {
                return false;
            }
        }

        if (mHandler == null)
            mHandler = new Handler();

        final IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBondingBroadcastReceiver, filter);

        mBluetoothGatt = device.connectGatt(getApplicationContext(), true, mGattCallback);

        //[2016.06.10][leenain] clear gatt cache
        refreshDeviceCache(mBluetoothGatt);
        mBluetoothDeviceAddress = address;

        return true;
    }

    public void clear() {
        mRecords.clear();
        broadcastUpdate(Const.INTENT_BLE_DATASETCHANGED, "");
    }

    public void close() {
        Log.d("-- close", " -- close");
        if (mBluetoothGatt != null) mBluetoothGatt.close();
        if (mRecords != null) mRecords.clear();

        mGlucoseMeasurementCharacteristic = null;
        mGlucoseMeasurementContextCharacteristic = null;
        mRACPCharacteristic = null;
        mDeviceSerialCharacteristic = null;
        mBluetoothGatt = null;
    }

    private void enableGlucoseMeasurementNotification(final BluetoothGatt gatt) {
        if (mGlucoseMeasurementCharacteristic == null) return;

        gatt.setCharacteristicNotification(mGlucoseMeasurementCharacteristic, true);
        final BluetoothGattDescriptor descriptor = mGlucoseMeasurementCharacteristic.getDescriptor(Const.BLE_DESCRIPTOR_DESCRIPTOR);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }

    private void enableGlucoseContextNotification(final BluetoothGatt gatt) {
        if (mGlucoseMeasurementContextCharacteristic == null) return;

        gatt.setCharacteristicNotification(mGlucoseMeasurementContextCharacteristic, true);
        final BluetoothGattDescriptor descriptor = mGlucoseMeasurementContextCharacteristic.getDescriptor(Const.BLE_DESCRIPTOR_DESCRIPTOR);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }

    private void enableRecordAccessControlPointIndication(final BluetoothGatt gatt) {
        if (mRACPCharacteristic == null) return;

        gatt.setCharacteristicNotification(mRACPCharacteristic, true);
        final BluetoothGattDescriptor descriptor = mRACPCharacteristic.getDescriptor(Const.BLE_DESCRIPTOR_DESCRIPTOR);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }

    private void enableTimeSyncIndication(final BluetoothGatt gatt) {
        if (mCustomTimeCharacteristic == null) return;

        gatt.setCharacteristicNotification(mCustomTimeCharacteristic, true);
        final BluetoothGattDescriptor descriptor = mCustomTimeCharacteristic.getDescriptor(Const.BLE_DESCRIPTOR_DESCRIPTOR);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }

    private boolean getCustomTimeSync() {
        if (mBluetoothGatt == null || mCustomTimeCharacteristic == null) {
            broadcastUpdate(Const.INTENT_BLE_ERROR, getResources().getString(R.string.ERROR_CONNECTION_STATE_CHANGE) + "null");
            return false;
        }

        clear();
        broadcastUpdate(Const.INTENT_BLE_OPERATESTARTED, "");

        setCustomTimeSync(mCustomTimeCharacteristic);

        return mBluetoothGatt.writeCharacteristic(mCustomTimeCharacteristic);
    }

    private void setCustomTimeSync(final BluetoothGattCharacteristic characteristic) {
        if (characteristic == null) return;

        Calendar currCal = new GregorianCalendar();

        byte bCurrYear1 = (byte) (currCal.get(Calendar.YEAR) & 0xff);
        byte bCurrYear2 = (byte) ((currCal.get(Calendar.YEAR) >> 8) & 0xff);
        byte bCurrMonth = (byte) ((currCal.get(Calendar.MONTH) + 1) & 0xff);
        byte bCurrDay = (byte) (currCal.get(Calendar.DAY_OF_MONTH) & 0xff);
        byte bCurrHour = (byte) (currCal.get(Calendar.HOUR_OF_DAY) & 0xff);
        byte bCurrMin = (byte) (currCal.get(Calendar.MINUTE) & 0xff);
        byte bCurrSec = (byte) (currCal.get(Calendar.SECOND) & 0xff);

        byte op_code_1 = (byte) ((byte) COMPLETE_RESULT_FROM_METER & 0xff);
        byte[] data = {op_code_1, 0x03, 0x01, 0x00, bCurrYear1, bCurrYear2, bCurrMonth, bCurrDay, bCurrHour, bCurrMin, bCurrSec};

        characteristic.setValue(new byte[data.length]);

        for (int i = 0; i < data.length; i++) {
            characteristic.setValue(data);
        }

    }

    private boolean getSequenceNumber() {
        if (mBluetoothGatt == null || mRACPCharacteristic == null) {
            broadcastUpdate(Const.INTENT_BLE_ERROR, getResources().getString(R.string.ERROR_CONNECTION_STATE_CHANGE) + "null");
            return false;
        }

        clear();
        broadcastUpdate(Const.INTENT_BLE_OPERATESTARTED, "");

        setOpCode(mRACPCharacteristic, OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_ALL_RECORDS);
        return mBluetoothGatt.writeCharacteristic(mRACPCharacteristic);

    }

    private boolean getAllRecords() {
        if (mBluetoothGatt == null || mRACPCharacteristic == null) {
            broadcastUpdate(Const.INTENT_BLE_ERROR, getResources().getString(R.string.ERROR_CONNECTION_STATE_CHANGE) + "null");
            return false;
        }

        clear();
        broadcastUpdate(Const.INTENT_BLE_OPERATESTARTED, "");

        Log.d("-- all records", "data");

        setOpCode(mRACPCharacteristic, OP_CODE_REPORT_STORED_RECORDS, OPERATOR_ALL_RECORDS);
        return mBluetoothGatt.writeCharacteristic(mRACPCharacteristic);

    }

    private boolean getAfterRecords() {
        if (mBluetoothGatt == null || mRACPCharacteristic == null) {
            broadcastUpdate(Const.INTENT_BLE_ERROR, getResources().getString(R.string.ERROR_CONNECTION_STATE_CHANGE) + "null");
            return false;
        }

        clear();
        broadcastUpdate(Const.INTENT_BLE_OPERATESTARTED, "");

        Log.d("-- after records", "data");

        if (mCustomTimeCharacteristic == null) { //0403
            setOpCode(mRACPCharacteristic, OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_GREATER_OR_EQUAL_RECORDS,
                    Integer.parseInt(_sequenceNumber.getText().toString().trim()));
        } else {
            if (mCustomTimeCharacteristic.getUuid().equals(Const.BLE_CHAR_CUSTOM_TIME_MC) == true)
                setOpCode(mRACPCharacteristic, OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_GREATER_OR_EQUAL_RECORDS,
                        Integer.parseInt(_sequenceNumber.getText().toString().trim()));
            else
                setOpCode(mRACPCharacteristic, OP_CODE_REPORT_STORED_RECORDS, OPERATOR_GREATER_OR_EQUAL_RECORDS,
                        Integer.parseInt(_sequenceNumber.getText().toString().trim()));
        }

        return mBluetoothGatt.writeCharacteristic(mRACPCharacteristic);

    }

    private void setOpCode(final BluetoothGattCharacteristic characteristic, final int opCode, final int operator, final Integer... params) {
        if (characteristic == null) return;

        final int size = 2 + ((params.length > 0) ? 1 : 0) + params.length * 2; // 1 byte for opCode, 1 for operator, 1 for filter type (if parameters exists) and 2 for each parameter
        characteristic.setValue(new byte[size]);

        int offset = 0;
        characteristic.setValue(opCode, BluetoothGattCharacteristic.FORMAT_UINT8, offset);
        offset += 1;

        characteristic.setValue(operator, BluetoothGattCharacteristic.FORMAT_UINT8, offset);
        offset += 1;

        if (params.length > 0) {
            characteristic.setValue(FILTER_TYPE_SEQUENCE_NUMBER, BluetoothGattCharacteristic.FORMAT_UINT8, offset);
            offset += 1;

            for (final Integer i : params) {
                characteristic.setValue(i, BluetoothGattCharacteristic.FORMAT_UINT16, offset);
                offset += 2;
            }
        }
    }

    public String getDateTime(long t) {
        java.text.DateFormat df = DateFormat.getDateFormat(this);// getDateFormat(context);
        String strDate = df.format(t * 1000);
        df = DateFormat.getTimeFormat(this);
        strDate += " " + df.format(t * 1000);

        return strDate;
    }

    public String getDate(long t) {
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdfNow.format(t * 1000);

        return date;
    }

    private float bytesToFloat(byte b0, byte b1) {
        return (float) unsignedByteToInt(b0) + ((unsignedByteToInt(b1) & 0x0F) << 8);

    }

    private int unsignedByteToInt(byte b) {
        return b & 0xFF;
    }

    private void showToast(final int messageResId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(IsensActivity.this, messageResId, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void broadcastUpdate(final String action, final String data) {
        final Intent intent = new Intent(action);
        if (data != "")
            intent.putExtra(Const.INTENT_BLE_EXTRA_DATA, data);
        sendBroadcast(intent);
    }

    private void requestBleAll() {
        if (getAllRecords() == false) {
            try {
                Thread.sleep(200);
                getAllRecords();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void requestBleAfter() {
        if (getAfterRecords() == false) {
            try {
                Thread.sleep(200);
                getAfterRecords();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
