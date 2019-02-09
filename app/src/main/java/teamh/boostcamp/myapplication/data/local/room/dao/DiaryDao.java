package teamh.boostcamp.myapplication.data.local.room.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.Diary;

/*
 * DiaryDao 관련 */
@Dao
public interface DiaryDao {

    @Query("SELECT * FROM diary WHERE timeStamp < :timeStamp ORDER BY timeStamp DESC LIMIT 3")
    Single<List<Diary>> loadMoreDiary(final long timeStamp);

    @Query("DELETE FROM diary")
    void deleteAll();

    @Delete
    Completable deleteDiary(Diary diary);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertDiary(Diary ...diaries);
}
