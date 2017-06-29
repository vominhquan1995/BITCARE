package asia.health.bitcare.model;

public class StandardInfoBean {
    private int afterBSMax;
    private int afterBSMin;
    private int beforeBSMax;
    private int beforeBSMin;
    private int diastolicBPMax;
    private int systolicBPMax;

    public StandardInfoBean(int systolicBPMax, int diastolicBPMax, int afterBSMax, int afterBSMin, int beforeBSMax, int beforeBSMin) {
        this.systolicBPMax = systolicBPMax;
        this.diastolicBPMax = diastolicBPMax;
        this.afterBSMax = afterBSMax;
        this.afterBSMin = afterBSMin;
        this.beforeBSMax = beforeBSMax;
        this.beforeBSMin = beforeBSMin;
    }

    public int getSystolicBPMax() {
        return this.systolicBPMax;
    }

    public void setSystolicBPMax(int systolicBPMax) {
        this.systolicBPMax = systolicBPMax;
    }

    public int getDiastolicBPMax() {
        return this.diastolicBPMax;
    }

    public void setDiastolicBPMax(int diastolicBPMax) {
        this.diastolicBPMax = diastolicBPMax;
    }

    public int getAfterBSMax() {
        return this.afterBSMax;
    }

    public void setAfterBSMax(int afterBSMax) {
        this.afterBSMax = afterBSMax;
    }

    public int getAfterBSMin() {
        return this.afterBSMin;
    }

    public void setAfterBSMin(int afterBSMin) {
        this.afterBSMin = afterBSMin;
    }

    public int getBeforeBSMax() {
        return this.beforeBSMax;
    }

    public void setBeforeBSMax(int beforeBSMax) {
        this.beforeBSMax = beforeBSMax;
    }

    public int getBeforeBSMin() {
        return this.beforeBSMin;
    }

    public void setBeforeBSMin(int beforeBSMin) {
        this.beforeBSMin = beforeBSMin;
    }

    public String toString() {
        return "StandardInfoBean [systolicBPMax=" + this.systolicBPMax + ", diastolicBPMax=" + this.diastolicBPMax + ", afterBSMax=" + this.afterBSMax + ", afterBSMin=" + this.afterBSMin + ", beforeBSMax=" + this.beforeBSMax + ", beforeBSMin=" + this.beforeBSMin + "]";
    }
}
