package asia.health.bitcare.fragment;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import asia.health.bitcare.R;
import asia.health.bitcare.activity.SplashActivity;
import asia.health.bitcare.base.BaseFragment;
import asia.health.bitcare.dialog.DialogChangePass;
import asia.health.bitcare.dialog.DialogConfirmPass;
import asia.health.bitcare.dialog.DialogLogOut;
import asia.health.bitcare.dialog.DialogInfo;
import asia.health.bitcare.model.User;
import asia.health.bitcare.mvp.presenter.ForgotPasswordPresenter;
import asia.health.bitcare.mvp.view.ForgotPasswordView;
import asia.health.bitcare.prefs.Constant;
import asia.health.bitcare.widget.toast.Boast;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralSettingsFragment extends BaseFragment implements View.OnClickListener, ForgotPasswordView {
    private LinearLayout lnUser;
    private LinearLayout lnChangePass;
    private LinearLayout lnLogOut;
    private LinearLayout lnMemberShip;

    @Override
    public int setFragmentView() {
        return R.layout.fragment_general_settings;
    }

    @Override
    public void initView() {
        lnUser = (LinearLayout) view.findViewById(R.id.lnUserInfo);
        lnChangePass = (LinearLayout) view.findViewById(R.id.lnChangePass);
        lnLogOut = (LinearLayout) view.findViewById(R.id.lnLogOut);
        lnMemberShip = (LinearLayout) view.findViewById(R.id.lnMemberShip);
    }

    @Override
    public void initValue() {

    }

    @Override
    public void initAction() {
        lnUser.setOnClickListener(this);
        lnChangePass.setOnClickListener(this);
        lnLogOut.setOnClickListener(this);
        lnMemberShip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnUserInfo:
                getMainActivity().switchFragment(new UserInfoFragment());
                break;
            case R.id.lnChangePass:
                new DialogConfirmPass.Build(getMainActivity())
                        .setOnConfirmPassListener(new DialogConfirmPass.Build.onConfirmPass() {
                            @Override
                            public void onConfirm(String pass) {
                                if (pass.equals(User.get().getUserPw())) {
                                    new DialogChangePass.Build(getMainActivity())
                                            .setOnChangePassListener(new DialogChangePass.Build.OnChangePassListener() {
                                                @Override
                                                public void onConfirm(String newPass) {
                                                    showProgressDialog();
                                                    new ForgotPasswordPresenter(GeneralSettingsFragment.this)
                                                            .modifyPassword(String.valueOf(User.get().getUserNum()), newPass);
                                                }

                                                @Override
                                                public void onCancel() {

                                                }
                                            }).show();
                                } else {
                                    Boast.makeText(getMainActivity(), "Password not match").show();
                                }
                            }

                            @Override
                            public void onCancel() {

                            }
                        }).show();
                break;
            case R.id.lnLogOut:
                new DialogLogOut.Build(getMainActivity()).setOnLogoutListener(new DialogLogOut.Build.OnLogoutListener() {
                    @Override
                    public void onConfirm() {
                        goLogin();
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
                break;
            case R.id.lnMemberShip:
//                new DialogLogOut.Build(getMainActivity()).setTitle(getResources().getString(R.string.cap_membership_withdrawal))
//                        .setContent(getResources().getString(R.string.cap_ask_membership_withdrawal))
//                        .setOnLogoutListener(new DialogLogOut.Build.OnLogoutListener() {
//                            @Override
//                            public void onConfirm() {
//                                Log.d("Membership Withdrawal", "Yes");
//                                //getSharePreferences().edit().putBoolean(Constant.AUTO_LOGIN, false).apply();
//                                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//                                //startActivity(browserIntent);
//                            }
//
//                            @Override
//                            public void onCancel() {
//                                Log.d("Membership Withdrawal", "No");
//                            }
//                        }).show();

                new DialogInfo.Build(getMainActivity())
                        .setTitle(getResources().getString(R.string.cap_membership_withdrawal))
                        .setMessage(getResources().getString(R.string.cap_ask_membership_withdrawal)).show();
                break;
        }
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
    }

    @Override
    public void onGetUserNumSuccess(int userNum, String serviceMsg) {

    }

    @Override
    public void onError(String errorMessage) {
        dismissProgressDialog();
        Boast.makeText(getMainActivity(), errorMessage).show();
    }

    @Override
    public void onModifyPasswordSuccess(String serviceMsg) {
        dismissProgressDialog();
        Boast.makeText(getMainActivity(), getMainActivity().getString(R.string.cap_password_changed)).show();
    }
}
