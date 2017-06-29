package asia.health.bitcare.mvp.view;

/**
 * Created by HP on 03-Jan-17.
 */

public interface ForgotPasswordView {
    void onGetUserNumSuccess(int userNum, String serviceMsg);

    void onError(String errorMessage);

    void onModifyPasswordSuccess(String serviceMsg);
}
