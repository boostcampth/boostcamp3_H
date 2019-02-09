package teamh.boostcamp.myapplication.data.model;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class Statics {

    private float week; // 요일
    private float emotion; // 감정
    private List<Entry> weekEmotionData; // 요일과 감정으로 만든 하나의 좌표 데이터

    // 생성자를 통한 주입
    public Statics(float week, float emotion) {
        weekEmotionData = new ArrayList<>();
        this.week = week;
        this.emotion = emotion;
    }

    public void createCoordinates(){
        weekEmotionData.add(new Entry(week, emotion));
    }

    public List<Entry> getThisWeekEmotions() {
        return this.weekEmotionData;
    }

}
