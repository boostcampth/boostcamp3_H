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


@Dao
public interface DiaryDao {

    @Query("SELECT * FROM diaries WHERE recordDate < :recordDate ORDER BY recordDate LIMIT :pageSize")
    Single<List<DiaryEntity>> loadDiaryList(@NonNull Date recordDate,
                                            final int pageSize);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(@NonNull DiaryEntity... diaryEntities);
    
}
