package asia.health.bitcare.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.health.bitcare.R;
import asia.health.bitcare.activity.LoginActivity;
import asia.health.bitcare.widget.toast.Boast;

/**
 * Created by Covisoft on 07/01/2016.
 */
public class DialogChangePass {
    public static class Build {

        private final String TAG = getClass().getSimpleName();
        private OnChangePassListener onChangePassListener;
        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private Button btnCancel;
        private Button btnConfirm;
        private TextView tvTitle,title;
        private LinearLayout vgPass, vgPassConfirm;
        private EditText edtPass, edtPassConfirm;

        public Build(final Activity activity) {
            builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_change_pass, null);
            builder.setView(view);
            dialog = builder.create();

            if (dialog != null) {
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setTitle(null);
                // set width - height for dialog
                DisplayMetrics metrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                double width = metrics.widthPixels * 0.8;
                double height = metrics.heightPixels * 0.8;
                dialog.getWindow().setLayout((int) width, (int) height);
            }
            title=(TextView) view.findViewById(R.id.title);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvTitle.setVisibility(View.GONE);
            btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            vgPass = (LinearLayout) view.findViewById(R.id.vgPass);
            vgPassConfirm = (LinearLayout) view.findViewById(R.id.vgPasswordConfirm);
            vgPass.findViewById(R.id.tvTitle).setVisibility(View.GONE);
            vgPassConfirm.findViewById(R.id.tvTitle).setVisibility(View.GONE);
            edtPass = ((EditText) vgPass.findViewById(R.id.edtInput));
            edtPassConfirm = (EditText) vgPassConfirm.findViewById(R.id.edtInput);
            edtPass.setHint(R.string.cap_password);
            edtPassConfirm.setHint(R.string.cap_confirmpassword);
            title.setText(R.string.cap_input_new_pass);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkNull() == null) {
                        onChangePassListener.onConfirm(edtPass.getText().toString());
                        dismiss();
                    } else {
                        Boast.makeText(activity, checkNull()).show();
                    }
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onChangePassListener.onCancel();
                    dismiss();
                }
            });
        }

        public Build setOnChangePassListener(OnChangePassListener onChangePassListener) {
            this.onChangePassListener = onChangePassListener;
            return this;
        }

        public void show() {
            Log.d(TAG, "show: ");
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

        public void dismiss() {
            Log.d(TAG, "dismiss: ");
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        public interface OnChangePassListener {
            void onConfirm(String newPass);

            void onCancel();
        }

        public String checkNull() {
            String errorMessage = null;
            if (edtPass.getText().toString().equals("")) {
                errorMessage = "Please input password";
            } else if (edtPass.length() < 4) {
                errorMessage = "Password lengh min is 4 digits";
            } else if (edtPassConfirm.getText().toString().equals("")) {
                errorMessage = "Please confirm password";
            } else if (!edtPassConfirm.getText().toString().equals(edtPass.getText().toString())) {
                errorMessage = "Confirm password not match";
            }
            return errorMessage;
        }
    }
}
