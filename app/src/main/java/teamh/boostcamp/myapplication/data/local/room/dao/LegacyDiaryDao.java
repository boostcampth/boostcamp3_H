package teamh.boostcamp.myapplication.data.local.room.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;

/*
 * LegacyDiaryDao 관련 */
@Dao
public interface LegacyDiaryDao {

    @Query("SELECT * FROM diary WHERE recordDate < :recordDate ORDER BY recordDate DESC LIMIT 3")
    Single<List<LegacyDiary>> loadMoreDiary(final long recordDate);

    @Query("DELETE FROM diary")
    void deleteAll();

    @Delete
    Completable deleteDiary(LegacyDiary diary);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertDiary(LegacyDiary...diaries);
}
