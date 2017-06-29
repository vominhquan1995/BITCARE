package asia.health.bitcare.mvp.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.mvp.view.ForgotIdView;
import asia.health.bitcare.network.API;
import asia.health.bitcare.network.APIConstant;

/**
 * Created by HP on 03-Jan-17.
 */

public class ForgotIdPresenter {
    private ForgotIdView view;

    public ForgotIdPresenter(ForgotIdView view) {
        this.view = view;
    }

    public void getId(String name, String birth, String mPhoneNo1, String mPhoneNo2, String mPhoneNo3) {
        API.getId(name, birth, mPhoneNo1, mPhoneNo2, mPhoneNo3, new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response,String serviceMsg) {
                try {
                    if (!response.getString(APIConstant.LOW_USERID).equals("")) {
                        view.onResponse(response.getString(APIConstant.LOW_USERID),serviceMsg);
                    } else {
                        view.onError(serviceMsg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    view.onResponse("",serviceMsg);
                }
            }

            @Override
            public void onError(String errorMessage,String serviceMsg) {
                view.onResponse(errorMessage, "");
            }
        });
    }
}
