package teamh.boostcamp.myapplication.data.remote.apis.deepaffects.request;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    public EmotionAnalyzeRequest(@NonNull String filePath) {

        File file = new File(filePath);

        final byte[] encodedByteArray = new byte[(int) file.length()];
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(encodedByteArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        request = new HashMap<>();
        request.put(QUERY_ENCODING, DEFAULT_ENCODING);
        request.put(QUERY_LANGUAGE_CODE, DEFAULT_LANGUAGE_CODE);
        request.put(QUERY_SAMPLE_RATE, DEFAULT_SAMPLE_RATE);
        request.put(QUERY_CONTENT, Base64.encodeToString(encodedByteArray, Base64.DEFAULT));
    }

    @NonNull
    public Map<String, String> getRequest() {
        return request;
    }
}
