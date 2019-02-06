package team_h.boostcamp.myapplication.data.remote.deepaffects;


import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import team_h.boostcamp.myapplication.data.remote.deepaffects.response.EmotionAnalysisResponse;

/*
 * annotation 을 통해 HTTP Method 호출 */
public interface EmotionAnalysisAPI {
    @NonNull
    @POST("sync/recognise_emotion")
    Single<List<EmotionAnalysisResponse>> analyzeRecordEmotion(@Query("apiKey") String apiKey,
                                                               @Body Map<String, String > params);
}
