package team_h.boostcamp.myapplication.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 * 일기 아이템 데이터를 가지는 DataClass */
@Entity(tableName = "diary")
public class Diary {

    // DB 에서 인식하기 위한 PK
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    private int id;

    // 저장한 날
    @ColumnInfo(name = "recordDate")
    @NonNull
    private String recordDate;

    // 저장된 파일 경로
    @ColumnInfo(name = "recordFilePath")
    @NonNull
    private String recordFilePath;

    // 설정한 태그들
    @ColumnInfo(name = "tags")
    private String tags;

    // 선택한 감정 번호
    @ColumnInfo(name = "selectedEmotion")
    @NonNull
    private int selectedEmotion;

    // API 를 통해 분석된 감정
    @ColumnInfo(name = "analyzedEmotion")
    private int analyzedEmotion;

    public Diary() { }

    public Diary(int diaryId, String recordDate, String recordFilePath, String tags, int selectedEmotion, int analyzedEmotion) {
        this.id = diaryId;
        this.recordDate = recordDate;
        this.recordFilePath = recordFilePath;
        this.tags = tags;
        this.selectedEmotion = selectedEmotion;
        this.analyzedEmotion = analyzedEmotion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getRecordFilePath() {
        return recordFilePath;
    }

    public void setRecordFilePath(String recordFilePath) {
        this.recordFilePath = recordFilePath;
    }

    public int getSelectedEmotion() {
        return selectedEmotion;
    }

    public void setSelectedEmotion(int selectedEmotion) {
        this.selectedEmotion = selectedEmotion;
    }

    public int getAnalyzedEmotion() {
        return analyzedEmotion;
    }

    public void setAnalyzedEmotion(int analyzedEmotion) {
        this.analyzedEmotion = analyzedEmotion;
    }
}
