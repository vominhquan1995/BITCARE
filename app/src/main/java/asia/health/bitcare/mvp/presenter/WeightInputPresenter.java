package asia.health.bitcare.mvp.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.model.Weight;
import asia.health.bitcare.mvp.view.WeightInputView;
import asia.health.bitcare.network.API;

/**
 * Created by HP on 04-Jan-17.
 */

public class WeightInputPresenter {
    private WeightInputView view;

    public WeightInputPresenter(WeightInputView view) {
        this.view = view;
    }

    public void addWeight(Weight weight) {
        API.addWeightData(weight, new API.OnAPIListener() {
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
