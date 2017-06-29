package asia.health.bitcare.activity;

import asia.health.bitcare.R;
import asia.health.bitcare.animation.GifView;
import asia.health.bitcare.base.BaseActivity;

public class DisconnectActivity extends BaseActivity {
    private GifView gifView;
    private int[] dataBitmapList;
    private boolean Status;

    @Override
    public int getView() {
        return R.layout.activity_disconnect;
    }

    @Override
    public void initView() {
        gifView = (GifView) findViewById(R.id.gifView);
    }

    @Override
    public void initValue() {
        dataBitmapList = new int[]{
                R.drawable.main_section_alert_goalani_1,
                R.drawable.main_section_alert_goalani_2,
                R.drawable.main_section_alert_goalani_3,
                R.drawable.main_section_alert_goalani_4,
                R.drawable.main_section_alert_goalani_5,
                R.drawable.main_section_alert_goalani_6,
                R.drawable.main_section_alert_goalani_7,
                R.drawable.main_section_alert_goalani_8,
                R.drawable.main_section_alert_goalani_9,
                R.drawable.main_section_alert_goalani_10,
                R.drawable.main_section_alert_goalani_11,
                R.drawable.main_section_alert_goalani_12,
                R.drawable.main_section_alert_goalani_13,
                R.drawable.main_section_alert_goalani_14,
                R.drawable.main_section_alert_goalani_15,
                R.drawable.main_section_alert_goalani_16,
                R.drawable.main_section_alert_goalani_17,
                R.drawable.main_section_alert_goalani_18
        };

    }

    @Override
    public void initAction() {
        gifView.setDataList(dataBitmapList);
        gifView.start();
    }

}

