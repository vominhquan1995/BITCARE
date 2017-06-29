package asia.health.bitcare.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.health.bitcare.R;

/**
 * Created by thong on 12/22/2016.
 * PROJECT BITCARE_ANDROID
 */

public abstract class BaseDialogFragment extends DialogFragment {

    protected View view;
    protected Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BCDialog);
        context = getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.setTitle(null);
            dialog.setCancelable(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setDialogView(), container, false);

        initView();
        initValue();
        initAction();

        return view;
    }

    protected abstract int setDialogView();

    protected abstract void initView();

    protected abstract void initValue();

    protected abstract void initAction();

}
