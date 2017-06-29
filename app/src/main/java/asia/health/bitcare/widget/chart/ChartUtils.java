package asia.health.bitcare.widget.chart;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.List;

import asia.health.bitcare.R;
import asia.health.bitcare.model.BloodPressure;
import asia.health.bitcare.model.BloodSugar;
import asia.health.bitcare.model.Weight;
import asia.health.bitcare.prefs.Constant;
import asia.health.bitcare.utils.CalculateUtils;
import asia.health.bitcare.utils.DateTimeUtils;

/**
 * Created by HP on 16-Jan-17.
 */

public class ChartUtils {
        //hide by Quan
//    /**
//     * Validate data
//     * If data < 12 , add more empty data
//     * If data >12 , get only 12 data
//     * <p>
//     * All of that follow application scenario
//     *
//     * @param list
//     */
//    public static List<Integer> validateData(List<Integer> list) {
//        if (list.size() > 12) {
//            list = list.subList(0, 12);
//        } else {
//            while (list.size() < 12) {
//                list.add(0);
//            }
//        }
//
//        for (int i = list.size() - 1; i >= 0; i--) {
//            if (list.get(i) <= 0) {
//                int j = list.size() - 1;
//                while (j > 0) {
//                    if (list.get(j) > 0) {
//                        list.set(i, list.get(j));
//                        break;
//                    } else {
//                        j--;
//                    }
//                }
//            }
//        }
//        return list;
//    }
//
//    public static List<String> validateXAsisLabel(List<String> list) {
//        if (list.size() > 12) {
//            list = list.subList(0, 12);
//        } else {
//            while (list.size() < 12) {
//                list.add(list.get(list.size() - 1));
//            }
//        }
//        return list;
//    }
//
//    /**
//     * Check and get lasted data if previous date have value
//     *
//     * @param list
//     * @return
//     */
//    public static int getPreviousValue(List<Integer> list) {
//        if (list.size() > 0) {
//            if (list.get(list.size() - 1) != 0) {
//                return list.get(list.size() - 1);
//            } else {
//                return 0;
//            }
//        } else {
//            return 0;
//        }
//    }

    //add new by Quan 19/02
    public static List<Weight> getWeight(List<Weight> weight, int size, int searchType) {
        List<Weight> result = new ArrayList<>();
        List<Integer> weightTemp;
        double bmi;
        //get list value x
        List<String> list = DateTimeUtils.getDateListString(searchType);
        //filter week
        if (list.size() != 0) {
            for (int i = 0; i < size; i++) {
                bmi = 0.0d;
                weightTemp = new ArrayList<>();
                for (Weight value : weight) {
                    if (DateTimeUtils.compareTwoDate(value.getMsDate().substring(0, 8), i, searchType)) {
                        weightTemp.add((int) value.getWeight());
                        value.setBMI(value.getWeight());
                        bmi = value.getBMI();
                    }
                }
                result.add(new Weight(CalculateUtils.calculateAverage(weightTemp), bmi,
                        String.valueOf(list.get(i)).toString().substring(4, 8)));
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (i < weight.size()) {
                    weight.get(i).setBMI(weight.get(i).getWeight());
                    result.add(new Weight((weight.get(i)).getWeight() == 0 ? 0 : (weight.get(i)).getWeight(),
                            (weight.get(i)).getBMI() == 0.0 ? 0.0 : (weight.get(i)).getBMI(),
                            (weight.get(i)).getMsDate().equals("") ? "" : (weight.get(i)).getMsDate().substring(4, 8)
                    ));
                }
            }
        }
        return result;
    }

    public static List<BloodPressure> getBloodPressure(List<BloodPressure> bpData, int size, int searchType) {
        List<BloodPressure> result = new ArrayList<>();
        List<Integer> sysBPTemp, diaBPTemp, pulseTemp, weightTemp;
        //get list value x
        List<String> list = DateTimeUtils.getDateListString(searchType);
        String Exercise = "";
        //filter week
        if (list.size() != 0) {
            for (int i = 0; i < size; i++) {
                sysBPTemp = new ArrayList<>();
                diaBPTemp = new ArrayList<>();
                pulseTemp = new ArrayList<>();
                weightTemp = new ArrayList<>();
                for (BloodPressure value : bpData) {
                    if (DateTimeUtils.compareTwoDate(value.getBpmsDate().substring(0, 8), i, searchType)) {
                        sysBPTemp.add(value.getBpSys());
                        diaBPTemp.add(value.getBpMin());
                        pulseTemp.add(value.getBpPulse());
                        Exercise = value.getBpExersiseYN();
                        if (value.getBpWeight() != 0.0) {
                            weightTemp.add((int) value.getBpWeight());
                        }
                    }
                }
                result.add(new BloodPressure(CalculateUtils.calculateAverage(sysBPTemp),
                        CalculateUtils.calculateAverage(diaBPTemp),
                        CalculateUtils.calculateAverage(pulseTemp),
                        (double) CalculateUtils.calculateAverage(weightTemp), Exercise,
                        String.valueOf(list.get(i)).toString().substring(4, 8)));
            }
        } else {
            for (int i = 0; i < size; i++) {
                //get 12 value
                if (i < bpData.size()) {
                    result.add(new BloodPressure(bpData.get(i).getBpSys() == 0 ? 0 : (bpData.get(i)).getBpSys(),
                            (bpData.get(i)).getBpMin() == 0 ? 0 : (bpData.get(i)).getBpMin(),
                            (bpData.get(i)).getBpPulse() == 0 ? 0 : (bpData.get(i)).getBpPulse(),
                            (bpData.get(i)).getBpWeight() == 0.0 ? 0.0 : bpData.get(i).getBpWeight(), bpData.get(i).getBpExersiseYN(),
                            (bpData.get(i)).getBpmsDate().equals("") ? "" : (bpData.get(i)).getBpmsDate().substring(4, 8)));
                }
            }
        }
        return result;
    }

    public static List<BloodSugar> getBloodSugar(List<BloodSugar> bpData, int size, int searchType) {
        List<BloodSugar> result = new ArrayList<>();
        List<Integer> bsVal, weightTemp;
        String BsType = "";
        //get list value x
        List<String> list = DateTimeUtils.getDateListString(searchType);
        //filter week
        String Exercise = "";
        if (list.size() != 0) {
            for (int i = 0; i < size; i++) {
                bsVal = new ArrayList<>();
                weightTemp = new ArrayList<>();
                for (BloodSugar value : bpData) {
                    if (DateTimeUtils.compareTwoDate(value.getBsmsDate().substring(0, 8), i, searchType)) {
                        bsVal.add(value.getBsVal());
                        BsType = value.getBsType();
                        Exercise = value.getBsExersiseYN();
                        if (value.getBsWeight() != 0.0) {
                            weightTemp.add((int) value.getBsWeight());
                        }
                    }
                }
                result.add(new BloodSugar(CalculateUtils.calculateAverage(bsVal),
                        (double) CalculateUtils.calculateAverage(weightTemp), BsType, Exercise,
                        String.valueOf(list.get(i)).toString().substring(4, 8)
                ));
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (i < bpData.size()) {
                    result.add(new BloodSugar((bpData.get(i)).getBsVal() == 0 ? 0 : (bpData.get(i)).getBsVal(),
                            (bpData.get(i)).getBsWeight() == 0.0 ? 0.0 : bpData.get(i).getBsWeight(),
                            bpData.get(i).getBsType(), bpData.get(i).getBsExersiseYN(),
                            (bpData.get(i)).getBsmsDate().equals("") ? "" : (bpData.get(i)).getBsmsDate().substring(4, 8)
                    ));
                }
            }
        }
        return result;
    }
}
