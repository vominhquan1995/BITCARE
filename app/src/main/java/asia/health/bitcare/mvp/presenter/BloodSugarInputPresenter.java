package asia.health.bitcare.mvp.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.model.BloodSugar;
import asia.health.bitcare.mvp.view.BloodSugarInputView;
import asia.health.bitcare.network.API;

/**
 * Created by HP on 04-Jan-17.
 */

public class BloodSugarInputPresenter {
    private BloodSugarInputView view;

    public BloodSugarInputPresenter(BloodSugarInputView view) {
        this.view = view;
    }

    public void addBloodSugar(BloodSugar bloodSugar) {
        API.addBSData(bloodSugar, new API.OnAPIListener() {
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
