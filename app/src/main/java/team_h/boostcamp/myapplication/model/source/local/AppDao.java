package team_h.boostcamp.myapplication.model.source.local;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import team_h.boostcamp.myapplication.model.Diary;
import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.model.Recommendation;

@Dao
public interface AppDao {

    // Diary 관련
    @Query("SELECT * FROM diary LIMIT 10")
    List<Diary> loadMoreDiary();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDiary(Diary ...diaries);

    @Delete
    void deleteDiary(Diary diary);

    @Query("SELECT * FROM diary WHERE diary.recordDate > :start AND diary.recordDate < :end AND diary.selectedEmotion = :emotion LIMIT 5")
    Flowable<List<Diary>> getSelectedRecord(String start, String end, int emotion);

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
    Single<Diary> loadSelectedDiary(int diaryId);

    @Query("SELECT diary.* FROM diary, recommended WHERE recommended.memoryId=:memoryId AND recommended.diaryId == diary.id")
    Single<List<Diary>> loadSelectedDiayLista(int memoryId);

    // Graph 관련
    @Query("SELECT diary.tags, diary.selectedEmotion, diary.analyzedEmotion " +
            "FROM diary WHERE diary.recordDate > :start AND diary.recordDate < :end")
    List<GraphData> getGraphData(String start, String end);


    class GraphData {
        String tags;
        int selectedEmotion;
        int analyzedEmotion;
    }
}
