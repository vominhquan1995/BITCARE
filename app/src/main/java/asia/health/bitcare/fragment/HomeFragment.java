package asia.health.bitcare.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import asia.health.bitcare.R;
import asia.health.bitcare.activity.SplashActivity;
import asia.health.bitcare.base.BaseFragment;
import asia.health.bitcare.model.User;
import asia.health.bitcare.mvp.presenter.HomePresenter;
import asia.health.bitcare.mvp.view.HomeView;
import asia.health.bitcare.network.APIConstant;
import asia.health.bitcare.prefs.Constant;
import asia.health.bitcare.widget.toast.Boast;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, HomeView {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private HomePresenter presenter;
    private boolean inBackground = false;

    @Override
    public int setFragmentView() {
        return R.layout.fragment_home_3;
    }

    @Override
    public void initView() {
        getMainActivity().setFragmentToolBarVisible(View.GONE);
    }

    @Override
    public void initValue() {
        presenter = new HomePresenter(this);
    }

    @Override
    public void initAction() {
        view.findViewById(R.id.btnBS).setOnClickListener(this);
        view.findViewById(R.id.btnBP1).setOnClickListener(this);
        view.findViewById(R.id.btnBP2).setOnClickListener(this);
        view.findViewById(R.id.btnWeight).setOnClickListener(this);
        load();
    }

    public void load() {
        showProgressDialog();
        presenter.getLastMSIInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBS:
                getMainActivity().switchFragment(new BloodSugarFragment());
                break;
            case R.id.btnBP1:
            case R.id.btnBP2:
                getMainActivity().switchFragment(new BloodPressureFragment());
                break;
            case R.id.btnWeight:
                getMainActivity().switchFragment(new WeightFragment());
                break;
        }
    }

    @Override
    public void onBpSysChange(int value) {
        ((TextView) view.findViewById(R.id.txtBpVal)).setText(String.valueOf(value) + "/");
    }

    @Override
    public void onBpMinChange(int value) {
        ((TextView) view.findViewById(R.id.txtBpMin)).setText(String.valueOf(value));
    }

    @Override
    public void onBpPulseChange(int value) {
        ((TextView) view.findViewById(R.id.txtBPM)).setText(String.valueOf(value));
    }

    @Override
    public void onBpMedicineChange(String value) {

    }

    @Override
    public void onBpExerciseChange(String value) {

    }

    @Override
    public void onBpMsDateChange(String value) {

    }

    @Override
    public void onBsValChange(int value) {
        ((TextView) view.findViewById(R.id.txtBsVal)).setText(String.valueOf(value));
    }

    @Override
    public void onBsTypeChange(String value) {
        if (value.equals(APIConstant.BSTYPE_B)) {
            ((TextView) view.findViewById(R.id.txtBSStyle)).setText(getString(R.string.cap_beforemeals) + " : ");
        } else {
            ((TextView) view.findViewById(R.id.txtBSStyle)).setText(getString(R.string.cap_aftermeals) + " : ");
        }
    }

    @Override
    public void onBsMedicineChange(String value) {

    }

    @Override
    public void onBsExerciseChange(String value) {

    }

    @Override
    public void onBsMsDateChange(String value) {

    }

    @Override
    public void onWeightChange(double value) {
        if (value == 0.0) {
            value = User.get().getWeight();
        }
        ((TextView) view.findViewById(R.id.txtWeight)).setText(String.valueOf(value));
        ((TextView) view.findViewById(R.id.txtHeight)).setText(String.valueOf(User.get().getHeight()));
    }

    @Override
    public void onWTMSDateChange(String value) {

    }

    @Override
    public void onError(String message) {
        dismissProgressDialog();
        if (message != null && !message.equals("")) {
            Boast.makeText(getMainActivity(), message).show();
        }
    }

    @Override
    public void onBMIChanged(double string) {
        NumberFormat formatter = new DecimalFormat("#0.0");
        ((TextView) view.findViewById(R.id.txtBMI)).setText(String.valueOf(formatter.format(string)).replace(",", "."));
    }

    @Override
    public void onBMRChanged(double string) {
        NumberFormat formatter = new DecimalFormat("#0");
        ((TextView) view.findViewById(R.id.txtBMR)).setText(String.valueOf(formatter.format(string)).replace(",", "."));
    }

    @Override
    public void onHealthConditionChanged(int anInt) {
        switch (anInt) {
            case 0:
                view.findViewById(R.id.imgStatus).setBackgroundResource(R.drawable.main_smile01);
                break;
            case 1:
                view.findViewById(R.id.imgStatus).setBackgroundResource(R.drawable.main_smile02);
                break;
            case 2:
                view.findViewById(R.id.imgStatus).setBackgroundResource(R.drawable.main_smile03);
                break;
        }
    }

    @Override
    public void onNoticeChanged(String notice) {
        ((TextView) view.findViewById(R.id.txtHealType)).setText(notice);
    }

    @Override
    public void onServiceMsg(String serviceMsg) {
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(getMainActivity(), serviceMsg).show();
    }

    @Override
    public void onFitWeightChanged(double value) {
        getMainActivity().updateFitWeight(String.valueOf(value));
    }

    @Override
    public void onSuccess() {
        dismissProgressDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Home", "Back to from background");
        if (inBackground) {
            presenter.checkMemberWithdraw(String.valueOf(User.get().getUserNum()));
            inBackground = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        inBackground = true;
    }

    @Override
    public void onWithdraw() {
        dismissProgressDialog();
        Log.d("Home", "Withdraw");
        goLogin();
    }

    @Override
    public void onMember() {
        dismissProgressDialog();
        Log.d("Home", "Member");
    }

    private void goLogin() {
        getSharePreferences().edit().putBoolean(Constant.AUTO_LOGIN, false).apply();
        getSharePreferences().edit().putString(Constant.USER_ID, "").apply();
        getSharePreferences().edit().putString(Constant.USER_PW, "").apply();
        Intent intent = new Intent(getMainActivity(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getMainActivity().startActivity(intent);
        getMainActivity().finish();
        Boast.makeText(getMainActivity(), getResources().getString(R.string.cap_withdraw_goto_login)).show();
    }
}
