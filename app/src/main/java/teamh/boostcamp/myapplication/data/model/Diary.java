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
    private final int id;
    private final int selectedEmotion;

    public Diary(int id,
                 @NonNull String recordDate,
                 @NonNull String recordFilePath,
                 @NonNull List<String> tags,
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

    @Nullable
    public List<String> getTags() {
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
