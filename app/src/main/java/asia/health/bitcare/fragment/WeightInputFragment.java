package asia.health.bitcare.fragment;


import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseFragment;
import asia.health.bitcare.dialog.DialogInfo;
import asia.health.bitcare.fragment.WeightFragment.OnForwardData;
import asia.health.bitcare.model.Weight;
import asia.health.bitcare.mvp.presenter.WeightInputPresenter;
import asia.health.bitcare.mvp.view.WeightInputView;
import asia.health.bitcare.prefs.DateTimeFormat;
import asia.health.bitcare.utils.DateTimeUtils;
import asia.health.bitcare.utils.DatetimeHelper;
import asia.health.bitcare.utils.StringUtils;
import asia.health.bitcare.utils.SystemHelper;
import asia.health.bitcare.widget.toast.Boast;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeightInputFragment extends BaseFragment implements View.OnClickListener,
        WeightInputView {

    public static WeightInputFragmentCallBack mCallBack;
    private Weight weight;
    private WeightInputPresenter presenter;
    private Button btnDateTime;
    private LinearLayout vgWeight;
    private LinearLayout vgInputTime;
    private LinearLayout vgButton;
    private LinearLayout lvPickerDateTime;
    private Button btnCancel;
    private Button btnConfirm;
    private EditText edtWeight;
    private TextView txtDate, txtDayOfWeek, txtTime;


    @Override
    public int setFragmentView() {
        return R.layout.fragment_weight_input_2;
    }

    @Override
    public void initView() {
        vgWeight = (LinearLayout) view.findViewById(R.id.vgWeight);
        edtWeight = (EditText) vgWeight.findViewById(R.id.edtInput);
        btnDateTime = (Button) view.findViewById(R.id.btnDateTime);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        vgInputTime = (LinearLayout) view.findViewById(R.id.vgInputTime);
        vgButton = (LinearLayout) view.findViewById(R.id.vgButton);
        lvPickerDateTime = (LinearLayout) vgInputTime.findViewById(R.id.lvPickerDateTime);
        txtTime = (TextView) vgInputTime.findViewById(R.id.txtTime);
        txtDate = (TextView) vgInputTime.findViewById(R.id.txtDate);
        txtDayOfWeek = (TextView) vgInputTime.findViewById(R.id.txtDayOfWeek);
        edtWeight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        setFocusEditText(edtWeight, null, null, null);
    }

    @Override
    public void initValue() {
        WeightFragment.setOnForwardData(new OnForwardData() {
            @Override
            public void onForward(Weight weight) {
                edtWeight.setText(String.valueOf(weight.getWeight()));
            }
        });

        presenter = new WeightInputPresenter(this);
        ((TextView) vgWeight.findViewById(R.id.tvTitle)).setText(R.string.cap_weight);
        ((TextView) vgWeight.findViewById(R.id.tvUnit)).setText(R.string.unit_kg);
    }

    @Override
    public void initAction() {
        lvPickerDateTime.setOnClickListener(this);
        DatetimeHelper.addOnClickDatePicker(lvPickerDateTime, txtDate, txtDayOfWeek, txtTime, DateTimeFormat.DATE_TIME_FORMAT,
                getString(R.string.up_save), getString(R.string.up_cancel), true);
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
                if (validateEdittext()) {
                    showProgressDialog();
                    try {
                        weight = new Weight();
                        weight.setBMI(Double.parseDouble(edtWeight.getText().toString().trim()));
                        weight.setWeight(Double.parseDouble(edtWeight.getText().toString().trim()));
                        weight.setMsDate(StringUtils.formatDataSimple(txtDate.getText().toString().trim() + DateTimeUtils.parseTime12to24(txtTime.getText().toString().trim(), txtDayOfWeek.getText().toString())));
                        presenter.addWeight(weight);
                    } catch (Exception e) {
                        e.printStackTrace();
                        dismissProgressDialog();
                        Boast.makeText(getMainActivity(), e.getMessage()).show();
                    }
                } else {
                    new DialogInfo.Build(getActivity()).
                            setTitle(context.getString(R.string.cap_warning))
                            .setMessage(context.getString(R.string.cap_message_warning)).show();
                }
                break;
        }
    }

    @Override
    public void onAddBloodPressureSuccess(String serviceMsg) {
        dismissProgressDialog();

        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(getMainActivity(), serviceMsg).show();
        mCallBack.onAddNew(weight);
        // disable keyboard
        SystemHelper.hideKeyboard(context);
        onBackPressed();

    }

    @Override
    public void onError(String errorMessage) {
        dismissProgressDialog();
        Boast.makeText(getMainActivity(), errorMessage).show();
    }

    public boolean validateEdittext() {
        if (edtWeight.getText().toString().trim().equals("")) {
            return false;
        }
        return true;
    }

    public interface WeightInputFragmentCallBack {

        void onAddNew(Weight weight);
    }
}
