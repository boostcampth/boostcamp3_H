package teamh.boostcamp.myapplication.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;

public class Recall implements Serializable {

    private int index;
    @NonNull
    private Date startDate;
    @NonNull
    private Date endDate;
    @NonNull
    private Emotion emotion;
    @NonNull
    private List<Diary> diaryList;

    public Recall(int index,
                  @NonNull Date startDate,
                  @NonNull Date endDate,
                  @NonNull Emotion emotion,
                  @NonNull List<Diary> diaryList) {
        this.index = index;
        this.startDate = startDate;
        this.endDate = endDate;
        this.emotion = emotion;
        this.diaryList = diaryList;
    }

    public int getIndex() {
        return index;
    }

    @NonNull
    public Date getStartDate() {
        return startDate;
    }

    @NonNull
    public Date getEndDate() {
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
