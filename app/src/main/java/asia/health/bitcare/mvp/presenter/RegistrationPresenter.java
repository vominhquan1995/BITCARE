package asia.health.bitcare.mvp.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.mvp.view.RegistrationView;
import asia.health.bitcare.network.API;
import asia.health.bitcare.network.APIConstant;

/**
 * Created by vomin on 06/02/2017.
 */

public class RegistrationPresenter {
    private RegistrationView view;

    public RegistrationPresenter(RegistrationView view) {
        this.view = view;
    }

    public void registration(final String userID, final String userPW, final String userNm, final String gender,
                             final String height, final String weight, final String userDate, final String mPhoneNo1,
                             final String mPhoneNo2, final String mPhoneNo3) {
        API.registration(userID, userPW, userNm, gender, height, weight, userDate, mPhoneNo1, mPhoneNo2, mPhoneNo3,
                new API.OnAPIListener() {
                    @Override
                    public void onSuccess(JSONObject response,String serviceMsg) throws JSONException {
                        view.onRegistrationSuccess(serviceMsg);
                    }

                    @Override
                    public void onError(String errorMessage,String serviceMsg) {
                        view.onRegistrationFail(errorMessage);
                    }
                });
    }

    public void checkID(final String userID) {
        API.idCheck(userID, new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response,String serviceMsg) throws JSONException {
                view.onUserNotExist();
            }

            @Override
            public void onError(String errorMessage,String serviceMsg) {
                view.onUserExist(serviceMsg);
            }
        });
    }
}
