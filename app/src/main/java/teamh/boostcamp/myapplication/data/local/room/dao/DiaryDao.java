package teamh.boostcamp.myapplication.data.local.room.dao;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Query;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.Diary;

@Dao
public interface DiaryDao {

    @Query("SELECT * FROM diary WHERE recordDate < :lastItemSavedTime ORDER BY recordDate LIMIT :pageSize")
    Single<List<Diary>> loadDiaryList(@NonNull final Date lastItemSavedTime,
                                      final int pageSize);

}
