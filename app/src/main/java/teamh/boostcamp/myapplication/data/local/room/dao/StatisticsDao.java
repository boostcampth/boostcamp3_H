package teamh.boostcamp.myapplication.data.local.room.dao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Query;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.CountedTag;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;

public interface StatisticsDao {

    @NonNull
    @Query("SELECT diary.timeStamp, diary.selectedEmotion, diary.selectedEmotion " +
            "FROM diary ORDER By timeStamp ASC LIMIT 14")
    Single<List<EmotionHistory>> loadRecentEmotionHistoryList();

    @NonNull
    @Query("SELECT diary.tags FROM diary ORDER By timeStamp ASC LIMIT 14")
    Single<List<CountedTag>> loadRecentCountedTagList();
}
