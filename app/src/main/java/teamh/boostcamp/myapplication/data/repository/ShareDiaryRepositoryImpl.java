package teamh.boostcamp.myapplication.data.repository;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.local.room.dao.DiaryDao;
import teamh.boostcamp.myapplication.data.model.ShareDiary;

public class ShareDiaryRepositoryImpl implements ShareDiaryRepository {

    private static volatile ShareDiaryRepositoryImpl INSTANCE;
    @NonNull
    private final DiaryDao diaryDao;
    @NonNull
    private final CompositeDisposable compositeDisposable;

    public ShareDiaryRepositoryImpl(@NonNull final DiaryDao diaryDao) {
        this.diaryDao = diaryDao;
        this.compositeDisposable = new CompositeDisposable();
    }

    @NonNull
    public static ShareDiaryRepositoryImpl getInstance(@NonNull final DiaryDao diaryDao) {
        if (INSTANCE == null) {
            synchronized (ShareDiaryRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ShareDiaryRepositoryImpl(diaryDao);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Single<ShareDiary> loadShareDiary(String id) {
        return diaryDao.selectDiaryById(id)
                .map(diaryEntity -> new ShareDiary(diaryEntity.getRecordDate().toString(),
                                                            diaryEntity.getSelectedEmotion(),
                                                            diaryEntity.getAnalyzedEmotion(),
                                                            "url"))
                .subscribeOn(Schedulers.io());
    }
}
