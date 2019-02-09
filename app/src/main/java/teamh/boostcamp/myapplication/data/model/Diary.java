package teamh.boostcamp.myapplication.data.model;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Diary {

    @NonNull
    private final String recordDate;
    @NonNull
    private final String recordFilePath;
    @Nullable
    private final List<String> tags;
    @NonNull
    private final Emotion selectedEmotion;
    private final int id;

    public Diary(int id,
                 @NonNull final String recordDate,
                 @NonNull final String recordFilePath,
                 @Nullable final List<String> tags,
                 @NonNull final Emotion selectedEmotion) {
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
}
