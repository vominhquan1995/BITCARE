package asia.health.bitcare;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by HP on 26-Dec-16.
 */

public class BitCareApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
