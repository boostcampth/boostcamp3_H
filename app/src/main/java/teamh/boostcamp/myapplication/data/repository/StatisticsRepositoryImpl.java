package teamh.boostcamp.myapplication.data.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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


    private StatisticsRepositoryImpl(@NonNull AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
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

        List<DiaryEntity> diaryEntityList = new ArrayList<>();


        diaryEntityList = (List<DiaryEntity>) appDatabase.diaryDao().loadDiaryList(lastItemSavedTime, RECENT_TERM);

        int size = diaryEntityList.size();

        HashMap<String, Integer> map = new HashMap<>();


        for (int i = 0; i < size; i++) {
            List<String> tags = diaryEntityList.get(i).getTags();

            for (int j = 0; j < tags.size(); j++) {
                if (map.containsKey(tags.get(j))) {
                    int count = map.get(tags.get(j)).intValue();
                    map.put(tags.get(j), count + 1);
                } else {
                    map.put(tags.get(j), 1);
                }
            }
        }
        final List<CountedTag> countedTags = new ArrayList<>();

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String tagName = iterator.next();
            int count = map.get(tagName);
            countedTags.add(new CountedTag(tagName,count));
        }
        return (Single<List<CountedTag>>) countedTags;
    }

    @NonNull
    @Override
    public Single<List<EmotionHistory>> loadRecentEmotionHistoryList(
            @NonNull Date lastItemSavedTime) {

        List<DiaryEntity> diaryEntityList = new ArrayList<>();
        final List<EmotionHistory> totalEmotionHistoryList = new ArrayList<>();

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
