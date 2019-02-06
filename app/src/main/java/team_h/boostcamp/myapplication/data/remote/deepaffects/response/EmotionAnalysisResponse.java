package team_h.boostcamp.myapplication.data.remote.deepaffects.response;

import androidx.annotation.NonNull;

/*
 * 분석된 감정 결과를 나타내는 Data class */
public class EmotionAnalysisResponse {

    @NonNull
    private final String emotion;

    @NonNull
    private final Float start;

    @NonNull
    private final Float end;

    public EmotionAnalysisResponse(@NonNull final String emotion,
                                   @NonNull Float start,
                                   @NonNull Float end) {
        this.emotion = emotion;
        this.start = start;
        this.end = end;
    }

    @NonNull
    public String getEmotion() {
        return emotion;
    }

    @NonNull
    public Float getStart() {
        return start;
    }

    @NonNull
    public Float getEnd() {
        return end;
    }
}
