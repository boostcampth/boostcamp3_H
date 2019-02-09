package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.NonNull;

/*
 * 일기 아이템 데이터를 가지는 DataClass */
public class LegacyDiary {

    private final int id;
    private final String recordDate;
    private final String recordFilePath;
    private final String tags;
    private final int selectedEmotion;

    public LegacyDiary(int id,
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
}
