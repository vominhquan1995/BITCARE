package asia.health.bitcare.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import asia.health.bitcare.R;
import asia.health.bitcare.widget.toast.Boast;

/**
 * Created by vomin on 11/02/2017.
 */

public class DialogConfirmPass {
    public static class Build {
        private onConfirmPass onConfirmPassListener;
        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private Button btnCancel;
        private Button btnConfirm;
        private LinearLayout vgPass, vgPassConfirm;
        private EditText edtPass;

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
                DisplayMetrics metrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                double width = metrics.widthPixels * 0.8;
                double height = metrics.heightPixels * 0.8;
                dialog.getWindow().setLayout((int) width, (int) height);
            }
            btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            vgPass = (LinearLayout) view.findViewById(R.id.vgPass);
            vgPassConfirm = (LinearLayout) view.findViewById(R.id.vgPasswordConfirm);
            vgPassConfirm.setVisibility(View.GONE);
            vgPass.findViewById(R.id.tvTitle).setVisibility(View.GONE);
            edtPass = ((EditText) vgPass.findViewById(R.id.edtInput));
            edtPass.setHint(R.string.cap_password);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkNull() == null) {
                        onConfirmPassListener.onConfirm(edtPass.getText().toString());
                        dismiss();
                    } else {
                        Boast.makeText(activity, checkNull()).show();
                    }
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onConfirmPassListener.onCancel();
                    dismiss();
                }
            });
        }

        public void show() {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

        public void dismiss() {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        public Build setOnConfirmPassListener(onConfirmPass onConfirmPassListener) {
            this.onConfirmPassListener = onConfirmPassListener;
            return this;
        }

        public interface onConfirmPass {
            void onConfirm(String pass);

            void onCancel();
        }

        public String checkNull() {
            String errorMessage = null;
            if (edtPass.getText().toString().equals("")) {
                errorMessage = "Please confirm password";
            }
            return errorMessage;
        }
    }
}
