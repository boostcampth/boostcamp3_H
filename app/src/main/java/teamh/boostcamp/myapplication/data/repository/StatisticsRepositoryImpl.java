package teamh.boostcamp.myapplication.data.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
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

        return appDatabase.diaryDao().loadDiaryList(lastItemSavedTime, RECENT_TERM)
                .map(diaryEntityList -> {
                    int size = diaryEntityList.size();

                    HashMap<String, Integer> map = new HashMap<>();

                    for (int i = 0; i < size; i++) {
                        List<String> tags = diaryEntityList.get(i).getTags();

                        for (int j = 0; j < tags.size(); j++) {
                            if (map.containsKey(tags.get(j))) {
                                int count = map.get(tags.get(j));
                                map.put(tags.get(j), count + 1);
                            } else {
                                map.put(tags.get(j), 1);
                            }
                        }
                    }

                    final List<CountedTag> countedTagList = new ArrayList<>();

                    Iterator<String> iterator = map.keySet().iterator();
                    while (iterator.hasNext()) {
                        String tagName = iterator.next();
                        int count = map.get(tagName);
                        countedTagList.add(new CountedTag(tagName, count));
                    }
                    return countedTagList;

                }).subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<List<EmotionHistory>> loadRecentEmotionHistoryList(
            @NonNull Date lastItemSavedTime) {

        return appDatabase.diaryDao().loadDiaryList(lastItemSavedTime, RECENT_TERM).
                map(diaryEntityList -> {
                    final int size = diaryEntityList.size();
                    List<EmotionHistory> emotionHistoryList = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        DiaryEntity diaryEntity = diaryEntityList.get(i);
                        emotionHistoryList.add(new EmotionHistory(diaryEntity.getRecordDate(),
                                diaryEntity.getSelectedEmotion(),
                                EmotionType.selectedEmotion));

                        emotionHistoryList.add(new EmotionHistory(diaryEntity.getRecordDate(),
                                diaryEntity.getSelectedEmotion(),
                                EmotionType.analyzedEmotion));

                    }
                    return emotionHistoryList;
                })
                .subscribeOn(Schedulers.io());
    }
}
