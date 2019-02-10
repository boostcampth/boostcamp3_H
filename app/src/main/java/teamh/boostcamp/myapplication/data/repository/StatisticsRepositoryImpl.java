package teamh.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.CountedTag;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;


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
    public Single<List<CountedTag>> loadRecentCountedTagList() {
        return appDatabase.diaryDao().loadRecentCountedTagList(RECENT_TERM)
                .map(CountedTagMapper::tagToCountedTagList)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<List<EmotionHistory>> loadRecentEmotionHistoryList() {
        return null;
    }

}
