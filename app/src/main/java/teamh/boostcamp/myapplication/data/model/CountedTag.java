package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.Nullable;

public class CountedTag {

    @Nullable
    private String tagName;
    private int count;

    public CountedTag(@Nullable String tagName, int count) {
        this.tagName = tagName;
        this.count = count;
    }

    @Nullable
    public String getTagName() {
        return tagName;
    }

    public int getCount() {
        return count;
    }
}
