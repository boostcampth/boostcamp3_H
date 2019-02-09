package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.NonNull;

public class Recall {

    private String createdDate;
    @NonNull
    private int randomEmotion;

    public Recall(String createdDate, @NonNull int randomEmotion) {
        this.createdDate = createdDate;
        this.randomEmotion = randomEmotion;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    @NonNull
    public int getRandomEmotion() {
        return randomEmotion;
    }
}
