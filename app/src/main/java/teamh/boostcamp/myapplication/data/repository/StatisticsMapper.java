package teamh.boostcamp.myapplication.data.repository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;

public class StatisticsMapper {

    public static List<EmotionHistory> toEmotionHistory(@NonNull List<DiaryEntity> diaryEntityList) {
        int size = diaryEntityList.size();

        ArrayList<EmotionHistory> emotionHistories = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            DiaryEntity diaryEntity = diaryEntityList.get(i);

            // TODO : 머지되면 에러 안남.
         /*   emotionHistories.add(new EmotionHistory((Date)legacyDiary.getRecordDate(),
                    (float) legacyDiary.getSelectedEmotion(),
                    (float) legacyDiary.getAnalyzedEmotion()));*/
        }
        return emotionHistories;
    }
}
