package teamh.boostcamp.myapplication.data.model;

import androidx.annotation.Nullable;

public class CountedTag {

    @Nullable
    private String tagName;
    private int count;

    public CountedTag(String tagName, int count) {
        this.tagName = tagName;
        this.count = count;
    }

    public String getTagName() {
        return tagName;
    }

    public int getCount() {
        return count;
    }
}
