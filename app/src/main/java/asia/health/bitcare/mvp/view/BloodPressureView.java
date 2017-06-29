package asia.health.bitcare.mvp.view;

import java.util.List;

import asia.health.bitcare.model.BloodPressure;

/**
 * Created by HP on 29-Dec-16.
 */

public interface BloodPressureView {
    void onGetListSuccess(List<BloodPressure> bloodPressureArray,String serviceMsg);

    void onGetFail(String errorMessage,String serviceMsg);
}
