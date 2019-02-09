package teamh.boostcamp.myapplication.data.model;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class Statics {

    private int dayOfWeek; // 요일
    private float emotionScore; // 감정

    // 생성자를 통한 주입
    public Statics(int dayOfWeek, float emotionScore) {
        this.dayOfWeek = dayOfWeek;
        this.emotionScore = emotionScore;
    }

    public int getDayOfWeek(){
        return dayOfWeek;
    }

    public float getEmotionScore(){
        return emotionScore;
    }

}
