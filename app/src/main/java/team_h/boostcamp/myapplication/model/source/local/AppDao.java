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
    @Query("SELECT * FROM diary WHERE id > :idx ORDER BY recordDate DESC LIMIT 10")
    Single<List<Diary>> loadMoreDiary(final int idx);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertDiary(Diary ...diaries);

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
