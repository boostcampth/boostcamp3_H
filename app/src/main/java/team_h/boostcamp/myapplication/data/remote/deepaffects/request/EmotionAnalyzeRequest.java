package team_h.boostcamp.myapplication.data.remote.deepaffects.request;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class EmotionAnalyzeRequest {

    /* 분석시 필요한 데이터들 */
    private static final String QUERY_ENCODING = "encoding";
    private static final String QUERY_LANGUAGE_CODE = "languageCode";
    private static final String QUERY_SAMPLE_RATE = "sampleRate";
    private static final String QUERY_CONTENT = "content";

    private static final String DEFAULT_ENCODING = "aac";
    private static final String DEFAULT_LANGUAGE_CODE = "ko-KR";
    private static final String DEFAULT_SAMPLE_RATE = "44100";


    private final Map<String, String> request;

    public EmotionAnalyzeRequest(String encodedRecord) {
        request = new HashMap<>();
        request.put(QUERY_ENCODING, DEFAULT_ENCODING);
        request.put(QUERY_LANGUAGE_CODE, DEFAULT_LANGUAGE_CODE);
        request.put(QUERY_SAMPLE_RATE, DEFAULT_SAMPLE_RATE);
        request.put(QUERY_CONTENT, encodedRecord);
    }

    @NonNull
    public Map<String, String> getRequest() {
        return request;
    }
}
