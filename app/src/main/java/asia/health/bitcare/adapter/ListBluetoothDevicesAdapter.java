package asia.health.bitcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseRecyclerAdapter;
import asia.health.bitcare.mvp.model.BluetoothItem;

/**
 * Created by thong on 12/28/2016.
 * PROJECT BITCARE_ANDROID
 */

public class ListBluetoothDevicesAdapter extends BaseRecyclerAdapter<BluetoothItem> {

    private Context context;

    public ListBluetoothDevicesAdapter(Context context, List<BluetoothItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.list_item_bluetooth_devices;
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, int position, BluetoothItem item) {
        BluetoothListViewHolder viewHolder = (BluetoothListViewHolder) holder;

        viewHolder.deviceName.setText(item.getName());
        viewHolder.deviceAddress.setText(item.getBluetoothId());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(getItemLayout(), parent, false);
        return new BluetoothListViewHolder(rootView);
    }

    private class BluetoothListViewHolder extends RecyclerView.ViewHolder {

        private TextView deviceName;
        private TextView deviceAddress;

        BluetoothListViewHolder(View itemView) {
            super(itemView);

            deviceName = (TextView) itemView.findViewById(R.id.device_name);
            deviceAddress = (TextView) itemView.findViewById(R.id.device_address);
        }
    }
}
