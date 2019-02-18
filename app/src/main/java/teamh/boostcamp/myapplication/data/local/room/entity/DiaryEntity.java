package teamh.boostcamp.myapplication.data.local.room.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import teamh.boostcamp.myapplication.data.model.Emotion;

/* Legacy 와 table 이름 충돌때문에 임시로 _ 설정 */
@Entity(tableName = "diaries")
public class DiaryEntity {

    // DB 에서 인식하기 위한 PK
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    // 저장한 날
    @ColumnInfo(name = "recordDate")
    @NonNull
    private Date recordDate;

    // 저장된 파일 경로
    @ColumnInfo(name = "recordFilePath")
    @NonNull
    private String recordFilePath;

    // 설정한 태그들
    @ColumnInfo(name = "tags")
    @Nullable
    private List<String> tags;

    // 선택한 감정 번호
    @ColumnInfo(name = "selectedEmotion")
    @NonNull
    private Emotion selectedEmotion;

    // API 를 통해 분석된 감정
    @ColumnInfo(name = "analyzedEmotion")
    @NonNull
    private Emotion analyzedEmotion;

    @Ignore
    public DiaryEntity() {
    }

    public DiaryEntity(@NonNull final String id,
                       @NonNull final Date recordDate,
                       @NonNull final String recordFilePath,
                       @Nullable final List<String> tags,
                       @NonNull final Emotion selectedEmotion,
                       @NonNull final Emotion analyzedEmotion) {
        this.id = id;
        this.recordDate = recordDate;
        this.recordFilePath = recordFilePath;
        this.tags = tags;
        this.selectedEmotion = selectedEmotion;
        this.analyzedEmotion = analyzedEmotion;
    }
    @NonNull
    public String getId() {
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

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setRecordDate(@NonNull Date recordDate) {
        this.recordDate = recordDate;
    }

    public void setRecordFilePath(@NonNull String recordFilePath) {
        this.recordFilePath = recordFilePath;
    }

    public void setTags(@Nullable List<String> tags) {
        this.tags = tags;
    }

    public void setSelectedEmotion(@NonNull Emotion selectedEmotion) {
        this.selectedEmotion = selectedEmotion;
    }

    public void setAnalyzedEmotion(@NonNull Emotion analyzedEmotion) {
        this.analyzedEmotion = analyzedEmotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiaryEntity that = (DiaryEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
