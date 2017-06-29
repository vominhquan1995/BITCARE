package asia.health.bitcare.mvp.view;

import java.util.List;

import asia.health.bitcare.model.BloodSugar;

/**
 * Created by HP on 04-Jan-17.
 */

public interface BloodSugarView {
    void onGetListSuccess(List<BloodSugar> bloodSugarArray, String serviceMsg);

    void onGetFail(String message, String errorMessage);
}
