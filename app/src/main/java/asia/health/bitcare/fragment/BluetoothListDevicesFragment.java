package asia.health.bitcare.fragment;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import asia.health.bitcare.R;
import asia.health.bitcare.adapter.ListBluetoothDevicesAdapter;
import asia.health.bitcare.base.BaseFragment;
import asia.health.bitcare.mvp.model.BluetoothItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class BluetoothListDevicesFragment extends BaseFragment {

    private RecyclerView rvBluetoothDevices;
    private List<BluetoothItem> bluetoothList = new ArrayList<>();
    ;
    private ListBluetoothDevicesAdapter adapter;

    @Override
    public int setFragmentView() {
        return R.layout.fragment_bluetooth_list_devices;
    }

    @Override
    public void initView() {
        rvBluetoothDevices = (RecyclerView) view.findViewById(R.id.rvBluetoothDevices);
    }

    @Override
    public void initValue() {
        adapter = new ListBluetoothDevicesAdapter(context, bluetoothList);
        rvBluetoothDevices.setLayoutManager(new LinearLayoutManager(context));
        rvBluetoothDevices.setItemAnimator(new DefaultItemAnimator());
        rvBluetoothDevices.setAdapter(adapter);

        prepareBluetoothDevices();
    }

    private void prepareBluetoothDevices() {

        for (BluetoothDevice bluetoothDevice : BluetoothAdapter.getDefaultAdapter().getBondedDevices()) {
            BluetoothItem bluetoothItem = new BluetoothItem(bluetoothDevice.getName(), bluetoothDevice.getAddress());
            bluetoothList.add(bluetoothItem);
        }
//        BluetoothItem bluetoothItem = new BluetoothItem("Device 1", "1B:2C:3D:4E:5F:6G");
//        bluetoothList.add(bluetoothItem);
//        bluetoothItem = new BluetoothItem("Device 2", "1B:2C:3D:4E:5F:6G");
//        bluetoothList.add(bluetoothItem);
//        bluetoothItem = new BluetoothItem("Device 3", "1B:2C:3D:4E:5F:6G");
//        bluetoothList.add(bluetoothItem);
//        bluetoothItem = new BluetoothItem("Device 4", "1B:2C:3D:4E:5F:6G");
//        bluetoothList.add(bluetoothItem);
//        bluetoothItem = new BluetoothItem("Device 5", "1B:2C:3D:4E:5F:6G");
//        bluetoothList.add(bluetoothItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initAction() {

    }
}
