package asia.health.bitcare.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import asia.health.bitcare.fragment.BloodSugarInputFragment.BloodSugarInputFragmentCallBack;
import asia.health.bitcare.model.BloodSugar;
import asia.health.bitcare.mvp.model.HistoryItem;
import asia.health.bitcare.mvp.presenter.BloodSugarPresenter;
import asia.health.bitcare.mvp.view.BloodSugarView;
import asia.health.bitcare.network.APIConstant;
import asia.health.bitcare.utils.DateTimeUtils;
import asia.health.bitcare.widget.chart.ChartUtils;
import asia.health.bitcare.widget.chart.LineChartNew;
import asia.health.bitcare.widget.toast.Boast;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodSugarFragment extends BaseFragment implements BloodSugarView,
        BloodSugarInputFragmentCallBack, OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private static OnForwardData onForwardData;
    private String typeFilter = "";
    private String mealFilter = "";
    private int currentType = R.id.menu_type_all;
    // declare UI View
    private RecyclerView rvHistory;
    private ImageView imgAll;
    private ImageView imgBefore;
    private ImageView imgAfter;
    //insert 03/01 by Quan
    private ListHistoryAdapter adapter;
    private List<HistoryItem> historyItems = new ArrayList<>();
    private List<BloodSugar> listBloodSugar = new ArrayList<>();
    private List<BloodSugar> listBloodSugarChart = new ArrayList<>();
    private BloodSugarPresenter presenter;
    // layout
    private ViewGroup historyLayout;
    private boolean isChartExtended;


    //add by Quan
    private LineChartNew lineChart;
    private XYMultipleSeriesDataset ds;
    private GraphicalView graphicalView;
    private XYMultipleSeriesRenderer msr;
    private LinearLayout graphLayout;

    public static void setOnForwardData(OnForwardData onForwardData) {
        BloodSugarFragment.onForwardData = onForwardData;
    }

    @Override
    public int setFragmentView() {
        return R.layout.fragment_blood_sugar_2;
    }

    @Override
    public void initView() {
        // define UI element
        //lineChart = new LineChart(view);
        rvHistory = (RecyclerView) view.findViewById(R.id.rvHistory);
        graphLayout = (LinearLayout) view.findViewById(R.id.chart);
        historyLayout = (ViewGroup) view.findViewById(R.id.historyLayout);
        imgAll = (ImageView) view.findViewById(R.id.imgAll);
        imgBefore = (ImageView) view.findViewById(R.id.imgBefore);
        imgAfter = (ImageView) view.findViewById(R.id.imgAfter);
    }

    @Override
    public void initValue() {
        BloodSugarInputFragment.mCallBack = this;
        presenter = new BloodSugarPresenter(this);
        //insert 03/01 by Quan
        adapter = new ListHistoryAdapter(context, historyItems, true, true);
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
        presenter.getBloodSugarList(APIConstant.BPMSTYPE_T);

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

        // button onClick
        imgAll.setOnClickListener(this);
        imgBefore.setOnClickListener(this);
        imgAfter.setOnClickListener(this);

        //set event click sort
        setOnPopupMenuClick(new BaseMainToolbarActivity.OnPopupMenuOnClick() {
            @Override
            public void onPeriodSort(int menuId) {
                //Filter by time only effected in chart
                currentType = menuId;
                showChart();
            }

            @Override
            public void onAnswerSort(int menuId) {
                //Filter by input type effect both of Chart and List
                switch (menuId) {
                    case R.id.menu_all:
                        typeFilter = "";
                        break;
                    case R.id.menu_equiment:
                        typeFilter = APIConstant.BSMSTYPE_D;
                        break;
                    case R.id.menu_uer_input:
                        typeFilter = APIConstant.BSMSTYPE_U;
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
        //Clear
        listBloodSugarChart.clear();

        // Clear data on chart
        for (BloodSugar bloodSugar : listBloodSugar) {
            if (mealFilter.equals("")) {
                listBloodSugarChart.add(bloodSugar);
            } else if (bloodSugar.getBsType().equals("A") && mealFilter.equals(APIConstant.BSTYPE_A)) {
                listBloodSugarChart.add(bloodSugar);
            } else if (bloodSugar.getBsType().equals("B") && mealFilter.equals(APIConstant.BSTYPE_B)) {
                listBloodSugarChart.add(bloodSugar);
            }
        }
        Collections.sort(listBloodSugarChart);//update value max min
        try {
            lineChart = new LineChartNew(ds, msr, context);
            //Clear view chart
            graphLayout.removeAllViews();
            if (listBloodSugarChart.size() != 0) {
                List<BloodSugar> sugarGraph = ChartUtils.getBloodSugar(listBloodSugarChart, 12, currentType);
                //case list 1 value it not sort and set max min value
                if (listBloodSugarChart.size() == 1) {
                    lineChart.setMaxValueYLeft(listBloodSugarChart.get(0).getBsVal());
                    lineChart.setMinValueYLeft(30);
                    lineChart.setMaxValueYRight(listBloodSugarChart.get(0).getBsWeight());
                    lineChart.setMinValueYRight(10);
                } else {
                    lineChart.setMaxValueYLeft(BloodSugar.maxBsVal);
                    lineChart.setMinValueYLeft(BloodSugar.minBsVal);
                    lineChart.setMaxValueYRight(BloodSugar.maxWeight);
                    lineChart.setMinValueYRight(BloodSugar.minWeight);
                }
                lineChart.dataSettingSugar(sugarGraph);
                graphicalView = lineChart.executeSugar(context, mealFilter);
                graphLayout.addView(graphicalView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Show list history data
     */
    private void showList() {
        historyItems.clear();
        for (BloodSugar bloodSugar : listBloodSugar) {
            if ((typeFilter.equals("") || bloodSugar.getBsmStyle().equals(typeFilter)) &&
                    (mealFilter.equals("") || bloodSugar.getBsType().equals(mealFilter))) {
                String value1 = String.valueOf(bloodSugar.getBsVal());
                String value2 = bloodSugar.getBsType().equals(APIConstant.BSTYPE_B) ?
                        context.getString(R.string.cap_before) :
                        context.getString(R.string.cap_after);
                String weight = String.valueOf(bloodSugar.getBsWeight());
                historyItems.add(new HistoryItem(DateTimeUtils.getDate(bloodSugar.getBsmsDate()),
                        DateTimeUtils.getTime(context, bloodSugar.getBsmsDate()),
                        value1,
                        value2,
                        weight,
                        bloodSugar.getBsExersiseYN().equals(APIConstant.BSEXERCISEYN_Y),
                        bloodSugar.getBsMedicineYN().equals(APIConstant.BSMEDICINEYN_Y)));
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGetListSuccess(List<BloodSugar> bloodSugarArray, String serviceMsg) {
        dismissProgressDialog();
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(getMainActivity(), serviceMsg).show();
        this.listBloodSugar = bloodSugarArray;
        Collections.sort(listBloodSugar);//add by Quan

        //Update by MrAn
        showChart();
        showList();
    }

    @Override
    public void onGetFail(String message, String serviceMsg) {
        dismissProgressDialog();
        if (message.equals("0")) {
            Boast.makeText(getActivity(), getString(R.string.cap_data_not_exist)).show();
        } else {
            if (serviceMsg != null && !serviceMsg.equals(""))
                Boast.makeText(getMainActivity(), serviceMsg).show();
        }
    }

    @Override
    public void onAddNew(BloodSugar bloodSugar) {
        listBloodSugar.add(bloodSugar);
        Collections.sort(listBloodSugar);

        //Update by MrAn
        showChart();
        showList();
    }

    public void forwardData() {
        if (onForwardData != null && listBloodSugar.size() != 0) {
            onForwardData.onForward(listBloodSugar.get(0));
        }
    }

    @Override
    public void onClick(View v) {
        //Filter from Before Meal/After Meal effect both of Chart and List History
        switch (v.getId()) {
            case R.id.imgAll:
                mealFilter = "";
                imgAll.setBackgroundResource((R.drawable.btn_12));
                imgBefore.setBackgroundResource((R.drawable.btn_21));
                imgAfter.setBackgroundResource((R.drawable.btn_31));
                break;
            case R.id.imgBefore:
                mealFilter = APIConstant.BSTYPE_B;
                imgAll.setBackgroundResource((R.drawable.btn_11));
                imgBefore.setBackgroundResource((R.drawable.btn_22));
                imgAfter.setBackgroundResource((R.drawable.btn_31));
                break;
            case R.id.imgAfter:
                mealFilter = APIConstant.BSTYPE_A;
                imgAll.setBackgroundResource((R.drawable.btn_11));
                imgBefore.setBackgroundResource((R.drawable.btn_21));
                imgAfter.setBackgroundResource((R.drawable.btn_32));
                break;
        }
        showChart();
        showList();
    }

    public interface OnForwardData {
        void onForward(BloodSugar bloodSugar);
    }
}

