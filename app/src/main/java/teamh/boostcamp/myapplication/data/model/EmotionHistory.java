package teamh.boostcamp.myapplication.data.model;

import java.util.Date;

import androidx.annotation.NonNull;

public class EmotionHistory {

    @NonNull
    private Date date; // 날짜
    @NonNull
    private Emotion selectedEmotion; // 사용자가 선택한 감정
    @NonNull
    private Emotion analyzedEmotion; // AI에 의해 분석된 감정

    // 생성자를 통한 주입
    public EmotionHistory(@NonNull Date date,
                          @NonNull Emotion selectedEmotion,
                          @NonNull Emotion analyzedEmotion) {
        this.date = date;
        this.selectedEmotion = selectedEmotion;
        this.analyzedEmotion = analyzedEmotion;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    @NonNull
    public Emotion getSelectedEmotion() {
        return selectedEmotion;
    }

    @NonNull
    public Emotion getAnalyzedEmotion() {
        return analyzedEmotion;
    }

}
