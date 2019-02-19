package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.NonNull;

public class ShareDiary {

    @NonNull
    private final String recordDate;
    @NonNull
    private final Emotion selectedEmotion;
    @NonNull
    private final Emotion analyzedEmotion;
    @NonNull
    private final String downloadUrl;

    public ShareDiary(@NonNull String recordDate,
                      @NonNull Emotion selectedEmotion,
                      @NonNull Emotion analyzedEmotion,
                      @NonNull String downloadUrl) {
        this.recordDate = recordDate;
        this.selectedEmotion = selectedEmotion;
        this.analyzedEmotion = analyzedEmotion;
        this.downloadUrl = downloadUrl;
    }

    @NonNull
    public String getRecordDate() {
        return recordDate;
    }

    @NonNull
    public Emotion getSelectedEmotion() {
        return selectedEmotion;
    }

    @NonNull
    public Emotion getAnalyzedEmotion() {
        return analyzedEmotion;
    }

    @NonNull
    public String getDownloadUrl() {
        return downloadUrl;
    }
}
