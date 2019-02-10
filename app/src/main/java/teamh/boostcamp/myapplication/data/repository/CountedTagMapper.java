package teamh.boostcamp.myapplication.data.repository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.model.CountedTag;

public class CountedTagMapper {

    public static List<CountedTag> tagToCountedTagList(@NonNull List<String> tagList) {
        int size = tagList.size();

        ArrayList<CountedTag> countedTagList = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            String tag = tagList.get(i);

            countedTagList.add(new CountedTag(tag, i));
        }
        return countedTagList;
    }
}
