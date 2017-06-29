package asia.health.bitcare.widget.chart;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import asia.health.bitcare.R;

/**
 * Created by MrAn on 16-Aug-16.
 */
public class LineChart {
    private final String TAG = getClass().getSimpleName();
    private final String DEFAULT_TITLE = "Data_";
    private com.github.mikephil.charting.charts.LineChart lineChart;
    private List<LineDataSet> listLineDataSet;
    private LineData lineData;
    private int maxValueYLeft = 0;
    private int minValueYLeft = 0;
    private int maxValueYRight = 0;
    private int minValueYRight = 0;
    private int leftLabelColor;
    private int rightLabelColor;
    private List<Integer> listColor = new ArrayList<>();
    private List<String> listXAxisValue;

    public LineChart(View view) {
        lineChart = (com.github.mikephil.charting.charts.LineChart) view.findViewById(R.id.chart);
        listLineDataSet = new ArrayList<>();
        lineData = new LineData();
        setUp();
    }

    /**
     * Add List data from List
     *
     * @param listInteger
     * @param title
     * @param color
     */
    public void addListData(List<Integer> listInteger, String title, int color) {
        listColor.add(color);
        if (listInteger != null) {
            List<Entry> listEntry = new ArrayList<>();
            for (int i = 0; i < listInteger.size(); i++) {
                listEntry.add(new Entry(i, listInteger.get(i)));
            }
            listLineDataSet.add(new LineDataSet(listEntry, title == null ? DEFAULT_TITLE + listLineDataSet.size() : title));
            show(listColor);
        }
    }

    /**
     * Add list data from arrays
     *
     * @param listInteger
     * @param title
     * @param color
     */
    public void addListData(int[] listInteger, String title, int color) {
        listColor.add(color);
        if (listInteger != null) {
            List<Entry> listEntry = new ArrayList<>();
            for (int i = 0; i < listInteger.length; i++) {
                listEntry.add(new Entry(i, listInteger[i]));
            }
            listLineDataSet.add(new LineDataSet(listEntry, title == null ? DEFAULT_TITLE : title));
            show(listColor);
        }
    }

    public void setListXAxisValue(List<String> listXAxisValue) {
        this.listXAxisValue = listXAxisValue;
    }

    /**
     * Clear all data of chart
     */
    public void clear() {
        listLineDataSet.clear();
        lineData.clearValues();
        listColor.clear();
        maxValueYLeft = 0;
        minValueYLeft = 0;
        maxValueYRight = 0;
        minValueYRight = 0;
        if (lineChart.getData() != null) {
            lineChart.clearValues();
        }
    }

    /**
     * Show chart
     *
     * @param listColor
     */
    private void show(List<Integer> listColor) {
        //Before show , clear last value
        lineData.clearValues();
        for (int i = 0; i < listLineDataSet.size(); i++) {
            if (i == listLineDataSet.size() - 1) {
                listLineDataSet.get(i).setAxisDependency(YAxis.AxisDependency.RIGHT);
            } else {
                listLineDataSet.get(i).setAxisDependency(YAxis.AxisDependency.LEFT);
            }
            listLineDataSet.get(i).setColors(new int[]{Color.TRANSPARENT, listColor.get(i)});
            listLineDataSet.get(i).setCircleColor(listColor.get(i));
            listLineDataSet.get(i).setCircleRadius(3f);
            listLineDataSet.get(i).setLineWidth(1.5f);
            listLineDataSet.get(i).setDrawCircleHole(false);
            listLineDataSet.get(i).setDrawHighlightIndicators(false);
            listLineDataSet.get(i).setFillColor(listColor.get(i));
            listLineDataSet.get(i).setDrawValues(false);
            lineData.addDataSet(listLineDataSet.get(i));
        }
        lineChart.setData(lineData);

        //X Axis at the bottom
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(listXAxisValue.size() - 1, false);
        xAxis.setLabelRotationAngle(45);
        xAxis.removeAllLimitLines();
        xAxis.setDrawAxisLine(false);
        //Replace default value display by date
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (listXAxisValue != null && listXAxisValue.size() > 0) {
                    int position = (int) value;
                    if (position >= 0) {
                        return listXAxisValue.get(position).substring(4,8);
                    } else return "";
                }
                return String.valueOf((int) value);
            }
        });

        //Set Y Axis Left
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setAxisMaximum(maxValueYLeft);
        leftAxis.setAxisMinimum(minValueYLeft);
        leftAxis.setTextColor(leftLabelColor);

        //Set Y Axis Right
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.removeAllLimitLines();
        rightAxis.setDrawAxisLine(false);
        rightAxis.setAxisMaximum(maxValueYRight);
        rightAxis.setAxisMinimum(minValueYRight);
        rightAxis.setTextColor(rightLabelColor);

        //Refresh chart
        lineChart.invalidate();
    }

    /**
     * Setup chart
     */
    private void setUp() {
        lineChart.setNoDataText("");
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.getDescription().setEnabled(false);
    }

    public void setMaxValueYLeft(int maxValueYLeft) {
        this.maxValueYLeft = maxValueYLeft;
    }

    public void setMinValueYLeft(int minValueYLeft) {
        this.minValueYLeft = minValueYLeft;
    }

    public void setMaxValueYRight(int maxValueYRight) {
        this.maxValueYRight = maxValueYRight;
    }

    public void setMinValueYRight(int minValueYRight) {
        this.minValueYRight = minValueYRight;
    }

    public void setLeftLabelColor(int leftLabelColor) {
        this.leftLabelColor = leftLabelColor;
    }

    public void setRightLabelColor(int rightLabelColor) {
        this.rightLabelColor = rightLabelColor;
    }
}
