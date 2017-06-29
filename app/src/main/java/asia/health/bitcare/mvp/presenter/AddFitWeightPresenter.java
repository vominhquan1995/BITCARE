package asia.health.bitcare.mvp.presenter;

import org.json.JSONObject;

import asia.health.bitcare.mvp.view.AddFitWeightView;
import asia.health.bitcare.network.API;

/**
 * Created by An Pham on 16-Mar-17.
 * Last modifined on 16-Mar-17
 */

public class AddFitWeightPresenter {
    private AddFitWeightView view;

    public AddFitWeightPresenter(AddFitWeightView view) {
        this.view = view;
    }

    public void add(final double fitWeight) {
        API.addFitWeight(fitWeight, new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response,String serviceMsg) {
                view.onSuccess(serviceMsg);
            }

            @Override
            public void onError(String errorMessage,String serviceMsg) {
                view.onError();
            }
        });
    }
}
