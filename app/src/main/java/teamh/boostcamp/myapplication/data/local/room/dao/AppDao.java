package teamh.boostcamp.myapplication.data.local.room.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;

/* Entity 끼리 Dao 나누기 */
@Dao
public interface AppDao {

    // LegacyDiary 관련
    @Query("SELECT * FROM diary WHERE id > :idx ORDER BY recordDate DESC LIMIT 10")
    Single<List<LegacyDiary>> loadMoreDiary(final int idx);

    @Query("SELECT * FROM diary WHERE recordDate > :start AND recordDate < :end AND selectedEmotion = :emotion LIMIT 5")
    Flowable<List<LegacyDiary>> getSelectedRecord(String start, String end, int emotion);

    // Graph 관련
    @Query("SELECT tags, selectedEmotion, analyzedEmotion " +
            "FROM diary WHERE recordDate > :start AND recordDate < :end")
    List<GraphData> getGraphData(String start, String end);


    class GraphData {
        String tags;
        int selectedEmotion;
        int analyzedEmotion;
    }
}
