package asia.health.bitcare.fragment;


import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import asia.health.bitcare.model.BloodPressure;
import asia.health.bitcare.mvp.model.HistoryItem;
import asia.health.bitcare.mvp.presenter.BloodPressurePresenter;
import asia.health.bitcare.mvp.view.BloodPressureView;
import asia.health.bitcare.network.APIConstant;
import asia.health.bitcare.utils.DateTimeUtils;
import asia.health.bitcare.utils.NumberUtil;
import asia.health.bitcare.widget.chart.ChartUtils;
import asia.health.bitcare.widget.chart.LineChartNew;
import asia.health.bitcare.widget.toast.Boast;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodPressureFragment extends BaseFragment implements BloodPressureView, BloodPressureInputFragment.BloodPressureInputFragmentCallBack {

    private static BloodPressureFragment.OnForwardData onForwardData;
    private final String TAG = getClass().getSimpleName();
    private boolean isGoingToInput;
    private String typeFilter = "";
    private int currentType = R.id.menu_type_all;
    // declare UI view
    private RecyclerView rvHistory;
    // layout
    private ViewGroup historyLayout;
    private boolean isChartExtended;
    private BloodPressurePresenter presenter;
    private ListHistoryAdapter adapter;
    private List<HistoryItem> historyItems = new ArrayList<>();
    private List<BloodPressure> listBloodPressure = new ArrayList<>();
    private LineChartNew lineChart;
    private XYMultipleSeriesDataset ds;
    private LinearLayout graphLayout;
    private GraphicalView graphicalView;
    private XYMultipleSeriesRenderer msr;

    public static void setOnForwardData(
            BloodPressureFragment.OnForwardData onForwardData) {
        BloodPressureFragment.onForwardData = onForwardData;
    }

    public BloodPressureFragment setGoingToInput(boolean goingToInput) {
        isGoingToInput = goingToInput;
        return this;
    }

    @Override
    public int setFragmentView() {
        return R.layout.fragment_blood_pressure_2;
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
        BloodPressureInputFragment.mCallBack = this;
        presenter = new BloodPressurePresenter(this);
        adapter = new ListHistoryAdapter(context, historyItems, true, false);
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
        presenter.getBloodPressureList(APIConstant.BPMSTYPE_T);

        // show/hide full chart
        graphLayout.setOnClickListener(new View.OnClickListener() {
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
        setOnPopupMenuClick(new BaseMainToolbarActivity.OnPopupMenuOnClick() {
            @Override
            public void onPeriodSort(int menuId) {
                currentType = menuId;
                showChart();
            }

            @Override
            public void onAnswerSort(int menuId) {
                switch (menuId) {
                    case R.id.menu_all:
                        typeFilter = "";
                        break;
                    case R.id.menu_equiment:
                        typeFilter = APIConstant.BPMSTYPE_D;
                        break;
                    case R.id.menu_uer_input:
                        typeFilter = APIConstant.BPMSTYPE_U;
                        break;
                }
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
        lineChart.clearData();

        // Filter data
        Collections.sort(listBloodPressure);
        //update value max min
        try {
            lineChart = new LineChartNew(ds, msr, context);
            //Clear view chart
            graphLayout.removeAllViews();
            if (listBloodPressure.size() != 0) {
                List<BloodPressure> pressureGraph = ChartUtils.getBloodPressure(listBloodPressure, 12, currentType);
                //case list not set value max min list
                if (listBloodPressure.size() == 1) {
                    int maxBpPulse = NumberUtil.getMax(listBloodPressure.get(0).getBpSys(), listBloodPressure.get(0).getBpMin());
                    maxBpPulse = NumberUtil.getMax(maxBpPulse, listBloodPressure.get(0).getBpPulse());
                    lineChart.setMaxValueYLeft(maxBpPulse);
                    lineChart.setMinValueYLeft(0);
                    lineChart.setMaxValueYRight(listBloodPressure.get(0).getBpWeight());
                    lineChart.setMinValueYRight(0);
                } else {
                    lineChart.setMaxValueYLeft(BloodPressure.maxBpPulse);
                    lineChart.setMinValueYLeft(BloodPressure.minBpPulse);
                    lineChart.setMaxValueYRight(BloodPressure.maxWeight);
                    lineChart.setMinValueYRight(BloodPressure.minWeight);
                }
                lineChart.dataSettingPressuare(pressureGraph);
                graphicalView = lineChart.executePressure(context);
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

        for (BloodPressure bloodPressure : listBloodPressure) {
            if ((typeFilter.equals("")) || bloodPressure.getBpmStyle().equals(typeFilter)) {
                String value1 = String.valueOf(bloodPressure.getBpSys());
                String value2 = String.valueOf(bloodPressure.getBpMin());
                String value3 = String.valueOf(bloodPressure.getBpPulse());
                String weight = String.valueOf(bloodPressure.getBpWeight());
                historyItems.add(new HistoryItem(DateTimeUtils.getDate(bloodPressure.getBpmsDate()),
                        DateTimeUtils.getTime(context, bloodPressure.getBpmsDate()),
                        value1,
                        value2,
                        value3,
                        weight,
                        bloodPressure.getBpExersiseYN().equals(APIConstant.BSEXERCISEYN_Y),
                        bloodPressure.getBpMedicineYN().equals(APIConstant.BSMEDICINEYN_Y)));
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGetListSuccess(List<BloodPressure> data, String serviceMsg) {
        dismissProgressDialog();
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(getMainActivity(), serviceMsg).show();
        this.listBloodPressure = data;
        if (listBloodPressure != null && listBloodPressure.size() > 0) {
            Collections.sort(listBloodPressure);

            // Updated by ThongPham
            showChart();
            showList();

            if (isGoingToInput) {
                forwardData();
            }
        } else {
            Boast.makeText(getActivity(), getString(R.string.cap_data_not_exist)).show();
        }
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

    @Override
    public void onAddNew(BloodPressure bloodPressure) {
        listBloodPressure.add(bloodPressure);
        Collections.sort(listBloodPressure);

        // Updated by ThongPham
        showChart();
        showList();
    }

    public void forwardData() {
        if (onForwardData != null && listBloodPressure.size() != 0) {
            onForwardData.onForward(listBloodPressure.get(0));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isGoingToInput) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getMainActivity().fragmentNavigator.goTo(new BloodPressureInputFragment());
                }
            }, 500);
            isGoingToInput = false;
        }

    }

    public interface OnForwardData {
        void onForward(BloodPressure bloodPressure);
    }
}
