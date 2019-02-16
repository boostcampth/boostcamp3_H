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
    public Completable insertDiary(@NonNull final DiaryEntity diaryEntity) {
        return Completable.fromAction(() -> diaryDao.insert(diaryEntity))
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
    public Completable deleteAll() {
        return Completable.fromAction(() -> diaryDao.deleteAll())
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<List<Diary>> loadAll() {
        return diaryDao.loadAll()
                .subscribeOn(Schedulers.io());
    }
}
