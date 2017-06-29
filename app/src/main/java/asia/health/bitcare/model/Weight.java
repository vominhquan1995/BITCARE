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
 * Created by HP on 04-Jan-17.
 */

public class Weight implements Comparable<Weight> {
    private int weightSEQ;
    private double weight;
    private String msDate;
    private double BMI;

    public Weight() {
    }

    public Weight(double weight, double bmi, String msDate) {
        this.weight = weight;
        this.msDate = msDate;
        this.BMI = bmi;
    }

    public Weight(int weightSEQ, double weight, String msDate, double BMI) {
        this.weightSEQ = weightSEQ;
        this.weight = weight;
        this.msDate = msDate;
        this.BMI = BMI;
    }

    public static List<Weight> getList(JSONArray jsonArray) {
        List<Weight> listData = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = (JSONObject) jsonArray.get(i);
                //TODO remove it
                if (data.getString(APIConstant.MSDATE).length() >= 12 &&
                        !data.getString(APIConstant.MSDATE).contains(":") &&
                        !data.getString(APIConstant.MSDATE).contains(" ")) {
                    Weight weight = new Weight();
                    weight.setWeightSEQ(data.getInt(APIConstant.WEIGHTSEQ));
                    weight.setWeight(data.getDouble(APIConstant.WEIGHT));
                    weight.setMsDate(data.getString(APIConstant.MSDATE));
                    weight.setBMI(data.getDouble(APIConstant.WEIGHT));
                    //set data
                    listData.add(weight);
                }
            }
            Collections.sort(listData);
            return listData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double getBMI() {
        return BMI;
    }

    public void setBMI(double weight) {
        this.BMI = ((double) Math.round((weight / ((User.get().getHeight() * 0.01d) * (0.01d * User.get().getHeight()))) * 10.0d)) / 10.0d;
    }

    public int getWeightSEQ() {
        return weightSEQ;
    }

    public void setWeightSEQ(int weightSEQ) {
        this.weightSEQ = weightSEQ;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getMsDate() {
        return msDate;
    }

    public void setMsDate(String msDate) {
        this.msDate = msDate;
    }


    public static double maxWeightBMI = 0;
    public static double minWeightBMI = Integer.MAX_VALUE;
    public static double maxWeight = 0;
    public static double minWeight = Integer.MAX_VALUE;

    @Override
    public int compareTo(Weight o) {
        maxWeightBMI = NumberUtil.getMax((int) maxWeightBMI, (int) o.getBMI());
        minWeightBMI = NumberUtil.getMin((int) minWeightBMI, (int) o.getBMI());
        maxWeight = NumberUtil.getMax((int) maxWeight, (int) o.getWeight());
        minWeight = NumberUtil.getMin((int) minWeight, (int) o.getWeight());
        return o.getMsDate().compareTo((getMsDate()));
    }

    public double roundTwoDecimals(double d) {
        String str = String.format("%.1f", d).replace(",", ".");
        return Double.valueOf(str);
    }
}
