package asia.health.bitcare.activity.signup;

import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.health.bitcare.R;
import asia.health.bitcare.activity.LoginActivity;
import asia.health.bitcare.base.BaseToolbarActivity;
import asia.health.bitcare.dialog.DialogInfo;
import asia.health.bitcare.model.User;
import asia.health.bitcare.mvp.presenter.RegistrationPresenter;
import asia.health.bitcare.mvp.view.RegistrationView;
import asia.health.bitcare.utils.DateTimeUtils;
import asia.health.bitcare.utils.StringUtils;
import asia.health.bitcare.widget.toast.Boast;

public class RegistrationActivity extends BaseToolbarActivity implements RegistrationView, View.OnClickListener {

    private LinearLayout vgID;
    private LinearLayout vgPassword;
    private LinearLayout vgPassConfirm;
    private LinearLayout vgHeight;
    private LinearLayout vgWeight;
    private LinearLayout vgName;
    private LinearLayout vgGenger;
    private LinearLayout vgBirthday;
    private LinearLayout vgPhoneNumber;
    private LinearLayout vgCheck;
    private CheckBox cbCheck;
    private Button btnCheckID;
    private String phone1, phone2, phone3;
    private EditText edtID, edtPassword, edtPassConfirm, edtHeight, edtWeigth, edtName, edtBirthday, edtPhoneNumber, edtGender;
    private RegistrationPresenter presenter;
    private boolean checkIDSuccess;

    @Override
    public int getView() {
        return R.layout.activity_registration;
    }

    @Override
    public void initView() {
        super.initView();
        vgID = (LinearLayout) findViewById(R.id.vgID);
        vgPassword = (LinearLayout) findViewById(R.id.vgPassword);
        vgPassConfirm = (LinearLayout) findViewById(R.id.vgPasswordConfirm);
        vgHeight = (LinearLayout) findViewById(R.id.vgHeight);
        vgWeight = (LinearLayout) findViewById(R.id.vgWeight);
        vgName = (LinearLayout) findViewById(R.id.vgName);
        vgGenger = (LinearLayout) findViewById(R.id.vgGenger);
        vgBirthday = (LinearLayout) findViewById(R.id.vgBirthday);
        vgPhoneNumber = (LinearLayout) findViewById(R.id.vgPhoneNumber);
        vgCheck = (LinearLayout) findViewById(R.id.vgCheck);

        ((TextView) vgID.findViewById(R.id.tvTitle)).setText(R.string.up_ID);
        ((TextView) vgPassword.findViewById(R.id.tvTitle)).setText(R.string.cap_password);
        ((TextView) vgPassConfirm.findViewById(R.id.tvTitle)).setText(R.string.cap_confirmpassword);
        ((TextView) vgGenger.findViewById(R.id.tvTitle)).setText(R.string.cap_genger);
        ((TextView) vgHeight.findViewById(R.id.tvTitle)).setText(R.string.cap_height);
        ((TextView) vgHeight.findViewById(R.id.tvUnit)).setText(R.string.unit_cm);
        ((TextView) vgWeight.findViewById(R.id.tvTitle)).setText(R.string.cap_weight);
        ((TextView) vgWeight.findViewById(R.id.tvUnit)).setText(R.string.unit_kg);
        ((TextView) vgName.findViewById(R.id.tvTitle)).setText(R.string.cap_name);
        ((TextView) vgBirthday.findViewById(R.id.tvTitle)).setText(R.string.cap_dateofbirth);
        ((TextView) vgPhoneNumber.findViewById(R.id.tvTitle)).setText(R.string.cap_mobilenumber);

        edtID = (EditText) vgID.findViewById(R.id.edtInput);
        edtPassword = (EditText) vgPassword.findViewById(R.id.edtInput);
        edtPassConfirm = (EditText) vgPassConfirm.findViewById(R.id.edtInput);
        edtGender = (EditText) vgGenger.findViewById(R.id.edtInput);
        edtHeight = (EditText) vgHeight.findViewById(R.id.edtInput);
        edtWeigth = (EditText) vgWeight.findViewById(R.id.edtInput);
        edtName = (EditText) vgName.findViewById(R.id.edtInput);
        edtBirthday = (EditText) vgBirthday.findViewById(R.id.edtInput);
        edtPhoneNumber = (EditText) vgPhoneNumber.findViewById(R.id.edtInput);
        cbCheck = (CheckBox) vgCheck.findViewById(R.id.cbCheck);
        //show value form init registration
        edtName.setText(getIntent().getStringExtra("USERNM"));
        phone1 = getIntent().getStringExtra("NUMBERPHONE1");
        phone2 = getIntent().getStringExtra("NUMBERPHONE2");
        phone3 = getIntent().getStringExtra("NUMBERPHONE3");
        edtPhoneNumber.setText(phone1 + "-" + phone2 + "-" + phone3);
        if (getIntent().getStringExtra("GENDER").equals(User.MALE))
            edtGender.setText(R.string.cap_male);
        else edtGender.setText(R.string.cap_female);
        edtBirthday.setText(DateTimeUtils.getDate(getIntent().getStringExtra("USERBTDATE")));
        //set value input
        edtWeigth.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtWeigth.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtHeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        //block input text
        edtName.setFocusable(false);
        edtName.setLongClickable(false);
        edtPhoneNumber.setFocusable(false);
        edtPhoneNumber.setLongClickable(false);
        edtBirthday.setFocusable(false);
        edtBirthday.setLongClickable(false);
        edtGender.setFocusable(false);
        edtGender.setLongClickable(false);
        findViewById(R.id.btnViewTerms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, TermsActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        btnCheckID = (Button) findViewById(R.id.btn_check_id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Boolean result = data.getBooleanExtra("CHECK", false);
            if (result) {
                cbCheck.setChecked(true);
            } else {
                cbCheck.setChecked(false);
            }
        }
    }

    @Override
    public void initValue() {
        presenter = new RegistrationPresenter(this);
        checkIDSuccess = false;
    }

    @Override
    public void initAction() {
        findViewById(R.id.btnSignUp).setOnClickListener(this);
        btnCheckID.setOnClickListener(this);
    }

    @Override
    public void onRegistrationSuccess(String serviceMsg) {
        dismissProgressDialog();
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(this, serviceMsg).show();
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        intent.putExtra("USERID", edtID.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onRegistrationFail(String errorMessage) {
        dismissProgressDialog();
        Boast.makeText(this, errorMessage).show();
    }

    @Override
    public void onUserNotExist() {
        btnCheckID.setBackgroundColor(getResources().getColor(R.color.list_even_row));
        btnCheckID.setTextColor(getResources().getColor(R.color.black));
        btnCheckID.setEnabled(false);
        edtID.setEnabled(false);
        dismissProgressDialog();
        //check id success
        checkIDSuccess = true;
    }

    @Override
    public void onUserExist(String serviceMsg) {
        //check id exist
        checkIDSuccess = false;
        btnCheckID.setBackgroundColor(getResources().getColor(R.color.color_dark_orange));
        btnCheckID.setTextColor(getResources().getColor(R.color.colorWhite));
        dismissProgressDialog();
        Boast.makeText(this, getResources().getString(R.string.id_duplication)).show();
        edtID.setText("");
        edtID.setFocusable(true);
    }

    public String checkNull() {
        String errorMessage = null;
        if (edtPassword.getText().toString().equals("")) {
            errorMessage = "Please input password";
        } else if (edtPassword.length() < 4) {
            errorMessage = "Password input 4 digits";
        } else if (edtPassConfirm.getText().toString().equals("")) {
            errorMessage = "Please confirm password";
        } else if (!edtPassConfirm.getText().toString().equals(edtPassword.getText().toString())) {
            errorMessage = "Confirm password not match";
        } else if (edtHeight.getText().toString().equals("")) {
            errorMessage = "Please input height";
        } else if (edtWeigth.getText().toString().equals("")) {
            errorMessage = "Please input weight";
        } else if (!cbCheck.isChecked()) {
            errorMessage = "Please acpept with terms of use";
        }
        return errorMessage;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_id:
                if (!edtID.getText().toString().trim().equals("")) {
                    showProgressDialog();
                    presenter.checkID(edtID.getText().toString().trim());
                } else {
                    Boast.makeText(this, getResources().getString(R.string.cap_request_id_input)).show();
                }
                break;
            case R.id.btnSignUp:
                if (checkIDSuccess) {
                    if (checkNull() == null) {
                        presenter.registration(
                                edtID.getText().toString().trim(),
                                edtPassword.getText().toString().trim(),
                                edtName.getText().toString().trim(),
                                edtGender.getText().toString().equals(context.getString(R.string.cap_male)) ? User.MALE : User.FEMALE,
                                edtHeight.getText().toString().trim(),
                                edtWeigth.getText().toString().trim(),
                                StringUtils.formatDataSimple(edtBirthday.getText().toString().trim()),
                                phone1,
                                phone2,
                                phone3
                        );
                    } else {
                        new DialogInfo.Build(RegistrationActivity.this).
                                setTitle(context.getString(R.string.cap_warning))
                                .setMessage(checkNull()).show();
                    }
                } else {
                    Boast.makeText(this, getResources().getString(R.string.msg_request_check_id)).show();
                }
                break;
        }
    }
}
