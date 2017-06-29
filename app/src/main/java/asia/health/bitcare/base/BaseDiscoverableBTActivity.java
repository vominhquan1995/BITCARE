package asia.health.bitcare.base;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * Created by Leon on 5/26/2017.
 */

public abstract class BaseDiscoverableBTActivity extends BaseBleActivity {

    private int REQUEST_DISCOVERABLE_BT;

    @Override
    protected void initRequestCode() {
        super.initRequestCode();
        REQUEST_DISCOVERABLE_BT = getDicoverableBTRequestCode();
    }

    protected abstract int getDicoverableBTRequestCode();

    private RequestDiscoverableCallback requestDiscoverableCallback;

    public void requestDiscoverable(RequestDiscoverableCallback requestDiscoverableCallback) {
        this.requestDiscoverableCallback = requestDiscoverableCallback;
        if (BluetoothAdapter.getDefaultAdapter().getScanMode() ==
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            requestDiscoverableCallback.onAllow();
        } else {
            ensureDiscoverable();
        }
    }

    private void ensureDiscoverable() {
//        if (D) Log.d(TAG, "ensure discoverable");
        if (BluetoothAdapter.getDefaultAdapter().getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_DISCOVERABLE_BT && requestDiscoverableCallback != null) {
            if (resultCode == RESULT_OK) {
                requestDiscoverableCallback.onAllow();
            } else {
                requestDiscoverableCallback.onDeny();
            }
        }
    }

    public interface RequestDiscoverableCallback {

        void onAllow();

        void onDeny();
    }
}
