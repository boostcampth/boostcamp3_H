package teamh.boostcamp.myapplication.data.repository;

import java.util.ArrayList;
import java.util.List;

import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;

class DiaryEntityMapper {

    static List<LegacyDiary> toDiaryList(List<DiaryEntity> entityList) {

        int size = entityList.size();

        List<LegacyDiary> diaryList = new ArrayList<>(size);

        for (int i = 0; i < size; ++i) {
            // 순서대로
            final DiaryEntity diaryEntity = entityList.get(i);
            diaryList.add(new LegacyDiary(
                    diaryEntity.getId(),
                    diaryEntity.getRecordDate(),
                    diaryEntity.getRecordFilePath(),
                    diaryEntity.getTags(),
                    diaryEntity.getSelectedEmotion()
            ));
        }

        return diaryList;
    }
}
