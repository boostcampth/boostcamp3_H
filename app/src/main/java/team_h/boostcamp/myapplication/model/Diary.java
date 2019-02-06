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
    private final int id;

    // 저장한 날
    @ColumnInfo(name = "recordDate")
    @NonNull
    private final String recordDate;

    // 저장된 파일 경로
    @ColumnInfo(name = "recordFilePath")
    @NonNull
    private final String recordFilePath;

    // 설정한 태그들
    @ColumnInfo(name = "tags")
    private final String tags;

    // 선택한 감정 번호
    @ColumnInfo(name = "selectedEmotion")
    private final int selectedEmotion;

    // API 를 통해 분석된 감정
    @ColumnInfo(name = "analyzedEmotion")
    private final int analyzedEmotion;

    public Diary(int id,
                 @NonNull String recordDate,
                 @NonNull String recordFilePath,
                 @NonNull String tags,
                 int selectedEmotion,
                 int analyzedEmotion) {
        this.id = id;
        this.recordDate = recordDate;
        this.recordFilePath = recordFilePath;
        this.tags = tags;
        this.selectedEmotion = selectedEmotion;
        this.analyzedEmotion = analyzedEmotion;
    }
    /*최대한 안바뀌게 -> final */
    public int getId() {
        return id;
    }

    @NonNull
    public String getRecordDate() {
        return recordDate;
    }

    @NonNull
    public String getTags() {
        return tags;
    }

    @NonNull
    public String getRecordFilePath() {
        return recordFilePath;
    }

    public int getSelectedEmotion() {
        return selectedEmotion;
    }

    public int getAnalyzedEmotion() {
        return analyzedEmotion;
    }
}
