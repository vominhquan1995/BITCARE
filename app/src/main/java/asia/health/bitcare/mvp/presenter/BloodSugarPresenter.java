package asia.health.bitcare.mvp.presenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.model.BloodSugar;
import asia.health.bitcare.mvp.view.BloodSugarView;
import asia.health.bitcare.network.API;
import asia.health.bitcare.network.APIConstant;

/**
 * Created by HP on 04-Jan-17.
 */

public class BloodSugarPresenter {
    private BloodSugarView view;

    public BloodSugarPresenter(BloodSugarView view) {
        this.view = view;
    }

    public void getBloodSugarList(String type) {
        API.getBloodSugarList(type, new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response,String serviceMsg) {
                try {
                    JSONArray bloodSugarResponse = response.getJSONArray(APIConstant.BSLIST);
                    if (bloodSugarResponse.length() > 0) {
                        view.onGetListSuccess(BloodSugar.getList(bloodSugarResponse),serviceMsg);
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
