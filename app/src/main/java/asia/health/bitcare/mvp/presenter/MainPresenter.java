package asia.health.bitcare.mvp.presenter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

import asia.health.bitcare.R;
import asia.health.bitcare.activity.MainActivity;
import asia.health.bitcare.adapter.NavAdapter;
import asia.health.bitcare.fragment.AddFitWeightFragment;
import asia.health.bitcare.fragment.BloodPressureFragment;
import asia.health.bitcare.fragment.BloodPressureInputFragment;
import asia.health.bitcare.fragment.BloodSugarFragment;
import asia.health.bitcare.fragment.BloodSugarInputFragment;
import asia.health.bitcare.fragment.BluetoothListDevicesFragment;
import asia.health.bitcare.fragment.DeviceSettingsFragment;
import asia.health.bitcare.fragment.GeneralSettingsFragment;
import asia.health.bitcare.fragment.HealthInfoFragment;
import asia.health.bitcare.fragment.HomeFragment;
import asia.health.bitcare.fragment.ModifyConsultationFragment;
import asia.health.bitcare.fragment.TermOfUseFragment;
import asia.health.bitcare.fragment.UserInfoFragment;
import asia.health.bitcare.fragment.WeightFragment;
import asia.health.bitcare.fragment.WeightInputFragment;
import asia.health.bitcare.helper.FragmentNavigator;
import asia.health.bitcare.mvp.view.MainView;
import asia.health.bitcare.utils.SystemHelper;
import asia.health.bitcare.widget.toast.Boast;

/**
 * Created by HP on 27-Dec-16.
 */

public class MainPresenter {
    private final String TAG = getClass().getSimpleName();
    //Helper
    public FragmentNavigator fragmentNavigator;
    // click back 2 times to exit app
    boolean doubleBackToExit = false;
    private Context context;
    private MainView mainView;
    private NavAdapter.OnNavItemSelected onNavItemSelected = new NavAdapter.OnNavItemSelected() {
        @Override
        public void onItemSelected(String itemId) {
            SystemHelper.hideKeyboard(context);
            mainView.onCloseDrawer();

            // Check to see which item was being clicked and perform appropriate action
            goToFragmentItem(itemId);
        }
    };

    public MainPresenter(MainView mainView) {
        this.context = (Context) mainView;
        this.mainView = mainView;
        init();
    }

    private void init() {
        fragmentNavigator = new FragmentNavigator(((MainActivity) mainView).getSupportFragmentManager(), R.id.frameContainer);
        fragmentNavigator.setOnStackChanged(new FragmentNavigator.onStackChanged() {
            @Override
            public void onChanged(Fragment fragment) {
                if (fragment instanceof HomeFragment) {
                    ((HomeFragment) fragmentNavigator.getActiveFragment()).load();
                    mainView.setFragmentToolBarVisible(View.GONE);
                } else if (fragment instanceof BloodPressureFragment) {
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                    mainView.updateToolBar(R.string.cap_bloodpressure, true, true, true, true);
                } else if (fragment instanceof BloodSugarFragment) {
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                    mainView.updateToolBar(R.string.cap_bloodsugar, true, true, true, true);
                } else if (fragment instanceof WeightFragment) {
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                    mainView.updateToolBar(R.string.cap_weight, true, true, true, false);
                } else if (fragment instanceof AddFitWeightFragment) {
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                    mainView.updateToolBar(R.string.cap_target_weight_input, false, false, false, false);
                } else if (fragment instanceof HealthInfoFragment) {
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                    ((MainActivity) mainView).updateToolBarTitle(((MainActivity) mainView).getString(R.string.cap_consultation_contents));
                } else if (fragment instanceof GeneralSettingsFragment) {
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                    mainView.updateToolBar(R.string.cap_generalsettings, false, false, false, false);
                } else if (fragment instanceof DeviceSettingsFragment) {
                    ((MainActivity) mainView).updateToolBarTitle(((MainActivity) mainView).getString(R.string.cap_devicesettings));
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                } else if (fragment instanceof TermOfUseFragment) {
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                    mainView.updateToolBar(R.string.cap_termofuse, false, false, false, false);
                } else if (fragment instanceof BloodPressureInputFragment) {
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                    mainView.updateToolBar(R.string.cap_bloodpresureinput, false, false, false, false);
                } else if (fragment instanceof BloodSugarInputFragment) {
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                    mainView.updateToolBar(R.string.cap_bloodsugarinput, false, false, false, false);
                } else if (fragment instanceof WeightInputFragment) {
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                    mainView.updateToolBar(R.string.cap_weight_input, false, false, false, false);
                } else if (fragment instanceof ModifyConsultationFragment) {
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                    mainView.updateToolBar(R.string.cap_modify_consultation_contents, false, false, false, false);
                } else if (fragment instanceof UserInfoFragment) {
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                    mainView.updateToolBar(R.string.cap_user_info, false, false, false, false);
                } else if (fragment instanceof BluetoothListDevicesFragment) {
                    mainView.setFragmentToolBarVisible(View.VISIBLE);
                    ((MainActivity) mainView).updateToolBarTitle(((MainActivity) mainView).getString(R.string.cap_list_connected_device));
                }
            }
        });
        fragmentNavigator.setRootFragment(new HomeFragment());
    }

    public void switchFragment(Fragment fragment) {
        String id = null;
        boolean clear = false;
        if (fragment instanceof HomeFragment) {
            id = context.getString(R.string.cap_home);
            clear = true;
        }
        if (fragment instanceof BloodPressureFragment) {
            id = context.getString(R.string.cap_bloodpressure);
            clear = true;
        }
        if (fragment instanceof BloodSugarFragment) {
            id = context.getString(R.string.cap_bloodsugar);
            clear = true;
        }
        if (fragment instanceof WeightFragment) {
            id = context.getString(R.string.cap_weight);
            clear = true;
        }
        if (fragment instanceof AddFitWeightFragment) {
            id = context.getString(R.string.cap_targetweight);
            clear = true;
        }
        if (fragment instanceof HealthInfoFragment) {
            id = context.getString(R.string.activity_main_notificationconsultation);
            clear = true;
        }
        if (fragment instanceof GeneralSettingsFragment) {
            id = context.getString(R.string.cap_generalsettings);
            clear = true;
        }
        if (fragment instanceof DeviceSettingsFragment) {
            id = context.getString(R.string.cap_devicesettings);
            clear = true;
        }
        if (fragment instanceof TermOfUseFragment) {
            id = context.getString(R.string.cap_termofuse);
            clear = true;
        }

        if (id != null) {
            mainView.setChecked(id);
        }

        if (clear) {
            fragmentNavigator.goToRoot();
        }

        fragmentNavigator.goTo(fragment);
    }

    private void goToFragmentItem(String id) {
        if (id.equals(context.getString(R.string.cap_home))) {
            fragmentNavigator.goToRoot();
        } else if (id.equals(context.getString(R.string.cap_bloodpressure))) {
            switchFragment(new BloodPressureFragment());
        } else if (id.equals(context.getString(R.string.cap_bloodsugar))) {
            switchFragment(new BloodSugarFragment());
        } else if (id.equals(context.getString(R.string.activity_main_notificationconsultation))) {
            switchFragment(new HealthInfoFragment());
        } else if (id.equals(context.getString(R.string.cap_weight))) {
            switchFragment(new WeightFragment());
        } else if (id.equals(context.getString(R.string.cap_targetweight))) {
            switchFragment(new AddFitWeightFragment());
        } else if (id.equals(context.getString(R.string.cap_settings))) {
            switchFragment(new GeneralSettingsFragment());
        } else if (id.equals(context.getString(R.string.cap_devicesettings))) {
            switchFragment(new DeviceSettingsFragment());
        } else if (id.equals(context.getString(R.string.cap_termofuse))) {
            switchFragment(new TermOfUseFragment());
        }
    }

    public void onBackPressed() {
        if (fragmentNavigator.getActiveFragment() instanceof BloodPressureInputFragment ||
                fragmentNavigator.getActiveFragment() instanceof BloodSugarInputFragment ||
                fragmentNavigator.getActiveFragment() instanceof ModifyConsultationFragment ||
                fragmentNavigator.getActiveFragment() instanceof UserInfoFragment ||
                fragmentNavigator.getActiveFragment() instanceof WeightInputFragment ||
                fragmentNavigator.getActiveFragment() instanceof BluetoothListDevicesFragment) {
            fragmentNavigator.goOneBack();
        } else if (fragmentNavigator.getActiveFragment() instanceof TermOfUseFragment ||
                fragmentNavigator.getActiveFragment() instanceof DeviceSettingsFragment ||
                fragmentNavigator.getActiveFragment() instanceof GeneralSettingsFragment ||
                fragmentNavigator.getActiveFragment() instanceof AddFitWeightFragment ||
                fragmentNavigator.getActiveFragment() instanceof BloodSugarFragment ||
                fragmentNavigator.getActiveFragment() instanceof BloodPressureFragment ||
                fragmentNavigator.getActiveFragment() instanceof WeightFragment) {
            fragmentNavigator.goToRoot();
            mainView.setChecked(context.getString(R.string.cap_home));
        } else if (fragmentNavigator.getActiveFragment() instanceof HealthInfoFragment) {
            if (((HealthInfoFragment) fragmentNavigator.getActiveFragment()).enableBack()) {
                fragmentNavigator.goToRoot();
                mainView.setChecked(context.getString(R.string.cap_home));
            } else {
                ((HealthInfoFragment) fragmentNavigator.getActiveFragment()).doBack();
            }
        } else {
            doubleBackToExit();
        }
    }

    private void doubleBackToExit() {
        if (doubleBackToExit) {
            mainView.exitApp();
            return;
        }

        doubleBackToExit = true;
        Boast.makeText(context, context.getString(R.string.double_back_to_exit)).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExit = false;
            }
        }, 2000); // reset the variable after 2s

    }

    public NavAdapter.OnNavItemSelected getOnNavItemSelected() {
        return onNavItemSelected;
    }
}
