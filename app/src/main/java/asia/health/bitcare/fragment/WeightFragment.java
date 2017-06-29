package asia.health.bitcare.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import asia.health.bitcare.R;
import asia.health.bitcare.adapter.ListHistoryAdapter;
import asia.health.bitcare.base.BaseFragment;
import asia.health.bitcare.base.BaseMainToolbarActivity;
import asia.health.bitcare.model.User;
import asia.health.bitcare.model.Weight;
import asia.health.bitcare.mvp.model.HistoryItem;
import asia.health.bitcare.mvp.presenter.WeightPresenter;
import asia.health.bitcare.mvp.view.WeightView;
import asia.health.bitcare.network.APIConstant;
import asia.health.bitcare.utils.DateTimeUtils;
import asia.health.bitcare.widget.chart.ChartUtils;
import asia.health.bitcare.widget.chart.LineChartNew;
import asia.health.bitcare.widget.toast.Boast;

/**
 * A simple {@link Fragment} subclass
 */
public class WeightFragment extends BaseFragment implements
        WeightInputFragment.WeightInputFragmentCallBack, WeightView {

    private static WeightFragment.OnForwardData onForwardData;
    private String typeFilter = "";
    private int currentFilter = R.id.menu_type_all;
    private RecyclerView rvHistory;
    // layout
    private ViewGroup historyLayout;
    private boolean isChartExtended;
    //insert 03/01 by Quan
    private ListHistoryAdapter adapter;
    private WeightPresenter presenter;
    private List<HistoryItem> historyItems = new ArrayList<>();
    private List<Weight> listWeightData = new ArrayList<>();
    private List<Weight> listWeightChart = new ArrayList<>();

    private LineChartNew lineChart;
    private XYMultipleSeriesDataset ds;
    private LinearLayout graphLayout;
    private GraphicalView graphicalView;
    private XYMultipleSeriesRenderer msr;

    public static void setOnForwardData(
            WeightFragment.OnForwardData onForwardData) {
        WeightFragment.onForwardData = onForwardData;
    }

    @Override
    public int setFragmentView() {
        return R.layout.fragment_weight_2;
    }

    @Override
    public void initView() {
        // define UI element
        rvHistory = (RecyclerView) view.findViewById(R.id.rvHistory);
        graphLayout = (LinearLayout) view.findViewById(R.id.chart);
        historyLayout = (ViewGroup) view.findViewById(R.id.historyLayout);
    }

    @Override
    public void initValue() {
        WeightInputFragment.mCallBack = this;
        presenter = new WeightPresenter(this);
        //insert 03/01 by Quan
        adapter = new ListHistoryAdapter(context, historyItems, false, false);
        rvHistory.setLayoutManager(new LinearLayoutManager(context));
        rvHistory.setItemAnimator(new DefaultItemAnimator());
        rvHistory.setAdapter(adapter);
        ds = new XYMultipleSeriesDataset();
        msr = new XYMultipleSeriesRenderer();
        lineChart = new LineChartNew(ds, msr, context);
    }

    @Override
    public void initAction() {
        showProgressDialog();
        presenter.getWeightList();

        // show/hide full chart
        graphLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isChartExtended) {
                    historyLayout.setVisibility(View.GONE);
                    isChartExtended = true;
                } else {
                    historyLayout.setVisibility(View.VISIBLE);
                    isChartExtended = false;
                }
            }
        });

        //set event click sort
        setOnPopupMenuClick(new BaseMainToolbarActivity.OnPopupMenuOnClick() {
            @Override
            public void onPeriodSort(int menuId) {
                currentFilter = menuId;
                showChart();
            }

            @Override
            public void onAnswerSort(int menuId) {
//                switch (menuId) {
//                    case R.id.menu_all:
//                        typeFilter = "";
//                        break;
//                    case R.id.menu_equiment:
//                        typeFilter = APIConstant.BPMSTYPE_D;
//                        break;
//                    case R.id.menu_uer_input:
//                        typeFilter = APIConstant.BPMSTYPE_U;
//                        break;
//                }
                showChart();
                showList();
            }
        });

    }

    /**
     * Show chart data
     */
    private void showChart() {
        // clear data
        listWeightChart.clear();

        //update value max min
        Collections.sort(listWeightData);
        try {
            lineChart = new LineChartNew(ds, msr, context);
            //Clear view chart
            graphLayout.removeAllViews();
            if (listWeightData.size() != 0) {
                List<Weight> pressureGraph = ChartUtils.getWeight(listWeightData, 12, currentFilter);
                //case list 1 value it not sort and set max min value
                if (listWeightData.size() == 1) {
                    lineChart.setMaxValueYLeft(listWeightData.get(0).getBMI());
                    lineChart.setMinValueYLeft(30);
                    lineChart.setMaxValueYRight(listWeightData.get(0).getWeight());
                    lineChart.setMinValueYRight(10);
                } else {
                    lineChart.setMaxValueYLeft(Weight.maxWeightBMI);
                    lineChart.setMinValueYLeft(Weight.minWeightBMI);
                    lineChart.setMaxValueYRight(Weight.maxWeight);
                    lineChart.setMinValueYRight(Weight.minWeight);
                }
                lineChart.dataSettingWeight(pressureGraph);
                graphicalView = lineChart.execute(context);
                graphLayout.addView(graphicalView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show list data
     */
    private void showList() {
        // clear history data
        historyItems.clear();

        for (Weight weight : listWeightData) {
            if ((typeFilter.equals(""))) {
                weight.setBMI(weight.getWeight());
                historyItems.add(new HistoryItem(
                        DateTimeUtils.getDate(weight.getMsDate()),
                        DateTimeUtils.getTime(context, weight.getMsDate()),
                        String.valueOf(roundTwoDecimals(weight.getBMI())), // BMI
                        String.valueOf(weight.getWeight()))); // Weight
            }

        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAddNew(Weight weight) {
        listWeightData.add(weight);
        Collections.sort(listWeightData);
        User.get().setWeight(weight.getWeight());

        // Updated by ThongPham
        showChart();
        showList();
    }

    @Override
    public void onGetListSuccess(List<Weight> weights, String serviceMsg) {
        dismissProgressDialog();
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(getMainActivity(), serviceMsg).show();
        this.listWeightData = weights;
        Collections.sort(listWeightData);

        // Updated by ThongPham
        showChart();
        showList();
    }

    @Override
    public void onGetFail(String errorMessage, String serviceMsg) {
        dismissProgressDialog();
        if (errorMessage.equals("0")) {
            Boast.makeText(getActivity(), getString(R.string.cap_data_not_exist)).show();
        } else {
            if (serviceMsg != null && !serviceMsg.equals(""))
                Boast.makeText(getMainActivity(), serviceMsg).show();
        }
    }

    public void forwardData() {
        if (onForwardData != null && listWeightData.size() != 0) {
            onForwardData.onForward(listWeightData.get(0));
        }
    }

    public interface OnForwardData {
        void onForward(Weight weight);
    }

    public double roundTwoDecimals(double d) {
        String str = String.format("%.1f", d).replace(",", ".");
        return Double.valueOf(str);
    }
}
