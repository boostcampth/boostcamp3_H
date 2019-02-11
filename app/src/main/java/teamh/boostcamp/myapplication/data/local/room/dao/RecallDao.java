package teamh.boostcamp.myapplication.data.local.room.dao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.entity.RecallEntity;

@Dao
public interface RecallDao {

    @Query("Select * FROM recalls ORDER BY createdDate")
    Single<List<RecallEntity>> loadRecallEntities();

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertRecall(@NonNull RecallEntity...recallEntities);

}
