package asia.health.bitcare.activity.forgot;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseActivity;
import asia.health.bitcare.dialog.DialogChangePass;
import asia.health.bitcare.dialog.DialogInfo;
import asia.health.bitcare.mvp.presenter.ForgotPasswordPresenter;
import asia.health.bitcare.mvp.view.ForgotPasswordView;
import asia.health.bitcare.prefs.DateTimeFormat;
import asia.health.bitcare.utils.DatetimeHelper;
import asia.health.bitcare.utils.StringUtils;
import asia.health.bitcare.widget.toast.Boast;

public class ForgotPasswordActivity extends BaseActivity implements ForgotPasswordView {

    private LinearLayout vgID;
    private LinearLayout vgBirthday;
    private EditText edtID, edtBirthday;
    private EditText edtPhoneNo1, edtPhoneNo2, edtPhoneNo3;
    private ForgotPasswordPresenter presenter;

    @Override
    public int getView() {
        return R.layout.activity_forgot_password;
    }

    @Override
    public void initView() {

        vgID = (LinearLayout) findViewById(R.id.vgID);

        vgBirthday = (LinearLayout) findViewById(R.id.vgBirthday);

        ((TextView) vgID.findViewById(R.id.tvTitle)).setText(R.string.up_ID);
        ((TextView) vgBirthday.findViewById(R.id.tvTitle)).setText(R.string.cap_dateofbirth);

        edtID = (EditText) vgID.findViewById(R.id.edtInput);
        edtBirthday = (EditText) vgBirthday.findViewById(R.id.edtInput);
        edtPhoneNo1 = (EditText) findViewById(R.id.edtPhoneNo1);
        edtPhoneNo2 = (EditText) findViewById(R.id.edtPhoneNo2);
        edtPhoneNo3 = (EditText) findViewById(R.id.edtPhoneNo3);

        DatetimeHelper.addOnClickDatePicker(edtBirthday, DateTimeFormat.DATE_FORMAT,
                getString(R.string.up_save), getString(R.string.up_cancel), false, null);

        findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEdittext()) {
                    showProgressDialog();
                    presenter.getPassword(edtID.getText().toString().trim(),
                            StringUtils.formatDataSimple(edtBirthday.getText().toString().trim()),
                            edtPhoneNo1.getText().toString().trim(),
                            edtPhoneNo2.getText().toString().trim(),
                            edtPhoneNo3.getText().toString().trim());
                } else {
                    new DialogInfo.Build(ForgotPasswordActivity.this).
                            setTitle(context.getString(R.string.cap_warning))
                            .setMessage(context.getString(R.string.cap_message_warning)).show();
                }

            }
        });
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void initValue() {
        presenter = new ForgotPasswordPresenter(this);
    }

    @Override
    public void initAction() {

    }

    @Override
    public void onGetUserNumSuccess(final int userNum, String serviceMsg) {
        dismissProgressDialog();
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(this, serviceMsg).show();
        new DialogChangePass.Build(this)
                .setOnChangePassListener(new DialogChangePass.Build.OnChangePassListener() {
                    @Override
                    public void onConfirm(String newPass) {
                        showProgressDialog();
                        presenter.modifyPassword(String.valueOf(userNum), newPass);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
    }

    @Override
    public void onError(String serviceMsg) {
        dismissProgressDialog();
        if (serviceMsg != null && !serviceMsg.equals(""))
            new DialogInfo.Build(ForgotPasswordActivity.this).
                    setTitle(context.getString(R.string.cap_warning))
                    .setMessage(serviceMsg).show();
    }

    @Override
    public void onModifyPasswordSuccess(String serviceMsg) {
        dismissProgressDialog();
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(this, serviceMsg).show();
        onBackPressed();
        Boast.makeText(ForgotPasswordActivity.this, "Confirm new password complete").show();
    }

    public boolean validateEdittext() {
        if (edtID.getText().toString().trim().equals("")) {
            return false;
        } else if (edtBirthday.getText().toString().equals("")) {
            return false;
        } else if (edtPhoneNo1.getText().toString().equals("")) {
            return false;
        } else if (edtPhoneNo2.getText().toString().equals("")) {
            return false;
        } else if (edtPhoneNo2.getText().toString().equals("")) {
            return false;
        }
        return true;
    }
}
