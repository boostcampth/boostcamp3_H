package teamh.boostcamp.myapplication.data.repository;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.local.room.dao.DiaryDao;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.request.EmotionAnalyzeRequest;
import teamh.boostcamp.myapplication.data.repository.mapper.AnalyzedEmotionMapper;
import teamh.boostcamp.myapplication.data.repository.mapper.DiaryMapper;

public class DiaryRepositoryImpl implements DiaryRepository {

    private static volatile DiaryRepositoryImpl INSTANCE;
    @NonNull
    private final DiaryDao diaryDao;
    @NonNull
    private final DeepAffectApiClient deepAffectApiClient;

    private DiaryRepositoryImpl(@NonNull final DiaryDao diaryDao,
                                @NonNull final DeepAffectApiClient deepAffectApiClient) {
        this.diaryDao = diaryDao;
        this.deepAffectApiClient = deepAffectApiClient;
    }

    @NonNull
    public static DiaryRepositoryImpl getInstance(@NonNull final DiaryDao diaryDao,
                                                  @NonNull final DeepAffectApiClient deepAffectApiClient) {
        if (INSTANCE == null) {
            synchronized (DiaryRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DiaryRepositoryImpl(diaryDao, deepAffectApiClient);
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

    @NonNull
    @Override
    public Completable insertDiary(@NonNull final DiaryEntity... diaryEntities) {
        return Completable.fromAction(() -> diaryDao.insert(diaryEntities))
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<Emotion> requestEmotionAnalyze(@NonNull EmotionAnalyzeRequest request) {
        return deepAffectApiClient.analyzeVoiceEmotion(request)
                .map(AnalyzedEmotionMapper::parseAnalyzedEmotion)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<DiaryEntity> loadRecentInsertedDiary() {
        return diaryDao.loadRecentInsertedDiary()
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override

    public Completable deleteDiary(@NonNull String id) {
        return Completable.fromAction(() -> diaryDao.delete(id))
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Observable<Diary> loadAll() {
        return diaryDao.loadAll()
                .flatMapIterable(diaryList -> diaryList)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Maybe<List<DiaryEntity>> loadNotBackupDiaryList(@NonNull List<String> diaryIdList) {
        return diaryDao.loadNotBackupDiaryList(diaryIdList)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable deleteAllDiaries() {
        return Completable.fromAction(() -> diaryDao.deleteAllDiaries())
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<List<DiaryEntity>> loadAllDiaryEntityList() {
        return diaryDao.loadAllDiaryEntities()
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable updateDiaryEntities(@NonNull DiaryEntity... diaryEntities) {
        return Completable.fromAction(diaryDao::updateDiaries)
                .subscribeOn(Schedulers.io());
    }
}
