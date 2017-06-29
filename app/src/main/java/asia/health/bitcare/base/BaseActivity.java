package asia.health.bitcare.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import asia.health.bitcare.widget.Loading;

/**
 * Created by MrAn on 21-Sep-16.
 */

public abstract class BaseActivity extends AppCompatActivity implements Init {
    private final String TAG = "BaseActivity";
    public SharedPreferences sharedPreferences;
    protected Context context;
    private Loading loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getView());

        initBaseValue();
        initView();
        initValue();
        initAction();
    }

    private void initBaseValue() {
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        initProgressDialog();
    }

    private void initProgressDialog() {
        loading = new Loading(this);
        ViewGroup layout = (ViewGroup) findViewById(android.R.id.content).getRootView();
        layout.addView(loading);

    }

    public abstract int getView();

    public void showProgressDialog() {
        loading.show();
    }

    public void dismissProgressDialog() {
        loading.hide();
    }
}
