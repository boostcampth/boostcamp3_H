package teamh.boostcamp.myapplication.data.repository.mapper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.Diary;

public class DiaryMapper {

    @NonNull
    public static List<Diary> toDiaryList(@NonNull List<DiaryEntity> diaryEntities) {

        int size = diaryEntities.size();

        List<Diary> diaryList = new ArrayList<>(size);

        for(int i = 0; i < size; ++i) {

            DiaryEntity diaryEntity = diaryEntities.get(i);

            diaryList.add(new Diary(
                    diaryEntity.getId(),
                    diaryEntity.getRecordDate(),
                    diaryEntity.getRecordFilePath(),
                    diaryEntity.getTags(),
                    diaryEntity.getSelectedEmotion()
            ));
        }

        return diaryList;
    }

    @NonNull
    public static Diary toDiary(@NonNull DiaryEntity diaryEntity) {

            return new Diary(
                    diaryEntity.getId(),
                    diaryEntity.getRecordDate(),
                    diaryEntity.getRecordFilePath(),
                    diaryEntity.getTags(),
                    diaryEntity.getSelectedEmotion());
    }
}
