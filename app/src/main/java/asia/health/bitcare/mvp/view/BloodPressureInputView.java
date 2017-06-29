package asia.health.bitcare.mvp.view;

/**
 * Created by HP on 03-Jan-17.
 */

public interface BloodPressureInputView {
    void onAddBloodPressureSuccess(String serviceMsg);

    void onError(String errorMessage);
}
