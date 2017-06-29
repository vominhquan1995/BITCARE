package asia.health.bitcare.base;

import android.support.v7.widget.Toolbar;

import asia.health.bitcare.R;

/**
 * Created by Leon on 9/28/2016.
 */

public abstract class BaseToolbarActivity extends BaseActivity {

    protected Toolbar toolbar;

    @Override
    public void initView() {

        initToolbar();
    }

    public void initToolbar() {

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                onBackPressed();
//            }
//        });
    }
}
