package asia.health.bitcare.mvp.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.mvp.view.InvestigateView;
import asia.health.bitcare.network.API;
import asia.health.bitcare.network.APIConstant;

/**
 * Created by An Pham on 06-Feb-17.
 * Last modifined on 06-Feb-17
 */

public class InvestigatePresenter {
    private InvestigateView view;

    public InvestigatePresenter(InvestigateView view) {
        this.view = view;
    }

    public void addHealth(int[] data) {
        API.addHealthQuest(
                String.valueOf(data[0]),
                String.valueOf(data[1]),
                String.valueOf(data[2]),
                String.valueOf(data[3]),
                String.valueOf(data[4]),
                String.valueOf(data[5]),
                String.valueOf(data[6]),
                String.valueOf(data[7]),
                String.valueOf(data[8]),
                String.valueOf(data[9]),
                String.valueOf(data[10]),
                String.valueOf(data[11]),
                new API.OnAPIListener() {
                    @Override
                    public void onSuccess(JSONObject response,String serviceMsg) throws JSONException {
                        view.onSuccess(response.getInt(APIConstant.FITAGE),serviceMsg);
                    }

                    @Override
                    public void onError(String errorMessage,String serviceMsg) {
                        view.onError(errorMessage);
                    }
                }
        );
    }
}
