package team_h.boostcamp.myapplication.api.emotion;

import java.util.HashMap;
import java.util.Map;

public class EmotionAnalyzeRequest {

    /* 분석시 필요한 데이터들 */
    private static final String QUERY_ENCODING = "encoding";
    private static final String QUERY_LANGUAGE_CODE = "languageCode";
    private static final String QUERY_SAMPLE_RATE = "sampleRate";
    private static final String QUERY_CONTENT = "content";

    private static final String DEFAULT_ENCODING = "aac";
    private static final String DEFAULT_LANGUAGE_CODE = "ko-KR";
    private static final String DEFAULT_SAMPLE_RATE = "44100";

    public static Map<String, String> request(String encodedRecord) {

        Map<String, String> req = new HashMap<>();
        req.put(QUERY_ENCODING, DEFAULT_ENCODING);
        req.put(QUERY_LANGUAGE_CODE, DEFAULT_LANGUAGE_CODE);
        req.put(QUERY_SAMPLE_RATE, DEFAULT_SAMPLE_RATE);
        req.put(QUERY_CONTENT, encodedRecord);

        return req;
    }
}
