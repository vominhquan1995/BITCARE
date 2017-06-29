package asia.health.bitcare.fragment;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseDiscoverableBTActivity;
import asia.health.bitcare.base.BaseFragment;
import asia.health.bitcare.ble.BluetoothService;
import asia.health.bitcare.ble.DeviceScanActivity;
import asia.health.bitcare.dialog.DialogRequestBluetooth;
import asia.health.bitcare.model.BloodPressure;
import asia.health.bitcare.mvp.presenter.BloodPressureInputPresenter;
import asia.health.bitcare.mvp.view.BloodPressureInputView;
import asia.health.bitcare.network.APIConstant;
import asia.health.bitcare.prefs.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceSettingsFragment_b extends BaseFragment implements View.OnClickListener, BloodPressureInputView {

    private final int REQUEST_CODE_SCAN_BT = 7226;

    private LinearLayout btnListDevice;
    private Switch switchBle;
    private LinearLayout lnPairing;
    private int blueToothOnOff;
    private FrameLayout lnHide;
    private DialogRequestBluetooth dialogRequestBluetooth;
    private LinearLayout HealthPairing;
    private BloodPressureInputPresenter presenter;
    private BloodPressure bloodPressure;
    private String mediation = APIConstant.BSMEDICINEYN_Y;
    private String exercise = APIConstant.BSEXERCISEYN_Y;

    @Override
    public int setFragmentView() {
        return R.layout.fragment_device_settings;
    }

    @Override
    public void initView() {
        btnListDevice = (LinearLayout) view.findViewById(R.id.lnListDevices);
        switchBle = (Switch) view.findViewById(R.id.switchBle);

        lnPairing = (LinearLayout) view.findViewById(R.id.lnPairing);
        lnHide = (FrameLayout) view.findViewById(R.id.lnHide);
//        HealthPairing = (LinearLayout) view.findViewById(R.id.HealthPairing);

        view.findViewById(R.id.llPairing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (device != null) {
                    bluetoothService.unpairDevice(device);
                }
                startActivityForResult(new Intent(context, DeviceScanActivity.class), REQUEST_CODE_SCAN_BT);
            }
        });
        presenter = new BloodPressureInputPresenter(this);
    }

    private BluetoothService bluetoothService;

    @Override
    public void initValue() {
//        lnPairing.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(context, DeviceScanActivity.class));
//            }
//        });
        bluetoothService = new BluetoothService(context, mHandler);
        bluetoothService.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (bluetoothService != null) {
            bluetoothService.stop();
        }
    }

    @Override
    public void initAction() {
        btnListDevice.setOnClickListener(this);
        lnPairing.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnListDevices:
                getMainActivity().switchFragment(new BluetoothListDevicesFragment());
                break;
            case R.id.lnPairing:
                lnHide.setVisibility(View.VISIBLE);
                new DialogRequestBluetooth.Build(getMainActivity()).setonConfirmRequestListener(new DialogRequestBluetooth.Build.onConfirmRequest() {
                    @Override
                    public void onYes() {
                        Log.d("Request", "Yes");
//                        ((BaseBleActivity) getActivity()).deviceConnection.connect();
//                        try {
//                            bloodPressure = new BloodPressure();
//                            bloodPressure.setBpmStyle(APIConstant.BPMSTYPE_U);
//                            bloodPressure.setBpSys(50);
//                            bloodPressure.setBpMin(140);
//                            bloodPressure.setBpPulse(60);
//                            bloodPressure.setBpWeight(120);
//                            bloodPressure.setBpMedicineYN(mediation);
//                            bloodPressure.setBpExersiseYN(exercise);
//                            bloodPressure.setBpmsDate(StringUtils.formatDataSimple("20170524112841"));
//                            presenter.addBloodPressure(bloodPressure);
//                        } catch (Exception e) {
//                            dismissProgressDialog();
//                            Boast.makeText(getMainActivity(), e.getMessage()).show();
//                        }
//                        final Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                lnHide.setVisibility(View.GONE);
//                            }
//                        }, 300000);

                        ((BaseDiscoverableBTActivity) getActivity()).requestDiscoverable(new BaseDiscoverableBTActivity.RequestDiscoverableCallback() {
                            @Override
                            public void onAllow() {

                                if (device != null) {
                                    bluetoothService.unpairDevice(device);
                                }
                                device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(Constant.DEFAULT_DEVICE_ADDRESS);
                                // Attempt to connect to the device
                                bluetoothService.connect(device);

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        lnHide.setVisibility(View.GONE);
                                    }
                                }, 300000);
                            }

                            @Override
                            public void onDeny() {

                                lnHide.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onNo() {
                        lnHide.setVisibility(View.GONE);
                        Log.d("Request", "No");
                    }
                }).show();
                break;
//            case R.id.HealthPairing:
//                getMainActivity().switchFragment(new BluetoothListDevicesFragment());
//                break;
        }
    }

    @Override
    public void onAddBloodPressureSuccess(String serviceMsg) {
        getMainActivity().switchFragment(new BloodPressureFragment());
    }

    @Override
    public void onError(String errorMessage) {
        Log.d("Success", errorMessage);
    }

    private BluetoothDevice device;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN_BT && resultCode == Activity.RESULT_OK) {

            String address = data.getExtras()
                    .getString(DeviceScanActivity.EXTRA_DEVICE_ADDRESS);
            // Get the BLuetoothDevice object
            device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
            // Attempt to connect to the device
            bluetoothService.connect(device);
        }
    }

    private String mConnectedDeviceName;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
//                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
//                    switch (msg.arg1) {
//                        case BluetoothService.STATE_CONNECTED:
//                            Log.d("BlueTooth",getString(R.string.title_connected_to));
//                            mConversationArrayAdapter.clear();
//                            break;
//                        case BluetoothService.STATE_CONNECTING:
//                            Log.d("BlueTooth",getString(R.string.title_connecting));
//                            break;
//                        case BluetoothService.STATE_LISTEN:
//                        case BluetoothService.STATE_NONE:
//                            Log.d("BlueTooth",getString(R.string.title_not_connected));
//                            break;
//                    }
                    break;
                case BluetoothService.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
//                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case BluetoothService.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
//                    mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);

                    Toast.makeText(context, mConnectedDeviceName + ":  " + readMessage, Toast.LENGTH_SHORT).show();

                    break;
                case BluetoothService.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(BluetoothService.DEVICE_NAME);
                    Toast.makeText(context, "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothService.MESSAGE_TOAST:
                    Toast.makeText(context, msg.getData().getString(BluetoothService.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
