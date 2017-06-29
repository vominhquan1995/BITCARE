package asia.health.bitcare.mvp.model;

/**
 * Created by VoMinhQuan on 30/12/2016.
 */

public class HistoryItem {
    private String date;
    private String time;
    private String value1;
    private String value2;
    private String value3;
    private String weight;
    private boolean isExersise;
    private boolean isUseMedicine;

    public HistoryItem() {
    }

    public HistoryItem(String date, String time, String value1, String value2, String value3, String weight, boolean isExersise, boolean isUseMedicine) {
        this.date = date;
        this.time = time;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.weight = weight;
        this.isExersise = isExersise;
        this.isUseMedicine = isUseMedicine;
    }

    public HistoryItem(String date, String time, String value1, String value2, String weight, boolean isExersise, boolean isUseMedicine) {
        this.date = date;
        this.time = time;
        this.value1 = value1;
        this.value2 = value2;
        this.weight = weight;
        this.isExersise = isExersise;
        this.isUseMedicine = isUseMedicine;
    }

    public HistoryItem(String date, String time, String value1, String weight) {
        this.date = date;
        this.time = time;
        this.value1 = value1;
        this.weight = weight;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean isExersise() {
        return isExersise;
    }

    public void setExersise(boolean exersise) {
        isExersise = exersise;
    }

    public boolean isUseMedicine() {
        return isUseMedicine;
    }

    public void setUseMedicine(boolean useMedicine) {
        isUseMedicine = useMedicine;
    }
}
