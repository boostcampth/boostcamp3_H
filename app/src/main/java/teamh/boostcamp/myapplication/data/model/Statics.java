package teamh.boostcamp.myapplication.data.model;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

public class Statics {

    private static final String SELECTED_EMOTIONS = "선택한 감정";
    private static final String ANALYZED_EMOTIONS = "분석된 감정";

    private List<Entry> thisWeekEmotions;
    private List<Entry> lastWeekEmotions;


    // 생성자를 통한 주입
    public Statics(List<Entry> thisWeekEmotions, List<Entry> lastWeekEmotions) {
        this.thisWeekEmotions = thisWeekEmotions;
        this.lastWeekEmotions = lastWeekEmotions;
    }

    public List<Entry> getThisWeekEmotions() {
        return this.thisWeekEmotions;
    }

    public List<Entry> getLastWeekEmotions() {
        return this.lastWeekEmotions;
    }

}
