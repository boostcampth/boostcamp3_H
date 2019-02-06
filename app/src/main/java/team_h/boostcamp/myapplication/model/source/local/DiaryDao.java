package team_h.boostcamp.myapplication.model.source.local;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Single;
import team_h.boostcamp.myapplication.model.Diary;

@Dao
public interface DiaryDao {

    // Diary 관련
    @Query("SELECT * FROM diary LIMIT 10")
    Single<List<Diary>> loadMoreDiary();

    @Delete
    Completable deleteDiary(Diary diary);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertDiary(Diary ...diaries);
}
