package teamh.boostcamp.myapplication.data.local.room.dao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Query;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.CountedTag;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;
@Dao
public interface StatisticsDao {

    @NonNull
    @Query("SELECT diaries.tags FROM diaries ORDER By recordDate ASC LIMIT :term")
    Single<List<String>> loadRecentCountedTagList(int term);
}
