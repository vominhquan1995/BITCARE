package asia.health.bitcare.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import asia.health.bitcare.network.APIConstant;
import asia.health.bitcare.utils.NumberUtil;

/**
 * Created by HP on 29-Dec-16.
 */

public class BloodPressure implements Comparable<BloodPressure> {
    private int bpSEQ;
    private String bpmStyle;
    private int bpSys;
    private int bpMin;
    private int bpPulse;
    private double bpWeight;
    private String bpMedicineYN;
    private String bpExersiseYN;
    private String bpmsDate;

    public BloodPressure(int bpSEQ, String bpmStyle,
                         int bpSys, int bpMin, int bpPulse,
                         double bpWeight, String bpMedicineYN,
                         String bpExersiseYN, String bpmsDate) {
        this.bpSEQ = bpSEQ;
        this.bpmStyle = bpmStyle;
        this.bpSys = bpSys;
        this.bpMin = bpMin;
        this.bpPulse = bpPulse;
        this.bpWeight = bpWeight;
        this.bpMedicineYN = bpMedicineYN;
        this.bpExersiseYN = bpExersiseYN;
        this.bpmsDate = bpmsDate;
    }

    public BloodPressure(int bpSys, int bpMin, int bpPulse,
                         double bpWeight, String bpExersiseYN, String bpmsDate) {
        this.bpExersiseYN = bpExersiseYN;
        this.bpSys = bpSys;
        this.bpMin = bpMin;
        this.bpPulse = bpPulse;
        this.bpWeight = bpWeight;
        this.bpmsDate = bpmsDate;
    }

    public BloodPressure() {
    }

    public static List<BloodPressure> getList(JSONArray jsonArray) {
        List<BloodPressure> listData = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = (JSONObject) jsonArray.get(i);

                //TODO remove it
                if (data.getString(APIConstant.BPMSDATE).length() >= 12 &&
                        !data.getString(APIConstant.BPMSDATE).contains(":") &&
                        !data.getString(APIConstant.BPMSDATE).contains(" ")) {
                    BloodPressure bloodPressure = new BloodPressure();
                    bloodPressure.setBpSEQ(data.getInt(APIConstant.BPSEQ));
                    bloodPressure.setBpmStyle(data.getString(APIConstant.BPMSTYPE));
                    bloodPressure.setBpSys(data.getInt(APIConstant.BPSYS));
                    bloodPressure.setBpMin(data.getInt(APIConstant.BPMIN));
                    bloodPressure.setBpPulse(data.getInt(APIConstant.BPPULSE));
                    bloodPressure.setBpWeight(data.getDouble(APIConstant.BPWEIGHT));
                    bloodPressure.setBpMedicineYN(data.getString(APIConstant.BPMEDICINEYN));
                    bloodPressure.setBpExersiseYN(data.getString(APIConstant.BPEXERCISEYN));
                    bloodPressure.setBpmsDate(data.getString(APIConstant.BPMSDATE));

                    //Set data
                    listData.add(bloodPressure);
                }
            }
            Collections.sort(listData);
            return listData;
        } catch (JSONException e) {
            return null;
        }
    }

    public int getBpSEQ() {
        return bpSEQ;
    }

    public void setBpSEQ(int bpSEQ) {
        this.bpSEQ = bpSEQ;
    }

    public String getBpmStyle() {
        return bpmStyle;
    }

    public void setBpmStyle(String bpmStyle) {
        this.bpmStyle = bpmStyle;
    }

    public int getBpSys() {
        return bpSys;
    }

    public void setBpSys(int bpSys) {
        this.bpSys = bpSys;
    }

    public int getBpMin() {
        return bpMin;
    }

    public void setBpMin(int bpMin) {
        this.bpMin = bpMin;
    }

    public int getBpPulse() {
        return bpPulse;
    }

    public void setBpPulse(int bpPulse) {
        this.bpPulse = bpPulse;
    }

    public double getBpWeight() {
        return bpWeight;
    }

    public void setBpWeight(double bpWeight) {
        this.bpWeight = bpWeight;
    }

    public String getBpMedicineYN() {
        return bpMedicineYN;
    }

    public void setBpMedicineYN(String bpMedicineYN) {
        this.bpMedicineYN = bpMedicineYN;
    }

    public String getBpExersiseYN() {
        return bpExersiseYN;
    }

    public void setBpExersiseYN(String bpExersiseYN) {
        this.bpExersiseYN = bpExersiseYN;
    }

    public String getBpmsDate() {
        return bpmsDate;
    }

    public void setBpmsDate(String bpmsDate) {
        this.bpmsDate = bpmsDate;
    }

    public static int maxBpPulse = 0;
    public static int minBpPulse = Integer.MAX_VALUE;
    public static double maxWeight = 0;
    public static double minWeight = Integer.MAX_VALUE;

    @Override
    public int compareTo(BloodPressure o) {
        maxBpPulse = NumberUtil.getMax(maxBpPulse, o.getBpPulse());
        maxBpPulse = NumberUtil.getMax(maxBpPulse, o.getBpMin());
        maxBpPulse = NumberUtil.getMax(maxBpPulse, o.getBpSys());
        minBpPulse = NumberUtil.getMin(minBpPulse, o.getBpPulse());
        minBpPulse = NumberUtil.getMin(minBpPulse, o.getBpMin());
        minBpPulse = NumberUtil.getMin(minBpPulse, o.getBpSys());
        maxWeight = NumberUtil.getMax((int) maxWeight, (int) o.getBpWeight());
        minWeight = NumberUtil.getMin((int) minWeight, (int) o.getBpWeight());
        return o.getBpmsDate().compareTo((getBpmsDate()));
    }
}
