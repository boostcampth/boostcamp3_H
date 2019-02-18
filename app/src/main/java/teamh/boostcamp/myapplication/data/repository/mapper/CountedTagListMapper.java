package teamh.boostcamp.myapplication.data.repository.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.CountedTag;

public class CountedTagListMapper {

    @NonNull
    public static List<CountedTag> toTagList(@NonNull List<DiaryEntity> diaryEntityList) {
        int size = diaryEntityList.size();

        // HashMap : String(단어)가 key가 되고, Integer(Count)가 Value가 된다.
        HashMap<String, Integer> hashMap = new HashMap<>();

        for (int i = 0; i < size; i++) {
            List<String> tagList = diaryEntityList.get(i).getTags();

            for (int j = 0; j < tagList.size(); j++) {
                if (hashMap.containsKey(tagList.get(j))) {
                    int count = hashMap.get(tagList.get(j));
                    hashMap.put(tagList.get(j), count + 1);
                } else {
                    hashMap.put(tagList.get(j), 1);
                }
            }
        }
        final List<CountedTag> countedTagList = new ArrayList<>();

        Iterator<String> iterator = hashMap.keySet().iterator(); // HashMap에 저장된 Key를 가지고 이터레이터 생성
        while (iterator.hasNext()) {
            String tagName = iterator.next(); // key 즉, String 값 tag가 나옴.
            int tagCount = hashMap.get(tagName); // Key값으로 Value 찾음.
            countedTagList.add(new CountedTag(tagName, tagCount));
        }
        return countedTagList;
    }
}
