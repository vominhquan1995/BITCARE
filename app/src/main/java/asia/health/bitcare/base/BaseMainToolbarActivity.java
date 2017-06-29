package asia.health.bitcare.base;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.health.bitcare.R;

/**
 * Created by Leon on 9/28/2016. PROJECT BITCARE_ANDROID Edited by thongph
 */

public abstract class BaseMainToolbarActivity extends BaseDiscoverableBTActivity implements View.OnClickListener {

    protected LinearLayout toolbar;
    protected LinearLayout btnBack;
    // New toolbar
    protected TextView tvToolbarTitle;
    protected ImageView btnPeriodSort;
    protected ImageView btnAnswerSort;
    protected ImageView btnInput;
    private OnPopupMenuOnClick onPopupMenuOnClick;

    public void setOnPopupMenuOnClick(
            OnPopupMenuOnClick onPopupMenuOnClick) {
        this.onPopupMenuOnClick = onPopupMenuOnClick;
    }

    @Override
    public void initView() {
        initMainToolbar();
    }

    @Override
    public void initValue() {
    }

    @Override
    public void initAction() {

    }

    public void setFragmentToolbarVisible(int visible) {
        toolbar.setVisibility(visible);
    }

    private void initMainToolbar() {
        toolbar = (LinearLayout) findViewById(R.id.main_tool_bar);

        btnBack = (LinearLayout) toolbar.findViewById(R.id.btnBack);
        tvToolbarTitle = (TextView) toolbar.findViewById(R.id.tvToolbarTitle);
        btnPeriodSort = (ImageView) toolbar.findViewById(R.id.btnPeriodSort);
        btnAnswerSort = (ImageView) toolbar.findViewById(R.id.btnAnswerSort);
        btnInput = (ImageView) toolbar.findViewById(R.id.btnInput);

        btnPeriodSort.setOnClickListener(this);
        btnAnswerSort.setOnClickListener(this);
        btnInput.setOnClickListener(this);
    }

    protected void showHidePopupMenu(boolean showPopupMenuLayout) {
        if (showPopupMenuLayout) {
            btnPeriodSort.setVisibility(View.VISIBLE);
            btnAnswerSort.setVisibility(View.VISIBLE);
        } else {
            btnPeriodSort.setVisibility(View.GONE);
            btnAnswerSort.setVisibility(View.GONE);
        }
    }

    protected void showHideInput(boolean showInputSearchLayout) {
        if (showInputSearchLayout) {
            btnInput.setVisibility(View.VISIBLE);
        } else {
            btnInput.setVisibility(View.GONE);
        }
    }

    protected void showInput(boolean showInput) {
        if (showInput) {
            btnInput.setVisibility(View.VISIBLE);
        } else {
            btnInput.setVisibility(View.GONE);
        }
    }

    protected  void hideSortAnswer(boolean showSortAnswer){
        if(showSortAnswer){
            btnAnswerSort.setVisibility(View.VISIBLE);
        }else{
            btnAnswerSort.setVisibility(View.GONE);
        }

    }

    protected void updateToolbar(int resTitle) {
        initMainToolbar();
        findViewById(R.id.lnSort).setVisibility(View.VISIBLE);
        tvToolbarTitle.setText(resTitle);
    }

    //add by Quan
    protected void updateToolBarTitle(String title) {
        findViewById(R.id.lnSort).setVisibility(View.GONE);
        tvToolbarTitle.setText(title);
    }

    @Override
    public int getView() {
        return 0;
    }

    private void showPopupMenu(final View view, int menuRes, final boolean isPeriodSort) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(menuRes);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (isPeriodSort) {
                    // on pop-up menu onClick
                    if (onPopupMenuOnClick != null) {
                        onPopupMenuOnClick.onPeriodSort(item.getItemId());
                    }
                } else {
                    // on pop-up menu onClick
                    if (onPopupMenuOnClick != null) {
                        onPopupMenuOnClick.onAnswerSort(item.getItemId());
                    }
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public abstract void btnInputOnClick();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPeriodSort:
                showPopupMenu(btnPeriodSort, R.menu.menu_period_sort, true);
                break;
            case R.id.btnAnswerSort:
                showPopupMenu(btnAnswerSort, R.menu.menu_answer_sort, false);
                break;
            case R.id.btnInput:
                btnInputOnClick();
                break;
        }
    }

    // pop-up menu onclick
    public interface OnPopupMenuOnClick {

        void onPeriodSort(int menuId);

        void onAnswerSort(int menuId);
    }
}
