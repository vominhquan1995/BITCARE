package asia.health.bitcare.activity.signup;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseToolbarActivity;
import asia.health.bitcare.dialog.DialogInfo;
import asia.health.bitcare.model.User;
import asia.health.bitcare.mvp.presenter.RegistrationInitPresenter;
import asia.health.bitcare.mvp.view.RegistrationInitView;
import asia.health.bitcare.prefs.DateTimeFormat;
import asia.health.bitcare.utils.DatetimeHelper;
import asia.health.bitcare.utils.StringUtils;
import asia.health.bitcare.utils.SystemHelper;
import asia.health.bitcare.widget.toast.Boast;

public class RegistrationInitActivity extends BaseToolbarActivity implements RegistrationInitView {
    private final String TAG = getClass().getSimpleName();
    private LinearLayout vgName;
    private LinearLayout vgGenger;
    private LinearLayout vgBirthday;
    private RadioGroup rgGender;
    private EditText edtName, edtBirthday;
    private EditText edtPhoneNo1, edtPhoneNo2, edtPhoneNo3;
    private RegistrationInitPresenter presenter;
    private String gender = User.MALE;

    @Override
    public int getView() {
        return R.layout.activity_registration_init;
    }

    @Override
    public void initView() {
        super.initView();

        vgName = (LinearLayout) findViewById(R.id.vgName);
        vgGenger = (LinearLayout) findViewById(R.id.vgGenger);
        vgBirthday = (LinearLayout) findViewById(R.id.vgBirthday);

        rgGender = (RadioGroup) vgGenger.findViewById(R.id.rgChoice);

        ((TextView) vgName.findViewById(R.id.tvTitle)).setText(R.string.cap_name);
        ((TextView) vgBirthday.findViewById(R.id.tvTitle)).setText(R.string.cap_dateofbirth);

        edtName = (EditText) vgName.findViewById(R.id.edtInput);
        edtBirthday = (EditText) vgBirthday.findViewById(R.id.edtInput);
        edtPhoneNo1 = (EditText) findViewById(R.id.edtPhoneNo1);
        edtPhoneNo2 = (EditText) findViewById(R.id.edtPhoneNo2);
        edtPhoneNo3 = (EditText) findViewById(R.id.edtPhoneNo3);


        DatetimeHelper.addFocusDatePicker(edtBirthday, DateTimeFormat.DATE_FORMAT,
                getString(R.string.up_save), getString(R.string.up_cancel), false, new DatetimeHelper.OnDateSeletionListener() {
                    @Override
                    public void onDateSelected(Calendar dateVal, String dateStr) {
                        edtPhoneNo1.requestFocus();
                    }
                });

        editTextListener();
    }

    private void editTextListener() {
        edtName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    edtBirthday.requestFocus();
                }
                return false;
            }
        });

        edtPhoneNo1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 3) {
                    edtPhoneNo2.requestFocus();
                }
            }
        });

        edtPhoneNo2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    edtPhoneNo3.requestFocus();
                }
            }
        });

        edtPhoneNo3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    SystemHelper.hideKeyboard(context);
                }
            }
        });
    }

    @Override
    public void initValue() {
        presenter = new RegistrationInitPresenter(this);
    }

    @Override
    public void initAction() {
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "checkedId : " + checkedId);
                if (checkedId == R.id.rdo1)
                    gender = User.MALE;
                else
                    gender = User.FEMALE;
            }
        });
        findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNull() == null) {
                    showProgressDialog();
                    presenter.registrationInit(
                            edtName.getText().toString().trim(),
                            gender,
                            StringUtils.formatDataSimple(edtBirthday.getText().toString().trim()),
                            edtPhoneNo1.getText().toString().trim(),
                            edtPhoneNo2.getText().toString().trim(),
                            edtPhoneNo3.getText().toString().trim()
                    );
                } else {
                    new DialogInfo.Build(RegistrationInitActivity.this).
                            setTitle(context.getString(R.string.cap_warning))
                            .setMessage(checkNull()).show();
                }

            }
        });
    }

    @Override
    public void onRegistrationInitSuccess(String serviceMsg) {
        dismissProgressDialog();
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(this, serviceMsg).show();
        Intent intent = new Intent(getBaseContext(), RegistrationActivity.class);
        intent.putExtra("USERNM", edtName.getText().toString().trim());
        intent.putExtra("GENDER", gender);
        intent.putExtra("USERBTDATE", StringUtils.formatDataSimple(edtBirthday.getText().toString().trim()));
        intent.putExtra("NUMBERPHONE1", edtPhoneNo1.getText().toString().trim());
        intent.putExtra("NUMBERPHONE2", edtPhoneNo2.getText().toString().trim());
        intent.putExtra("NUMBERPHONE3", edtPhoneNo3.getText().toString().trim());
        startActivity(intent);
    }

    @Override
    public void onRegistrationInitFail(String errorMessage) {
        dismissProgressDialog();
        Boast.makeText(this, errorMessage).show();
    }

    public String checkNull() {
        String errorMessage = null;
        if (edtName.getText().toString().trim().equals("")) {
            errorMessage = "Please input user name";
        } else if (edtBirthday.getText().toString().equals("")) {
            errorMessage = "Please input birthday";
        } else if (edtPhoneNo1.getText().toString().equals("") || edtPhoneNo2.getText().toString().equals("")
                || edtPhoneNo3.getText().toString().equals("")) {
            errorMessage = "Please input number phone";
        }
        return errorMessage;
    }
}
