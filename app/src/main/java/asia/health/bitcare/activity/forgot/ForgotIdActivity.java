package asia.health.bitcare.activity.forgot;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseActivity;
import asia.health.bitcare.dialog.DialogInfo;
import asia.health.bitcare.mvp.presenter.ForgotIdPresenter;
import asia.health.bitcare.mvp.view.ForgotIdView;
import asia.health.bitcare.prefs.DateTimeFormat;
import asia.health.bitcare.utils.DatetimeHelper;
import asia.health.bitcare.utils.StringUtils;
import asia.health.bitcare.widget.toast.Boast;

public class ForgotIdActivity extends BaseActivity implements ForgotIdView {

    private LinearLayout vgName;
    private LinearLayout vgBirthday;
    private EditText edtName, edtBirthday;
    private ForgotIdPresenter presenter;
    private EditText edtPhoneNo1, edtPhoneNo2, edtPhoneNo3;

    @Override
    public int getView() {
        return R.layout.activity_forgot_id;
    }

    @Override
    public void initView() {

        vgName = (LinearLayout) findViewById(R.id.vgName);
        vgBirthday = (LinearLayout) findViewById(R.id.vgBirthday);

        ((TextView) vgName.findViewById(R.id.tvTitle)).setText(R.string.cap_name);
        ((TextView) vgBirthday.findViewById(R.id.tvTitle)).setText(R.string.cap_dateofbirth);

        edtName = (EditText) vgName.findViewById(R.id.edtInput);
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
                    presenter.getId(edtName.getText().toString().trim(),
                            StringUtils.formatDataSimple(edtBirthday.getText().toString().trim()),
                            edtPhoneNo1.getText().toString().trim(),
                            edtPhoneNo2.getText().toString().trim(),
                            edtPhoneNo3.getText().toString().trim());
                } else {
                    new DialogInfo.Build(ForgotIdActivity.this).
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
        presenter = new ForgotIdPresenter(this);
    }

    @Override
    public void initAction() {

    }

    @Override
    public void onResponse(String userID, String serviceMsg) {
        dismissProgressDialog();
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(this, serviceMsg).show();
        new DialogInfo.Build(this).
                setTitle(context.getString(R.string.forgot_id_check))
                .setMessage(context.getString(R.string.forgot_id_response).replace("$$$", userID))
                .show();
    }

    @Override
    public void onError(String serviceMsg) {
        dismissProgressDialog();
        if (serviceMsg != null && !serviceMsg.equals(""))
            new DialogInfo.Build(this).
                    setTitle(context.getString(R.string.forgot_id_check))
                    .setMessage(serviceMsg)
                    .show();
    }

    public boolean validateEdittext() {
        if (edtName.getText().toString().trim().equals("")) {
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
