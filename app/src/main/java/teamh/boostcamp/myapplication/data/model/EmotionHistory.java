package teamh.boostcamp.myapplication.data.model;

import java.util.Date;

import androidx.annotation.NonNull;

public class EmotionHistory {

    @NonNull
    private Date date; // 날짜
    @NonNull
    private Emotion emotion; // 감정

    // 생성자를 통한 주입
    public EmotionHistory(@NonNull Date date, @NonNull Emotion emotion) {
        this.date = date;
        this.emotion = emotion;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    @NonNull
    public Emotion getEmotion() {
        return emotion;
    }

}
