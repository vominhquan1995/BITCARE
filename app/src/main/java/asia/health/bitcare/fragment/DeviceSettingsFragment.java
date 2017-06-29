package asia.health.bitcare.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseDiscoverableBTActivity;
import asia.health.bitcare.base.BaseFragment;
import asia.health.bitcare.ble.BluetoothService;
import asia.health.bitcare.bluetooth.ANDManager;
import asia.health.bitcare.bt.BluetoothConnector;
import asia.health.bitcare.bt.BtConstants;
import asia.health.bitcare.bt.BtService;
import asia.health.bitcare.bt.DeviceListActivity;
import asia.health.bitcare.dialog.DialogRequestBluetooth;
import asia.health.bitcare.isens.IsensActivity;
import asia.health.bitcare.model.BloodPressure;
import asia.health.bitcare.mvp.presenter.BloodPressureInputPresenter;
import asia.health.bitcare.mvp.view.BloodPressureInputView;
import asia.health.bitcare.network.APIConstant;
import asia.health.bitcare.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceSettingsFragment extends BaseFragment implements View.OnClickListener, BloodPressureInputView {
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    public static double mGlucose = 0;
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
    private ANDManager andManager;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BtService mBtService = null;
    private BluetoothConnector mBtConnector = null;
    private BluetoothDevice device;
    private String mConnectedDeviceName;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BtConstants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BtService.STATE_CONNECTED:
                            Log.d("Pumpkin", "BtService.STATE_CONNECTED");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    reqData();
                                }
                            }, 5000);
                            break;
                        case BtService.STATE_CONNECTING:
                            Log.d("Pumpkin", "BtService.STATE_CONNECTING");
                            break;
                        case BtService.STATE_LISTEN:
                            Log.d("Pumpkin", "BtService.STATE_LISTEN");
                            break;
                        case BtService.STATE_NONE:
                            Log.d("Pumpkin", "BtService.STATE_NONE");
                            break;
                    }
                    break;
                case BtConstants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = StringUtils.BytesToHex(writeBuf);
                    ;
                    Log.d("Pumpkin", "BtConstants.MESSAGE_WRITE => " + writeMessage);
                    break;
                case BtConstants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d("Pumpkin", "BtConstants.MESSAGE_READ => " + mConnectedDeviceName + ":  " + readMessage);
                    break;
                case BtConstants.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(BluetoothService.DEVICE_NAME);
                    Toast.makeText(context, "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case BtConstants.MESSAGE_TOAST:
                    Toast.makeText(context, msg.getData().getString(BluetoothService.TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        andManager = new ANDManager(getActivity());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBtService == null) {
            mBtService = new BtService(getActivity(), mHandler);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        andManager.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        andManager.onStop();
    }

//    private BluetoothBPService bluetoothService;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        andManager.onDestroy();
    }

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
//                    bluetoothService.unpairDevice(device);
                }
                mGlucose = 0;
                startActivityForResult(new Intent(context, IsensActivity.class), REQUEST_CODE_SCAN_BT);
            }
        });
        presenter = new BloodPressureInputPresenter(this);
    }

    @Override
    public void initValue() {
//        lnPairing.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(context, DeviceScanActivity.class));
//            }
//        });
//        bluetoothService = new BluetoothBPService(context, mHandler);
//        bluetoothService.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        if (bluetoothService != null) {
//            bluetoothService.stop();
//        }
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
//                new DialogRequestBluetooth.Build(getMainActivity()).setonConfirmRequestListener(new DialogRequestBluetooth.Build.onConfirmRequest() {
//                    @Override
//                    public void onYes() {
//                        Log.d("Request", "Yes");
                        andManager.onStart();
                        andManager.onResume();
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
//                                if (device != null) {
//                                    bluetoothService.unpairDevice(device);
//                                }
//                                device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(Constant.DEFAULT_DEVICE_ADDRESS);
//                                // Attempt to connect to the device
//                                bluetoothService.connect(device);
//
//                                final Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        lnHide.setVisibility(View.GONE);
//                                    }
//                                }, 300000);
                            }

                            @Override
                            public void onDeny() {
//                                lnHide.setVisibility(View.GONE);
                            }
                        });
//                    }

//                    @Override
//                    public void onNo() {
////                        lnHide.setVisibility(View.GONE);
//                        Log.d("Request", "No");
//                    }
//                }).show();
                break;
//            case R.id.HealthPairing:
//                getMainActivity().switchFragment(new BluetoothListDevicesFragment());
//                break;
        }
    }

    @Override
    public void onAddBloodPressureSuccess(String serviceMsg) {
        try {
            getMainActivity().switchFragment(new BloodPressureInputFragment());
        } catch (Exception e) {

        }
    }

    @Override
    public void onError(String errorMessage) {
        Log.d("Success", errorMessage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("Pumpkin", "REQUEST_CONNECT_DEVICE_SECURE");
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("Pumpkin", "REQUEST_CONNECT_DEVICE_INSECURE");
                    connectDevice(data, false);
                }
                break;

            case REQUEST_CODE_SCAN_BT:
                if (resultCode == Activity.RESULT_OK) {
                    double glucose = data.getDoubleExtra("glucose", 0);
                    Log.d("Pumpkin", "REQUEST_CODE_SCAN_BT => " + glucose);
                    mGlucose = glucose;
                    getMainActivity().switchFragment(new BloodSugarInputFragment());
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mBtService.connect(device, secure);
        /*if(mBtConnector == null) {
            mBtConnector = new BluetoothConnector(device, true, mBluetoothAdapter, null);
        }

        try {
            mBtConnector.connect();
        } catch (IOException e) {
            Log.d("Pumpkin", "exception => " + e.toString());
        }*/
    }

    private void reqData() {
        Log.d("Pumpkin", "reqData()");
        /*byte[] data = new byte[7];
        data[0] = (byte)0x80;
        data[1] = 0x02;
        data[2] = ~(0x02);
        data[3] = 0x01;
        data[4] = 0x01;
        data[5] = (byte)(~(data[0]^data[2]^data[4]));
        data[6] = (byte)(~(data[1]^data[3]));*/

        byte[] data = new byte[6];
        data[0] = (byte) 0x80;
        data[1] = 0x01;
        data[2] = ~(0x01);
        data[3] = 0x00;
        data[4] = (byte) (~(data[0] ^ data[2]));
        data[5] = (byte) (~(data[1] ^ data[3]));

        mBtService.write(data);
    }
}
