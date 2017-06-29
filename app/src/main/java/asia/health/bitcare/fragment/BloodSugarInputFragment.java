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
import asia.health.bitcare.dialog.DialogInfo;
import asia.health.bitcare.fragment.BloodSugarFragment.OnForwardData;
import asia.health.bitcare.model.BloodSugar;
import asia.health.bitcare.mvp.presenter.BloodSugarInputPresenter;
import asia.health.bitcare.mvp.view.BloodSugarInputView;
import asia.health.bitcare.network.APIConstant;
import asia.health.bitcare.prefs.DateTimeFormat;
import asia.health.bitcare.utils.DateTimeUtils;
import asia.health.bitcare.utils.DatetimeHelper;
import asia.health.bitcare.utils.StringUtils;
import asia.health.bitcare.utils.SystemHelper;
import asia.health.bitcare.widget.toast.Boast;
import asia.health.bitcare.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodSugarInputFragment extends BaseFragment implements View.OnClickListener, BloodSugarInputView {
    public static BloodSugarInputFragmentCallBack mCallBack;
    private final String TAG = getClass().getSimpleName();
    private BloodSugarInputPresenter presenter;
    private LinearLayout lvPickerDateTime;
    private LinearLayout vgBSType;
    private LinearLayout vgBSVal;
    private LinearLayout vgBSWeight;
    private LinearLayout vgBSMedicine;
    private LinearLayout vgBSExercise;
    private LinearLayout vgInputTime;
    private LinearLayout vgButton;
    private Button btnCancel;
    private Button btnConfirm;
    private String mediation = APIConstant.BSMEDICINEYN_Y;
    private String exercise = APIConstant.BSEXERCISEYN_Y;
    private String meals = APIConstant.BSTYPE_B;
    private BloodSugar bloodSugar;
    private RadioGroup rgBSType;
    private RadioGroup rgBSMedicine;
   // private RadioGroup rgBSExercise;
    private EditText edtBSVal;
    private EditText edtBSWeight;
    private TextView txtDate,txtDayOfWeek,txtTime;
    private String mBsmStyle;

    @Override
    public int setFragmentView() {
        return R.layout.fragment_blood_sugar_input_2;
    }

    @Override
    public void initView() {
        vgBSType = (LinearLayout) view.findViewById(R.id.vgType);
        vgBSMedicine = (LinearLayout) view.findViewById(R.id.vgMedicine);
       // vgBSExercise = (LinearLayout) view.findViewById(R.id.vgExercise);
        vgInputTime = (LinearLayout) view.findViewById(R.id.vgInputTime);
        vgButton = (LinearLayout) view.findViewById(R.id.vgButton);

        vgBSVal = (LinearLayout) view.findViewById(R.id.vgVal);
        edtBSVal = (EditText) vgBSVal.findViewById(R.id.edtInput);
        vgBSWeight = (LinearLayout) view.findViewById(R.id.vgWeight);
        edtBSWeight = (EditText) vgBSWeight.findViewById(R.id.edtInput);

        btnCancel = (Button) vgButton.findViewById(R.id.btnCancel);
        btnConfirm = (Button) vgButton.findViewById(R.id.btnConfirm);
        lvPickerDateTime = (LinearLayout) vgInputTime.findViewById(R.id.lvPickerDateTime);
        txtTime=(TextView) vgInputTime.findViewById(R.id.txtTime);
        txtDate=(TextView) vgInputTime.findViewById(R.id.txtDate);
        txtDayOfWeek=(TextView) vgInputTime.findViewById(R.id.txtDayOfWeek);
        edtBSWeight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        setFocusEditText(edtBSVal,edtBSWeight,null,null);
    }

    @Override
    public void initValue() {
        BloodSugarFragment.setOnForwardData(new OnForwardData() {
            @Override
            public void onForward(BloodSugar bloodSugar) {
                if (bloodSugar.getBsType() == APIConstant.BSTYPE_B) {
                    ((RadioButton) rgBSType.getChildAt(0)).setChecked(true);
                    meals = APIConstant.BSTYPE_B;
                } else {
                    ((RadioButton) rgBSType.getChildAt(1)).setChecked(true);
                    meals = APIConstant.BSTYPE_A;
                }

                if (bloodSugar.getBsMedicineYN() == APIConstant.BSMEDICINEYN_Y) {
                    ((RadioButton) rgBSMedicine.getChildAt(0)).setChecked(true);
                    mediation = APIConstant.BSMEDICINEYN_Y;
                } else {
                    ((RadioButton) rgBSMedicine.getChildAt(1)).setChecked(true);
                    mediation = APIConstant.BSMEDICINEYN_N;
                }

            //    if (bloodSugar.getBsExersiseYN() == APIConstant.BSEXERCISEYN_Y) {
            //        ((RadioButton) rgBSExercise.getChildAt(0)).setChecked(true);
            //        exercise = APIConstant.BSEXERCISEYN_Y;
            //    } else {
            //        ((RadioButton) rgBSExercise.getChildAt(1)).setChecked(true);
            //        exercise = APIConstant.BSEXERCISEYN_N;
            //    }

            //    edtBSVal.setText(String.valueOf(bloodSugar.getBsVal()));
                edtBSWeight.setText(String.valueOf(bloodSugar.getBsWeight()));

            }
        });

        if(DeviceSettingsFragment.mGlucose != 0) {
            edtBSVal.setText(Integer.toString((int)DeviceSettingsFragment.mGlucose));
            DeviceSettingsFragment.mGlucose = 0;
            mBsmStyle = APIConstant.BSMSTYPE_D;
            edtBSWeight.setText(String.valueOf(String.valueOf(User.get().getWeight())));
        } else {
            mBsmStyle = APIConstant.BSMSTYPE_U;
        }

        presenter = new BloodSugarInputPresenter(this);

        ((TextView) vgBSType.findViewById(R.id.tvTitle)).setText(R.string.cap_time_measuring);
        rgBSType = (RadioGroup) vgBSType.findViewById(R.id.rdgChoice);
        ((RadioButton) rgBSType.getChildAt(0)).setText(R.string.cap_beforemeals);
        ((RadioButton) rgBSType.getChildAt(1)).setText(R.string.cap_aftermeals);

        ((TextView) vgBSMedicine.findViewById(R.id.tvTitle)).setText(R.string.cap_mediation);
        rgBSMedicine = (RadioGroup) vgBSMedicine.findViewById(R.id.rdgChoice);
        ((RadioButton) rgBSMedicine.getChildAt(0)).setText(R.string.cap_taking);
        ((RadioButton) rgBSMedicine.getChildAt(1)).setText(R.string.cap_nottaking);

      //  ((TextView) vgBSExercise.findViewById(R.id.tvTitle)).setText(R.string.cap_enough_exercise);
      //  rgBSExercise = (RadioGroup) vgBSExercise.findViewById(R.id.rdgChoice);
      //  ((RadioButton) rgBSExercise.getChildAt(0)).setText(R.string.cap_enough);
      //  ((RadioButton) rgBSExercise.getChildAt(1)).setText(R.string.cap_insufficiency);

        ((TextView) vgBSVal.findViewById(R.id.tvTitle)).setText(R.string.cap_bloodsugar);
        ((TextView) vgBSVal.findViewById(R.id.tvUnit)).setText(R.string.unit_mg_dl);
        ((TextView) vgBSWeight.findViewById(R.id.tvTitle)).setText(R.string.cap_weight);
        ((TextView) vgBSWeight.findViewById(R.id.tvUnit)).setText(R.string.unit_kg);
        ((TextView) vgBSMedicine.findViewById(R.id.tvTitle)).setText(R.string.cap_mediation);

    }

    @Override
    public void initAction() {
        DatetimeHelper.addOnClickDatePicker(lvPickerDateTime,txtDate,txtDayOfWeek,txtTime, DateTimeFormat.DATE_TIME_FORMAT,
                getString(R.string.up_save), getString(R.string.up_cancel), true);
        setRadioCheckedValue();
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    private void setRadioCheckedValue() {
        rgBSMedicine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "checkedId : " + checkedId);
                if (checkedId == R.id.rdo1) {
                    mediation = APIConstant.BSMEDICINEYN_Y;
                } else {
                    mediation = APIConstant.BSMEDICINEYN_N;
                }
            }
        });
       // rgBSExercise.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
       //     @Override
       //     public void onCheckedChanged(RadioGroup group, int checkedId) {
        //        Log.d(TAG, "checkedId : " + checkedId);
       //         if (checkedId == R.id.rdo1) {
       //             exercise = APIConstant.BSEXERCISEYN_Y;
       //         } else {
       //             exercise = APIConstant.BSEXERCISEYN_N;
       //         }
       //     }
       // });
        rgBSType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "checkedId : " + checkedId);
                if (checkedId == R.id.rdo1) {
                    meals = APIConstant.BSTYPE_B;
                } else {
                    meals = APIConstant.BSTYPE_A;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                onBackPressed();
                break;
            case R.id.btnConfirm:
                if(validateEdittext()){
                    showProgressDialog();
                    try {
                        bloodSugar = new BloodSugar();
                        bloodSugar.setBsmStyle(mBsmStyle);
                        bloodSugar.setBsType(meals);
                        bloodSugar.setBsVal(Integer.parseInt(edtBSVal.getText().toString().trim()));
                        bloodSugar.setBsWeight(Double.parseDouble(edtBSWeight.getText().toString().trim()));
                        bloodSugar.setBsMedicineYN(mediation);
                        bloodSugar.setBsExersiseYN(exercise);
                        bloodSugar.setBsmsDate(StringUtils.formatDataSimple(txtDate.getText().toString().trim()+ DateTimeUtils.parseTime12to24(txtTime.getText().toString().trim(),txtDayOfWeek.getText().toString())));
                        presenter.addBloodSugar(bloodSugar);
                    } catch (Exception e) {
                        e.printStackTrace();
                        dismissProgressDialog();
                        Boast.makeText(getMainActivity(), e.getMessage()).show();
                    }
                }else{
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

        if(mBsmStyle.equalsIgnoreCase(APIConstant.BSMSTYPE_U)) {
            mCallBack.onAddNew(bloodSugar);
        }

        SystemHelper.hideKeyboard(context);
        onBackPressed();
    }

    @Override
    public void onError(String errorMessage) {
        dismissProgressDialog();
        Boast.makeText(getMainActivity(), errorMessage).show();
    }

    public interface BloodSugarInputFragmentCallBack {

        void onAddNew(BloodSugar bloodSugar);
    }

    public boolean validateEdittext() {
        if (edtBSVal.getText().toString().trim().equals("")) {
            return  false;
        } else if (edtBSWeight.getText().toString().equals("")) {
            return  false;
        }
        return true;
    }
}
