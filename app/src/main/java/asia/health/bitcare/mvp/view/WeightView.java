package asia.health.bitcare.mvp.view;

import java.util.List;

import asia.health.bitcare.model.Weight;

/**
 * Created by HP on 04-Jan-17.
 */

public interface WeightView {
    void onGetListSuccess(List<Weight> weights, String serviceMsg);

    void onGetFail(String message, String errorMessage);
}
