package asia.health.bitcare.mvp.presenter;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.mvp.view.UserInfoView;
import asia.health.bitcare.network.API;

/**
 * Created by HP on 04-Jan-17.
 */

public class UserInfoPresenter {
    private final String TAG = getClass().getSimpleName();
    private UserInfoView view;

    public UserInfoPresenter(UserInfoView view) {
        this.view = view;
    }

    public void modifyUser(String userNm, String gender, String height, String weight,
                           String userDate, String mPhoneNo1, String mPhoneNo2, String mPhoneNo3) {
        Log.d(TAG, "modifyUser: userNm" + userNm);
        Log.d(TAG, "modifyUser: gender" + gender);
        Log.d(TAG, "modifyUser: height" + height);
        Log.d(TAG, "modifyUser: weight" + weight);
        Log.d(TAG, "modifyUser: userDate" + userDate);
        Log.d(TAG, "modifyUser: mPhoneNo1" + mPhoneNo1);
        Log.d(TAG, "modifyUser: mPhoneNo2" + mPhoneNo2);
        Log.d(TAG, "modifyUser: mPhoneNo3" + mPhoneNo3);
        API.modifyUser(userNm, gender, height, weight, userDate, mPhoneNo1, mPhoneNo2, mPhoneNo3, new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response,String serviceMsg) throws JSONException {
                view.onModifySuccess(serviceMsg);
            }

            @Override
            public void onError(String errorMessage,String serviceMsg) {
                view.onModifyError(serviceMsg);
            }
        });
    }
}
