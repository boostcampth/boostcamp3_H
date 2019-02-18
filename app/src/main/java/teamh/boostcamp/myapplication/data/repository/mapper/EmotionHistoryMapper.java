package teamh.boostcamp.myapplication.data.repository.mapper;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;
import teamh.boostcamp.myapplication.data.model.EmotionType;

public class EmotionHistoryMapper {

    @NonNull
    public static List<Pair<EmotionHistory, EmotionHistory>> toPairEmotionHistoryList(
            @NonNull List<DiaryEntity> diaryEntityList) {

        final int size = diaryEntityList.size();
        List<Pair<EmotionHistory, EmotionHistory>> emotionHistoryList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            DiaryEntity diaryEntity = diaryEntityList.get(i);
            emotionHistoryList.add(new Pair<>(new EmotionHistory(diaryEntity.getRecordDate(),
                    diaryEntity.getSelectedEmotion(),
                    EmotionType.selectedEmotion), new EmotionHistory(diaryEntity.getRecordDate(),
                    diaryEntity.getAnalyzedEmotion(),
                    EmotionType.analyzedEmotion)));
        }
        return emotionHistoryList;
    }
}
