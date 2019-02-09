package teamh.boostcamp.myapplication.data.repository;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.local.room.dao.DiaryDao;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.Diary;

public class DiaryRepositoryImpl implements DiaryRepository {

    private static DiaryRepositoryImpl INSTANCE;
    private DiaryDao diaryDao;

    public DiaryRepositoryImpl(@NonNull final DiaryDao diaryDao) {
        this.diaryDao = diaryDao;
    }

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
    public Single<List<Diary>> loadDiaryList(@NonNull Date lastItemSavedTime, int pageSize) {
        return diaryDao.loadDiaryList(lastItemSavedTime, pageSize)
                .map(DiaryEntityMapper::toDiary)
                .subscribeOn(Schedulers.io());
    }
}
