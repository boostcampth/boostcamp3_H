package teamh.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.local.room.dao.DiaryDao;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.request.EmotionAnalyzeRequest;


/*
 * Repository 를 data/repository + 가공까지 ! */
public class DiaryRepository implements DiaryRepositoryContract {

    private static DiaryRepository INSTANCE;
    private DeepAffectApiClient deepAffectApiClient;
    private DiaryDao diaryDao;

    private DiaryRepository(@NonNull DeepAffectApiClient deepAffectApiClient,
                            @NonNull DiaryDao diaryDao) {
        this.deepAffectApiClient = deepAffectApiClient;
        this.diaryDao = diaryDao;
    }

    public static DiaryRepository getInstance(@NonNull DeepAffectApiClient deepAffectApiClient,
                                              @NonNull DiaryDao diaryDao) {
        if (INSTANCE == null) {
            synchronized (DiaryRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DiaryRepository(deepAffectApiClient, diaryDao);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Single<List<LegacyDiary>> loadMoreDiaryItems(final int idx) {
        return diaryDao.loadMoreDiary(idx)
                .map(DiaryEntityMapper::toDiaryList)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable insertRecordItem(@NonNull DiaryEntity diaryItem) {
        return diaryDao.insertDiary(diaryItem).subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<Integer> analyzeVoiceEmotion(@NonNull EmotionAnalyzeRequest request) {
        return deepAffectApiClient.analyzeVoiceEmotion(request)
                .map(AnalyzedEmotionMapper::parseAnalyzedEmotion)
                .subscribeOn(Schedulers.io());
    }

    /*@NonNull
    @Override
    public Completable clearAllData() {
        return Completable.fromAction(() -> diaryDao.deleteAll())
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable insertRecordItems(@NonNull DiaryEntity... diaries) {
        return diaryDao.insertDiary(diaries)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable deleteRecordItem(@NonNull DiaryEntity diary) {
        return diaryDao.deleteDiary(diary).subscribeOn(Schedulers.io());
    }*/
}
