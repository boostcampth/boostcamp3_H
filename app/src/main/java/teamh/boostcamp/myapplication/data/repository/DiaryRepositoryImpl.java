package teamh.boostcamp.myapplication.data.repository;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.local.room.dao.DiaryDao;
import teamh.boostcamp.myapplication.data.model.Diary;

public class DiaryRepositoryImpl implements DiaryRepository {

    @NonNull
    private static volatile DiaryRepositoryImpl INSTANCE;
    @NonNull
    private final DiaryDao diaryDao;

    public DiaryRepositoryImpl(@NonNull final DiaryDao diaryDao) {
        this.diaryDao = diaryDao;
    }

    @NonNull
    public static DiaryRepositoryImpl getInstance(@NonNull final DiaryDao diaryDao) {
        if(INSTANCE == null) {
            synchronized (DiaryRepositoryImpl.class) {
                if(INSTANCE == null) {
                    INSTANCE = new DiaryRepositoryImpl(diaryDao);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Single<List<Diary>> loadDiaryList(@NonNull final Date startAfter,
                                             final int pageSize) {
        return diaryDao.loadDiaryList(startAfter, pageSize)
                .map(DiaryMapper::toDiaryList)
                .subscribeOn(Schedulers.io());
    }
}
