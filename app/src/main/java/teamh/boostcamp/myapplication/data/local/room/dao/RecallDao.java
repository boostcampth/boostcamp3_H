package teamh.boostcamp.myapplication.data.local.room.dao;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.model.RecallEntity;

@Dao
public interface RecallDao {

    @Query("Select * FROM recall ORDER BY createdDate")
    Single<List<RecallEntity>> loadRecallEntities();

    @Query("Select * FROM diary WHERE recordDate > :startDate AND recordDate < :endDate AND selectedEmotion = :emotion ORDER BY recordDate LIMIT :limitCount")
    List<Diary> selectDiary(Emotion emotion, Date startDate, Date endDate, int limitCount);
}
