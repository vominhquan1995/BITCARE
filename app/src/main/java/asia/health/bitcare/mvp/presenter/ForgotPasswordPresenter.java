package asia.health.bitcare.mvp.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.mvp.view.ForgotPasswordView;
import asia.health.bitcare.network.API;
import asia.health.bitcare.network.APIConstant;

/**
 * Created by HP on 03-Jan-17.
 */

public class ForgotPasswordPresenter {
    private ForgotPasswordView view;

    public ForgotPasswordPresenter(ForgotPasswordView view) {
        this.view = view;
    }

    public void getPassword(String userID,
                            String birth,
                            String mPhoneNo1,
                            String mPhoneNo2,
                            String mPhoneNo3) {
        API.getPassword(userID, birth, mPhoneNo1, mPhoneNo2, mPhoneNo3, new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response,String serviceMsg) throws JSONException {
                if (!response.getString(APIConstant.USERNUM).equals("")) {
                    view.onGetUserNumSuccess(response.getInt(APIConstant.USERNUM),serviceMsg);
                } else {
                    view.onError(serviceMsg);
                }
            }

            @Override
            public void onError(String errorMessage,String serviceMsg) {
                view.onError(errorMessage);
            }
        });
    }

    public void modifyPassword(String userNum, String newPass) {
        API.modifyPassword(userNum, newPass, new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response,String serviceMsg) throws JSONException {
                view.onModifyPasswordSuccess(serviceMsg);
            }

            @Override
            public void onError(String errorMessage,String serviceMsg) {
                view.onError(serviceMsg);
            }
        });
    }
}
