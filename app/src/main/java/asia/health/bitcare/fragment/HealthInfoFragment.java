package asia.health.bitcare.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import asia.health.bitcare.R;
import asia.health.bitcare.adapter.ListHealthInfoAdapter;
import asia.health.bitcare.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthInfoFragment extends BaseFragment {

    private final int HEALTH_INFO_VIEW = 0;
    private final int HEALTH_INFO_INNER_VIEW = 1;
    private final int HEALTH_INFO_DETAIL_VIEW = 2;
    private int lastPosition = 0;

    private final int[] HEALTH_INFO_INNER_LIST = new int[]{
            R.array.health_info_list1,
            R.array.health_info_list2,
            R.array.health_info_list3,
            R.array.health_info_list4,
            R.array.health_info_list5,
            R.array.health_info_list6,
    };

    private final int[] HEALTH_INFO_DETAIL_LIST = new int[]{
            R.array.health_detail_1,
            R.array.health_detail_2,
            R.array.health_detail_3,
            R.array.health_detail_4,
            R.array.health_detail_5,
            R.array.health_detail_6,
    };

    private RecyclerView rvHealthInfo;
    private ListHealthInfoAdapter adapter;
    private CardView cardHealthInfo;
    private TextView tvHealthInfo;

    private int currentView;
    private int currentInner;

    @Override
    public int setFragmentView() {
        return R.layout.fragment_health_info;
    }

    @Override
    public void initView() {
        rvHealthInfo = (RecyclerView) view.findViewById(R.id.rvHealthInfo);
        cardHealthInfo = (CardView) view.findViewById(R.id.cardHealthInfo);
        tvHealthInfo = (TextView) view.findViewById(R.id.tvHealthInfo);
    }

    @Override
    public void initValue() {
        setHealthInfoListData(getResources().getStringArray(R.array.health_info_list));
        currentView = HEALTH_INFO_VIEW;
    }

    @Override
    public void initAction() {
        // set onItemClick adapter
        adapter.setOnItemClickListener(new OnItemClickListener() {
            public static final String TAG = "";

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (currentView) {
                    case HEALTH_INFO_VIEW:
                        currentView = HEALTH_INFO_INNER_VIEW;
                        currentInner = position;
                        lastPosition = position;
                        getMainActivity().updateToolBarTitle(adapter.getTitle(position));
                        updateListData(getResources().getStringArray(HEALTH_INFO_INNER_LIST[position]));
                        break;
                    case HEALTH_INFO_INNER_VIEW:
                        currentView = HEALTH_INFO_DETAIL_VIEW;
                        cardHealthInfo.setVisibility(View.VISIBLE);
                        rvHealthInfo.setVisibility(View.GONE);
                        getMainActivity().updateToolBarTitle(adapter.getTitle(position));
                        tvHealthInfo.setText(
                                getResources().getStringArray(HEALTH_INFO_DETAIL_LIST[currentInner])[position]);
                        break;
                }
            }
        });
    }

    private void setHealthInfoListData(String[] listData) {
        adapter = new ListHealthInfoAdapter(context, Arrays.asList(listData));
        rvHealthInfo.setLayoutManager(new LinearLayoutManager(context));
        rvHealthInfo.setItemAnimator(new DefaultItemAnimator());
        rvHealthInfo.setAdapter(adapter);
    }

    private void updateListData(String[] listData) {
        adapter.update(listData);
    }

    public void doBack() {
        Log.d("Back press", "current view : " + currentView);
        view.findViewById(R.id.scrollView).scrollTo(0, 0);
        switch (currentView) {
            case HEALTH_INFO_INNER_VIEW:
                currentView = HEALTH_INFO_VIEW;
                getMainActivity().updateToolBarTitle(getMainActivity().getString(R.string.activity_main_notificationconsultation));
                updateListData(getResources().getStringArray(R.array.health_info_list));
                break;
            case HEALTH_INFO_DETAIL_VIEW:
                rvHealthInfo.setVisibility(View.VISIBLE);
                cardHealthInfo.setVisibility(View.GONE);
                tvHealthInfo.setText("");
                currentView = HEALTH_INFO_INNER_VIEW;
                updateListData(getResources().getStringArray(HEALTH_INFO_INNER_LIST[currentInner]));
                String[] listTitle = getResources().getStringArray(R.array.health_info_list);
                getMainActivity().updateToolBarTitle(listTitle[lastPosition]);
                break;
        }
    }

    public boolean enableBack() {
        return currentView == HEALTH_INFO_VIEW;
    }
}
