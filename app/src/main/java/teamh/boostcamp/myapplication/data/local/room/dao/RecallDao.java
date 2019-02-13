package teamh.boostcamp.myapplication.data.local.room.dao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.entity.RecallEntity;

@Dao
public interface RecallDao {

    @Query("Select * FROM recalls ORDER BY createdDate DESC")
    Single<List<RecallEntity>> loadRecallEntities();

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertRecall(@NonNull RecallEntity...recallEntities);

    @Query("SELECT * FROM recalls ORDER BY createdDate DESC LIMIT 1")
    Single<RecallEntity> loadRecentRecallEntity();

    @Query("DELETE FROM recalls WHERE id=:index")
    void deleteRecall(@NonNull int index);

}
