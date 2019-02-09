package teamh.boostcamp.myapplication.data.model;

import java.util.List;

import androidx.annotation.NonNull;

public class Recall {

    @NonNull
    private String startDate;
    @NonNull
    private String endDate;
    @NonNull
    private Emotion emotion;
    @NonNull
    private List<Diary> diaryList;

    public Recall(@NonNull String startDate,
                  @NonNull String endDate,
                  @NonNull Emotion emotion,
                  @NonNull List<Diary> diaryList) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.emotion = emotion;
        this.diaryList = diaryList;
    }

    @NonNull
    public String getStartDate() {
        return startDate;
    }

    @NonNull
    public String getEndDate() {
        return endDate;
    }

    @NonNull
    public Emotion getEmotion() {
        return emotion;
    }

    @NonNull
    public List<Diary> getDiaryList() {
        return diaryList;
    }
}
