package teamh.boostcamp.myapplication.data.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.CountedTag;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;
import teamh.boostcamp.myapplication.data.model.EmotionType;


public class StatisticsRepositoryImpl implements StatisticsRepository {

    private static final int RECENT_TERM = 14;
    private static volatile StatisticsRepositoryImpl INSTANCE;
    @NonNull
    final AppDatabase appDatabase;
    private List<DiaryEntity> diaryEntityList;
    private List<EmotionHistory> totalEmotionHistoryList;

    private StatisticsRepositoryImpl(@NonNull AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        totalEmotionHistoryList = new ArrayList<>();
    }

    public static StatisticsRepositoryImpl getInstance(@NonNull AppDatabase appDatabase) {
        if (INSTANCE == null) {
            synchronized (StatisticsRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StatisticsRepositoryImpl(appDatabase);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Single<List<CountedTag>> loadRecentCountedTagList(
            @NonNull Date lastItemSavedTime) {
        return null;
    }

    @NonNull
    @Override
    public Single<List<EmotionHistory>> loadRecentEmotionHistoryList(
            @NonNull Date lastItemSavedTime) {

        diaryEntityList = (List<DiaryEntity>) appDatabase.diaryDao()
                .loadDiaryList(lastItemSavedTime, RECENT_TERM);
        int size = diaryEntityList.size();

        ArrayList<EmotionHistory> selectedEmotionHistoryArrayList = new ArrayList<>();
        ArrayList<EmotionHistory> analyzedEmotionHistoryArrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            DiaryEntity diaryEntity = diaryEntityList.get(i);
            selectedEmotionHistoryArrayList.add(new EmotionHistory(diaryEntity.getRecordDate(), diaryEntity.getSelectedEmotion(), EmotionType.selectedEmotion));
            analyzedEmotionHistoryArrayList.add(new EmotionHistory(diaryEntity.getRecordDate(), diaryEntity.getAnalyzedEmotion(), EmotionType.analyzedEmotion));
        }

        totalEmotionHistoryList.addAll(selectedEmotionHistoryArrayList);
        totalEmotionHistoryList.addAll(analyzedEmotionHistoryArrayList);

        return (Single<List<EmotionHistory>>) totalEmotionHistoryList;
    }
}
