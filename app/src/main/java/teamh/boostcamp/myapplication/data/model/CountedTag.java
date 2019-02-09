package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.NonNull;

public class CountedTag {

    @NonNull
    private String tagName;
    private int count;

    public CountedTag(@NonNull String tagName, int count) {
        this.tagName = tagName;
        this.count = count;
    }

    @NonNull
    public String getTagName() {
        return tagName;
    }

    public int getCount() {
        return count;
    }
}
