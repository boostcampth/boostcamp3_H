package teamh.boostcamp.myapplication.data.model;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class HashTag {

    private String hashTagWord;
    private ArrayList<String> hashTagList;

    HashTag(String hashTagWord) {
        this.hashTagWord = hashTagWord;
        hashTagList = new ArrayList<>();
    }

    public List<String> getHashTagWord() {
        return this.hashTagList;
    }

    public void addItem(@NonNull String word) {
        hashTagList.add(word);
    }
}
