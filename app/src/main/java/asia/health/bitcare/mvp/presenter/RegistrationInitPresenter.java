package asia.health.bitcare.mvp.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.mvp.view.RegistrationInitView;
import asia.health.bitcare.network.API;

/**
 * Created by An Pham on 06-Feb-17.
 * Last modifined on 06-Feb-17
 */

public class RegistrationInitPresenter {
    private RegistrationInitView view;

    public RegistrationInitPresenter(RegistrationInitView view) {
        this.view = view;
    }

    public void registrationInit(final String userNm, final String gender, final String userDate, final String mPhoneNo1,
                                 final String mPhoneNo2, final String mPhoneNo3) {
        API.registrationInit(userNm, gender, userDate, mPhoneNo1, mPhoneNo2, mPhoneNo3,
                new API.OnAPIListener() {
                    @Override
                    public void onSuccess(JSONObject response,String serviceMsg) throws JSONException {
                        view.onRegistrationInitSuccess(serviceMsg);
                    }

                    @Override
                    public void onError(String errorMessage,String serviceMsg) {
                        view.onRegistrationInitFail(errorMessage);
                    }
                });
    }
}
