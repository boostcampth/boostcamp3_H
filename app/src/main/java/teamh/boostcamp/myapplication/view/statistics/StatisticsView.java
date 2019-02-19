package teamh.boostcamp.myapplication.view.statistics;

import android.util.Pair;

import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.model.CountedTag;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;

public interface StatisticsView {

    void updateStatisticsData(@NonNull List<Pair<EmotionHistory,EmotionHistory>> emotionHistoryList);

    void checkLoadStatisticsDataSuccessMessage();

    void checkLoadStatisticsDataFailMessage();

    void updateTagListData(@NonNull List<CountedTag> countedTagList);

    void checkLoadTagListSuccessMessage();

    void checkLoadTagListFailMessage();
}
