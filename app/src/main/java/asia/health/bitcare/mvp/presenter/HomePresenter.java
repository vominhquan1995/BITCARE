package asia.health.bitcare.mvp.presenter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import asia.health.bitcare.mvp.view.HomeView;
import asia.health.bitcare.network.API;
import asia.health.bitcare.network.APIConstant;

/**
 * Created by An Pham on 06-Feb-17.
 * Last modifined on 06-Feb-17
 */

public class HomePresenter {
    private final double MAX_WEIGHT = 150;
    private final double MIN_WEIGHT = 30;
    private HomeView view;

    public HomePresenter(HomeView view) {
        this.view = view;
    }

    public void getLastMSIInfo() {
        API.lastMsInfo(new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response,String serviceMsg) throws JSONException {
                view.onSuccess();
                JSONObject data = response.getJSONObject(APIConstant.LAST_MEASUREMENT);
                if (data != null && data.length() > 0) {
                    view.onBpSysChange(data.getInt(APIConstant.BPSYS));
                    view.onBpMinChange(data.getInt(APIConstant.BPMIN));
                    view.onBpPulseChange(data.getInt(APIConstant.BPPULSE));
                    view.onBpMedicineChange(data.getString(APIConstant.BPMEDICINEYN));
                    view.onBpExerciseChange(data.getString(APIConstant.BPEXERCISEYN));
                    view.onBpMsDateChange(data.getString(APIConstant.BPMSDATE));
                    view.onBsValChange(data.getInt(APIConstant.BSVAL));
                    view.onBsTypeChange(data.getString(APIConstant.BSTYPE));
                    view.onBsMedicineChange(data.getString(APIConstant.BSMEDICINEYN));
                    view.onBsExerciseChange(data.getString(APIConstant.BSEXERCISEYN));
                    view.onBsMsDateChange(data.getString(APIConstant.BSMSDATE));
                    view.onWeightChange(data.getInt(APIConstant.WEIGHT));
                    view.onWTMSDateChange(data.getString(APIConstant.WTMSDATE));
                    view.onBMIChanged(data.getDouble(APIConstant.BMI));
                    view.onBMRChanged(data.getDouble(APIConstant.BMR));
                    view.onHealthConditionChanged(data.getInt(APIConstant.HEALTHCONDITION));
                    view.onNoticeChanged(data.getString(APIConstant.NOTICE));
                    view.onFitWeightChanged(data.getDouble(APIConstant.FITWEIGHT));
                    view.onServiceMsg(serviceMsg);
                }
            }

            @Override
            public void onError(String errorMessage,String serviceMsg) {
                view.onError(serviceMsg);
            }
        });
    }

    public double roundTwoDecimals(double d) {
        String str = String.format("%.1f", d).replace(",", ".");
        return Double.valueOf(str);
    }

    public int calculateProgress(double weight) {
        if (weight <= MIN_WEIGHT) {
            return 0;
        } else {
            double percent = (weight + MIN_WEIGHT) / MAX_WEIGHT;
            return (int) (percent * 1000);
        }
    }
    public  void checkMemberWithdraw(final  String userNum){
        API.checkMemberWithdraw(userNum, new API.OnAPIListener() {
            @Override
            public void onSuccess(JSONObject response, String serviceMsg) throws JSONException {
                JSONArray jsonArray=response.getJSONArray(APIConstant.USERINFO);
                JSONObject data=jsonArray.getJSONObject(0);
                if(data.getString(APIConstant.WITHDRAW).equals(APIConstant.WITHDRAW_Y)){
                    view.onWithdraw();
                }else{
                    view.onMember();
                }
            }
            @Override
            public void onError(String errorMessage, String serviceMsg) {
            }
        });
    }
}
