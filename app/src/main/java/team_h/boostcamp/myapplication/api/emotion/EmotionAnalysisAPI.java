package team_h.boostcamp.myapplication.api.emotion;


import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/*
 * annotation 을 통해 HTTP Method 호출 */
public interface EmotionAnalysisAPI {

    // Rx 와
    @POST("sync/recognise_emotion")
    Single<List<EmotionAnalysisResponse>> analyzeRecordEmotion(@Query("apiKey") String apiKey,
                                                               @Body Map<String, String > params);
    // Callback 비교
    @POST("sync/recognise_emotion")
    Call<List<EmotionAnalysisResponse>> analyzeRecordEmotionByCallback(@Query("apiKey") String apiKey,
                                                                       @Body Map<String, String > params);

}
