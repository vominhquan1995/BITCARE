package asia.health.bitcare.fragment;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseFragment;
import asia.health.bitcare.dialog.DialogInfo;
import asia.health.bitcare.model.User;
import asia.health.bitcare.mvp.presenter.UserInfoPresenter;
import asia.health.bitcare.mvp.view.UserInfoView;
import asia.health.bitcare.utils.DateTimeUtils;
import asia.health.bitcare.widget.toast.Boast;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends BaseFragment implements View.OnClickListener, UserInfoView {
    private LinearLayout vgID;
    private LinearLayout vgName;
    private LinearLayout vgGender;
    private LinearLayout vgHeight;
    private LinearLayout vgWeight;
    private LinearLayout vgDate;

    private EditText edtUserID;
    private EditText edtUserName;
    private EditText edtUserWeight;
    private EditText edtUserHeight;
    private EditText edtUserDate;
    private EditText edtGender;

    private EditText edtPhoneNo1;
    private EditText edtPhoneNo2;
    private EditText edtPhoneNo3;

    private Button btnCancel;
    private Button btnConfirm;

    private UserInfoPresenter presenter;
    // private RadioGroup rgGender;

    @Override
    public int setFragmentView() {
        return R.layout.fragment_user_info;
    }

    @Override
    public void initView() {
        vgID = (LinearLayout) view.findViewById(R.id.vgID);
        vgName = (LinearLayout) view.findViewById(R.id.vgName);
        vgGender = (LinearLayout) view.findViewById(R.id.vgGender);
        vgWeight = (LinearLayout) view.findViewById(R.id.vgWeight);
        vgHeight = (LinearLayout) view.findViewById(R.id.vgHeight);
        vgDate = (LinearLayout) view.findViewById(R.id.vgDate);
        edtPhoneNo1 = (EditText) view.findViewById(R.id.edtPhoneNo1);
        edtPhoneNo2 = (EditText) view.findViewById(R.id.edtPhoneNo2);
        edtPhoneNo3 = (EditText) view.findViewById(R.id.edtPhoneNo3);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);

        edtUserID = ((EditText) vgID.findViewById(R.id.edtInput));
        edtUserName = ((EditText) vgName.findViewById(R.id.edtInput));
        edtUserWeight = ((EditText) vgWeight.findViewById(R.id.edtInput));
        edtUserHeight = ((EditText) vgHeight.findViewById(R.id.edtInput));
        edtUserDate = ((EditText) vgDate.findViewById(R.id.edtInput));
        edtGender = ((EditText) vgGender.findViewById(R.id.edtInput));
    }

    @Override
    public void initValue() {
        presenter = new UserInfoPresenter(this);
        ((TextView) vgID.findViewById(R.id.tvTitle)).setText(R.string.id_email);
        ((TextView) vgName.findViewById(R.id.tvTitle)).setText(R.string.cap_name);
        ((TextView) vgGender.findViewById(R.id.tvTitle)).setText(R.string.cap_sex);
        ((TextView) vgWeight.findViewById(R.id.tvTitle)).setText(R.string.cap_weight);
        ((TextView) vgWeight.findViewById(R.id.tvUnit)).setText(R.string.unit_kg);
        ((TextView) vgHeight.findViewById(R.id.tvTitle)).setText(R.string.cap_height);
        ((TextView) vgHeight.findViewById(R.id.tvUnit)).setText(R.string.unit_cm);
        ((TextView) vgDate.findViewById(R.id.tvTitle)).setText(R.string.cap_dateofbirth);
        edtUserID.setText(User.get().getUserId());
        edtUserID.setFocusable(false);
        edtUserID.setLongClickable(false);
        edtUserName.setText(User.get().getUserNm());
        edtUserName.setFocusable(false);
        edtUserName.setLongClickable(false);
        edtUserWeight.setText(String.valueOf(User.get().getWeight()));
        edtUserWeight.setFocusable(false);
        edtUserWeight.setLongClickable(false);
        edtUserHeight.setText(String.valueOf(User.get().getHeight()));
        edtUserDate.setText(DateTimeUtils.getDate(User.get().getUserBtDate()));
        edtUserDate.setFocusable(false);
        edtUserDate.setLongClickable(false);
        edtPhoneNo1.setText(User.get().getMPhoneNo1());
        edtPhoneNo2.setText(User.get().getMPhoneNo2());
        edtPhoneNo3.setText(User.get().getMPhoneNo3());
        Log.d("Gender", User.get().getGender().toString());
        if (User.get().getGender().toString().equals(User.MALE))
            edtGender.setText(R.string.cap_male);
        else edtGender.setText(R.string.cap_female);
        edtGender.setFocusable(false);
        edtGender.setLongClickable(false);
    }

    @Override
    public void initAction() {
        /* hide by Quan 18/01
        DatetimeHelper.addOnClickDatePicker(((EditText) vgDate.findViewById(R.id.edtInput)),
                DateTimeFormat.DATE_FORMAT_2, getString(R.string.up_save), getString(R.string.up_cancel), false);
        */
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                onBackPressed();
                break;
            case R.id.btnConfirm:
                if (checkValueInput() != null) {
                    new DialogInfo.Build(getMainActivity())
                            .setTitle(context.getString(R.string.cap_warning))
                            .setMessage(checkValueInput())
                            .show();
                } else {
                    showProgressDialog();
                    presenter.modifyUser(
                            edtUserName.getText().toString().trim(),
                            edtGender.getText().equals(context.getString(R.string.cap_male)) ? User.MALE : User.FEMALE,
                            edtUserHeight.getText().toString().trim(),
                            edtUserWeight.getText().toString().trim(),
                            edtUserDate.getText().toString().replace("-", "").trim(),
                            edtPhoneNo1.getText().toString().trim(),
                            edtPhoneNo2.getText().toString().trim(),
                            edtPhoneNo3.getText().toString().trim()
                    );
                    User.get().setHeight(Double.valueOf(edtUserHeight.getText().toString()));
                }
                break;
        }
    }

    @Override
    public void onModifySuccess(String serviceMsg) {
        dismissProgressDialog();
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(getMainActivity(), serviceMsg).show();
        onBackPressed();
    }

    @Override
    public void onModifyError(String serviceMsg) {
        dismissProgressDialog();
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(getMainActivity(), serviceMsg).show();
    }

    public String checkValueInput() {
        String mess = null;
        if (edtUserHeight.getText().toString().equals("")) {
            mess = "Please input your height";
        } else if (edtPhoneNo1.getText().toString().equals("") ||
                edtPhoneNo2.getText().toString().equals("") ||
                edtPhoneNo3.getText().toString().equals("")) {
            mess = "Please input your number phone";
        }
        return mess;
    }
}
