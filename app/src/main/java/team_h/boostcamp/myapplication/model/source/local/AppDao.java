package team_h.boostcamp.myapplication.model.source.local;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import team_h.boostcamp.myapplication.model.Diary;

@androidx.room.Dao
public interface AppDao {

    @Query("SELECT * FROM diary LIMIT 10")
    List<Diary> loadMoreDiary();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDiary(Diary ...diaries);

    @Delete
    void deleteDiary(Diary diary);

    @Query("SELECT * FROM diary WHERE diary.recordDate > :start AND diary.recordDate < :end AND diary.selectedEmotion = :emotion")
    List<Diary> getSelectedRecord(String start, String end, int emotion);

    // 메모리즈 가져오는 쿼리 작성 필수


    @Query("SELECT diary.tags, diary.selectedEmotion, diary.analyzedEmotion " +
            "FROM diary WHERE diary.recordDate > :start AND diary.recordDate < :end")
    List<GraphData> getGraphData(String start, String end);


    class GraphData {
        String tags;
        int selectedEmotion;
        int analyzedEmotion;
    }
}
