package asia.health.bitcare.utils;

import android.util.Log;

/**
 * Created by HP on 13-Jan-17.
 */

public class NumberUtil {

    /**
     * Get Max value
     * @param value
     * @param valueCompare
     * @return
     */
    public static int getMax(int value, int valueCompare) {
        return (valueCompare > value) ? valueCompare : value;
    }

    /**
     * Get Min value
     * @param value
     * @param valueCompare
     * @return
     */
    public static int getMin(int value, int valueCompare) {
        return (valueCompare < value) ? valueCompare : value;
    }
}
