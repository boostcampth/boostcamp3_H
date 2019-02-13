package teamh.boostcamp.myapplication.data.repository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.local.room.entity.RecallEntity;
import teamh.boostcamp.myapplication.data.model.Recall;

public class RecallRepositoryImpl implements RecallRepository {

    private volatile static RecallRepositoryImpl INSTANCE;

    @NonNull
    final private AppDatabase appDatabase;

    private RecallRepositoryImpl(@NonNull AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    public static RecallRepositoryImpl getInstance(AppDatabase appDatabase) {
        if (INSTANCE == null) {
            synchronized (RecallRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecallRepositoryImpl(appDatabase);
                }
            }
        }
        return INSTANCE;
    }

    public Single<List<Recall>> loadRecallList() {

        return appDatabase.recallDao().loadRecallEntities()
                .flatMapObservable(Observable::fromIterable)
                .flatMapMaybe(recallEntity -> {
                    final Date endDate = generateEndDate(recallEntity.getCreatedDate());
                    final Date startDate = generateStartDate(endDate);
                    return appDatabase.diaryDao().loadDiaryListByEmotionAndDate(recallEntity.getEmotion(),
                            startDate, endDate, 5)
                            .map(diaries -> new Recall(recallEntity.getId(), startDate, endDate, recallEntity.getEmotion(), diaries));
                })
                .subscribeOn(Schedulers.io())
                .toList();
    }

    @NonNull
    @Override
    public Completable deleteRecall(int selectedRecallId) {
        return Completable.fromAction(() -> appDatabase.recallDao().deleteRecall(selectedRecallId))
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Maybe<Recall> insertRecall(@NonNull final RecallEntity recallEntity) {
        return Completable.fromAction(() -> appDatabase.recallDao().insertRecall(recallEntity))
                .andThen(appDatabase.recallDao().loadRecentRecallEntity())
                .flatMapMaybe(recallEntity1 -> {
                    final Date endDate = generateEndDate(recallEntity.getCreatedDate());
                    final Date startDate = generateStartDate(endDate);
                    return appDatabase.diaryDao().loadDiaryListByEmotionAndDate(recallEntity.getEmotion(),
                            startDate, endDate, 5);
                })
                .map(diaries -> {
                    final Date endDate = generateEndDate(recallEntity.getCreatedDate());
                    final Date startDate = generateStartDate(endDate);
                    return new Recall(recallEntity.getId(), startDate, endDate, recallEntity.getEmotion(), diaries);
                })
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    private Date generateStartDate(@NonNull Date endDate) {
        return new Date(endDate.getTime()
                - TimeUnit.DAYS.toMillis(14)
                - 1000);
    }

    @NonNull
    private Date generateEndDate(@NonNull Date createDate) {
        return new Date(createDate.getTime()
                - (createDate.getTime() % TimeUnit.DAYS.toMillis(1))
                - TimeUnit.HOURS.toMillis(9)
                - 1000);
    }
}
