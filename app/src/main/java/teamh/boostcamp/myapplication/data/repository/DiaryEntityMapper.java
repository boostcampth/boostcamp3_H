package teamh.boostcamp.myapplication.data.repository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.Diary;

public class DiaryEntityMapper {

    @NonNull
    public static List<Diary> toDiaryList(@NonNull List<DiaryEntity> diaryEntities) {

        int size = diaryEntities.size();

        List<Diary> ret = new ArrayList<>(size);

        for(int i = 0; i < size; ++i) {

            DiaryEntity diaryEntity = diaryEntities.get(i);

            ret.add(new Diary(
                    diaryEntity.getId(),
                    diaryEntity.getRecordDate(),
                    diaryEntity.getRecordFilePath(),
                    diaryEntity.getTags(),
                    diaryEntity.getSelectedEmotion()
            ));
        }

        return ret;
    }
}
