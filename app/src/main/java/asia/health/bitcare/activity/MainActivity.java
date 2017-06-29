package asia.health.bitcare.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.health.bitcare.R;
import asia.health.bitcare.adapter.NavAdapter;
import asia.health.bitcare.base.BaseMainToolbarActivity;
import asia.health.bitcare.fragment.AddFitWeightFragment;
import asia.health.bitcare.fragment.BloodPressureFragment;
import asia.health.bitcare.fragment.BloodPressureInputFragment;
import asia.health.bitcare.fragment.BloodSugarFragment;
import asia.health.bitcare.fragment.BloodSugarInputFragment;
import asia.health.bitcare.fragment.HealthInfoFragment;
import asia.health.bitcare.fragment.ModifyConsultationFragment;
import asia.health.bitcare.fragment.WeightFragment;
import asia.health.bitcare.fragment.WeightInputFragment;
import asia.health.bitcare.helper.FragmentNavigator;
import asia.health.bitcare.mvp.presenter.MainPresenter;
import asia.health.bitcare.mvp.view.MainView;
import asia.health.bitcare.utils.SystemHelper;

/**
 * Created by HP on 20-Dec-16.
 */

public class MainActivity extends BaseMainToolbarActivity implements MainView {
    public static onReceiveDataListener onReceiveDataListener;
    private final String TAG = getClass().getSimpleName();
    public FragmentNavigator fragmentNavigator;
    // Navigation View
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView rlNav;
    private NavAdapter navAdapter;
    private MainPresenter mainPresenter;
    private LinearLayout lnNav;

    @Override
    protected int getEnableBluetoothRequestCode() {
        return 1111;
    }

    @Override
    protected int getDicoverableBTRequestCode() {
        return 2222;
    }

    @Override
    public int getView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        super.initView();
        navigationView = (NavigationView) findViewById(R.id.navDrawer);
        rlNav = (RecyclerView) findViewById(R.id.navRows);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        lnNav = (LinearLayout) findViewById(R.id.lnNav);
        findViewById(R.id.lnMenuBar).setLayoutParams(new LinearLayout.LayoutParams(
                DrawerLayout.LayoutParams.MATCH_PARENT,
                DrawerLayout.LayoutParams.WRAP_CONTENT,
                0.08f
        ));
        findViewById(R.id.lnContainer).setLayoutParams(new LinearLayout.LayoutParams(
                DrawerLayout.LayoutParams.MATCH_PARENT,
                DrawerLayout.LayoutParams.WRAP_CONTENT,
                0.92f
        ));
    }

    @Override
    public void initValue() {
        mainPresenter = new MainPresenter(this);
        navigationView.setItemIconTintList(null);

        fragmentNavigator = mainPresenter.fragmentNavigator;
        rlNav.setLayoutManager(new LinearLayoutManager(context));
        rlNav.setItemAnimator(new DefaultItemAnimator());

        navAdapter = new NavAdapter(this);
        rlNav.setAdapter(navAdapter);
        navAdapter.setOnNavItemSelected(mainPresenter.getOnNavItemSelected());
        onReceiveDataListener = new onReceiveDataListener() {
            @Override
            public void onReceiveData() {
                if (!(mainPresenter.fragmentNavigator.getActiveFragment() instanceof BloodPressureInputFragment)) {
                    if (mainPresenter.fragmentNavigator.getActiveFragment() instanceof BloodPressureFragment) {
                        btnInputOnClick();
                    } else {
                        mainPresenter.switchFragment(new BloodPressureFragment().setGoingToInput(true));
                    }
                } else {
                    Log.d("Main", "Skip on receive");
                }
            }
        };
    }

    public void updateFitWeight(String value) {
        if (value != null && !value.equals(""))
            ((TextView) findViewById(R.id.txtFitWeight)).setText(value + "kg");
    }

    @Override
    public void initAction() {
        findViewById(R.id.btnMenu).setOnClickListener(this);
        findViewById(R.id.space).setOnClickListener(this);
        findViewById(R.id.lnHide).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnFitWeight).setOnClickListener(this);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                findViewById(R.id.space).setVisibility(View.VISIBLE);
                SystemHelper.hideKeyboard(context);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                findViewById(R.id.lnHide).setVisibility(View.GONE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void updateToolBar(int resTitle, boolean showPopupMenuLayout, boolean showInputSearchLayout, boolean showInput, boolean showSortAnswer) {
        super.updateToolbar(resTitle);

        showHidePopupMenu(showPopupMenuLayout);
        showHideInput(showInputSearchLayout);

        showInput(showInput);
        hideSortAnswer(showSortAnswer);
    }

    public void updateToolBarTitle(String title) {
        super.updateToolBarTitle(title);
    }

    @Override
    public void btnInputOnClick() {
        if (fragmentNavigator.getActiveFragment() instanceof BloodPressureFragment) {
            fragmentNavigator.goTo(new BloodPressureInputFragment());
            ((BloodPressureFragment) fragmentNavigator.getPreviousFragment()).forwardData();
        } else if (fragmentNavigator.getActiveFragment() instanceof BloodSugarFragment) {
            fragmentNavigator.goTo(new BloodSugarInputFragment());
            ((BloodSugarFragment) fragmentNavigator.getPreviousFragment()).forwardData();
        } else if (fragmentNavigator.getActiveFragment() instanceof WeightFragment) {
            fragmentNavigator.goTo(new WeightInputFragment());
            ((WeightFragment) fragmentNavigator.getPreviousFragment()).forwardData();
        } else if (fragmentNavigator.getActiveFragment() instanceof HealthInfoFragment) {
            fragmentNavigator.goTo(new ModifyConsultationFragment());
        }
    }

    @Override
    public void onCloseDrawer() {
        drawerLayout.closeDrawers();
        findViewById(R.id.lnHide).setVisibility(View.GONE);
    }

    @Override
    public void exitApp() {
        MainActivity.this.moveTaskToBack(true);
    }

    @Override
    public void setChecked(String id) {
        navAdapter.setSelectedItem(id);
    }

    @Override
    public void setFragmentToolBarVisible(int visible) {
        super.setFragmentToolbarVisible(visible);
    }

    @Override
    public void onBackPressed() {
        SystemHelper.hideKeyboard(context);
        onCloseDrawer();
        mainPresenter.onBackPressed();
    }

    public void switchFragment(Fragment fragment) {
        mainPresenter.switchFragment(fragment);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.lnHide:
            case R.id.space:
                drawerLayout.closeDrawer(lnNav);
                findViewById(R.id.lnHide).setVisibility(View.GONE);
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnMenu:
                drawerLayout.openDrawer(lnNav);
                findViewById(R.id.lnHide).setVisibility(View.VISIBLE);
                break;
            case R.id.btnFitWeight:
                switchFragment(new AddFitWeightFragment());
                break;
        }
    }

    public interface onReceiveDataListener {
        void onReceiveData();
    }
}
