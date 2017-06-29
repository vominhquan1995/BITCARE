package asia.health.bitcare.fragment;


import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseFragment;
import asia.health.bitcare.model.User;
import asia.health.bitcare.mvp.presenter.AddFitWeightPresenter;
import asia.health.bitcare.mvp.view.AddFitWeightView;
import asia.health.bitcare.prefs.Preferences;
import asia.health.bitcare.utils.StringUtils;
import asia.health.bitcare.widget.toast.Boast;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFitWeightFragment extends BaseFragment implements View.OnClickListener, AddFitWeightView {

    private EditText edtTargetWeight;
    private String data;

    @Override
    public int setFragmentView() {
        return R.layout.fragment_target_weight_2;
    }

    @Override
    public void initView() {
        edtTargetWeight = (EditText) view.findViewById(R.id.edtTargetWeight);
        edtTargetWeight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    @Override
    public void initValue() {
        ((TextView) view.findViewById(R.id.tvTitle)).setText(R.string.cap_weight_now);
        ((TextView) view.findViewById(R.id.tvUnit)).setText(R.string.unit_kg);
        ((TextView) view.findViewById(R.id.tvTargetWeight)).setText(String.valueOf(User.get().getWeight()));
    }

    @Override
    public void initAction() {
        view.findViewById(R.id.btnCancel).setOnClickListener(this);
        view.findViewById(R.id.btnConfirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                onBackPressed();
                break;
            case R.id.btnConfirm:
                data = edtTargetWeight.getText().toString().trim();
                if (StringUtils.checkEmpty(data))
                    new AddFitWeightPresenter(this).add(Double.parseDouble(edtTargetWeight.getText().toString().trim()));
                else
                    Boast.makeText(getMainActivity(), getString(R.string.cap_message_warning)).show();
                break;
        }
    }

    @Override
    public void onSuccess(String serviceMsg) {
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(getMainActivity(), serviceMsg).show();
        getSharePreferences().edit().putString(Preferences.CURRENT_FIT_WEIGHT, data).commit();
        getMainActivity().updateFitWeight(data);
        getMainActivity().onBackPressed();
    }

    @Override
    public void onError() {
        Boast.makeText(getMainActivity(), getString(R.string.error)).show();
    }
}
