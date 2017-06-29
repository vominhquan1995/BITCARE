package asia.health.bitcare.activity.signup;

import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseToolbarActivity;

public class TermsActivity extends BaseToolbarActivity implements View.OnClickListener {

    private boolean check = false;

    @Override
    public int getView() {
        return R.layout.activity_terms;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btnDontAgree:
                intent.putExtra("CHECK", check);
                setResult(0, intent);
                finish();
                break;
            case R.id.btnAgree:
                check = true;
                intent.putExtra("CHECK", check);
                setResult(0, intent);
                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("CHECK", false);
        setResult(0, intent);
        finish();
    }

    @Override
    public void initView() {
        super.initView();

        toolbar.setTitle(R.string.cap_termofuse);
        toolbar.setNavigationIcon(null);

        TextView tvTerms = (TextView) findViewById(R.id.tvTerms);
        tvTerms.setMovementMethod(new ScrollingMovementMethod());

        findViewById(R.id.btnDontAgree).setOnClickListener(this);
        findViewById(R.id.btnAgree).setOnClickListener(this);
    }

    @Override
    public void initValue() {

    }

    @Override
    public void initAction() {

    }
}
