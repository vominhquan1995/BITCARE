package asia.health.bitcare.fragment;


import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseFragment;
import asia.health.bitcare.bluetooth.ANDManager;
import asia.health.bitcare.dialog.DialogInfo;
import asia.health.bitcare.fragment.BloodPressureFragment.OnForwardData;
import asia.health.bitcare.model.BloodPressure;
import asia.health.bitcare.model.User;
import asia.health.bitcare.mvp.presenter.BloodPressureInputPresenter;
import asia.health.bitcare.mvp.view.BloodPressureInputView;
import asia.health.bitcare.network.APIConstant;
import asia.health.bitcare.prefs.DateTimeFormat;
import asia.health.bitcare.utils.DateTimeUtils;
import asia.health.bitcare.utils.DatetimeHelper;
import asia.health.bitcare.utils.StringUtils;
import asia.health.bitcare.utils.SystemHelper;
import asia.health.bitcare.widget.toast.Boast;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodPressureInputFragment extends BaseFragment implements View.OnClickListener, BloodPressureInputView {

    public static BloodPressureInputFragmentCallBack mCallBack;
    private final String TAG = getClass().getSimpleName();
    private BloodPressure bloodPressure;
    private Button btnDateTime;
    private LinearLayout vgBPSys;
    private LinearLayout vgBPMin;
    private LinearLayout vgBPPulse;
    private LinearLayout vgBPWeight;
    private LinearLayout vgBPMedicine;
    //  private LinearLayout vgEnoughExercise;
    private LinearLayout vgInputTime;
    private LinearLayout vgButton;
    private LinearLayout lvPickerDateTime;
    private Button btnCancel;
    private Button btnConfirm;
    private RadioGroup rgBpMedicine;
    // private RadioGroup rgBpExersise;
    private String mediation = APIConstant.BSMEDICINEYN_Y;
    private String exercise = APIConstant.BSEXERCISEYN_Y;
    private EditText edtBPSys;
    private EditText edtBPMin;
    private EditText edtBPPulse;
    private EditText edtBPWeight;
    private BloodPressureInputPresenter presenter;
    private TextView txtDate, txtDayOfWeek, txtTime;
    private String mBPMStyle;

    @Override
    public int setFragmentView() {
        return R.layout.fragment_blood_pressure_input_2;
    }

    @Override
    public void initView() {
        btnDateTime = (Button) view.findViewById(R.id.btnDateTime);

        vgBPMedicine = (LinearLayout) view.findViewById(R.id.vgMedicine);
        //  vgEnoughExercise = (LinearLayout) view.findViewById(R.id.vgExercise);
        vgInputTime = (LinearLayout) view.findViewById(R.id.vgInputTime);
        vgButton = (LinearLayout) view.findViewById(R.id.vgButton);

        vgBPSys = (LinearLayout) view.findViewById(R.id.vgSys);
        edtBPSys = (EditText) vgBPSys.findViewById(R.id.edtInput);
        vgBPMin = (LinearLayout) view.findViewById(R.id.vgMin);
        edtBPMin = (EditText) vgBPMin.findViewById(R.id.edtInput);
        vgBPPulse = (LinearLayout) view.findViewById(R.id.vgPulse);
        edtBPPulse = (EditText) vgBPPulse.findViewById(R.id.edtInput);
        vgBPWeight = (LinearLayout) view.findViewById(R.id.vgWeight);
        edtBPWeight = (EditText) vgBPWeight.findViewById(R.id.edtInput);

        btnCancel = (Button) vgButton.findViewById(R.id.btnCancel);
        btnConfirm = (Button) vgButton.findViewById(R.id.btnConfirm);
        lvPickerDateTime = (LinearLayout) vgInputTime.findViewById(R.id.lvPickerDateTime);
        txtTime = (TextView) vgInputTime.findViewById(R.id.txtTime);
        txtDate = (TextView) vgInputTime.findViewById(R.id.txtDate);
        txtDayOfWeek = (TextView) vgInputTime.findViewById(R.id.txtDayOfWeek);
        edtBPWeight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        setFocusEditText(edtBPSys, edtBPMin, edtBPPulse, edtBPWeight);

        edtBPSys.setText(ANDManager.BPSys);
        edtBPMin.setText(ANDManager.BPMin);
        edtBPPulse.setText(ANDManager.BPPulse);
        edtBPWeight.setText(String.valueOf(User.get().getWeight()));
    }

    @Override
    public void initValue() {
        BloodPressureFragment.setOnForwardData(new OnForwardData() {
            @Override
            public void onForward(BloodPressure bloodPressure) {
                if (bloodPressure.getBpMedicineYN() == APIConstant.BSMEDICINEYN_Y) {
                    ((RadioButton) rgBpMedicine.getChildAt(0)).setChecked(true);
                    mediation = APIConstant.BSMEDICINEYN_Y;
                } else {
                    ((RadioButton) rgBpMedicine.getChildAt(1)).setChecked(true);
                    mediation = APIConstant.BSMEDICINEYN_N;
                }

                // if (bloodPressure.getBpExersiseYN() == APIConstant.BSEXERCISEYN_Y) {
                //     ((RadioButton) rgBpExersise.getChildAt(0)).setChecked(true);
                //     exercise = APIConstant.BSEXERCISEYN_Y;
                // } else {
                //      ((RadioButton) rgBpExersise.getChildAt(1)).setChecked(true);
                //      exercise = APIConstant.BSEXERCISEYN_N;
                //  }

                // edtBPSys.setText(String.valueOf(bloodPressure.getBpSys()));
                //  edtBPMin.setText(String.valueOf(bloodPressure.getBpMin()));
                //  edtBPPulse.setText(String.valueOf(bloodPressure.getBpPulse()));
                //  Log.d(TAG, "edtBPWeight : " + bloodPressure.getBpWeight());
//                edtBPWeight.setText(String.valueOf(bloodPressure.getBpWeight()));
            }
        });

//        if (ANDManager.mGlucose != 0) {
//            //edtBSVal.setText(Integer.toString((int)DeviceSettingsFragment.mGlucose));
//            ANDManager.mGlucose = 0;
//            mBPMStyle = APIConstant.BPMSTYPE_D;
//            edtBPWeight.setText(String.valueOf(User.get().getWeight()));
//            Log.d(TAG, "ANDManager edtBSWeight : " + String.valueOf(User.get().getWeight()));
//        } else {
//            mBPMStyle = APIConstant.BPMSTYPE_U;
//        }

        presenter = new BloodPressureInputPresenter(this);
        ((TextView) vgBPSys.findViewById(R.id.tvTitle)).setText(R.string.cap_systolic);
        ((TextView) vgBPSys.findViewById(R.id.tvUnit)).setText(R.string.unit_mmhg);
        ((TextView) vgBPMin.findViewById(R.id.tvTitle)).setText(R.string.cap_diastole);
        ((TextView) vgBPMin.findViewById(R.id.tvUnit)).setText(R.string.unit_mmhg);
        ((TextView) vgBPPulse.findViewById(R.id.tvTitle)).setText(R.string.cap_pulse);
        ((TextView) vgBPPulse.findViewById(R.id.tvUnit)).setText(R.string.unit_bpm);
        ((TextView) vgBPWeight.findViewById(R.id.tvTitle)).setText(R.string.cap_weight_short);
        ((TextView) vgBPWeight.findViewById(R.id.tvUnit)).setText(R.string.unit_kg);
        ((TextView) vgBPMedicine.findViewById(R.id.tvTitle)).setText(R.string.cap_mediation);
        rgBpMedicine = (RadioGroup) vgBPMedicine.findViewById(R.id.rdgChoice);
        ((RadioButton) rgBpMedicine.getChildAt(0)).setText(R.string.cap_taking);
        ((RadioButton) rgBpMedicine.getChildAt(1)).setText(R.string.cap_nottaking);
        // ((TextView) vgEnoughExercise.findViewById(R.id.tvTitle)).setText(R.string.cap_doexercise);
        // rgBpExersise = (RadioGroup) vgEnoughExercise.findViewById(R.id.rdgChoice);
        // ((RadioButton) rgBpExersise.getChildAt(0)).setText(R.string.cap_enough);
        // ((RadioButton) rgBpExersise.getChildAt(1)).setText(R.string.cap_no);
    }

    @Override
    public void initAction() {
        DatetimeHelper.addOnClickDatePicker(lvPickerDateTime, txtDate, txtDayOfWeek, txtTime, DateTimeFormat.DATE_TIME_FORMAT,
                getString(R.string.up_save), getString(R.string.up_cancel), true);
        setRadioCheckedValue();
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    private void setRadioCheckedValue() {
        rgBpMedicine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "checkedId : " + checkedId);
                if (checkedId == R.id.rdo1)
                    mediation = APIConstant.BSMEDICINEYN_Y;
                else mediation = APIConstant.BSMEDICINEYN_N;
            }
        });
        // rgBpExersise.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        //     @Override
        //     public void onCheckedChanged(RadioGroup group, int checkedId) {
        //         Log.d(TAG, "checkedId : " + checkedId);
        //         if (checkedId == R.id.rdo1)
        //             exercise = APIConstant.BSEXERCISEYN_Y;
        //         else exercise = APIConstant.BSEXERCISEYN_N;
        //     }
        // });
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
                        bloodPressure = new BloodPressure();
                        if (ANDManager.BMPSTYPE_D) {
                            bloodPressure.setBpmStyle(APIConstant.BPMSTYPE_D);
                        } else {
                            bloodPressure.setBpmStyle(APIConstant.BPMSTYPE_U);
                        }
//                        bloodPressure.setBpmStyle(mBPMStyle);
                        bloodPressure.setBpSys(Integer.parseInt(edtBPSys.getText().toString().trim()));
                        bloodPressure.setBpMin(Integer.parseInt(edtBPMin.getText().toString().trim()));
                        bloodPressure.setBpPulse(Integer.parseInt(edtBPPulse.getText().toString().trim()));
                        bloodPressure.setBpWeight(Double.parseDouble(edtBPWeight.getText().toString().trim()));
                        bloodPressure.setBpMedicineYN(mediation);
                        bloodPressure.setBpExersiseYN(exercise);
                        bloodPressure.setBpmsDate(StringUtils.formatDataSimple(txtDate.getText().toString().trim() + DateTimeUtils.parseTime12to24(txtTime.getText().toString().trim(), txtDayOfWeek.getText().toString())));
                        presenter.addBloodPressure(bloodPressure);
                    } catch (Exception e) {
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

        if (serviceMsg != null && !serviceMsg.equals("")) {
            Boast.makeText(getMainActivity(), serviceMsg).show();
        }
//        if (mBPMStyle.equalsIgnoreCase(APIConstant.BPMSTYPE_U)) {
        mCallBack.onAddNew(bloodPressure);
//        }
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
        if (edtBPSys.getText().toString().trim().equals("")) {
            return false;
        } else if (edtBPMin.getText().toString().equals("")) {
            return false;
        } else if (edtBPPulse.getText().toString().equals("")) {
            return false;
        } else if (edtBPWeight.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    public interface BloodPressureInputFragmentCallBack {
        void onAddNew(BloodPressure bloodPressure);
    }
}
