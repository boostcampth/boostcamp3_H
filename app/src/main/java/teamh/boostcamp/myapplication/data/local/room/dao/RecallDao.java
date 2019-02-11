package teamh.boostcamp.myapplication.data.local.room.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.entity.RecallEntity;

@Dao
public interface RecallDao {

    @Query("Select * FROM recalls ORDER BY createdDate")

    Single<List<RecallEntity>> loadRecallEntities();
}
