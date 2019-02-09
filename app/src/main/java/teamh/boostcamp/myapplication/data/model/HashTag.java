package teamh.boostcamp.myapplication.data.model;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class HashTag {

    private String text;
    private int count;
    private ArrayList<String> hashTagList;

    HashTag(String text) {
        this.text = text;
        hashTagList = new ArrayList<>();
    }

    public List<String> getHashTagList() {
        return this.hashTagList;
    }

    public void addItem(@NonNull String word) {
        hashTagList.add(word);
        count++;
    }

    public int getCount(){
        return count;
    }
}
