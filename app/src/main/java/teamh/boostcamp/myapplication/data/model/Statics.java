package teamh.boostcamp.myapplication.data.model;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class Statics {

    private static final String SELECTED_EMOTIONS = "선택한 감정";
    private static final String ANALYZED_EMOTIONS = "분석된 감정";

    private List<Entry> thisWeekEmotions;
    private List<Entry> lastWeekEmotions;
    private LineDataSet thisWeekLineDataSet;
    private LineDataSet lastWeekLineDataSet;
    private ArrayList<ILineDataSet> iLineDataSets;

    // 생성자를 통한 주입
    public Statics(List<Entry> thisWeekEmotions, List<Entry> lastWeekEmotions) {
        this.thisWeekEmotions = thisWeekEmotions;
        this.lastWeekEmotions = lastWeekEmotions;
        this.iLineDataSets = new ArrayList<>();
    }

    public List<Entry> getThisWeekEmotions() {
        return this.thisWeekEmotions;
    }

    public List<Entry> getLastWeekEmotions() {
        return this.lastWeekEmotions;
    }

    public void setThisWeekLineDataSet() {
        thisWeekLineDataSet = new LineDataSet(thisWeekEmotions, SELECTED_EMOTIONS);
        iLineDataSets.add(thisWeekLineDataSet);
    }

    public void setLastWeekLineDataSet(){
        lastWeekLineDataSet = new LineDataSet(lastWeekEmotions, ANALYZED_EMOTIONS);
        iLineDataSets.add(lastWeekLineDataSet);
    }

    public List<ILineDataSet> getILineDataSet(){
        return iLineDataSets;
    }

}
