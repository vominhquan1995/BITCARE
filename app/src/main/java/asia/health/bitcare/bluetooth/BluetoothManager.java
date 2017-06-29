package asia.health.bitcare.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothManager extends Activity {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;
    private OutputStream outputStream;
    private InputStream inputStream;
    private static final int REQUEST_ENABLE_BT = 2;
    public static final int REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {

                        } else {
                            Toast.makeText(this, "Should have camera permission to run", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void init() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                DeviceDialog();
            } else {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }
        } else {
            ErrorDialog("This device is disabled Bluetooth.");
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("KKC", "onActivityResult " + requestCode);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    DeviceDialog();
                } else {
                    Log.d("KKC", "Bluetooth is not enabled");
                }
                break;
        }
    }


    public Set<BluetoothDevice> getPairedDevices() {
        return bluetoothAdapter.getBondedDevices();
    }

    @Override
    public void onBackPressed() {
        new CloseTask().execute();
        super.onBackPressed();
    }

    public void doConnect(BluetoothDevice device) {
        bluetoothDevice = device;

        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothAdapter.cancelDiscovery();
            new ConnectTask().execute();
        } catch (IOException e) {
            Log.e("KKC", e.toString(), e);
            ErrorDialog("doConnect " + e.toString());
        }
    }


    public void DeviceDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Select Device");

        Set<BluetoothDevice> pairedDevices = getPairedDevices();
        final BluetoothDevice[] devices = pairedDevices.toArray(new BluetoothDevice[0]);
        String[] items = new String[devices.length];
        for (int i = 0; i < devices.length; i++) {
            items[i] = devices[i].getName();
        }

        alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                doConnect(devices[which]);
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.show();
    }


    public void ErrorDialog(String text) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("ERROR");
        alertDialogBuilder.setMessage(text);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alertDialogBuilder.show();
    }


    private class ConnectTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            Log.d("KKC", "Bluetooth Connecting...");
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                inputStream = bluetoothSocket.getInputStream();

//                new EchoOffCommand().run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
//                new LineFeedOffCommand().run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
//                new TimeoutCommand(125).run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
//                new SelectProtocolCommand(ObdProtocols.AUTO).run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
//                new AmbientAirTemperatureCommand().run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());

            } catch (Throwable t) {
                Log.e("KKC", "connect? " + t.getMessage());
                return t;
            }
            return null;
        }


        @Override
        protected void onPostExecute(Object result) {

            if (result instanceof Throwable) {
                Log.d("KKC", "ConnectTask " + result.toString());
                ErrorDialog("ConnectTask " + result.toString());
            } else {
                Log.d("KKC", "Bluetooth Opened");
                try {
//                    commandThread.getRPMThread().start();
//                    commandThread.getFuelThread().start();
//                    commandThread.getSpeedThread().start();
                } catch (Exception e) {
                    Log.d("KKC", "ConnectTask Exception = " + e.toString());
                }
            }

        }
    }


    private class CloseTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected Object doInBackground(Void... params) {
            try {
                try {
                    outputStream.close();
                } catch (Throwable t) {/*ignore*/}
                try {
                    inputStream.close();
                } catch (Throwable t) {/*ignore*/}
                bluetoothSocket.close();
            } catch (Throwable t) {
                return t;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof Throwable) {
                Log.e("", result.toString(), (Throwable) result);
                ErrorDialog(result.toString());
            }
        }
    }
}
