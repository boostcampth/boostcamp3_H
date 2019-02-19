package teamh.boostcamp.myapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStateUtil {

    private static final String TAG = "NetworkStateUtil";

    private static ConnectivityManager sConnectivityManager = null;

    public static boolean isNetworkConnected(Context context) {

        // 연결 확인 객체가 없는 경우에만 객체를 작성
        if(sConnectivityManager == null) {
            sConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        NetworkInfo networkInfo = null;

        try {
            networkInfo = sConnectivityManager.getActiveNetworkInfo();
        } catch (NullPointerException e) {
            Log.d(TAG, "Network check util error");
            e.printStackTrace();
        }

        // 결과 반환
        return networkInfo != null && networkInfo.isConnected();
    }
}
