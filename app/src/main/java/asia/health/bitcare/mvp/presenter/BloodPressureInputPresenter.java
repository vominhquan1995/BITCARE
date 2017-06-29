package asia.health.bitcare.mvp.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.model.BloodPressure;
import asia.health.bitcare.mvp.view.BloodPressureInputView;
import asia.health.bitcare.network.API;

/**
 * Created by HP on 03-Jan-17.
 */

public class BloodPressureInputPresenter {
    private BloodPressureInputView view;

    public BloodPressureInputPresenter(BloodPressureInputView view) {
        this.view = view;
    }

    public void addBloodPressure(BloodPressure bloodPressure) {
        API.addBPData(bloodPressure, new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response,String serviceMsg) throws JSONException {
                view.onAddBloodPressureSuccess(serviceMsg);
            }

            @Override
            public void onError(String errorMessage,String serviceMsg) {
                view.onError(errorMessage);
            }
        });
    }
}
