package asia.health.bitcare.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Leon on 10/31/2016.
 */

public class SystemHelper {

    public static void restartApp(Context context) {

        Intent intent = context.getApplicationContext().getPackageManager()
                .getLaunchIntentForPackage(context.getApplicationContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @SuppressWarnings("ConstantConditions")
    public static void hideKeyboard(Context context) {

        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager =
                    (InputMethodManager) context.
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
