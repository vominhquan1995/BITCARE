package asia.health.bitcare.mvp.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.model.User;
import asia.health.bitcare.mvp.view.LoginView;
import asia.health.bitcare.network.API;

/**
 * Created by HP on 29-Dec-16.
 */

public class LoginPresenter {

    public interface OnCheckHeathInfo {
        void onAnswered();
        void onNull();
        void onError();
    }

    private final String TAG = getClass().getSimpleName();
    private LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    public void login(final String userId, final String password) {
        API.login(userId, password, new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response,String serviceMsg) {
                //Login success . Save userID , password
                User.get().setUserPw(password);
                User.get().init(response);

                //Notify UI
                loginView.onLoginSuccess(serviceMsg);
            }
            @Override
            public void onError(String errorMessage,String serviceMsg) {
                loginView.onLoginFail(serviceMsg);
            }
        });
    }

    public void checkHealthInfo(final OnCheckHeathInfo onCheckHeathInfo) {
        API.healthQuestInfo(new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response,String serviceMsg) throws JSONException {
                onCheckHeathInfo.onAnswered();
            }
            @Override
            public void onError(String errorMessage,String serviceMsg) {
                if (errorMessage!=null){
                    onCheckHeathInfo.onError();
                    return;
                }
                onCheckHeathInfo.onNull();
            }
        });
    }
}
