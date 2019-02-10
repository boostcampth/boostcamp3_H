package teamh.boostcamp.myapplication.data.local.room.dao;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;

@Dao
public interface DiaryDao {

    @Query("SELECT * FROM diaries WHERE recordDate < :recordDate ORDER BY recordDate LIMIT :pageSize")
    Single<List<DiaryEntity>> loadDiaryList(@NonNull Date recordDate,
                                            final int pageSize);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(@NonNull DiaryEntity... diaryEntities);

    @NonNull
    @Query("SELECT * FROM diaries ORDER By diaries.recordDate ASC LIMIT :term")
    Single<List<EmotionHistory>> loadRecentEmotionHistoryList(int term);
}
