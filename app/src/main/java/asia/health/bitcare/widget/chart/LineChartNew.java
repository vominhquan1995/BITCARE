package asia.health.bitcare.widget.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.BubbleChart;
import org.achartengine.chart.CombinedXYChart;
import org.achartengine.chart.CombinedXYChart.XYCombinedChartDef;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.model.XYValueSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.util.MathHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import asia.health.bitcare.R;
import asia.health.bitcare.model.BloodPressure;
import asia.health.bitcare.model.BloodSugar;
import asia.health.bitcare.model.StandardInfoBean;
import asia.health.bitcare.model.Weight;
import asia.health.bitcare.network.APIConstant;

/**
 * Created by VoMinhQuan on 17/02/2017.
 */

public class LineChartNew extends TimeChart {
    //define list line chart
    double[] valueYLeft1;
    double[] valueYLeft2;
    double[] valueYLeft3;
    double[] valueWeight;
    double[] dateValue;
    //define value max min chart
    double minValueYLeft = -1.0d;
    double maxValueYLeft = -1.0d;
    double minValueYRight = -1.0d;
    double maxValueYRight = -1.0d;
    private Context context;
    //private StandardInfoBean standardInfoBean;
    private XYMultipleSeriesDataset dataset;
    private XYMultipleSeriesRenderer renderer;
    int[] idate;
    List<Weight> valueListWeight;
    List<BloodSugar> valueListSugar;
    List<BloodPressure> valueListPressure;
    List<String> dateList;

    public LineChartNew(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer, Context context) {
        super(dataset, renderer);
        this.dataset = new XYMultipleSeriesDataset();
        this.valueListWeight = new ArrayList();
        this.dateList = new ArrayList();
        this.valueListSugar = new ArrayList<>();
        this.valueListPressure = new ArrayList<>();
        this.context = context;
        //set value for zone limit
        // this.standardInfoBean = new StandardInfoBean(120, 100, 160, 140, 120, 100);
    }

    public void dataSettingWeight(List<Weight> data) throws ParseException {
        this.valueListWeight.addAll(data);
        Collections.reverse(this.valueListWeight);
        this.dateValue = new double[this.valueListWeight.size()];
        this.valueWeight = new double[this.valueListWeight.size()];
        this.valueYLeft1 = new double[this.valueListWeight.size()];
        this.idate = new int[this.valueListWeight.size()];
        for (int i = 0; i < this.valueListWeight.size(); i++) {
            this.dateValue[i] = Double.parseDouble(this.valueListWeight.get(i).getMsDate());
            this.valueWeight[i] = this.valueListWeight.get(i).getWeight();
            this.valueYLeft1[i] = this.valueListWeight.get(i).getBMI();
            this.idate[i] = i + 1;
            this.dateList.add(this.valueListWeight.get(i).getMsDate());
        }
    }

    public void dataSettingSugar(List<BloodSugar> data) throws ParseException {
        this.valueListSugar.addAll(data);
        Collections.reverse(this.valueListSugar);
        this.dateValue = new double[this.valueListSugar.size()];
        this.valueYLeft1 = new double[this.valueListSugar.size()];
        this.valueYLeft2 = new double[this.valueListSugar.size()];
        this.valueYLeft3 = new double[this.valueListSugar.size()];
        this.valueWeight = new double[this.valueListSugar.size()];
        this.idate = new int[this.valueListSugar.size()];
        for (int i = 0; i < this.valueListSugar.size(); i++) {
            this.dateValue[i] = Double.parseDouble((this.valueListSugar.get(i)).getBsmsDate());
            this.valueYLeft1[i] = (double) (this.valueListSugar.get(i)).getBsVal();
            //check type value after or before to add list-get value with condition
            if (this.valueListSugar.get(i).getBsType().equals("B")) {
                this.valueYLeft2[i] = (double) (this.valueListSugar.get(i)).getBsVal();
            } else {
                this.valueYLeft3[i] = (double) (this.valueListSugar.get(i)).getBsVal();
            }
            this.valueWeight[i] = (this.valueListSugar.get(i)).getBsWeight();
            this.idate[i] = i + 1;
            this.dateList.add((this.valueListSugar.get(i)).getBsmsDate());
        }
    }

    public void dataSettingPressuare(List<BloodPressure> data) throws ParseException {
        this.valueListPressure.addAll(data);
        Collections.reverse(this.valueListPressure);
        this.dateValue = new double[this.valueListPressure.size()];
        this.valueYLeft1 = new double[this.valueListPressure.size()];
        this.valueYLeft2 = new double[this.valueListPressure.size()];
        this.valueYLeft3 = new double[this.valueListPressure.size()];
        this.valueWeight = new double[this.valueListPressure.size()];
        this.idate = new int[this.valueListPressure.size()];
        for (int i = 0; i < this.valueListPressure.size(); i++) {
            this.valueYLeft1[i] = (double) (this.valueListPressure.get(i)).getBpSys();
            this.valueYLeft2[i] = (double) (this.valueListPressure.get(i)).getBpMin();
            this.valueYLeft3[i] = (double) (this.valueListPressure.get(i)).getBpPulse();
            this.valueWeight[i] = (this.valueListPressure.get(i)).getBpWeight();
            this.dateValue[i] = Double.parseDouble((this.valueListPressure.get(i)).getBpmsDate());
            this.idate[i] = i + 1;
            this.dateList.add(((BloodPressure) this.valueListPressure.get(i)).getBpmsDate());
        }
    }

    //setting size for line and point chart
    protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        this.renderer = new XYMultipleSeriesRenderer(6);
        int length = colors.length;
        int i;
        XYSeriesRenderer r;
        this.renderer.setLabelsTextSize(22.0f);
        this.renderer.setLegendTextSize(22.0f);
        this.renderer.setPointSize(4.0f);
        //set background color
        this.renderer.setBackgroundColor(Color.parseColor("#303030"));
        //show grid and set color
        this.renderer.setShowGrid(true);
        this.renderer.setGridLineWidth(2f);
        //set color line grid the same background color
        if(colors.length==2){
            this.renderer.setGridColor(Color.parseColor("#303030"), 1);
            this.renderer.setGridColor(Color.parseColor("#000000"), 0);
        }else  if(colors.length==3){
            this.renderer.setGridColor(Color.parseColor("#303030"), 1);
            this.renderer.setGridColor(Color.parseColor("#000000"), 2);
            this.renderer.setGridColor(Color.parseColor("#000000"), 0);
        }else if(colors.length==4){
            this.renderer.setGridColor(Color.parseColor("#000000"), 1);
            this.renderer.setGridColor(Color.parseColor("#000000"), 2);
            this.renderer.setGridColor(Color.parseColor("#303030"), 3);
            this.renderer.setGridColor(Color.parseColor("#000000"), 0);
        }
        this.renderer.setMarginsColor(Color.parseColor("#000000"));
        this.renderer.setApplyBackgroundColor(true);
        this.renderer.setYLabelsPadding(5);
        this.renderer.setMargins(new int[]{0, 45, 10, 45});
        for (i = 0; i < length; i++) {
            r = new XYSeriesRenderer();
            r.setDisplayChartValues(true);
            r.setFillPoints(true);
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            r.setChartValuesTextSize(0.0f);
            r.setLineWidth(3.0f);
            this.renderer.addSeriesRenderer(r);
        }
        return this.renderer;
    }


    //setting chart
    protected void setChartSetting(String Type, XYMultipleSeriesRenderer renderer, double xMin, double xMax, double yMin, double yMax, double yMinW, double yMaxW, int colorX, int colorYLeft, int colorYRight) {
        renderer.setAxisTitleTextSize(30.0f);
        renderer.setChartTitleTextSize(40.0f);
        renderer.setZoomEnabled(false, false);
        //set hide legend
        renderer.setShowLegend(false);
        //set max min axis
        renderer.setPanLimits(new double[]{xMin, xMax, yMin, yMax});
        //rotate label x
        //renderer.setXLabelsAngle(20.0f);
        renderer.setPanEnabled(false, false);
        //set color axes chart
        renderer.setAxesColor(Color.parseColor("#000000"));
        //set color value x
        renderer.setXLabelsColor(colorX);
        renderer.setXLabels(10);
        renderer.setYLabels(15);
        double tempTime = (xMax - xMin) / 20.0d;
        switch (Type) {
            case "S": {
                renderer.setYLabelsAlign(Paint.Align.LEFT, 0);
                renderer.setYLabelsAlign(Paint.Align.RIGHT, 0);
                renderer.setYLabelsAlign(Paint.Align.LEFT, 1);
                renderer.setYLabelsAlign(Paint.Align.LEFT, 2);
                renderer.setYLabelsAlign(Paint.Align.RIGHT, 2);
                renderer.setYAxisAlign(Paint.Align.RIGHT, 1);
                if (this.valueListSugar.size() == 1) {
                    renderer.setXAxisMin(0.0d);
                    renderer.setXAxisMax(2.0d);
                } else {
                    //scale value X
                    // setting used to move the graph on x axis to the right ,setting max values to be display in x axis
                    //line 1
                    renderer.setXAxisMin(xMin - tempTime, 0);
                    renderer.setXAxisMax(xMax + tempTime, 0);
                    //line 2
                    renderer.setXAxisMin(xMin - tempTime, 1);
                    renderer.setXAxisMax(xMax + tempTime, 1);
                    //line 3
                    renderer.setXAxisMin(xMin - tempTime, 2);
                    renderer.setXAxisMax(xMax + tempTime, 2);
                    //set max min axis y
                }
                //line 1
                renderer.setYAxisMin(yMin, 0);
                renderer.setYAxisMax(yMax, 0);
                //line 2 weight
                renderer.setYAxisMin(yMinW, 1);
                renderer.setYAxisMax(yMaxW, 1);
                //line 3
                renderer.setYAxisMin(yMin, 2);
                renderer.setYAxisMax(yMax, 2);

                renderer.setYLabelsColor(1, colorYRight);
                renderer.setYLabelsColor(0, colorYLeft);
                renderer.setYLabelsColor(2, colorYLeft);
                break;
            }
            case "P": {
                renderer.setYAxisAlign(Paint.Align.LEFT, 0);
                renderer.setYLabelsAlign(Paint.Align.RIGHT, 0);
                renderer.setYLabelsAlign(Paint.Align.RIGHT, 1);
                renderer.setYLabelsAlign(Paint.Align.RIGHT, 2);
                renderer.setYAxisAlign(Paint.Align.RIGHT, 3);
                renderer.setYLabelsAlign(Paint.Align.LEFT, 3);
                if (this.valueListPressure.size() == 1) {
                    renderer.setXAxisMin(0.0d);
                    renderer.setXAxisMax(2.0d);
                } else {
                    renderer.setXAxisMin(xMin - tempTime, 0);
                    renderer.setXAxisMax(xMax + tempTime, 0);
                    renderer.setXAxisMin(xMin - tempTime, 1);
                    renderer.setXAxisMax(xMax + tempTime, 1);
                    renderer.setXAxisMin(xMin - tempTime, 2);
                    renderer.setXAxisMax(xMax + tempTime, 2);
                    renderer.setXAxisMin(xMin - tempTime, 3);
                    renderer.setXAxisMax(xMax + tempTime, 3);
                }
                //set color asix Y left
                renderer.setYLabelsColor(0, colorYLeft);
                renderer.setYLabelsColor(1, colorYLeft);
                renderer.setYLabelsColor(2, colorYLeft);
                renderer.setYLabelsColor(3, colorYRight);
                renderer.setYAxisMin(yMin, 0);
                renderer.setYAxisMax(yMax, 0);
                renderer.setYAxisMin(yMin, 1);
                renderer.setYAxisMax(yMax, 1);
                renderer.setYAxisMin(yMin, 2);
                renderer.setYAxisMax(yMax, 2);
                renderer.setYAxisMin(yMinW, 3);
                renderer.setYAxisMax(yMaxW, 3);
                break;
            }
            case "W": {
                renderer.setYAxisAlign(Paint.Align.LEFT, 0);
                renderer.setYAxisAlign(Paint.Align.RIGHT, 1);
                renderer.setYLabelsAlign(Paint.Align.RIGHT, 0);
                renderer.setYLabelsAlign(Paint.Align.LEFT, 1);
                if (this.valueListWeight.size() == 1) {
                    renderer.setXAxisMin(0.0d);
                    renderer.setXAxisMax(2.0d);
                } else {
                    renderer.setXAxisMin(xMin - tempTime, 0);
                    renderer.setXAxisMax(xMax + tempTime, 0);
                    renderer.setXAxisMin(xMin - tempTime, 1);
                    renderer.setXAxisMax(xMax + tempTime, 1);
                }
                renderer.setYAxisMin(yMin, 0);
                renderer.setYAxisMax(yMax, 0);
                renderer.setYAxisMin(yMinW, 1);
                renderer.setYAxisMax(yMaxW, 1);
                //renderer.setAxesColor(axesColor);
                renderer.setXLabelsColor(colorX);//set color x
                renderer.setYLabelsColor(0, colorYLeft);//set color value left
                renderer.setYLabelsColor(1, colorYRight); //set color value right
                break;
            }
        }
    }

    protected XYMultipleSeriesDataset buildDateDataset(String typeChart, List<int[]> xValues, List<double[]> yValues) {
        TimeSeries line1 = new TimeSeries("");
        TimeSeries line2 = new TimeSeries("");
        TimeSeries line3 = new TimeSeries("");
        TimeSeries line4 = new TimeSeries("");
        int i;
        int length;
        switch (typeChart) {
            case "P": {
                length = 4;
                if (xValues == null) {
                    line1.add(MathHelper.NULL_VALUE, MathHelper.NULL_VALUE);
                    line2.add(MathHelper.NULL_VALUE, MathHelper.NULL_VALUE);
                    line3.add(MathHelper.NULL_VALUE, MathHelper.NULL_VALUE);
                    line4.add(MathHelper.NULL_VALUE, MathHelper.NULL_VALUE);
                    this.dataset.addSeries(line1);
                    this.dataset.addSeries(line2);
                    this.dataset.addSeries(line3);
                    this.dataset.addSeries(line4);
                } else if (xValues.size() > 0) {
                    for (i = 0; i < length; i++) {
                        //set value x y for every line
                        XYSeries lineX = new XYSeries("", i);
                        //set location x with value y
                        int[] xV = xValues.get(i);
                        double[] yV = yValues.get(i);
                        int seriesLength = xV.length;
                        for (int k = 0; k < seriesLength; k++) {
                            if (yV[k] != 0.0d) {
                                lineX.add((double) xV[k], yV[k]);
                            }
                        }
                        this.dataset.addSeries(i, lineX);
                    }
                }
                for (i = 0; i < this.valueListPressure.size(); i++) {
                    //set location x with i+1 example 1,2,3...12
                    this.renderer.addXTextLabel((i + 1), (this.dateList.get(i)).toString());
                    this.renderer.setXLabels(0);
                }
                break;
            }
            case "S": {
                length = 3;
                if (xValues == null) {
                    line1.add(MathHelper.NULL_VALUE, MathHelper.NULL_VALUE);
                    line2.add(MathHelper.NULL_VALUE, MathHelper.NULL_VALUE);
                    line3.add(MathHelper.NULL_VALUE, MathHelper.NULL_VALUE);
                    this.dataset.addSeries(line1);
                    this.dataset.addSeries(line2);
                    this.dataset.addSeries(line3);
                } else if (xValues.size() > 0) {
                    for (i = 0; i < length; i++) {
                        //set value x y for every line
                        XYSeries lineX = new XYSeries("", i);
                        //set location x with value y
                        int[] xV = xValues.get(i);
                        double[] yV = yValues.get(i);
                        int seriesLength = xV.length;
                        for (int k = 0; k < seriesLength; k++) {
                            if (yV[k] != 0.0d) {
                                lineX.add((double) xV[k], yV[k]);
                            }
                        }
                        this.dataset.addSeries(i, lineX);
                    }
                }
                for (i = 0; i < this.valueListSugar.size(); i++) {
                    //set location x with i+1 example 1,2,3...12
                    this.renderer.addXTextLabel((i + 1), (this.dateList.get(i)).toString());
                    this.renderer.setXLabels(0);
                }
                break;
            }
            case "W": {
                length = 2;
                if (xValues == null) {
                    line1.add(MathHelper.NULL_VALUE, MathHelper.NULL_VALUE);
                    line2.add(MathHelper.NULL_VALUE, MathHelper.NULL_VALUE);
                    this.dataset.addSeries(line1);
                    this.dataset.addSeries(line2);
                } else if (xValues.size() > 0) {
                    for (i = 0; i < length; i++) {
                        //set value x y for every line
                        XYSeries lineX = new XYSeries("", i);
                        //set location x with value y
                        int[] xV = xValues.get(i);
                        double[] yV = yValues.get(i);
                        int seriesLength = xV.length;
                        for (int k = 0; k < seriesLength; k++) {
                            if (yV[k] != 0.0d) {
                                lineX.add((double) xV[k], yV[k]);
                            }
                        }
                        this.dataset.addSeries(i, lineX);
                    }
                }
                for (i = 0; i < this.valueListWeight.size(); i++) {
                    //set location x with i+1 example 1,2,3...12
                    this.renderer.addXTextLabel((i + 1), (this.dateList.get(i)).toString());
                    this.renderer.setXLabels(0);
                }
                break;
            }
        }
        return this.dataset;
    }

    public GraphicalView execute(Context context) {
        int i;
        String[] titles = new String[]{"", ""};
        List<int[]> dates = new ArrayList();
        List<double[]> values = new ArrayList();
        int length = titles.length;
        if (this.dateValue != null && this.dateValue.length > 0) {
            for (i = 0; i < length; i++) {
                dates.add(new int[this.dateValue.length]);
                for (int j = 0; j < this.dateValue.length; j++) {
                    dates.get(i)[j] = this.idate[j];
                }
            }
        }
        values.add(this.valueYLeft1);
        values.add(this.valueWeight);
        if (!(values == null || values.size() <= 0 || values.get(0) == null)) {
            length = ((double[]) values.get(0)).length;
        }
        //set Y left and Y right
        XYMultipleSeriesRenderer renderer = buildRenderer(new int[]{Color.parseColor("#01afd1"), Color.parseColor("#8ebe51")}, new PointStyle[]{PointStyle.CIRCLE, PointStyle.CIRCLE});
        setChartSetting("W", renderer, (double) this.idate[0], (double) this.idate[this.dateValue.length - 1], minValueYLeft , maxValueYLeft, minValueYRight,  maxValueYRight,
                Color.parseColor("#999999"),
                Color.parseColor("#999999"),
                Color.parseColor("#999999"));
        this.valueYLeft1 = null;
        this.valueWeight = null;
        this.dateValue = null;
        return ChartFactory.getLineChartView(context, buildDateDataset("W", dates, values), renderer);
    }

    //execute suger
    public GraphicalView executeSugar(Context context, String typeBs) {
        int i;
        int j;
        String[] titles = new String[]{"", "", ""};
        List<int[]> dates = new ArrayList();
        List<double[]> values = new ArrayList();
        int length = titles.length;
        if (this.dateValue != null && this.dateValue.length > 0) {
            for (i = 0; i < length; i++) {
                dates.add(new int[this.dateValue.length]);
                for (j = 0; j < this.dateValue.length; j++) {
                    (dates.get(i))[j] = this.idate[j];
                }
            }
        }
        values.add(this.valueYLeft2);
        values.add(this.valueWeight);
        values.add(this.valueYLeft3);
        XYMultipleSeriesRenderer renderer = buildRenderer(new int[]{Color.parseColor("#f34a56"), Color.parseColor("#83b416"), Color.parseColor("#4092ff")}, new PointStyle[]{PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE});
        setChartSetting("S", renderer, (double) this.idate[0], (double) this.idate[this.dateValue.length - 1], minValueYLeft , maxValueYLeft, minValueYRight,  maxValueYRight,
                Color.parseColor("#999999"),
                Color.parseColor("#999999"),
                Color.parseColor("#999999"));
        //set point size with conditon is do exersise
//        XYValueSeries sunSeries = new XYValueSeries("");
//        XYValueSeries sunSeries2 = new XYValueSeries("");
//        double pointSize = 0.5d;
//        //set size point value
//        for (j = 0; j < this.valueListSugar.size(); j++) {
//            if (this.valueListSugar.get(j).getBsExersiseYN().equals("Y")) {
//                //add Point in value
//                sunSeries.add((double) (((float) j) + 1.0f), this.valueYLeft1[j], pointSize);
//            } else {
//                sunSeries2.add((double) (((float) j) + 1.0f), this.valueYLeft1[j], pointSize);
//            }
//        }
        //set size point value
//        sunSeries.add(100.0d, 0.0d, 5.0d);
//        sunSeries2.add(100.0d, 0.0d, 5.0d);
        // split chart to 4 zone
//        XYSeries xYSeries = new XYSeries("");
//        XYSeries xYSeries1 = new XYSeries("");
//        XYSeries xYSeries2 = new XYSeries("");
//        XYSeries xYSeries3 = new XYSeries("");
//        //user value from standardinfo set pandding for every zone
//        for (i = 0; i < this.valueYLeft1.length + 2; i++) {
//            xYSeries.add((double) i, (double) this.standardInfoBean.getAfterBSMax());
//        }
//        for (i = 0; i < this.valueYLeft1.length + 2; i++) {
//            xYSeries1.add((double) i, (double) this.standardInfoBean.getAfterBSMin());
//        }
//        for (i = 0; i < this.valueYLeft1.length + 2; i++) {
//            xYSeries2.add((double) i, (double) this.standardInfoBean.getBeforeBSMax());
//        }
//        for (i = 0; i < this.valueYLeft1.length + 2; i++) {
//            xYSeries3.add((double) i, (double) this.standardInfoBean.getBeforeBSMin());
//        }
        this.valueYLeft2 = null;
        this.valueWeight = null;
        this.idate = null;
        this.dateValue = null;
//        SimpleSeriesRenderer lightRenderer = new XYSeriesRenderer();
//        SimpleSeriesRenderer lightRenderer2 = new XYSeriesRenderer();
//        //set color point value
//        lightRenderer.setColor(Color.parseColor("#B320b2aa"));
//        lightRenderer2.setColor(Color.parseColor("#C3d3d3d3"));
        //set serial limit
//        SimpleSeriesRenderer waterRenderer1 = new XYSeriesRenderer();
//        SimpleSeriesRenderer waterRenderer2 = new XYSeriesRenderer();
//        SimpleSeriesRenderer waterRenderer3 = new XYSeriesRenderer();
//        SimpleSeriesRenderer waterRenderer4 = new XYSeriesRenderer();
//        if (typeBs.equals(APIConstant.BSTYPE_B)) {
//            waterRenderer1.setColor(Color.parseColor("#303030"));
//            waterRenderer2.setColor(Color.parseColor("#303030"));
//            waterRenderer3.setColor(Color.parseColor("#80e1d1bb"));
//            waterRenderer4.setColor(Color.parseColor("#303030"));
//        } else if (typeBs.equals(APIConstant.BSTYPE_A)) {
//            waterRenderer1.setColor(Color.parseColor("#80ffdcf6"));
//            waterRenderer2.setColor(Color.parseColor("#303030"));
//            waterRenderer3.setColor(Color.parseColor("#303030"));
//            waterRenderer4.setColor(Color.parseColor("#303030"));
//        } else {
//            waterRenderer1.setColor(Color.parseColor("#80ffdcf6"));
//            waterRenderer2.setColor(Color.parseColor("#303030"));
//            waterRenderer3.setColor(Color.parseColor("#80e1d1bb"));
//            waterRenderer4.setColor(Color.parseColor("#303030"));
//        }
        XYMultipleSeriesDataset dataset = buildDateDataset("S", dates, values);
//        dataset.addSeries(0, xYSeries);
//        dataset.addSeries(1, xYSeries1);
//        dataset.addSeries(2, xYSeries2);
//        dataset.addSeries(3, xYSeries3);
//        dataset.addSeries(3, sunSeries);
//        dataset.addSeries(4, sunSeries2);
//        renderer.addSeriesRenderer(0, waterRenderer1);
//        renderer.addSeriesRenderer(1, waterRenderer2);
//        renderer.addSeriesRenderer(2, waterRenderer3);
//        renderer.addSeriesRenderer(3, waterRenderer4);
//        renderer.addSeriesRenderer(3, lightRenderer);
//        renderer.addSeriesRenderer(4, lightRenderer2);
        CombinedXYChart.XYCombinedChartDef[] types = new XYCombinedChartDef[3];
        types[0] = new XYCombinedChartDef(TYPE, 0);
        types[1] = new XYCombinedChartDef(TYPE, 1);
        types[2] = new XYCombinedChartDef(TYPE, 2);
//        types[3] = new XYCombinedChartDef(BarChart.TYPE, 0);
//        types[4] = new XYCombinedChartDef(BarChart.TYPE, 1);
//        types[5] = new XYCombinedChartDef(BarChart.TYPE, 2);
//        types[6] = new XYCombinedChartDef(BarChart.TYPE, 3);
//        types[3] = new XYCombinedChartDef(BubbleChart.TYPE, 3);
//        types[4] = new XYCombinedChartDef(BubbleChart.TYPE, 4);
        return ChartFactory.getCombinedXYChartView(context, dataset, renderer, types);
    }

    //execute pressuare
    public GraphicalView executePressure(Context context) {
        int i;
        String[] titles = new String[]{"", "", "", ""};
        List<int[]> dates = new ArrayList();
        List<double[]> values = new ArrayList();
        int length = titles.length;
        if (this.dateValue != null && this.dateValue.length > 0) {
            for (i = 0; i < length; i++) {
                dates.add(new int[this.dateValue.length]);
                for (int j = 0; j < this.dateValue.length; j++) {
                    ((int[]) dates.get(i))[j] = this.idate[j];
                }
            }
        }
        values.add(this.valueYLeft1);
        values.add(this.valueYLeft2);
        values.add(this.valueYLeft3);
        values.add(this.valueWeight);
        XYMultipleSeriesRenderer renderer = buildRenderer(new int[]{Color.parseColor("#f34a56"), Color.parseColor("#5d55ff"), Color.parseColor("#4092ff"), Color.parseColor("#83b416")}, new PointStyle[]{PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE});
        setChartSetting("P", renderer, (double) this.idate[0], (double) this.idate[this.dateValue.length - 1], minValueYLeft , maxValueYLeft, minValueYRight,  maxValueYRight,
                Color.parseColor("#999999"),
                Color.parseColor("#999999"),
                Color.parseColor("#999999"));
//        XYValueSeries sunSeries = new XYValueSeries("");
//        XYValueSeries sunSeries2 = new XYValueSeries("");
//        double pointSize = 1.0d;
//        //set color point with condition Exercise or not
//        for (i = 0; i < this.valueListPressure.size(); i++) {
//            if (((BloodPressure) this.valueListPressure.get(i)).getBpExersiseYN().equals("Y")) {
//                sunSeries.add((double) (((float) i) + 1.0f), this.valueYLeft1[i], pointSize);
//                sunSeries.add((double) (((float) i) + 1.0f), this.valueYLeft2[i], pointSize);
//                sunSeries.add((double) (((float) i) + 1.0f), this.valueYLeft3[i], pointSize);
//            } else {
//                sunSeries2.add((double) (((float) i) + 1.0f), this.valueYLeft1[i], pointSize);
//                sunSeries2.add((double) (((float) i) + 1.0f), this.valueYLeft2[i], pointSize);
//                sunSeries2.add((double) (((float) i) + 1.0f), this.valueYLeft3[i], pointSize);
//            }
//        }
//        sunSeries.add(100.0d, 0.0d, 5.0d);
//        sunSeries2.add(100.0d, 0.0d, 5.0d);
        XYMultipleSeriesDataset dataset = this.buildDateDataset("P", dates, values);
        //set area default chart
//        XYSeries xYSeries = new XYSeries("");
//        for (i = 0; i < this.valueYLeft1.length + 2; i++) {
//            xYSeries.add((double) i, (double) this.standardInfoBean.getSystolicBPMax());
//        }
//        XYSeries xYSeries1 = new XYSeries("");
//        for (i = 0; i < this.valueYLeft1.length + 2; i++) {
//            xYSeries1.add((double) i, (double) this.standardInfoBean.getDiastolicBPMax());
//        }
//        SimpleSeriesRenderer waterRenderer1 = new XYSeriesRenderer();
//        SimpleSeriesRenderer waterRenderer2 = new XYSeriesRenderer();
//        waterRenderer1.setColor(Color.parseColor("#c6c6c6"));
//        waterRenderer2.setColor(Color.parseColor("#c6c6c6"));
        //waterRenderer1.setLineWidth(5.0f);
        // waterRenderer2.setLineWidth(5.0f);
        this.valueYLeft1 = null;
        this.valueYLeft2 = null;
        this.valueYLeft3 = null;
        this.valueWeight = null;
        this.idate = null;
        this.dateValue = null;
//        SimpleSeriesRenderer lightRenderer = new XYSeriesRenderer();
//        SimpleSeriesRenderer lightRenderer2 = new XYSeriesRenderer();
//        lightRenderer.setColor(Color.parseColor("#B320b2aa"));
//        lightRenderer2.setColor(Color.parseColor("#C3d3d3d3"));
//        dataset.addSeries(0, xYSeries);
//        dataset.addSeries(1, xYSeries1);
//        dataset.addSeries(4, sunSeries);
//        dataset.addSeries(5, sunSeries2);
//        renderer.addSeriesRenderer(0, waterRenderer1);
//        renderer.addSeriesRenderer(1, waterRenderer2);
//        renderer.addSeriesRenderer(4, lightRenderer);
//        renderer.addSeriesRenderer(5, lightRenderer2);
        XYCombinedChartDef[] types = new XYCombinedChartDef[4];
//        types[0] = new XYCombinedChartDef(TYPE, 0);
//        types[1] = new XYCombinedChartDef(TYPE, 1);
        types[0] = new XYCombinedChartDef(TYPE, 0);
        types[1] = new XYCombinedChartDef(TYPE, 1);
        types[2] = new XYCombinedChartDef(TYPE, 2);
        types[3] = new XYCombinedChartDef(TYPE, 3);
//        types[4] = new XYCombinedChartDef(BubbleChart.TYPE, 4);
//        types[5] = new XYCombinedChartDef(BubbleChart.TYPE, 5);
        return ChartFactory.getCombinedXYChartView(context, dataset, renderer, types);
    }

    public void clearData() {
        this.dataset.clear();
        this.renderer = new XYMultipleSeriesRenderer();
    }

    public void setMinValueYLeft(double minValueYLeft) {
        this.minValueYLeft = minValueYLeft;
    }

    public void setMaxValueYLeft(double maxValueYLeft) {
        this.maxValueYLeft =  GetRounded(maxValueYLeft)+10;
    }

    public void setMaxValueYRight(double maxValueYRight) {
        this.maxValueYRight =  GetRounded(maxValueYRight)+4;
    }

    public void setMinValueYRight(double minValueYRight) {
        this.minValueYRight =  minValueYRight;
    }
    public int GetRounded(double value)
    {
        int number=(int) value;
        if ((value % 10) < 5)
            return number - number % 10;
        else
            return number + (10 - (number % 10));
    }
}
