package asia.health.bitcare.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import asia.health.bitcare.R;

/**
 * Created by Covisoft on 07/01/2016.
 */
public class DialogLogOut {
    public static class Build {
        private final String TAG = getClass().getSimpleName();
        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private TextView btnCancel;
        private TextView btnConfirm;
        private TextView txtTitle;
        private TextView txtContent;
        private OnLogoutListener onLogoutListener;
        public Build(Activity activity) {
            builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_logout, null);
            builder.setView(view);
            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            btnConfirm = (TextView) view.findViewById(R.id.btnConfirm);
            btnCancel = (TextView) view.findViewById(R.id.btnCancel);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtContent = (TextView) view.findViewById(R.id.txtContent);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLogoutListener.onConfirm();
                    dismiss();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLogoutListener.onCancel();
                    dismiss();
                }
            });
        }

        public Build setOnLogoutListener(OnLogoutListener onLogoutListener) {
            this.onLogoutListener = onLogoutListener;
            return this;
        }

        public Build setTitle(String title) {
            this.txtTitle.setText(title);
            return this;
        }
        public Build setContent(String content) {
            this.txtContent.setText(content);
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

        public interface OnLogoutListener {
            void onConfirm();

            void onCancel();
        }

    }
}
