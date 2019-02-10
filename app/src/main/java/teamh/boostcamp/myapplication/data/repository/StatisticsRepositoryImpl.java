package teamh.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.local.room.dao.StatisticsDao;
import teamh.boostcamp.myapplication.data.model.CountedTag;
import teamh.boostcamp.myapplication.data.model.EmotionHistory;

public class StatisticsRepositoryImpl implements StatisticsRepository {

    private static volatile StatisticsRepositoryImpl INSTANCE;
    @NonNull
    private StatisticsDao statisticsDao;

    private StatisticsRepositoryImpl(@NonNull StatisticsDao statisticsDao) {
        this.statisticsDao = statisticsDao;
    }

    public static StatisticsRepositoryImpl getInstance(@NonNull StatisticsDao statisticsDao) {
        if (INSTANCE == null) {
            synchronized (StatisticsRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StatisticsRepositoryImpl(statisticsDao);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Single<List<CountedTag>> loadRecentCountedTagList() {
        return statisticsDao.loadRecentCountedTagList(14)
                .map(CountedTagMappter::tagToCountedTagMapper)
                .subscribeOn(Schedulers.io());
    }

}
