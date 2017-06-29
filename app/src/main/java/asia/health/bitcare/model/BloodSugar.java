package asia.health.bitcare.model;

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

public class BloodSugar implements Comparable<BloodSugar> {
    private int bsSEQ;
    private String bsmStyle;
    private String bsType;
    private int bsVal;
    private double bsWeight;
    private String bsMedicineYN;
    private String bsExersiseYN;
    private String bsmsDate;

    public BloodSugar() {
    }

    public BloodSugar(int bsSEQ, String bsmStyle, String bsType,
                      int bsVal, double bsWeight, String bsMedicineYN, String bsExersiseYN, String bsmsDate) {
        this.bsSEQ = bsSEQ;
        this.bsmStyle = bsmStyle;
        this.bsType = bsType;
        this.bsVal = bsVal;
        this.bsWeight = bsWeight;
        this.bsMedicineYN = bsMedicineYN;
        this.bsExersiseYN = bsExersiseYN;
        this.bsmsDate = bsmsDate;
    }

    public BloodSugar(int bsVal, double bsWeight, String bsType, String bsExersiseYN,
                      String bsmsDate) {
        this.bsExersiseYN = bsExersiseYN;
        this.bsVal = bsVal;
        this.bsWeight = bsWeight;
        this.bsType = bsType;
        this.bsmsDate = bsmsDate;
    }

    public static List<BloodSugar> getList(JSONArray jsonArray) {
        List<BloodSugar> listData = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = (JSONObject) jsonArray.get(i);

                //TODO remove it
                if (data.getString(APIConstant.BSMSDATE).length() >= 12 &&
                        !data.getString(APIConstant.BSMSDATE).contains(":") &&
                        !data.getString(APIConstant.BSMSDATE).contains(" ")) {

                    BloodSugar bloodSugar = new BloodSugar();
                    bloodSugar.setBsSEQ(data.getInt(APIConstant.BSSEQ));
                    bloodSugar.setBsExersiseYN(data.getString(APIConstant.BSEXERCISEYN));
                    bloodSugar.setBsMedicineYN(data.getString(APIConstant.BSMEDICINEYN));
                    bloodSugar.setBsmsDate(data.getString(APIConstant.BSMSDATE));
                    bloodSugar.setBsmStyle(data.getString(APIConstant.BSMSTYPE));
                    bloodSugar.setBsType(data.getString(APIConstant.BSTYPE));
                    bloodSugar.setBsVal(data.getInt(APIConstant.BSVAL));
                    bloodSugar.setBsWeight(data.getDouble(APIConstant.BSWEIGHT));
                    listData.add(bloodSugar);
                }
            }
            Collections.sort(listData);
            return listData;
        } catch (JSONException e) {
            return null;
        }
    }

    public int getBsSEQ() {
        return bsSEQ;
    }

    public void setBsSEQ(int bsSEQ) {
        this.bsSEQ = bsSEQ;
    }

    public String getBsmStyle() {
        return bsmStyle;
    }

    public void setBsmStyle(String bsmStyle) {
        this.bsmStyle = bsmStyle;
    }

    public String getBsType() {
        return bsType;
    }

    public void setBsType(String bsType) {
        this.bsType = bsType;
    }

    public int getBsVal() {
        return bsVal;
    }

    public void setBsVal(int bsVal) {
        this.bsVal = bsVal;
    }

    public double getBsWeight() {
        return bsWeight;
    }

    public void setBsWeight(double bsWeight) {
        this.bsWeight = bsWeight;
    }

    public String getBsMedicineYN() {
        return bsMedicineYN;
    }

    public void setBsMedicineYN(String bsMedicineYN) {
        this.bsMedicineYN = bsMedicineYN;
    }

    public String getBsExersiseYN() {
        return bsExersiseYN;
    }

    public void setBsExersiseYN(String bsExersiseYN) {
        this.bsExersiseYN = bsExersiseYN;
    }

    public String getBsmsDate() {
        return bsmsDate;
    }

    public void setBsmsDate(String bsmsDate) {
        this.bsmsDate = bsmsDate;
    }

    public static int maxBsVal = 0;
    public static int minBsVal = Integer.MAX_VALUE;
    public static double maxWeight = 0;
    public static double minWeight = Integer.MAX_VALUE;

    @Override
    public int compareTo(BloodSugar o) {
        maxBsVal = NumberUtil.getMax(maxBsVal, o.getBsVal());
        minBsVal = NumberUtil.getMin(minBsVal, o.getBsVal());
        maxWeight = NumberUtil.getMax((int) maxWeight, (int) o.getBsWeight());
        minWeight = NumberUtil.getMin((int) minWeight, (int) o.getBsWeight());
        return o.getBsmsDate().compareTo((getBsmsDate()));
    }
}
