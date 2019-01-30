package team_h.boostcamp.myapplication.api_service.emotion.mapper;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team_h.boostcamp.myapplication.api_service.emotion.EmotionAnalysisResponse;

/*
 * 분석된 감정을 0 - 4 사이의 값으로 매핑하기 위한 툴 */
public class AnalyzedEmotionMapper {

    private static final Map<String, Integer> SCORE_MAP = createScoreMap();

    private static Map<String, Integer> createScoreMap() {
        Map<String, Integer> scoreMap = new HashMap<>();
        // put emotions - score
        scoreMap.put("excited", 1);
        scoreMap.put("happy", 1);
        scoreMap.put("neutral", 0);
        scoreMap.put("sad", -1);
        scoreMap.put("frustrated", -1);
        scoreMap.put("mad", -1);
        scoreMap.put("fear", -1);
        return scoreMap;
    }


    /* 분석된 감정은 0 - 4 사이 값으로 매핑된다
    *  알고리즘은 API 에서 분석한 감정들을 안좋은 감정, 보통, 좋은 감정 3가지로 나눈후
    *  안좋은 감정 -1 점, 보통 감정 0 점, 좋은 감정 1 점으로 설정하고
    *  (각 감정 X 점수들을 합산) / 전체 시간 = -1 ~ 1 사이의 값으로 정규화하고
    *  0.4 단위로 각 구간을 0 - 4 값으로 매핑하여 돌려주는 것이다.*/
    public static int parseAnalyzedEmotion(List<EmotionAnalysisResponse> emotionAnalysisResponses) {

        // 현재는 테스트 용도 -> 추후 수정
        if(emotionAnalysisResponses != null) {

            int totalTime = 0;
            int totalScore = 0;

            for(EmotionAnalysisResponse response : emotionAnalysisResponses) {
                // 총 시간 계산
                totalTime += response.getEnd() - response.getStart();

                if(SCORE_MAP.containsKey(response.getEmotion())) {
                    totalScore = SCORE_MAP.get(response.getEmotion()) * (int)(response.getEnd()-response.getStart());
                }

                Log.e("Test", response.getEmotion());
                // 점수 계산
            }

            float emotion = (float)totalScore / (float)totalTime;
            String emotionLog = "";

            Log.e("Test", emotion + " ");

            if(-1 <= emotion && emotion < -0.6) {
                emotionLog = "Very 나쁨";
            } else if(-0.6 <= emotion && emotion < -0.2) {
                emotionLog = "나쁨";
            } else if(-0.2 <= emotion && emotion < 0.2) {
                emotionLog = "평범";
            } else if(0.2 <= emotion && emotion < 0.6) {
                emotionLog = "좋음";
            } else if(0.6 <= emotion && emotion <= 1) {
                emotionLog = "아주 좋음";
            }

            Log.e("Test TotalScore : ", totalScore + " TotalTime : " + totalTime + " Emotion : " + emotionLog);
        }

        return -1;
    }
}
