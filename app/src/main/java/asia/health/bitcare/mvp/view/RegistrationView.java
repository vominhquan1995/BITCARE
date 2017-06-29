package asia.health.bitcare.mvp.view;

/**
 * Created by vomin on 06/02/2017.
 */

public interface RegistrationView {
    void onRegistrationSuccess(String serviceMsg);

    void onRegistrationFail(String errorMessage);

    void onUserNotExist();
    void onUserExist(String serviceMsg);
}
