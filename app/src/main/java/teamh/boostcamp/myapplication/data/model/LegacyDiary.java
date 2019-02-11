package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 * 일기 아이템 데이터를 가지는 DataClass */
@Entity(tableName = "diary")
public class LegacyDiary {

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

    // TimeStamp 추가
    @ColumnInfo(name = "timeStamp")
    private final long timeStamp;

    public LegacyDiary(int id,
                       @NonNull String recordDate,
                       @NonNull String recordFilePath,
                       @NonNull String tags,
                       final int selectedEmotion,
                       final int analyzedEmotion,
                       final long timeStamp) {
        this.id = id;
        this.recordDate = recordDate;
        this.recordFilePath = recordFilePath;
        this.tags = tags;
        this.selectedEmotion = selectedEmotion;
        this.analyzedEmotion = analyzedEmotion;
        this.timeStamp = timeStamp;
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

    public long getTimeStamp() {
        return timeStamp;
    }
}
