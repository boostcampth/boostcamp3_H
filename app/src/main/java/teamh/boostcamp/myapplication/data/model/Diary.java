package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.NonNull;

/*
 * Diary class 새로 작성
 * 기존 Diary 는 DiaryEntity 의 모습과 동일하고,
 * 화면에서 보이는 일기들이 가져야하는 속성들만 빼서 작성
 * 최대한 Immutable 하게 작성 */
public class Diary {


    @NonNull
    private final String recordDate;
    @NonNull
    private final String recordFilePath;
    @NonNull
    private final String tags;
    private final int id;
    private final int selectedEmotion;

    public Diary(int id,
                 @NonNull String recordDate,
                 @NonNull String recordFilePath,
                 @NonNull String tags,
                 final int selectedEmotion) {
        this.id = id;
        this.recordDate = recordDate;
        this.recordFilePath = recordFilePath;
        this.tags = tags;
        this.selectedEmotion = selectedEmotion;
    }

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
}
