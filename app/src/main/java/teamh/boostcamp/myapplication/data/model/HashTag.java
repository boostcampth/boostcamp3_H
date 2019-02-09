package teamh.boostcamp.myapplication.data.model;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HashTag {

    @Nullable
    private String text;
    @Nullable
    private int count;

    HashTag(String text) {
        this.text = text;
        count++;
    }

    public String getText(){

        return text;
    }

    public int getCount(){
        return count;
    }
}
