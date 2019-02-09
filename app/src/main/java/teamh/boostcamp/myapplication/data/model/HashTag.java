package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.Nullable;

public class HashTag {

    @Nullable
    private String text;
    private int count;

    public HashTag(String text, int count) {
        this.text = text;
        this.count = count;
    }

    public String getText() {
        return text;
    }

    public int getCount() {
        return count;
    }
}
