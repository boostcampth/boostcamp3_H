package teamh.boostcamp.myapplication.data.local.room.dao;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.model.Emotion;

@Dao
public interface DiaryDao {

    @Query("SELECT * FROM diaries WHERE recordDate < :recordDate ORDER BY recordDate DESC LIMIT :pageSize")
    Single<List<DiaryEntity>> loadDiaryList(@NonNull Date recordDate,
                                            final int pageSize);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(@NonNull DiaryEntity...diaryEntities);

    @Query("Select * FROM diaries WHERE recordDate > :startDate AND recordDate < :endDate AND selectedEmotion = :emotion ORDER BY recordDate LIMIT :limitCount")
    Single<List<Diary>> selectDiaryListByEmotionAndDate(Emotion emotion, Date startDate, Date endDate, int limitCount);

    @Query("SELECT * FROM diaries ORDER BY recordDate DESC LIMIT 1")
    Single<DiaryEntity> loadRecentInsertedDiary();
}
