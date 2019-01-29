package team_h.boostcamp.myapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
 * Created By JongSeong */
public class NetworkStateUtil {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NETWORK_CONNECTED, NETWORK_NOT_CONNECTED})

    public @interface NetworkState {}

    public static final int NETWORK_CONNECTED = 1;
    public static final int NETWORK_NOT_CONNECTED = 2;

    private static ConnectivityManager sConnectivityManager = null;

    public static @NetworkState int isNetworkConnected(Context context) {

        // 연결 확인 객체가 없는 경우에만 객체를 작성
        if(sConnectivityManager == null) {
            sConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        // 연결 상태 확인
        NetworkInfo networkInfo = sConnectivityManager.getActiveNetworkInfo();

        // 결과 반환
        return networkInfo != null && networkInfo.isConnected() ?
                NETWORK_CONNECTED : NETWORK_NOT_CONNECTED;
    }
}
