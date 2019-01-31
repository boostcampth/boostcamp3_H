package team_h.boostcamp.myapplication.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "recommended")
public class Recommendation {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;


    // 이화중님 수정
    @ForeignKey(entity = Memory.class,
            parentColumns = "id",
            childColumns = "memoryId")
    private int memoryId;

    @NonNull
    @ForeignKey(entity = Diary.class,
            parentColumns = "id",
            childColumns = "diaryId")
    private int diaryId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(int memoryId) {
        this.memoryId = memoryId;
    }

    public int getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(int diaryId) {
        this.diaryId = diaryId;
    }
}
