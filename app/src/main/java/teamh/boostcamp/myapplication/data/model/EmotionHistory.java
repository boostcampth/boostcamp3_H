package teamh.boostcamp.myapplication.data.model;

import java.util.Date;

import androidx.annotation.NonNull;

/**
 * @author 이승우
 *
 * emotionType(1) - 선택한 감정
 * emotionType(2) - 분석된 감정
 * */
public class EmotionHistory {

    @NonNull
    private Date date; // 날짜
    @NonNull
    private Emotion emotion; // 감정
    private EmotionType emotionType; // 감정 타입

    // 생성자를 통한 주입
    public EmotionHistory(@NonNull Date date,
                          @NonNull Emotion emotion,
                          EmotionType emotionType) {
        this.date = date;
        this.emotion = emotion;
        this.emotionType = emotionType;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    @NonNull
    public Emotion getEmotion() {
        return emotion;
    }

    public EmotionType getEmotionType(){
        return emotionType;
    }

}
