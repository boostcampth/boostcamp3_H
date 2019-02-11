package teamh.boostcamp.myapplication.data.repository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
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
                .flatMapSingle(recallEntity -> {
                    final Date endDate = generateEndDate(recallEntity.getCreatedDate());
                    final Date startDate = generateStartDate(endDate);
                    return appDatabase.diaryDao().selectDiaryListByEmotionAndDate(recallEntity.getEmotion(),
                            startDate, endDate, 5)
                            .map(diaries -> new Recall(startDate, endDate, recallEntity.getEmotion(), diaries));
                })
                .subscribeOn(Schedulers.io())
                .toList();
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
