package team_h.boostcamp.myapplication.api_service;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Url 에 맞는 Client 생성 */
public class APIClient {

    private static final String BASE_URL = "https://proxy.api.deepaffects.com/audio/generic/api/v2/";

    private static APIClient INSTANCE;
    private static Retrofit retrofit;

    private APIClient() {
        if(INSTANCE == null) {
            synchronized (APIClient.class) {
                if(INSTANCE == null) {
                    // Log 를 확인하기 위한 okHttp 설정
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    // OkHttp Object 생성
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(loggingInterceptor)
                            .build();

                    // retrofit client 생성
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(client)
                            .build();
                }
            }
        }
    }

    public static APIClient getInstance() {
        if(INSTANCE == null)
            INSTANCE = new APIClient();
        return INSTANCE;
    }

    public Retrofit getClient() {
        return retrofit;
    }
}