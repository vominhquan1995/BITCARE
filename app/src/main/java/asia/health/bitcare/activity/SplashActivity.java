package asia.health.bitcare.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseActivity;
import asia.health.bitcare.model.User;
import asia.health.bitcare.mvp.presenter.LoginPresenter;
import asia.health.bitcare.mvp.view.LoginView;
import asia.health.bitcare.network.API;
import asia.health.bitcare.prefs.Constant;
import asia.health.bitcare.widget.toast.Boast;

public class SplashActivity extends BaseActivity implements LoginView {

    @Override
    public int getView() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initValue() {

    }

    @Override
    public void initAction() {

        countDownToLogin();
    }

    /**
     * wait for 1s & move to LoginActivity
     */
    private void countDownToLogin() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOnline()) {
                    API.checkConnection(new API.OnAPIListener() {
                        @Override
                        public void onSuccess(JSONObject response, String serviceMsg) {
                            if (sharedPreferences.getBoolean(Constant.AUTO_LOGIN, false)) {
                                String userID = sharedPreferences.getString(Constant.USER_ID, "");
                                String userPw = sharedPreferences.getString(Constant.USER_PW, "");
                                if (!userID.equals("") && !userPw.equals("")) {
                                    if (serviceMsg != null && !serviceMsg.equals(""))
                                        Boast.makeText(SplashActivity.this, serviceMsg).show();
                                    new LoginPresenter(SplashActivity.this).login(userID, userPw);
                                } else {
                                    onLoginFail(serviceMsg);
                                }
                            } else {
                                onLoginFail(serviceMsg);
                            }
                        }

                        @Override
                        public void onError(String errorMessage, String serviceMsg) {
                            if (serviceMsg != null && !serviceMsg.equals(""))
                                Boast.makeText(SplashActivity.this, serviceMsg).show();
                            startActivity(new Intent(context, DisconnectActivity.class));
                            finish();
                        }
                    });
                } else {
                    startActivity(new Intent(context, DisconnectActivity.class));
                    finish();
                    //Go to DisconnectActivity
                }

            }
        }, Constant.SPLASH_TIME);
    }

    //add by Quan 16/01 check network
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onLoginSuccess(String serviceMsg) {
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(SplashActivity.this, serviceMsg).show();
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }

    @Override
    public void onLoginFail(String serviceMsg) {
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(SplashActivity.this, serviceMsg).show();
        startActivity(new Intent(context, LoginActivity.class));
        finish();
    }
}
