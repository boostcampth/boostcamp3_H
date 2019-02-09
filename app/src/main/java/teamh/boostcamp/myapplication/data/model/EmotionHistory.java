package teamh.boostcamp.myapplication.data.model;

import java.util.Date;

import androidx.annotation.NonNull;

public class EmotionHistory {

    @NonNull
    private Date date; // 날짜
    private float emotion; // 감정x

    // 생성자를 통한 주입
    public EmotionHistory(@NonNull Date date, float emotion) {
        this.date = date;
        this.emotion = emotion;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public float getEmotion() {
        return emotion;
    }

}
