package asia.health.bitcare.mvp.presenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import asia.health.bitcare.model.Weight;
import asia.health.bitcare.mvp.view.WeightView;
import asia.health.bitcare.network.API;
import asia.health.bitcare.network.APIConstant;

/**
 * Created by HP on 04-Jan-17.
 */

public class WeightPresenter {
    private final String TAG = getClass().getSimpleName();
    private WeightView view;

    public WeightPresenter(WeightView view) {
        this.view = view;
    }

    public void getWeightList() {
        API.getWeightList(new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response,String serviceMsg) throws JSONException {
                try {
                    JSONArray weightResponse = response.getJSONArray(APIConstant.LOW_WEIGHT);
                    if (weightResponse.length() > 0) {
                        view.onGetListSuccess(Weight.getList(weightResponse),serviceMsg);
                    } else {
                        view.onGetFail("0",serviceMsg);
                    }
                } catch (JSONException e) {
                    view.onGetFail("Message : " + e.getMessage(),serviceMsg);
                }
            }

            @Override
            public void onError(String errorMessage,String serviceMsg) {
                view.onGetFail("Message : " + errorMessage, serviceMsg);
            }
        });
    }
}
