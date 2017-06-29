package asia.health.bitcare.mvp.presenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import asia.health.bitcare.R;
import asia.health.bitcare.model.BloodPressure;
import asia.health.bitcare.mvp.view.BloodPressureView;
import asia.health.bitcare.network.API;
import asia.health.bitcare.network.APIConstant;
import asia.health.bitcare.utils.CalculateUtils;
import asia.health.bitcare.utils.DateTimeUtils;
import asia.health.bitcare.widget.chart.ChartUtils;

/**
 * Created by HP on 29-Dec-16.
 */

public class BloodPressurePresenter {
    private final String TAG = getClass().getSimpleName();
    private BloodPressureView view;

    public BloodPressurePresenter(BloodPressureView view) {
        this.view = view;
    }

    public void getBloodPressureList(String type) {
        API.getBloodPressureList(type, new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response,String serviceMsg) {
                try {
                    JSONArray bloodPressureResponse = response.getJSONArray(APIConstant.BPLIST);
                    if (bloodPressureResponse.length() > 0) {
                        view.onGetListSuccess(BloodPressure.getList(bloodPressureResponse),serviceMsg);
                    } else {
                        view.onGetFail("0",serviceMsg);
                    }
                } catch (JSONException e) {
                    view.onGetFail("Message : " + e.getMessage(),serviceMsg);
                }
            }

            @Override
            public void onError(String errorMessage,String serviceMsg) {
                view.onGetFail("Message : " + errorMessage,serviceMsg);
            }
        });
    }
}
