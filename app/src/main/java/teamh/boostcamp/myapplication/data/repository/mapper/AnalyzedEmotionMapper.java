package teamh.boostcamp.myapplication.data.repository.mapper;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.response.EmotionAnalysisResponse;

public class AnalyzedEmotionMapper {

    private static final int POSITIVE_EMOTION = 1;
    private static final int NEGATIVE_EMOTION = -1;
    private static final int NEUTRAL_EMOTION = 0;

    private static Map<String, Integer> SCORE_MAP;

    /* 분석된 감정은 0 - 4 사이 값으로 매핑된다
     *  알고리즘은 API 에서 분석한 감정들을 안좋은 감정, 보통, 좋은 감정 3가지로 나눈후
     *  안좋은 감정 -1 점, 보통 감정 0 점, 좋은 감정 1 점으로 설정하고
     *  (각 감정 X 점수들을 합산) / 전체 시간 = -1 ~ 1 사이의 값으로 정규화하고
     *  0.4 단위로 각 구간을 0 - 4 값으로 매핑하여 돌려주는 것이다.*/
    public static int parseAnalyzedEmotion(@NonNull List<EmotionAnalysisResponse> emotionAnalysisResponses) {

        if (SCORE_MAP == null) {
            SCORE_MAP = new HashMap<>();
            SCORE_MAP.put("excited", POSITIVE_EMOTION);
            SCORE_MAP.put("happy", POSITIVE_EMOTION);
            SCORE_MAP.put("neutral", NEUTRAL_EMOTION);
            SCORE_MAP.put("sad", NEGATIVE_EMOTION);
            SCORE_MAP.put("frustrated", NEUTRAL_EMOTION);
            SCORE_MAP.put("mad", NEGATIVE_EMOTION);
            SCORE_MAP.put("fear", NEGATIVE_EMOTION);
        }

        // 기본 기분은 평범으로 설정
        int result = NEUTRAL_EMOTION;

        int totalTime = 0;
        int totalScore = 0;

        for (EmotionAnalysisResponse response : emotionAnalysisResponses) {
            // 총 시간 계산
            totalTime += response.getEnd() - response.getStart();

            try {
                if (SCORE_MAP.containsKey(response.getEmotion())) {
                    totalScore = SCORE_MAP.get(response.getEmotion()) * (int) (response.getEnd() - response.getStart());
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            Log.e("Test", response.getEmotion());
        }

        float emotion = (float) totalScore / (float) totalTime;

        Log.e("Test", emotion + " ");

        if (-1 <= emotion && emotion < -0.6) {
            result = 0;
        } else if (-0.6 <= emotion && emotion < -0.2) {
            result = 1;
        } else if (0.2 <= emotion && emotion < 0.6) {
            result = 3;
        } else if (0.6 <= emotion && emotion <= 1) {
            result = 4;
        }

        Log.d("Test TotalScore : ", totalScore + " TotalTime : " + totalTime + " Emotion : " + result);

        return result;
    }
}
