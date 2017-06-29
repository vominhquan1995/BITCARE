package asia.health.bitcare.mvp.view;

/**
 * Created by HP on 03-Jan-17.
 */

public interface ForgotIdView {
    void onResponse(String string, String userID);

    void onError(String message);
}
