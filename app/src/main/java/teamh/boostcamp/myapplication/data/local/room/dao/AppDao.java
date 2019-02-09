package teamh.boostcamp.myapplication.data.local.room.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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
    @Query("SELECT * FROM LegacyDiary WHERE id > :idx ORDER BY recordDate DESC LIMIT 10")
    Single<List<LegacyDiary>> loadMoreDiary(final int idx);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertDiary(LegacyDiary...diaries);

    @Delete
    void deleteDiary(LegacyDiary diary);

    @Query("SELECT * FROM LegacyDiary WHERE LegacyDiary.recordDate > :start AND LegacyDiary.recordDate < :end AND LegacyDiary.selectedEmotion = :emotion LIMIT 5")
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

    @Query("SELECT * FROM LegacyDiary WHERE id=:diaryId")
    Single<LegacyDiary> loadSelectedDiary(int diaryId);

    @Query("SELECT LegacyDiary.* FROM LegacyDiary, recommended WHERE recommended.memoryId=:memoryId AND recommended.diaryId == LegacyDiary.id")
    Single<List<LegacyDiary>> loadSelectedDiayLista(int memoryId);

    // Graph 관련
    @Query("SELECT LegacyDiary.tags, LegacyDiary.selectedEmotion, LegacyDiary.analyzedEmotion " +
            "FROM LegacyDiary WHERE LegacyDiary.recordDate > :start AND LegacyDiary.recordDate < :end")
    List<GraphData> getGraphData(String start, String end);


    class GraphData {
        String tags;
        int selectedEmotion;
        int analyzedEmotion;
    }
}
