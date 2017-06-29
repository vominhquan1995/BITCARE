package asia.health.bitcare.mvp.view;

/**
 * Created by HP on 29-Dec-16.
 */

public interface LoginView {
    void onLoginSuccess(String message);

    void onLoginFail(String serviceMsg);
}
