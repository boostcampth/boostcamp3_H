package teamh.boostcamp.myapplication.data.model;

import java.util.List;

import androidx.annotation.NonNull;

public class Recall {

    @NonNull
    private String startDate;
    @NonNull
    private String endDate;
    private int randomEmotion;
    @NonNull
    private List<Diary> diaryList;

    public Recall(@NonNull String startDate,
                  @NonNull String endDate,
                  int randomEmotion,
                  @NonNull List<Diary> diaryList) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.randomEmotion = randomEmotion;
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

    public int getRandomEmotion() {
        return randomEmotion;
    }

    @NonNull
    public List<Diary> getDiaryList() {
        return diaryList;
    }
}
