package asia.health.bitcare.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by HP on 27-Dec-16.
 */

public class NetworkUtil {
    private static final int DEFAULT_REQUEST_TIME_OUT = 30;
    private static OkHttpClient okHttpClient;

    /**
     * Create an OkHttpClient and add request time out
     * Add this to AndroidNetworking.setOkHttpClient(this);
     *
     * @param requestTimeOut
     * @return
     */
    public static OkHttpClient createOkHttpClient(int requestTimeOut) {
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(requestTimeOut, TimeUnit.SECONDS)
                .readTimeout(requestTimeOut, TimeUnit.SECONDS)
                .writeTimeout(requestTimeOut, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }


    /**
     * Create an OkHttpClient and add default request time out
     * Add this to AndroidNetworking.setOkHttpClient(this);
     *
     * @return
     */
    public static OkHttpClient createDefaultOkHttpClient() {
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(DEFAULT_REQUEST_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_REQUEST_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_REQUEST_TIME_OUT, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }
}
