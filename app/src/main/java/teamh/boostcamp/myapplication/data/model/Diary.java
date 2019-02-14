package teamh.boostcamp.myapplication.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Diary implements Serializable {

    @NonNull
    private final String id;
    @NonNull
    private final Date recordDate;
    @NonNull
    private final String recordFilePath;
    @Nullable
    private final List<String> tags;
    @NonNull
    private final Emotion selectedEmotion;

    public Diary(@NonNull final String id,
                 @NonNull final Date recordDate,
                 @NonNull final String recordFilePath,
                 @Nullable final List<String> tags,
                 @NonNull final Emotion selectedEmotion) {
        this.id = id;
        this.recordDate = recordDate;
        this.recordFilePath = recordFilePath;
        this.tags = tags;
        this.selectedEmotion = selectedEmotion;
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

    @Override
    public String toString() {
        String result = "";
        for(String item : tags) {
            result = result.concat(item + " ");
        }
        return result;
    }
}
