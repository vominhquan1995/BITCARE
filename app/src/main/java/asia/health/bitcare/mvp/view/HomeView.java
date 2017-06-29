package asia.health.bitcare.mvp.view;

/**
 * Created by An Pham on 06-Feb-17.
 * Last modifined on 06-Feb-17
 */

public interface HomeView {

    void onBpSysChange(int value);

    void onBpMinChange(int value);

    void onBpPulseChange(int value);

    void onBpMedicineChange(String value);

    void onBpExerciseChange(String value);

    void onBpMsDateChange(String value);

    void onBsValChange(int value);

    void onBsTypeChange(String value);

    void onBsMedicineChange(String value);

    void onBsExerciseChange(String value);

    void onBsMsDateChange(String value);

    void onWeightChange(double value);

    void onWTMSDateChange(String value);

    void onSuccess();

    void onError(String message);

    void onBMIChanged(double string);

    void onBMRChanged(double string);

    void onHealthConditionChanged(int anInt);

    void onNoticeChanged(String string);

    void onServiceMsg(String serviceMsg);

    void onFitWeightChanged(double value);

    void onWithdraw();

    void onMember();
}
