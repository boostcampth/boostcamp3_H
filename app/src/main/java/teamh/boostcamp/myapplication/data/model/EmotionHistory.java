package teamh.boostcamp.myapplication.data.model;

import java.util.Date;

import androidx.annotation.NonNull;

public class EmotionHistory {

    @NonNull
    private Date date; // 요일
    private float emotionScore; // 감정

    // 생성자를 통한 주입
    public EmotionHistory(Date date, float emotionScore) {
        this.date = date;
        this.emotionScore = emotionScore;
    }

    public Date getDayOfWeek() {
        return date;
    }

    public float getEmotionScore() {
        return emotionScore;
    }

}
