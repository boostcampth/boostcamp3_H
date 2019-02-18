package teamh.boostcamp.myapplication.data.repository;

import android.util.Pair;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.local.room.dao.DiaryDao;
import teamh.boostcamp.myapplication.data.model.CountedTag;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;
import teamh.boostcamp.myapplication.data.repository.mapper.CountedTagListMapper;
import teamh.boostcamp.myapplication.data.repository.mapper.EmotionHistoryMapper;


public class StatisticsRepositoryImpl implements StatisticsRepository {

    private static final int RECENT_TERM = 7;
    private static volatile StatisticsRepositoryImpl INSTANCE;
    @NonNull
    private final DiaryDao diaryDao;

    private StatisticsRepositoryImpl(@NonNull DiaryDao diaryDao) {
        this.diaryDao = diaryDao;
    }

    public static StatisticsRepositoryImpl getInstance(@NonNull DiaryDao diaryDao) {
        if (INSTANCE == null) {
            synchronized (StatisticsRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StatisticsRepositoryImpl(diaryDao);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Single<List<CountedTag>> loadRecentCountedTagList(
            @NonNull Date lastItemSavedTime) {

        return diaryDao.loadDiaryList(lastItemSavedTime, RECENT_TERM)
                .map(CountedTagListMapper::toTagList)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<List<Pair<EmotionHistory, EmotionHistory>>> loadRecentEmotionHistoryList(
            @NonNull Date lastItemSavedTime) {
        return diaryDao.loadDiaryList(lastItemSavedTime, RECENT_TERM)
                .map(EmotionHistoryMapper::toPairEmotionHistoryList)
                .subscribeOn(Schedulers.io());
        // 비동기 처리를 어느 스레드에서 할 지.
    }
}
