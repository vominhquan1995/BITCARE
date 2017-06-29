package asia.health.bitcare.utils;

import android.util.Log;

import java.util.List;

/**
 * Created by HP on 17-Jan-17.
 */

public class CalculateUtils {
    public static int calculateAverage(List<Integer> marks) {
        Integer sum = 0;
        if (!marks.isEmpty()) {
            for (Integer mark : marks) {
                if (mark != 0) {
                    sum += mark;
                } else {
                }
            }
            return (int) sum / marks.size();
        }
        return (int) sum;
    }
}
