package asia.health.bitcare.mvp.model;

/**
 * Created by thong on 12/28/2016.
 * PROJECT BITCARE_ANDROID
 */

public class BluetoothItem {

    private String name;
    private String bluetoothId;

    public BluetoothItem() {
    }

    public BluetoothItem(String name, String bluetoothId) {
        this.name = name;
        this.bluetoothId = bluetoothId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBluetoothId() {
        return bluetoothId;
    }

    public void setBluetoothId(String bluetoothId) {
        this.bluetoothId = bluetoothId;
    }
}
