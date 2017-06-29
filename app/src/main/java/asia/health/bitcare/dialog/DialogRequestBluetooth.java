package asia.health.bitcare.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import asia.health.bitcare.R;

/**
 * Created by vomin on 23/05/2017.
 */

public class DialogRequestBluetooth {
    public static class Build {
        private final String TAG = getClass().getSimpleName();
        private onConfirmRequest onConfirmRequestListener;
        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private TextView btnCancel;
        private TextView btnConfirm;
        private TextView txtTitle, txtContent;

        public Build(final Activity activity) {
            builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_logout, null);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtContent = (TextView) view.findViewById(R.id.txtContent);
            txtTitle.setText(R.string.cap_title_bluetooth_request);
            txtContent.setText(R.string.content_bluetooth_request);
            builder.setView(view);
            dialog = builder.create();

            if (dialog != null) {
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                DisplayMetrics metrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                double width = metrics.widthPixels * 0.8;
                double height = metrics.heightPixels * 0.8;
                dialog.getWindow().setLayout((int) width, (int) height);
            }

            btnConfirm = (TextView) view.findViewById(R.id.btnConfirm);
            btnCancel = (TextView) view.findViewById(R.id.btnCancel);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onConfirmRequestListener.onYes();
                    dismiss();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onConfirmRequestListener.onNo();
                    dismiss();
                }
            });
        }

        public Build setonConfirmRequestListener(onConfirmRequest onConfirmRequestListener) {
            this.onConfirmRequestListener = onConfirmRequestListener;
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

        public interface onConfirmRequest {
            void onYes();

            void onNo();
        }
    }
}
