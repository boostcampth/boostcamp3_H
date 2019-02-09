package teamh.boostcamp.myapplication.data.local.room.entity;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import teamh.boostcamp.myapplication.data.local.room.typeConverter.DateTypeConverter;
import teamh.boostcamp.myapplication.data.local.room.typeConverter.EmotionTypeConverter;
import teamh.boostcamp.myapplication.data.local.room.typeConverter.TagListTypeConverter;
import teamh.boostcamp.myapplication.data.model.Emotion;

/* Legacy 와 table 이름 충돌때문에 임시로 _ 설정 */
@Entity(tableName = "diary_")
public class DiaryEntity {

    // DB 에서 인식하기 위한 PK
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private final int id;

    // 저장한 날
    @ColumnInfo(name = "recordDate")
    @NonNull
    private final Date recordDate;

    // 저장된 파일 경로
    @ColumnInfo(name = "recordFilePath")
    @NonNull
    private final String recordFilePath;

    // 설정한 태그들
    @ColumnInfo(name = "tags")
    @Nullable
    private final List<String> tags;

    // 선택한 감정 번호
    @ColumnInfo(name = "selectedEmotion")
    @NonNull
    private final Emotion selectedEmotion;

    // API 를 통해 분석된 감정
    @ColumnInfo(name = "analyzedEmotion")
    @NonNull
    private final Emotion analyzedEmotion;

    public DiaryEntity(final int id,
                       @NonNull Date recordDate,
                       @NonNull String recordFilePath,
                       @Nullable List<String> tags,
                       @NonNull Emotion selectedEmotion,
                       @NonNull Emotion analyzedEmotion) {
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
    public Date getRecordDate() {
        return recordDate;
    }

    @Nullable
    public List<String> getTags() {
        return tags;
    }

    @NonNull
    public String getRecordFilePath() {
        return recordFilePath;
    }

    @NonNull
    public Emotion getSelectedEmotion() {
        return selectedEmotion;
    }

    @NonNull
    public Emotion getAnalyzedEmotion() {
        return analyzedEmotion;
    }
}
