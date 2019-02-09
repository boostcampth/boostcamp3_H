package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.NonNull;

public class PlayList {

    private int selectedEmotion;
    @NonNull
    private String recordedDate;
    @NonNull
    private String filePath;

    public PlayList(int selectedEmotion,
                    @NonNull String recordedDate,
                    @NonNull String filePath) {
        this.selectedEmotion = selectedEmotion;
        this.recordedDate = recordedDate;
        this.filePath = filePath;
    }

    public int getSelectedEmotion() {
        return selectedEmotion;
    }

    @NonNull
    public String getRecordedDate() {
        return recordedDate;
    }

    @NonNull
    public String getFilePath() {
        return filePath;
    }
}
