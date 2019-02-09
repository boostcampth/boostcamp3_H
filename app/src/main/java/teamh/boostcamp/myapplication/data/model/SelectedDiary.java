package teamh.boostcamp.myapplication.data.model;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

public class SelectedDiary {

    private int selectedEmotion;
    @Nonnull
    private String recordedDate;

    public SelectedDiary(int selectedEmotion, @Nonnull String recordedDate) {
        this.selectedEmotion = selectedEmotion;
        this.recordedDate = recordedDate;
    }

    public int getSelectedEmotion() {
        return selectedEmotion;
    }

    @Nonnull
    public String getRecordedDate() {
        return recordedDate;
    }
}
