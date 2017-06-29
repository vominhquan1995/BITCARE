package asia.health.bitcare.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseToolbarActivity;
import asia.health.bitcare.model.User;
import asia.health.bitcare.mvp.presenter.InvestigatePresenter;
import asia.health.bitcare.mvp.view.InvestigateView;
import asia.health.bitcare.widget.toast.Boast;

/**
 * Created by HP on 23-Dec-16.
 */

public class InvestigateActivity extends BaseToolbarActivity implements View.OnClickListener, InvestigateView {
    private final String TAG = getClass().getSimpleName();
    private LinearLayout lnInvestigate;
    private LinearLayout lnResult;
    private TextView txtTitle;
    private TextView txtName;
    private TextView txtYear;
    private RadioGroup rg;

    private int currentPos = 0;
    private List<InvestigateData> dataList;
    private int[] resultArrays = new int[12];

    private InvestigatePresenter presenter;

    @Override
    public int getView() {
        return R.layout.activity_investigate;
    }


    @Override
    public void initView() {
        super.initView();

        toolbar.setTitle(R.string.investigate_title);
        toolbar.setNavigationIcon(null);
        toolbar.getMenu().removeItem(R.id.menu_input);

        lnInvestigate = (LinearLayout) findViewById(R.id.lnInvestigate);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        rg = (RadioGroup) findViewById(R.id.rg);
        lnResult = (LinearLayout) findViewById(R.id.lnResult);
        txtName = (TextView) findViewById(R.id.txtName);
        txtYear = (TextView) findViewById(R.id.txtYear);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                currentPos++;
                if (currentPos < dataList.size() - 1) {
                    saveData();
                    setData(currentPos);
                } else {
                    presenter.addHealth(resultArrays);
                }
                break;
            case R.id.btnConfirm:
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void saveData() {
        int index = rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId()));
        resultArrays[currentPos - 1] = index;
    }

    @Override
    public void initValue() {
        presenter = new InvestigatePresenter(this);
        dataList = new ArrayList<>();
        dataList.add(new InvestigateData(getResources().getString(R.string.investigate_smoking_habit),
                new String[]{
                        getResources().getString(R.string.investigate_smoking_habit_choice_1),
                        getResources().getString(R.string.investigate_smoking_habit_choice_2),
                        getResources().getString(R.string.investigate_smoking_habit_choice_3),
                        getResources().getString(R.string.investigate_smoking_habit_choice_4),
                        getResources().getString(R.string.investigate_smoking_habit_choice_5),
                        getResources().getString(R.string.investigate_smoking_habit_choice_6)
                }));
        dataList.add(new InvestigateData(getResources().getString(R.string.investigate_life),
                new String[]{
                        getResources().getString(R.string.investigate_life_choice_1),
                        getResources().getString(R.string.investigate_life_choice_2),
                        getResources().getString(R.string.investigate_life_choice_3)
                }));
        dataList.add(new InvestigateData(getResources().getString(R.string.investigate_meat),
                new String[]{
                        getResources().getString(R.string.investigate_meat_choice_1),
                        getResources().getString(R.string.investigate_meat_choice_2),
                        getResources().getString(R.string.investigate_meat_choice_3)
                }));
        dataList.add(new InvestigateData(getResources().getString(R.string.investigate_fruit),
                new String[]{
                        getResources().getString(R.string.investigate_fruit_choice_1),
                        getResources().getString(R.string.investigate_fruit_choice_2),
                        getResources().getString(R.string.investigate_fruit_choice_3),
                        getResources().getString(R.string.investigate_fruit_choice_4)
                }));
        dataList.add(new InvestigateData(getResources().getString(R.string.investigate_sleep),
                new String[]{
                        getResources().getString(R.string.investigate_sleep_choice_1),
                        getResources().getString(R.string.investigate_sleep_choice_2),
                        getResources().getString(R.string.investigate_sleep_choice_3)
                }));
        dataList.add(new InvestigateData(getResources().getString(R.string.investigate_drink),
                new String[]{
                        getResources().getString(R.string.investigate_drink_choice_1),
                        getResources().getString(R.string.investigate_drink_choice_2),
                        getResources().getString(R.string.investigate_drink_choice_3)
                }));
        dataList.add(new InvestigateData(getResources().getString(R.string.investigate_smoking_habit),
                new String[]{
                        getResources().getString(R.string.investigate_blood_choice_1),
                        getResources().getString(R.string.investigate_blood_choice_2),
                        getResources().getString(R.string.investigate_blood_choice_3),
                        getResources().getString(R.string.investigate_blood_choice_4)
                }));
        dataList.add(new InvestigateData(getResources().getString(R.string.investigate_payer),
                new String[]{
                        getResources().getString(R.string.investigate_payer_choice_1),
                        getResources().getString(R.string.investigate_payer_choice_2)
                }));
        dataList.add(new InvestigateData(getResources().getString(R.string.investigate_exercise),
                new String[]{
                        getResources().getString(R.string.investigate_exercise_choice_1),
                        getResources().getString(R.string.investigate_exercise_choice_2),
                        getResources().getString(R.string.investigate_exercise_choice_3)
                }));
        dataList.add(new InvestigateData(getResources().getString(R.string.investigate_cold),
                new String[]{
                        getResources().getString(R.string.investigate_cold_choice_1),
                        getResources().getString(R.string.investigate_cold_choice_2),
                        getResources().getString(R.string.investigate_cold_choice_3)
                }));
        dataList.add(new InvestigateData(getResources().getString(R.string.investigate_character),
                new String[]{
                        getResources().getString(R.string.investigate_character_choice_1),
                        getResources().getString(R.string.investigate_character_choice_2),
                        getResources().getString(R.string.investigate_character_choice_3),
                        getResources().getString(R.string.investigate_character_choice_4)
                }));
        dataList.add(new InvestigateData(getResources().getString(R.string.investigate_tension),
                new String[]{
                        getResources().getString(R.string.investigate_tension_choice_1),
                        getResources().getString(R.string.investigate_tension_choice_2)
                }));

        setData(currentPos);
    }

    private void setData(int currentPos) {
        txtTitle.setText(dataList.get(currentPos).getTitle());
        rg.removeAllViews();
        for (String value : dataList.get(currentPos).getData()) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setButtonDrawable(R.drawable.general_comp_btn_radio);
            radioButton.setText(value);
            radioButton.setPadding(10,0,0,0);
            rg.addView(radioButton);
            RadioGroup.LayoutParams params = (RadioGroup.LayoutParams) radioButton.getLayoutParams();
            params.setMargins(0, 5, 0, 5);
            radioButton.setLayoutParams(params);
        }
        ((RadioButton) rg.getChildAt(0)).setChecked(true);
        rg.invalidate();
    }

    @Override
    public void initAction() {
        findViewById(R.id.btnNext).setOnClickListener(this);
        findViewById(R.id.btnConfirm).setOnClickListener(this);
    }

    @Override
    public void onSuccess(int fitAge, String serviceMsg) {
        if (serviceMsg != null && !serviceMsg.equals(""))
            Boast.makeText(this, serviceMsg).show();
        lnInvestigate.setVisibility(View.GONE);
        lnResult.setVisibility(View.VISIBLE);
        txtName.setText(User.get().getUserNm() + getResources().getString(R.string.investigate_the_health_of));
        txtYear.setText(String.valueOf(fitAge) + getResources().getString(R.string.investigate_year));
    }

    @Override
    public void onError(String errorMessage) {
        Boast.makeText(this, errorMessage).show();
    }

    private class InvestigateData {
        private String title;
        private String[] data;

        public InvestigateData() {
        }

        public InvestigateData(String title, String[] data) {
            this.title = title;
            this.data = data;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String[] getData() {
            return data;
        }

        public void setData(String[] data) {
            this.data = data;
        }
    }
}
