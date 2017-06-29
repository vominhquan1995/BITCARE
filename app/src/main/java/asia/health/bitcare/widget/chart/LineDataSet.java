package asia.health.bitcare.widget.chart;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

/**
 * Created by HP on 16-Jan-17.
 */

public class LineDataSet extends com.github.mikephil.charting.data.LineDataSet {
    private final String TAG = getClass().getSimpleName();

    public LineDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
        if (getEntryForIndex(index).getY() == 0) {
            return mColors.get(0);
        } else {
            return mColors.get(1);
        }
    }
}
