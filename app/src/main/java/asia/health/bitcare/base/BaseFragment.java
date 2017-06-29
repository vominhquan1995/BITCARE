package asia.health.bitcare.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import asia.health.bitcare.R;
import asia.health.bitcare.activity.MainActivity;
import asia.health.bitcare.base.BaseMainToolbarActivity.OnPopupMenuOnClick;
import asia.health.bitcare.utils.SystemHelper;

/**
 * Created by thong on 12/21/2016.
 */

public abstract class BaseFragment extends Fragment implements Init {
    protected Context context;
    protected View view;
    protected Toolbar toolbar;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setFragmentView(), container, false);
        context = getActivity();
        initBaseValue();

        // init value
        initView();
        initValue();
        initAction();

        return view;
    }

    private void initBaseValue() {
        view.findViewById(R.id.rootLayout).setBackgroundResource(R.drawable.general_bg);
        view.findViewById(R.id.rootLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // get view
    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public abstract int setFragmentView();

    protected MainActivity getMainActivity() {
        return ((MainActivity) getActivity());
    }

    protected SharedPreferences getSharePreferences() {
        return getMainActivity().sharedPreferences;
    }

    protected void showProgressDialog() {
        getMainActivity().showProgressDialog();
    }

    protected void dismissProgressDialog() {
        getMainActivity().dismissProgressDialog();
    }

    protected void onBackPressed() {
        SystemHelper.hideKeyboard(context);
        getMainActivity().onBackPressed();
    }

    protected void setOnPopupMenuClick(OnPopupMenuOnClick onPopupMenuClick) {
        getMainActivity().setOnPopupMenuOnClick(onPopupMenuClick);
    }
    protected void setFocusEditText(final EditText editText1, final EditText editText2,final EditText editText3,final EditText editText4){
        editText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    editText2.requestFocus();
                    return true;
                }
                return false;
            }
        });
        if(editText2!=null){
            editText2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        editText3.requestFocus();
                        return true;
                    }
                    return false;
                }
            });
        }else {
            editText1.setImeOptions(EditorInfo.IME_ACTION_DONE);
            return;
        }
        if(editText3!=null){
            editText3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        editText4.requestFocus();
                        return true;
                    }
                    return false;
                }
            });
        }else{
            editText2.setImeOptions(EditorInfo.IME_ACTION_DONE);
            return;
        }
        if(editText4!=null){
            editText4.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }
    }
}
