package teamh.boostcamp.myapplication.data.repository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.dao.RecallDao;
import teamh.boostcamp.myapplication.data.model.Recall;

public class RecallRepositoryImpl implements RecallRepository {

    volatile private static RecallRepositoryImpl INSTANCE;
    @NonNull
    final private RecallDao recallDao;

    private RecallRepositoryImpl(RecallDao recallDao) {
        this.recallDao = recallDao;
    }

    public static RecallRepositoryImpl getInstance(RecallDao recallDao) {
        if (INSTANCE == null) {
            synchronized (RecallRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecallRepositoryImpl(recallDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    @NonNull
    public Single<List<Recall>> loadRecallList() {

        return recallDao.loadRecallEntities().flatMapObservable(recallEntities -> Observable.fromIterable(recallEntities))
                .flatMap(recallEntity -> {
                    Date endDate = recallEntity.getCreatedDate();
                    Date startDate = generateStartDate(endDate);

                    return Observable.just(recallDao.selectDiary(recallEntity.getEmotion(), startDate, endDate, 5))
                            .map(listSingle -> new Recall(startDate, endDate, recallEntity.getEmotion(), listSingle));
                }).toList();
    }

    private Date generateStartDate(Date endDate){
        return new Date(endDate.getTime() - TimeUnit.DAYS.toMillis(14));
    }
}
