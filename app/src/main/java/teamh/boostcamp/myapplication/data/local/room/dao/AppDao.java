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
import teamh.boostcamp.myapplication.data.model.Memory;
import teamh.boostcamp.myapplication.data.model.Recommendation;

/* Entity 끼리 Dao 나누기 */
@Dao
public interface AppDao {

    // LegacyDiary 관련
    @Query("SELECT * FROM diary WHERE id > :idx ORDER BY recordDate DESC LIMIT 10")
    Single<List<LegacyDiary>> loadMoreDiary(final int idx);

    @Query("SELECT * FROM diary WHERE recordDate > :start AND recordDate < :end AND selectedEmotion = :emotion LIMIT 5")
    Flowable<List<LegacyDiary>> getSelectedRecord(String start, String end, int emotion);

    // Memory 기능 관련
    @Query("SELECT * FROM memory ORDER BY id DESC LIMIT 1")
    Single<Memory> loadRecentMemory();

    @Query("SELECT * FROM memory")
    Flowable<List<Memory>> loadMemories();

    @Insert()
    Completable insertMemory(Memory memory);

    @Insert()
    Completable insertRecommendation(Recommendation recommendation);

    @Delete
    Completable deleteMemory(Memory memory);

    @Query("SELECT * FROM recommended WHERE memoryId=:memoryId")
    Single<List<Recommendation>> loadSelectedDiaryList(int memoryId);

    @Query("SELECT * FROM diary WHERE id=:diaryId")
    Single<LegacyDiary> loadSelectedDiary(int diaryId);

    @Query("SELECT * FROM diary, recommended WHERE recommended.memoryId=:memoryId AND recommended.diaryId = diary.id")
    Single<List<LegacyDiary>> loadSelectedDiayLista(int memoryId);

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
