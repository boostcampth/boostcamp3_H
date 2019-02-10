package teamh.boostcamp.myapplication.view.graph;

import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;

public interface StatisticsView {

    void addStatisticsData(@NonNull List<EmotionHistory> emotionHistoryList);
}
