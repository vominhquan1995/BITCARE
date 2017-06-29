package asia.health.bitcare.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.health.bitcare.R;
import asia.health.bitcare.activity.forgot.ForgotIdActivity;
import asia.health.bitcare.activity.forgot.ForgotPasswordActivity;
import asia.health.bitcare.activity.signup.RegistrationInitActivity;
import asia.health.bitcare.animation.GifView;
import asia.health.bitcare.base.BaseActivity;
import asia.health.bitcare.dialog.DialogInfo;
import asia.health.bitcare.mvp.presenter.LoginPresenter;
import asia.health.bitcare.mvp.view.LoginView;
import asia.health.bitcare.prefs.Constant;
import asia.health.bitcare.utils.SystemHelper;
import asia.health.bitcare.utils.TouchEffect;
import asia.health.bitcare.widget.toast.Boast;

/**
 * Created by HP on 20-Dec-16.
 */

public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private GifView gifView;
    private int[] dataBitmapList;
    private EditText edtId;
    private EditText edtPassword;
    private LinearLayout btnForgotId;
    private LinearLayout btnForgotPass;
    private Button btnSignUp;
    private Button btnLogin;
    private CheckBox chkAutoLogin;
    private boolean autoLogin = false;
    private LoginPresenter loginPresenter;

    @Override
    public int getView() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        //gifView = (GifView) findViewById(R.id.gifView);
        chkAutoLogin = (CheckBox) findViewById(R.id.cbAuto);
        edtId = (EditText) findViewById(R.id.edtId);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        TouchEffect.addAlpha(btnSignUp);
        btnForgotId = (LinearLayout) findViewById(R.id.btnForgotId);
        TouchEffect.addAlpha(btnForgotId);
        btnForgotPass = (LinearLayout) findViewById(R.id.btnForgotPass);
        TouchEffect.addAlpha(btnForgotPass);
        try {
            edtId.setText(getIntent().getStringExtra("USERID"));
        } catch (Exception ex) {

        }
    }

    @Override
    public void initValue() {
        loginPresenter = new LoginPresenter(this);
        dataBitmapList = new int[]{
                R.drawable.main_section_alert_goalani_1,
                R.drawable.main_section_alert_goalani_2,
                R.drawable.main_section_alert_goalani_3,
                R.drawable.main_section_alert_goalani_4,
                R.drawable.main_section_alert_goalani_5,
                R.drawable.main_section_alert_goalani_6,
                R.drawable.main_section_alert_goalani_7,
                R.drawable.main_section_alert_goalani_8,
                R.drawable.main_section_alert_goalani_9,
                R.drawable.main_section_alert_goalani_10,
                R.drawable.main_section_alert_goalani_11,
                R.drawable.main_section_alert_goalani_12,
                R.drawable.main_section_alert_goalani_13,
                R.drawable.main_section_alert_goalani_14,
                R.drawable.main_section_alert_goalani_15,
                R.drawable.main_section_alert_goalani_16,
                R.drawable.main_section_alert_goalani_17,
                R.drawable.main_section_alert_goalani_18
        };
    }

    @Override
    public void initAction() {
        btnForgotId.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        chkAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    autoLogin = true;
                } else {
                    autoLogin = false;
                }
            }
        });

        //gifView.setDataList(dataBitmapList);
        //gifView.start();
    }

    private void login() {
        showProgressDialog();
        loginPresenter.login(edtId.getText().toString(), edtPassword.getText().toString());
        SystemHelper.hideKeyboard(context);
    }

    @Override
    public void onLoginSuccess(final String serviceMsg) {
        loginPresenter.checkHealthInfo(new LoginPresenter.OnCheckHeathInfo() {
            @Override
            public void onAnswered() {
                dismissProgressDialog();
                if (serviceMsg != null && !serviceMsg.equals(""))
                    Boast.makeText(LoginActivity.this, serviceMsg).show();
                checkAutoLogin();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
            @Override
            public void onNull() {
                if (serviceMsg != null && !serviceMsg.equals(""))
                    Boast.makeText(LoginActivity.this, serviceMsg).show();
                checkAutoLogin();
                edtId.setText("");
                edtPassword.setText("");
                dismissProgressDialog();
                startActivity(new Intent(LoginActivity.this, InvestigateActivity.class));
            }

            @Override
            public void onError() {
                dismissProgressDialog();
                Boast.makeText(LoginActivity.this, getString(R.string.error_incorrect_login_information)).show();
                autoLogin = false;
                checkAutoLogin();
            }
        });
    }

    /**
     * Save auto login data
     */
    private void checkAutoLogin() {
        //Save auto login
        sharedPreferences.edit().putBoolean(Constant.AUTO_LOGIN, autoLogin).apply();
        if (autoLogin) {
            sharedPreferences.edit().putString(Constant.USER_ID, edtId.getText().toString()).apply();
            sharedPreferences.edit().putString(Constant.USER_PW, edtPassword.getText().toString()).apply();
        } else {
            sharedPreferences.edit().putString(Constant.USER_ID, "").apply();
            sharedPreferences.edit().putString(Constant.USER_PW, "").apply();
        }
    }

    @Override
    public void onLoginFail(String serviceMsg) {
        dismissProgressDialog();
        Boast.makeText(this, serviceMsg).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnForgotId:
                startActivity(new Intent(context, ForgotIdActivity.class));
                break;
            case R.id.btnForgotPass:
                startActivity(new Intent(context, ForgotPasswordActivity.class));
                break;
            case R.id.btnSignUp:
                startActivity(new Intent(context, RegistrationInitActivity.class));
                break;
            case R.id.btnLogin:
                if(validateEdittext()){
                    login();
                }else{
                        new DialogInfo.Build(LoginActivity.this).
                                setTitle(context.getString(R.string.cap_warning))
                                .setMessage(context.getString(R.string.cap_message_warning)).show();
                }
                break;
        }
    }
    public boolean validateEdittext() {
        if (edtId.getText().toString().trim().equals("")) {
            return false;
        } else if (edtPassword.getText().toString().equals("")) {
            return false;
        }
        return true;
    }
}
